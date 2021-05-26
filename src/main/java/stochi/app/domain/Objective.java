package stochi.app.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Objective.
 */
@Document(collection = "objective")
public class Objective implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("note")
    private String note;

    @Field("user_login")
    private String userLogin;

    @Field("amount_tot")
    private Float amountTot;

    @Field("amount_var")
    private Float amountVar;

    private Periode periodicity;

    private String color;

    private String nameIcon;

    private String type;

    public Objective() {}

    public Objective(
        String name,
        String note,
        String userLogin,
        Float amountTot,
        Float amountVar,
        Periode periodicity,
        String color,
        String nameIcon,
        String type
    ) {
        this.name = name;
        this.note = note;
        this.userLogin = userLogin;
        this.amountTot = amountTot;
        this.amountVar = amountVar;
        this.periodicity = periodicity;
        this.color = color;
        this.nameIcon = nameIcon;
        this.type = type;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Objective id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Objective name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return this.note;
    }

    public Objective note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public Objective userLogin(String userLogin) {
        this.userLogin = userLogin;
        return this;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNameIcon() {
        return nameIcon;
    }

    public void setNameIcon(String nameIcon) {
        this.nameIcon = nameIcon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Float getAmountTot() {
        return this.amountTot;
    }

    public Objective amountTot(Float amountTot) {
        this.amountTot = amountTot;
        return this;
    }

    public void setAmountTot(Float amountTot) {
        this.amountTot = amountTot;
    }

    public Float getAmountVar() {
        return this.amountVar;
    }

    public Objective amountVar(Float amountVar) {
        this.amountVar = amountVar;
        return this;
    }

    public void setAmountVar(Float amountVar) {
        this.amountVar = amountVar;
    }

    public Periode getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periode periodicity) {
        this.periodicity = periodicity;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Objective)) {
            return false;
        }
        return id != null && id.equals(((Objective) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Objective{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", note='" + getNote() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", amountTot=" + getAmountTot() +
            ", amountVar=" + getAmountVar() +
            "}";
    }
}
