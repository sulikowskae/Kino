package sample;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class TabelaDane {

    protected ObservableList<Film> filmy;
    protected ObservableList<Sala> sale;

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
    }
}