package sample;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.io.*;
import java.util.ArrayList;


public class DodajSala implements HierarchicalController<MainController> {

    public TextField number;
    public TextField type;
    public TextField seats;
    public TableView<Sala> tabelka;
    private MainController parentController;

    public void dodaj(ActionEvent actionEvent) {
        Sala sala = new Sala();
        sala.setNumer(number.getText());
        sala.setTyp(type.getText());
        sala.setMiejsca(seats.getText().isEmpty() ? null : Integer.parseInt(seats.getText()));
        dodajDoBazy(sala);
        tabelka.getItems().add(sala);
    }

    private void dodajDoBazy(Sala s) {
        try (Session ses = parentController.getTabelaDane().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.persist(s);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
        //tabelka.getItems().addAll(parentController.getDataContainer().getStudents());
        tabelka.setEditable(true);
        tabelka.setItems(parentController.getTabelaDane().getSala());
    }

    public MainController getParentController() {
        return parentController;
    }

    public void initialize() {
        number.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    number.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });
        seats.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    seats.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });
        for (TableColumn<Sala, ?> salaTableColumn : tabelka.getColumns()) {
            if ("number".equals(salaTableColumn.getId())) {
                salaTableColumn.setCellValueFactory(new PropertyValueFactory<>("numer"));
            } else if ("type".equals(salaTableColumn.getId())) {
                salaTableColumn.setCellValueFactory(new PropertyValueFactory<>("opis"));
            } else if ("seats".equals(salaTableColumn.getId())) {
                salaTableColumn.setCellValueFactory(new PropertyValueFactory<>("miejsca"));
            }
        }

    }

    public void zapisz(ActionEvent actionEvent) {
        ArrayList<Sala> saleList = new ArrayList<>(tabelka.getItems());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.obj"))) {
            oos.writeObject(saleList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Uwaga na serializację: https://sekurak.pl/java-vs-deserializacja-niezaufanych-danych-i-zdalne-wykonanie-kodu-czesc-i/ */
    public void wczytaj(ActionEvent actionEvent) {
        ArrayList<Sala> saleList;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.obj"))) {
            saleList = (ArrayList<Sala>) ois.readObject();
            tabelka.getItems().clear();
            tabelka.getItems().addAll(saleList);
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void synchronizuj(ActionEvent actionEvent) {
        parentController.getTabelaDane().setSala(tabelka.getItems());
    }

    public void dodajJesliEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            dodaj(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
        }
    }
}