import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import sample.Film;
import sample.Sala;
import sample.Seans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * chcemy zrobić testy. Za pomocą testów powinniśmy: dodać
 * kilka przykładowych filmów, dodać kilka przykładowych sal,
 * dodać kilka przykładowych seansów, sprawdzić, czy uda się
 * wyszukać dodane filmy po nazwie, sale po numerze, seanse po filmie i sali
 */
public class  TestyBazodanowe {

    protected static Configuration config;
    protected static SessionFactory sessionFactory;
    private Film f1 = new Film("The Room", "A story of Johnny being torn apart by Lisa ", 75, 15);
    private Film f2 = new Film("Rubber", "A tire kiling people with psychic powers", 80, 18);
    private Sala s1 = new Sala("Typ 1", "2", 34);
    private Sala s2 = new Sala("Typ 5", "4", 27);
    private Sala s3 = new Sala("Typ 4", "2", 45);


    @BeforeClass
    public static void setup() {
        config = new Configuration().configure("hibernate-test.cfg.xml");
        sessionFactory = config.buildSessionFactory();
    }

    @Test
    public void testujPołączenie() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            ses.getTransaction().commit();
        }
    }

    @Test
    public void dodajFilmy() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            ses.save(f1);
            ses.save(f2);
            List re = ses.createQuery("select count(*) from Film").list();
            Number val = (Number) re.get(0);
            ses.getTransaction().rollback();
            Assert.assertEquals(2, val.intValue());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void testAddStudentsTransaction() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            ses.save(s1);
            ses.save(s2);
            ses.getTransaction().commit();
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            List re = ses.createQuery("select count(*) from Sala").list();
            Number val = (Number) re.get(0);
            ses.getTransaction().commit();
            Assert.assertEquals(2, val.intValue());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void dodajSale() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            ses.save(s1);
            ses.save(s2);
            //ses.save(s3);
            List re = ses.createQuery("select count(*) from Sala").list();
            Number val = (Number) re.get(0);
            ses.getTransaction().rollback();
            Assert.assertEquals(2, val.intValue());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @Test
    public void dodajSeanse() {
        try (Session ses = sessionFactory.openSession()) {
            DateFormat format = new SimpleDateFormat("d.MM.yyyy HH:mm", new Locale("pl"));
            Date d1 = format.parse("01.03.2018 13:25");
            Date d2 = format.parse("03.03.2018 14:25");
            ses.beginTransaction();
            ses.save(new Seans(f1, s1, d1));
            List re = ses.createQuery("select count(*) from Seans").list();
            Number val = (Number) re.get(0);
            ses.getTransaction().rollback();
            Assert.assertEquals(1, val.intValue());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    @AfterClass
    public static void tearDown() {
        sessionFactory.close();
    }

}


