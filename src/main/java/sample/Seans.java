package sample;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="SEANSE")
public class Seans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Integer id;
    @ManyToOne(cascade=CascadeType.ALL)
    private Sala sala;
    @ManyToOne(cascade=CascadeType.ALL)
    private Film film;
    @Column(name="DATA")
    private Date date;

    public Seans() { }

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

    public String toString() {
        return film + ", " + sala +", " + date.toString();
    }
}
