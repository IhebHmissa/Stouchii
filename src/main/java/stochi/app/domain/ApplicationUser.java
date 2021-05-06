package stochi.app.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ApplicationUser.
 */
@Document(collection = "application_user")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("solde_user")
    private Float soldeUser;

    @DBRef
    @Field("user")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ApplicationUser id(String id) {
        this.id = id;
        return this;
    }

    public Float getSoldeUser() {
        return this.soldeUser;
    }

    public ApplicationUser soldeUser(Float soldeUser) {
        this.soldeUser = soldeUser;
        return this;
    }

    public void setSoldeUser(Float soldeUser) {
        this.soldeUser = soldeUser;
    }

    public User getUser() {
        return this.user;
    }

    public ApplicationUser user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return id != null && id.equals(((ApplicationUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", soldeUser=" + getSoldeUser() +
            "}";
    }
}
