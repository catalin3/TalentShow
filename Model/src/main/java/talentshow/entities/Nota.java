package talentshow.entities;

import java.io.Serializable;

public class Nota implements Serializable {
    private int idJuriu;
    private int idParticipant;
    private int nota;

    public Nota(int idJuriu, int idParticipant, int nota) {
        this.idJuriu = idJuriu;
        this.idParticipant = idParticipant;
        this.nota = nota;
    }

    public int getIdJuriu() {

        return idJuriu;
    }

    public void setIdJuriu(int idJuriu) {
        this.idJuriu = idJuriu;
    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(int idParticipant) {
        this.idParticipant = idParticipant;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
