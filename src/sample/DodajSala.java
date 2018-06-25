package sample;

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

    public TextField number_txt;
    public TextField type_txt;
    public TextField seats_txt;
    public TableView<Sala> tabelka;
    private MainController parentController;

    public void dodaj(ActionEvent actionEvent) {
        Sala sala = new Sala();
        sala.setNumer(number_txt.getText());
        sala.setTyp(type_txt.getText());
        sala.setMiejsca(seats_txt.getText().isEmpty() ? null : Integer.parseInt(seats_txt.getText()));
        tabelka.getItems().add(sala);
        this.synchronizuj();
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
        number_txt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    number_txt.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });
        seats_txt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    seats_txt.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });
        for (TableColumn<Sala, ?> salaTableColumn : tabelka.getColumns()) {
            if ("number".equals(salaTableColumn.getId())) {
                salaTableColumn.setCellValueFactory(new PropertyValueFactory<>("numer"));
            } else if ("type".equals(salaTableColumn.getId())) {
                salaTableColumn.setCellValueFactory(new PropertyValueFactory<>("typ"));
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

    public void synchronizuj() {
        parentController.getTabelaDane().setSala(tabelka.getItems());
    }

    public void dodajJesliEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            dodaj(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
        }
    }
}