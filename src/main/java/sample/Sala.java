package sample;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name ="SALE")
public class Sala  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="NUMER")
    private String numer;

    @Column(name = "TYP")
    private String typ;

    public Sala(String typ, String numer, Integer miejsca) {
        this.typ = typ;
        this.numer = numer;
        this.miejsca = miejsca;
    }
    @Column(name="MIEJSCA")
    private Integer miejsca; // liczba miejsc

    public Sala() {

    }

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

    public String toString() { return numer;}
}
