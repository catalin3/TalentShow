package talentshow.entities;

import java.io.Serializable;

public class Participant implements Serializable {
    private int id;
    private String nume;
    private String status;

    public Participant(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Participant(int id, String nume, String status) {

        this.id = id;
        this.nume = nume;
        this.status = status;
    }
}
