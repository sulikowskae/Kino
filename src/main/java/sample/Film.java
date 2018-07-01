package sample;


import javax.persistence.*;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "FILMY")
public class Film implements Serializable {
    /*
    film (nazwa, opis, czas trwania w minutach, limit wiekowy,  dla chętnych - obrazek z ilustracją)*/


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FILM_ID")
    protected Integer id;
    @Column(name = "NAZWA")
    private String nazwa;
    @Column(name = "OPIS")
    private String opis;
    @Column(name = "CZAS")
    private Integer czas; //czas trwania
    @Column(name = "LIMIT")
    private Integer limit; //limit wiekowy
    @OneToMany(mappedBy = "film",cascade=CascadeType.ALL)
    protected List<Seans>  seansList;

    //private BufferedImage obrazek;

    public Film()  {}

    public Film(String nazwa, String opis, Integer czas, Integer limit) {
        this.nazwa = nazwa;
        this.opis = opis;
        this.czas = czas;
        this.limit = limit;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /* public Film(String nazwa, String opis, Integer czas, Integer limit, BufferedImage obrazek) {
            this.nazwa = nazwa;
            this.opis = opis;
            this.czas = czas;
            this.limit = limit;
            try {
                this.obrazek=ImageIO.read(new File(nazwa+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    */
    public List<Seans> getSeansList() {
        return seansList;
    }

    public void setSeansList(List<Seans> seansList) {
        this.seansList = seansList;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getSkrot() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Integer getCzas() {
        return czas;
    }

    public void setCzas(Integer czas) {
        this.czas = czas;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


    public String toString() {
        return nazwa;
    }
}
