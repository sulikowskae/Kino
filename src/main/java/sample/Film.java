package sample;

import java.awt.image.BufferedImage;

public class Film {

    /*
    film (nazwa, opis, czas trwania w minutach, limit wiekowy,  dla chętnych - obrazek z ilustracją)*/

    private String nazwa;
    private String opis;
    private Integer czas; //czas trwania
    private Integer limit; //limit wiekowy
    private BufferedImage obrazek;

    public Film()  {}

    public Film(String nazwa, String opis, Integer czas, Integer limit) {
        this.nazwa = nazwa;
        this.opis = opis;
        this.czas = czas;
        this.limit = limit;
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

    public BufferedImage getObrazek() {
        return obrazek;
    }

    public void setObrazek(BufferedImage obrazek) {
        this.obrazek = obrazek;
    }

    public String toString() {
        return nazwa;
    }
}
