package stochi.app.domain;

import java.io.Serializable;
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

    @Field("periodicty")
    private String periodicty;

    private Periode periodictyy;

    public Category() {}

    public Category(String nameCatego) {
        this.nameCatego = nameCatego;
    }

    public Category(String type, String userLogin, String originType, Float montant, String nameCatego, @Size(max = 7) String color) {
        this.type = type;
        this.nameCatego = nameCatego;
        this.originType = originType;
        this.montant = montant;
        this.color = color;
        this.userLogin = userLogin;
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

    public String getPeriodicty() {
        return this.periodicty;
    }

    public Category periodicty(String periodicty) {
        this.periodicty = periodicty;
        return this;
    }

    public void setPeriodicty(String periodicty) {
        this.periodicty = periodicty;
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
            ", periodicty='" + getPeriodicty() + "'" +
            "}";
    }
}
