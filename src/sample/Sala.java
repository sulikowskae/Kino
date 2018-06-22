package sample;

public class Sala {
    private String typ;
    private String numer;
    private Integer miejsca; // liczba miejsc

    /*public Sala(String numer, String typ, Integer miejsca) {
        this.numer = numer;
    }*/


    public String getTyp() {

        return typ;
    }

    public void setTyp(String typ) {

        this.typ = typ;
    }

    public String getNumer() {
        return numer;
    }

    public void setNumer(String numer) {
        this.numer = numer;
    }

    public Integer getMiejsca() {
        return miejsca;
    }

    public void setMiejsca(Integer miejsca) {
        this.miejsca = miejsca;
    }
}
