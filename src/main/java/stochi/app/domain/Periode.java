package stochi.app.domain;

import java.io.Serializable;
import java.time.Duration;
import java.time.Period;
import java.time.ZonedDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Periode.
 */
@Document(collection = "periode")
public class Periode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("date_deb")
    private ZonedDateTime dateDeb;

    @Field("date_fin")
    private ZonedDateTime dateFin;

    @Field("frequancy")
    private String frequancy;

    @Field("fixed_montant")
    private Float fixedMontant;

    @Field("numberleft")
    private Long numberleft;

    @Field("type_catego")
    private String typeCatego;

    private long[] calculDurations(ZonedDateTime DDB, ZonedDateTime DDF) {
        Period period = Period.between(DDB.toLocalDate(), DDF.toLocalDate());
        Duration duration = Duration.between(DDB.toLocalTime(), DDF.toLocalTime());

        if (duration.isNegative()) {
            period = period.minusDays(1);
        }
        return new long[] { period.getDays(), period.getMonths(), period.getYears() };
    }

    public Periode() {}

    public Periode(ZonedDateTime dateDeb, ZonedDateTime dateFin, String frequancy, Float fixedMontant, String typeCatego) {
        this.dateDeb = dateDeb;
        this.dateFin = dateFin;
        this.frequancy = frequancy;
        if (frequancy.equals("jour")) this.numberleft = calculDurations(dateDeb, dateFin)[0];
        if (frequancy.equals("semaine")) this.numberleft = calculDurations(dateDeb, dateFin)[0] / 7;
        if (frequancy.equals("2semaine")) this.numberleft = calculDurations(dateDeb, dateFin)[0] / 14;
        if (frequancy.equals("mois")) this.numberleft = calculDurations(dateDeb, dateFin)[1];
        if (frequancy.equals("trimestre")) this.numberleft = calculDurations(dateDeb, dateFin)[1] / 4;
        if (frequancy.equals("semestre")) this.numberleft = calculDurations(dateDeb, dateFin)[1] / 6;
        if (frequancy.equals("annee")) this.numberleft = calculDurations(dateDeb, dateFin)[2]; else this.numberleft = 0L;
        this.fixedMontant = fixedMontant;
        this.typeCatego = typeCatego;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Periode id(String id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getDateDeb() {
        return this.dateDeb;
    }

    public Periode dateDeb(ZonedDateTime dateDeb) {
        this.dateDeb = dateDeb;
        return this;
    }

    public void setDateDeb(ZonedDateTime dateDeb) {
        this.dateDeb = dateDeb;
    }

    public ZonedDateTime getDateFin() {
        return this.dateFin;
    }

    public Periode dateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getFrequancy() {
        return this.frequancy;
    }

    public Periode frequancy(String frequancy) {
        this.frequancy = frequancy;
        return this;
    }

    public void setFrequancy(String frequancy) {
        this.frequancy = frequancy;
    }

    public Float getFixedMontant() {
        return this.fixedMontant;
    }

    public Periode fixedMontant(Float fixedMontant) {
        this.fixedMontant = fixedMontant;
        return this;
    }

    public void setFixedMontant(Float fixedMontant) {
        this.fixedMontant = fixedMontant;
    }

    public Long getNumberleft() {
        return this.numberleft;
    }

    public Periode numberleft(Long numberleft) {
        this.numberleft = numberleft;
        return this;
    }

    public void setNumberleft(Long numberleft) {
        this.numberleft = numberleft;
    }

    public String getTypeCatego() {
        return this.typeCatego;
    }

    public Periode typeCatego(String typeCatego) {
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
        if (!(o instanceof Periode)) {
            return false;
        }
        return id != null && id.equals(((Periode) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Periode{" +
            "id=" + getId() +
            ", dateDeb='" + getDateDeb() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", frequancy='" + getFrequancy() + "'" +
            ", fixedMontant=" + getFixedMontant() +
            ", numberleft=" + getNumberleft() +
            ", typeCatego='" + getTypeCatego() + "'" +
            "}";
    }
}
