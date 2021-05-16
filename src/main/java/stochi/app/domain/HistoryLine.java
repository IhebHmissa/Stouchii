package stochi.app.domain;

import java.io.Serializable;
import java.time.LocalDate;
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
    private LocalDate dateModif;

    @NotNull
    @Field("montant")
    private Float montant;

    @Field("user_login")
    private String userLogin;

    @Field("note")
    private String note;

    @Field("type_catego")
    private String typeCatego;

    @Field("soldeuseravant")
    private Float soldeuseravant;

    private Float depensechart;
    private Float revenuschart;

    private String origintype;

    private String color;

    private String icon;

    public HistoryLine() {}

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull LocalDate dateModif,
        @NotNull Float montant,
        String userLogin,
        String note,
        String typeCatego,
        Float soldeuseravant,
        Float depensechart,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.dateModif = dateModif;
        this.montant = montant;
        this.userLogin = userLogin;
        this.note = note;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.depensechart = depensechart;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull LocalDate dateModif,
        @NotNull Float montant,
        String userLogin,
        String note,
        Float revenuschart,
        String typeCatego,
        Float soldeuseravant,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.dateModif = dateModif;
        this.montant = montant;
        this.userLogin = userLogin;
        this.note = note;
        this.revenuschart = revenuschart;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull LocalDate dateModif,
        @NotNull Float montant,
        String userLogin,
        String typeCatego,
        Float soldeuseravant,
        Float depensechart,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.dateModif = dateModif;
        this.montant = montant;
        this.userLogin = userLogin;

        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.depensechart = depensechart;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull LocalDate dateModif,
        @NotNull Float montant,
        String userLogin,
        Float revenuschart,
        String typeCatego,
        Float soldeuseravant,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.dateModif = dateModif;
        this.montant = montant;
        this.userLogin = userLogin;

        this.revenuschart = revenuschart;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull Float montant,
        String userLogin,
        String note,
        String typeCatego,
        Float soldeuseravant,
        Float depensechart,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.montant = montant;
        this.userLogin = userLogin;
        this.note = note;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.depensechart = depensechart;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull Float montant,
        String userLogin,
        String note,
        Float revenuschart,
        String typeCatego,
        Float soldeuseravant,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;

        this.montant = montant;
        this.userLogin = userLogin;
        this.note = note;
        this.revenuschart = revenuschart;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull Float montant,
        String userLogin,
        String typeCatego,
        Float soldeuseravant,
        Float depensechart,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.montant = montant;
        this.userLogin = userLogin;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.depensechart = depensechart;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull Float montant,
        String userLogin,
        Float revenuschart,
        String typeCatego,
        Float soldeuseravant,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.montant = montant;
        this.userLogin = userLogin;
        this.revenuschart = revenuschart;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull LocalDate dateModif,
        @NotNull Float montant,
        String userLogin,
        String typeCatego,
        Float soldeuseravant,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.dateModif = dateModif;
        this.montant = montant;
        this.userLogin = userLogin;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull LocalDate dateModif,
        @NotNull Float montant,
        String userLogin,
        String typeCatego,
        Float soldeuseravant,
        String note,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.dateModif = dateModif;
        this.montant = montant;
        this.userLogin = userLogin;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.note = note;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

    public HistoryLine(
        @NotNull String categoryName,
        @NotNull LocalDate dateModif,
        @NotNull Float montant,
        String userLogin,
        String note,
        String typeCatego,
        Float soldeuseravant,
        String origintype,
        String icon,
        String color
    ) {
        this.categoryName = categoryName;
        this.dateModif = dateModif;
        this.montant = montant;
        this.userLogin = userLogin;
        this.note = note;
        this.typeCatego = typeCatego;
        this.soldeuseravant = soldeuseravant;
        this.origintype = origintype;
        this.icon = icon;
        this.color = color;
    }

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

    public LocalDate getDateModif() {
        return this.dateModif;
    }

    public HistoryLine dateModif(LocalDate dateModif) {
        this.dateModif = dateModif;
        return this;
    }

    public String getOrigintype() {
        return origintype;
    }

    public void setOrigintype(String origintype) {
        this.origintype = origintype;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        icon = icon;
    }

    public void setDateModif(LocalDate dateModif) {
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

    public Float getSoldeuseravant() {
        return soldeuseravant;
    }

    public void setSoldeuseravant(Float soldeuseravant) {
        this.soldeuseravant = soldeuseravant;
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

    public Float getDepensechart() {
        return depensechart;
    }

    public void setDepensechart(Float depensechart) {
        this.depensechart = depensechart;
    }

    public Float getRevenuschart() {
        return revenuschart;
    }

    public void setRevenuschart(Float revenuschart) {
        revenuschart = revenuschart;
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
