import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import sample.Film;
import sample.Sala;

import java.util.List;

/**
 * chcemy zrobić testy. Za pomocą testów powinniśmy: dodać
 * kilka przykładowych filmów, dodać kilka przykładowych sal,
 * dodać kilka przykładowych seansów, sprawdzić, czy uda się
 * wyszukać dodane filmy po nazwie, sale po numerze, seanse po filmie i sali
 */
public class TestyBazodanowe {

    protected static Configuration config;
    protected static SessionFactory sessionFactory;

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
            ses.save(new Film("The Room", "A story of Johnny being torn apart by Lisa ", 75,15));
            ses.save(new Film("Rubber", "A tire kiling people with psychic powers",80,18));
            List re = ses.createQuery("select count(*) from Filmy").list();
            Number val = (Number) re.get(0);
            ses.getTransaction().rollback();
            Assert.assertEquals(2, val.intValue());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
    @Test
    public void dodajSale() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            ses.save(new Sala("Typ 1","2",34));
            ses.save(new Sala("Typ 2", "3",27));
            ses.save(new Sala("Typ 3", "3",27));
            List re = ses.createQuery("select count(*) from Sale").list();
            Number val = (Number) re.get(0);
            ses.getTransaction().rollback();
            Assert.assertEquals(3, val.intValue());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /*@Test
    public void testAddStudentsTransaction() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            ses.save(new Sala());
            ses.save(new Sala());
            ses.getTransaction().commit();
        } catch (Exception e) {
            throw new AssertionError(e);
        }

        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            List re = ses.createQuery("select count(*) from Student").list();
            Number val = (Number) re.get(0);
            ses.getTransaction().commit();
            Assert.assertEquals(2, val.intValue());
        } catch (Exception e) {
            throw new AssertionError(e);
        }
*/

    @AfterClass
    public static void tearDown() {
        sessionFactory.close();
    }

}
