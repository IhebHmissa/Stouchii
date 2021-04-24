package stochi.app.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A HistoryLine.
 */
@Document(collection = "history_line")
public class HistoryLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("category_name")
    private String categoryName;

    @NotNull
    @Field("date_modif")
    private ZonedDateTime dateModif;

    @NotNull
    @Field("montant")
    private Float montant;

    @Field("user_login")
    private String userLogin;

    @Field("note")
    private String note;

    @Field("type_catego")
    private String typeCatego;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HistoryLine id(String id) {
        this.id = id;
        return this;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public HistoryLine categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ZonedDateTime getDateModif() {
        return this.dateModif;
    }

    public HistoryLine dateModif(ZonedDateTime dateModif) {
        this.dateModif = dateModif;
        return this;
    }

    public void setDateModif(ZonedDateTime dateModif) {
        this.dateModif = dateModif;
    }

    public Float getMontant() {
        return this.montant;
    }

    public HistoryLine montant(Float montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Float montant) {
        this.montant = montant;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public HistoryLine userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getNote() {
        return this.note;
    }

    public HistoryLine note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTypeCatego() {
        return this.typeCatego;
    }

    public HistoryLine typeCatego(String typeCatego) {
        this.typeCatego = typeCatego;
        return this;
    }

    public void setTypeCatego(String typeCatego) {
        this.typeCatego = typeCatego;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HistoryLine)) {
            return false;
        }
        return id != null && id.equals(((HistoryLine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryLine{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            ", dateModif='" + getDateModif() + "'" +
            ", montant=" + getMontant() +
            ", userLogin='" + getUserLogin() + "'" +
            ", note='" + getNote() + "'" +
            ", typeCatego='" + getTypeCatego() + "'" +
            "}";
    }
}
