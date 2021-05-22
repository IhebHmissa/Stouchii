package stochi.app.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Objectifs")
public class Objectif {

    @Id
    private String id;

    private Periode periodicity;
    private String name;
    private String note;
    private Float amountTot;
    private String userLogin;
    private Float amountva;

    public Objectif() {}

    public Objectif(Periode periodicity, String name, String note, Float amountTot, Float amountva) {
        this.periodicity = periodicity;
        this.name = name;
        this.note = note;
        this.amountTot = amountTot;
        this.amountva = amountva;
    }

    public Objectif(Periode periodicity, String name, Float amountTot, String userLogin, Float amountva) {
        this.periodicity = periodicity;
        this.name = name;
        this.amountTot = amountTot;
        this.userLogin = userLogin;
        this.amountva = amountva;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Periode getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periode periodicity) {
        this.periodicity = periodicity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Float getAmountTot() {
        return amountTot;
    }

    public void setAmountTot(Float amountTot) {
        this.amountTot = amountTot;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Float getAmountva() {
        return amountva;
    }

    public void setAmountva(Float amountva) {
        this.amountva = amountva;
    }
}
