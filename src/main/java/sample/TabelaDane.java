package sample;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class TabelaDane {

    private final Configuration config;
    private final SessionFactory sessionFactory;
    protected ObservableList<Film> filmy;
    protected ObservableList<Sala> sale;
    protected ObservableList<Seans> seanse;


    public ObservableList<Seans> getSeanse() {
        return seanse;
    }
    public void setSeanse(ObservableList<Seans> seanse) {
        this.seanse = seanse;
    }

    
    public ObservableList<Sala> getSala() {
        return sale;
    }
    public void setSala(List<Sala> sale) {

        this.sale = FXCollections.observableArrayList(sale);
    }

    public ObservableList<Film> getFilmy() {
        return filmy;
    }
    public void setFilmy(List<Film> filmy) {
        this.filmy = FXCollections.observableArrayList(filmy);
    }

    public TabelaDane() {
        filmy = FXCollections.observableArrayList();
        sale = FXCollections.observableArrayList();
        seanse = FXCollections.observableArrayList();

    }
    public DataContainer() {
        filmy = FXCollections.observableArrayList();
        sale = FXCollections.observableArrayList();
        seanse = FXCollections.observableArrayList();
        config = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = config.buildSessionFactory();
        wezFilmyZBazyDanych();
        wezSaleZBazyDanych();
        wezSeanseZBazyDanych();
    }

    private void wezSaleZBazyDanych() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            Query<Sala> query = ses.createQuery("from Sala", Sala.class);
            sale.addAll(query.list());
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    private void wezFilmyZBazyDanych() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            Query<Film> query = ses.createQuery("from Film", Film.class);
            students.addAll(query.list());
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    private void wezSeanseZBazyDanych() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            Query<Seans> query = ses.createQuery("from Seans", Seans.class);
            students.addAll(query.list());
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}