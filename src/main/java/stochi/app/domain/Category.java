package stochi.app.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Category.
 */
@Document(collection = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("type")
    private String type;

    @Field("name_catego")
    private String nameCatego;

    @Field("origin_type")
    private String originType;

    @Field("montant")
    private Float montant;

    @Size(max = 7)
    @Field("color")
    private String color;

    @Field("user_login")
    private String userLogin;

    @Field("min_montant")
    private Float minMontant;

    @Field("max_montant")
    private Float maxMontant;

    private Float average;

    private Periode periodictyy;

    private String nameIcon;

    private String dest;

    private ZonedDateTime modifDate;
    private String note;

    public Category() {}

    public Category(String nameCatego) {
        this.nameCatego = nameCatego;
    }

    public Category(String nameCatego, Float minMontant, Float maxMontant, Float average, Periode periodictyy) {
        this.nameCatego = nameCatego;
        this.minMontant = minMontant;
        this.maxMontant = maxMontant;
        this.average = average;
        this.periodictyy = periodictyy;
    }

    public Category(String nameCatego, Float minMontant, Float maxMontant, Float average) {
        this.nameCatego = nameCatego;
        this.minMontant = minMontant;
        this.maxMontant = maxMontant;
        this.average = average;
    }

    public Category(String nameCatego, Float minMontant, Float maxMontant, Float average, Float montant) {
        this.nameCatego = nameCatego;
        this.montant = montant;
        this.minMontant = minMontant;
        this.maxMontant = maxMontant;
        this.average = average;
    }

    public Category(String type, Float montant, String nameCatego, String note, ZonedDateTime date) {
        this.type = type;
        this.nameCatego = nameCatego;
        this.montant = montant;
        this.note = note;
        this.modifDate = date;
    }

    public Category(
        String type,
        String userLogin,
        String originType,
        Float montant,
        String nameCatego,
        @Size(max = 7) String color,
        String icopone,
        String dest
    ) {
        this.type = type;
        this.nameCatego = nameCatego;
        this.originType = originType;
        this.montant = montant;
        this.color = color;
        this.userLogin = userLogin;
        this.nameIcon = icopone;
        this.dest = dest;
    }

    public Category(
        String type,
        String userLogin,
        String originType,
        Float montant,
        String nameCatego,
        @Size(max = 7) String color,
        String icopone
    ) {
        this.type = type;
        this.nameCatego = nameCatego;
        this.originType = originType;
        this.montant = montant;
        this.color = color;
        this.userLogin = userLogin;
        this.nameIcon = icopone;
    }

    public Category(String type, String userLogin, String originType, Float montant, String nameCatego, @Size(max = 7) String color) {
        this.type = type;
        this.nameCatego = nameCatego;
        this.originType = originType;
        this.montant = montant;
        this.color = color;
        this.userLogin = userLogin;
    }

    public void setperiodicity(Long longg) {
        this.periodictyy.setNumberleft(longg);
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Category id(String id) {
        this.id = id;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Category type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameCatego() {
        return this.nameCatego;
    }

    public Category nameCatego(String nameCatego) {
        this.nameCatego = nameCatego;
        return this;
    }

    public void setNameCatego(String nameCatego) {
        this.nameCatego = nameCatego;
    }

    public String getOriginType() {
        return this.originType;
    }

    public Category originType(String originType) {
        this.originType = originType;
        return this;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }

    public Float getMontant() {
        return this.montant;
    }

    public Category montant(Float montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public String getColor() {
        return this.color;
    }

    public Category color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public Category userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Float getMinMontant() {
        return this.minMontant;
    }

    public Category minMontant(Float minMontant) {
        this.minMontant = minMontant;
        return this;
    }

    public void setMinMontant(Float minMontant) {
        this.minMontant = minMontant;
    }

    public Float getMaxMontant() {
        return this.maxMontant;
    }

    public Category maxMontant(Float maxMontant) {
        this.maxMontant = maxMontant;
        return this;
    }

    public void setMaxMontant(Float maxMontant) {
        this.maxMontant = maxMontant;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public String getNameIcon() {
        return nameIcon;
    }

    public void setNameIcon(String nameIcon) {
        this.nameIcon = nameIcon;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public ZonedDateTime getModifDate() {
        return modifDate;
    }

    public void setModifDate(ZonedDateTime modifDate) {
        this.modifDate = modifDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    public Periode getPeriodictyy() {
        return periodictyy;
    }

    public void setPeriodictyy(Periode periodictyy) {
        this.periodictyy = periodictyy;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", nameCatego='" + getNameCatego() + "'" +
            ", originType='" + getOriginType() + "'" +
            ", montant=" + getMontant() +
            ", color='" + getColor() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", minMontant=" + getMinMontant() +
            ", maxMontant=" + getMaxMontant() +
            ", periodicty='" + getAverage() + "'" +
            "}";
    }
}
