package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name ="SALA")
public class Sala  implements Serializable {


    @Id @GeneratedValue
    @Column(name = "SALA_ID")
    protected Integer id;

    @Column(name="NUMER")
    private String numer;

    @Column(name = "TYP")
    private String typ;
    @OneToMany(mappedBy = "sala",cascade=CascadeType.ALL)
    protected List<Seans>  seansList;



    @Column(name="MIEJSCA")
    private Integer miejsca; // liczba miejsc

    public Sala() {}

    public Sala(String typ, String numer, Integer miejsca) {
        this.typ = typ;
        this.numer = numer;
        this.miejsca = miejsca;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Seans> getSeansList() {
        return seansList;
    }

    public void setSeansList(List<Seans> seansList) {
        this.seansList = seansList;
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
