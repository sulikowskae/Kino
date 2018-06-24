package sample;

import java.util.Date;

public class Seans {
    private Sala sala;
    private Film film;
    private Date date;

    public Seans(Film film, Sala sala, Date date) {
        this.film=film;
        this.sala=sala;
        this.date=date;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
