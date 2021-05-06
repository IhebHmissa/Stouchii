package stochi.app.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stochi.app.config.Constants;
import stochi.app.domain.Authority;
import stochi.app.domain.Category;
import stochi.app.domain.User;
import stochi.app.repository.AuthorityRepository;
import stochi.app.repository.UserRepository;
import stochi.app.security.AuthoritiesConstants;
import stochi.app.security.SecurityUtils;
import stochi.app.service.dto.AdminUserDTO;
import stochi.app.service.dto.UserDTO;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    @Autowired
    private CategoryService categoryService;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public Float soldeUser(String login) {
        Optional<User> constants = userRepository.findOneByLogin(login);
        User value = constants.orElseThrow(() -> new RuntimeException("No such data found"));
        return value.getSoldeUser();
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(
                user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    userRepository.save(user);
                    this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                }
            );
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(
                user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    userRepository.save(user);
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(
                user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    userRepository.save(user);
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                }
            );
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                }
            );
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(true);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        newUser.setSoldeUser(userDTO.getSoldeUser());

        Category cat1 = new Category("Depense", newUser.getLogin(), "Catego", 0f, "Food & Drinks", "red", "cutlery", "FoodCategorie");
        categoryService.save(cat1);
        Category cat2 = new Category("Depense", newUser.getLogin(), "Catego", 0f, "Housing", "#1DAED9", "home", "HousingCategorie");
        categoryService.save(cat2);
        Category cat3 = new Category("Depense", newUser.getLogin(), "Catego", 0f, "Healthcare", "green", "heartbeat", "HealthCategorie");
        categoryService.save(cat3);
        Category cat4 = new Category("Depense", newUser.getLogin(), "Catego", 0f, "Public services", "purple", "tasks", "PubliCategorie");
        categoryService.save(cat4);
        Category cat5 = new Category("Depense", newUser.getLogin(), "Catego", 0f, "Transportation", "#F37A21", "bus", "TransportCategorie");
        categoryService.save(cat5);
        Category cat6 = new Category(
            "Depense",
            newUser.getLogin(),
            "Catego",
            0f,
            "Education",
            "#E90454",
            "graduation-cap",
            "EducationCategorie"
        );
        categoryService.save(cat6);
        Category cat7 = new Category("Depense", newUser.getLogin(), "Catego", 0f, "Fun", "#58CB39", "smile-o", "FunCategorie");
        categoryService.save(cat7);
        Category cat8 = new Category("Depense", newUser.getLogin(), "Catego", 0f, "Various", "#23289F", "cart-plus", "VariousCategorie");
        categoryService.save(cat8);
        Category cat9 = new Category("Depense", newUser.getLogin(), "Catego", 0f, "Unexpected", "#BB1616", "warning", "UnexCategorie");
        categoryService.save(cat9);
        Category cat10 = new Category("Depense", newUser.getLogin(), "Food & Drinks", 0f, "Food", "red", "fast-food");
        categoryService.save(cat10);
        Category cat11 = new Category("Depense", newUser.getLogin(), "Food & Drinks", 0f, "Bar & Cafe", "red", "cafe");
        categoryService.save(cat11);
        Category cat12 = new Category("Depense", newUser.getLogin(), "Food & Drinks", 0f, "Restaurant", "red", "restaurant");
        categoryService.save(cat12);
        Category cat13 = new Category("Depense", newUser.getLogin(), "Housing", 0f, "Rent", "#1DAED9", "night-shelter");
        categoryService.save(cat13);
        Category cat14 = new Category("Depense", newUser.getLogin(), "Housing", 0f, "Loan", "#1DAED9", "payment");
        categoryService.save(cat14);
        Category cat15 = new Category("Depense", newUser.getLogin(), "Housing", 0f, "Taxes", "#1DAED9", "assignment");
        categoryService.save(cat15);
        Category cat16 = new Category("Depense", newUser.getLogin(), "Healthcare", 0f, "Insurance", "green", "briefcase-check");
        categoryService.save(cat16);
        Category cat17 = new Category("Depense", newUser.getLogin(), "Healthcare", 0f, "consultation", "green", "doctor");
        categoryService.save(cat17);
        Category cat18 = new Category("Depense", newUser.getLogin(), "Healthcare", 0f, "Medicine", "green", "pill");
        categoryService.save(cat18);
        Category cat19 = new Category("Depense", newUser.getLogin(), "Healthcare", 0f, "Radiology", "green", "radiology-box");
        categoryService.save(cat19);
        Category cat20 = new Category("Depense", newUser.getLogin(), "Healthcare", 0f, "Hospitalization", "green", "hospital-building");
        categoryService.save(cat20);
        Category cat21 = new Category("Depense", newUser.getLogin(), "Healthcare", 0f, "Other", "green", "heart-plus");
        categoryService.save(cat21);
        Category cat22 = new Category("Depense", newUser.getLogin(), "Public services", 0f, "Electricity", "purple", "electrical-services");
        categoryService.save(cat22);
        Category cat23 = new Category("Depense", newUser.getLogin(), "Public services", 0f, "Heater", "purple", "whatshot");
        categoryService.save(cat23);
        Category cat24 = new Category("Depense", newUser.getLogin(), "Public services", 0f, "Water", "purple", "water-damage");
        categoryService.save(cat24);
        Category cat25 = new Category("Depense", newUser.getLogin(), "Public services", 0f, "Phone", "purple", "phone");
        categoryService.save(cat25);
        Category cat26 = new Category("Depense", newUser.getLogin(), "Public services", 0f, "Internet", "purple", "wifi");
        categoryService.save(cat26);
        Category cat27 = new Category("Depense", newUser.getLogin(), "Transportation", 0f, "Public transport", "#F37A21", "train");
        categoryService.save(cat27);
        Category cat28 = new Category("Depense", newUser.getLogin(), "Transportation", 0f, "Taxi", "#F37A21", "taxi");
        categoryService.save(cat28);
        Category cat29 = new Category("Depense", newUser.getLogin(), "Transportation", 0f, "Travel", "#F37A21", "airplane");
        categoryService.save(cat29);
        Category cat30 = new Category("Depense", newUser.getLogin(), "Transportation", 0f, "Insurance", "#F37A21", "shield-check");
        categoryService.save(cat30);
        Category cat31 = new Category("Depense", newUser.getLogin(), "Transportation", 0f, "Vehicle maintenance", "#F37A21", "car-cog");
        categoryService.save(cat31);
        Category cat32 = new Category("Depense", newUser.getLogin(), "Transportation", 0f, "Fuel", "#F37A21", "fuel");
        categoryService.save(cat32);
        Category cat33 = new Category("Depense", newUser.getLogin(), "Education", 0f, "Registration", "#E90454", "app-registration");
        categoryService.save(cat33);
        Category cat34 = new Category("Depense", newUser.getLogin(), "Education", 0f, "Remedial teaching", "#E90454", "menu-book");
        categoryService.save(cat34);
        Category cat35 = new Category("Depense", newUser.getLogin(), "Education", 0f, "Supplies", "#E90454", "backpack");
        categoryService.save(cat35);
        Category cat36 = new Category("Depense", newUser.getLogin(), "Fun", 0f, "Culture,Sporting events", "#58CB39", "ticket");
        categoryService.save(cat36);
        Category cat37 = new Category("Depense", newUser.getLogin(), "Fun", 0f, "Beauty & Wellness", "#58CB39", "lipstick");
        categoryService.save(cat37);
        Category cat38 = new Category("Depense", newUser.getLogin(), "Fun", 0f, "Hobbies & Passion", "#58CB39", "heart-box");
        categoryService.save(cat38);
        Category cat39 = new Category("Depense", newUser.getLogin(), "Fun", 0f, "Books", "#58CB39", "library");
        categoryService.save(cat39);
        Category cat40 = new Category("Depense", newUser.getLogin(), "Fun", 0f, "Subscriptions", "#58CB39", "card-account-details-star");
        categoryService.save(cat40);
        Category cat41 = new Category("Depense", newUser.getLogin(), "Fun", 0f, "Sport & Fitness", "#58CB39", "handball");
        categoryService.save(cat41);
        Category cat43 = new Category("Depense", newUser.getLogin(), "Various", 0f, "Gifts", "#23289F", "card-giftcard");
        categoryService.save(cat43);
        Category cat44 = new Category("Depense", newUser.getLogin(), "Various", 0f, "Pets", "#23289F", "pets");
        categoryService.save(cat44);
        Category cat45 = new Category("Depense", newUser.getLogin(), "Various", 0f, "Garden", "#23289F", "grass");
        categoryService.save(cat45);
        Category cat46 = new Category("Depense", newUser.getLogin(), "Unexpected", 0f, "Fines", "#BB1616", "warning");
        categoryService.save(cat46);
        Category cat47 = new Category("Revenus", newUser.getLogin(), "Catego", 0f, "Income", "#F7EE27", "dollar", "IncomeCategorie");
        categoryService.save(cat47);
        Category cat48 = new Category("Revenus", newUser.getLogin(), "Income", 0f, "Salary", "#F7EE27", "IncomeCategorie");
        categoryService.save(cat48);
        Category cat49 = new Category("Revenus", newUser.getLogin(), "Income", 0f, "Sale & Rents", "#F7EE27", "IncomeCategorie");
        categoryService.save(cat49);
        Category cat50 = new Category("Revenus", newUser.getLogin(), "Income", 0f, "Refund", "#F7EE27", "IncomeCategorie");
        categoryService.save(cat50);
        Category cat51 = new Category("Revenus", newUser.getLogin(), "Income", 0f, "Family allowances", "#F7EE27", "IncomeCategorie");
        categoryService.save(cat51);
        Category cat52 = new Category("Revenus", newUser.getLogin(), "Income", 0f, "Help", "#F7EE27", "IncomeCategorie");
        categoryService.save(cat52);
        Category cat53 = new Category("Revenus", newUser.getLogin(), "Income", 0f, "diverse", "#F7EE27", "IncomeCategorie");
        categoryService.save(cat53);
        Category cat54 = new Category("Revenus", newUser.getLogin(), "Income", 0f, "Gifts", "#F7EE27", "IncomeCategorie");
        categoryService.save(cat54);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        return Optional
            .of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    this.clearUserCaches(user);
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    user.setLangKey(userDTO.getLangKey());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO
                        .getAuthorities()
                        .stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                    userRepository.save(user);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(AdminUserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(
                user -> {
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                    log.debug("Deleted User: {}", user);
                }
            );
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                    }
                    user.setLangKey(langKey);
                    user.setImageUrl(imageUrl);
                    userRepository.save(user);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                }
            );
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    userRepository.save(user);
                    this.clearUserCaches(user);
                    log.debug("Changed password for User: {}", user);
                }
            );
    }

    public Page<AdminUserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserDTO::new);
    }

    public Page<UserDTO> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserDTO::new);
    }

    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login);
    }

    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(
                user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                }
            );
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
