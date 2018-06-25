
package sample;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class DodajFilm implements HierarchicalController<MainController> {

    public TextField title;
    public TextField descript;
    public TextField time;
    public TextField restrict;
    public TextField img;
    public TableView<Film> tabelka;
    private MainController parentController;

    public void dodaj(ActionEvent actionEvent) {
        Film fil = new Film();
        fil.setNazwa(title.getText());
        fil.setOpis(descript.getText());
        fil.setCzas(time.getText().isEmpty() ? null : Integer.parseInt(time.getText()));
        fil.setLimit(restrict.getText().isEmpty() ? null : Integer.parseInt(restrict.getText()));
        //fil.setObrazek(obrazek.....);
        dodajDoBazy(fil);
        tabelka.getItems().add(fil);
        this.synchronizuj();
    }

    private void dodajDoBazy(Film f) {
        try (Session ses = parentController.getTabelaDane().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.persist(f);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
        tabelka.setItems(parentController.getTabelaDane().getFilmy());
    }

    public void usunZmiany() {
        tabelka.getItems().clear();
        tabelka.getItems().addAll(parentController.getTabelaDane().getFilmy());
    }

    public MainController getParentController() {
        return parentController;
    }

    public void initialize() {
        for (TableColumn<Film, ?> filmyTableColumn : tabelka.getColumns()) {
            if ("title".equals(filmyTableColumn.getId())) {
                filmyTableColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
            } else if ("descript".equals(filmyTableColumn.getId())) {
                filmyTableColumn.setCellValueFactory(new PropertyValueFactory<>("opis"));
            } else if ("time".equals(filmyTableColumn.getId())) {
                filmyTableColumn.setCellValueFactory(new PropertyValueFactory<>("czas"));
            } else if ("restrict".equals(filmyTableColumn.getId())) {
                filmyTableColumn.setCellValueFactory(new PropertyValueFactory<>("limit"));
            }
        }

    }

    public void zapisz(ActionEvent actionEvent) {
        ArrayList<Film> filmyList = new ArrayList<>(tabelka.getItems());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.obj"))) {
            oos.writeObject(filmyList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uwaga na serializację: https://sekurak.pl/java-vs-deserializacja-niezaufanych-danych-i-zdalne-wykonanie-kodu-czesc-i/
     */
    public void wczytaj(ActionEvent actionEvent) {
        ArrayList<Film> filmyList;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.obj"))) {
            filmyList = (ArrayList<Film>) ois.readObject();
            tabelka.getItems().clear();
            tabelka.getItems().addAll(filmyList);
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void synchronizuj() {
        parentController.getTabelaDane().setFilmy(tabelka.getItems());
    }

}