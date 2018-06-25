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
 * Created by pwilkin on 19-Apr-18.
 * chcemy zrobić testy. Za pomocą testów powinniśmy: dodać
 * kilka przykładowych filmów, dodać kilka przykładowych sal,
 * dodać kilka przykładowych seansów, sprawdzić, czy uda się
 * wyszukać dodane filmy po nazwie, sale po numerze, seanse po filmie i sali
 */
public class DatabaseTests {

    protected static Configuration config;
    protected static SessionFactory sessionFactory;

    @BeforeClass
    public static void setup() {
        config = new Configuration().configure("hibernate-test.cfg.xml");
        sessionFactory = config.buildSessionFactory();
    }

    @Test
    public void testDbConnection() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            ses.getTransaction().commit();
        }
    }

    @Test
    public void addStudents() {
        try (Session ses = sessionFactory.openSession()) {
            ses.beginTransaction();
            ses.save(new Film());
            ses.save(new Film());
            List re = ses.createQuery("select count(*) from Student").list();
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

    }

    @AfterClass
    public static void tearDown() {
        sessionFactory.close();
    }

}
