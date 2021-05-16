package stochi.app.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Notification.
 */
@Document(collection = "notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("amount")
    private Float amount;

    @Field("user_login")
    private String userLogin;

    @Field("category_name")
    private String categoryName;

    @Field("time")
    private LocalDate time;

    @Field("type")
    private String type;

    public Notification() {}

    public Notification(Float amount, String userLogin, String categoryName, LocalDate time, String type) {
        this.amount = amount;
        this.userLogin = userLogin;
        this.categoryName = categoryName;
        this.time = time;
        this.type = type;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Notification id(String id) {
        this.id = id;
        return this;
    }

    public Float getAmount() {
        return this.amount;
    }

    public Notification amount(Float amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public Notification userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public Notification categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public LocalDate getTime() {
        return this.time;
    }

    public Notification time(LocalDate time) {
        this.time = time;
        return this;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public String getType() {
        return this.type;
    }

    public Notification type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", userLogin='" + getUserLogin() + "'" +
            ", categoryName='" + getCategoryName() + "'" +
            ", time='" + getTime() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
