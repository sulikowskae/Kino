package sample;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.query.Query;

import javax.persistence.TemporalType;
import javax.swing.*;
import javax.xml.crypto.Data;
import java.io.*;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.abs;


public class DodajSeans implements HierarchicalController<MainController> {

    public ComboBox<Film> filmy;
    public ComboBox<Sala> sale;
    public TextField godzina;
    public TextField data_txt;

    public TableView<Seans> tabelka;
    public Label alert;
    private MainController parentController;

    private Date stworzDate() throws ParseException {
        String string = data_txt.getText()+"."+godzina.getText();
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy.HH:mm", new Locale("pl"));
        return format.parse(string);
    }
    public boolean sprawdzDostepnoscSali(Sala s, Date start, Integer czas_trw) {
        boolean bool = true;
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        cal.add(Calendar.MINUTE, czas_trw);
        Date koniec = cal.getTime();

        if (s.getSeansList() != null) {
            for (Seans seans : s.getSeansList()) { //sprawdza czy te które są nie zazębiają się z nowym
                Date b = seans.getDate();
                Date e = seans.getKoniec();
                if (b == start || e == koniec|| (start.before(b) && koniec.after(b)) ||
                        (start.before(e) && koniec.after(e)) ||
                        (start.before(b) && koniec.after(e))) {
                    alert.setText("Sala zajęta przez seans: \n" + seans.toString());
                    bool = false;
                }
            }
        }
        return bool;
    }

        public boolean sprawdzDate() {
        String string = data_txt.getText() + "."+ godzina.getText();
        DateFormat format = new SimpleDateFormat("d.MM.yyyy.HH:mm", new Locale("pl"));
        format.setLenient(false);
        Date date = null;
        try {
            date = format.parse(string);
        } catch (ParseException e1) {
            e1.getSuppressed();
            return false;
        }
        alert.setText("");
        return true;
    }
    public void dodaj(ActionEvent actionEvent) throws ParseException {
        if (filmy.getValue()==null) {
            alert.setText("Wybierz film!");
        } else if (sale.getValue()==null) {
            alert.setText("Wybierz salę!");
        }else if (!sprawdzDate()) {
            alert.setText("Błędna data lub godzina!");
        } else if (sprawdzDostepnoscSali(sale.getValue(),stworzDate(),filmy.getValue().getCzas())) {

            Seans seans = new Seans(filmy.getValue(), sale.getValue(), stworzDate());

            System.out.println(sale.getValue().getSeansList()!= null ? sale.getValue().getSeansList(): "No list!");
            dodajDoBazy(seans);
            sale.getValue().getSeansList().add(seans);
        }

    }

    public void dodajDoBazy(Seans sn) {
        try (Session ses = parentController.getTabelaDane().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.save(sn);
            ses.update(sn.getSala());
            ses.getTransaction().commit();
            tabelka.getItems().add(sn);

        } catch (HibernateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.show();
        }
    }

    public void setParentController(MainController parentController) {
        this.parentController = parentController;
        tabelka.setEditable(true);
        tabelka.setItems(parentController.getTabelaDane().getSeanse());
    }

    public MainController getParentController() {
        return parentController;
    }

   public void initialize() {
         /*godzina.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    godzina.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });*/



        for (TableColumn<Seans, ?> seansTableColumn : tabelka.getColumns()) {
            if ("film".equals(seansTableColumn.getId())) {
                seansTableColumn.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getFilm().getNazwa()));
            }  else if ("data".equals(seansTableColumn.getId())) {
                Format formatter = new SimpleDateFormat("dd-MM-yyyy,  HH:mm");
                seansTableColumn.setCellValueFactory(cell -> new SimpleObjectProperty(formatter.format(cell.getValue().getDate())));
            } else if ("sala".equals(seansTableColumn.getId())) {
                seansTableColumn.setCellValueFactory(cell -> new SimpleObjectProperty(cell.getValue().getSala().getNumer()));
            }
        }

    }

    public void zapisz(ActionEvent actionEvent) {
        ArrayList<Seans> seanseList = new ArrayList<>(tabelka.getItems());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data.obj"))) {
            oos.writeObject(seanseList);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uwaga na serializację: https://sekurak.pl/java-vs-deserializacja-niezaufanych-danych-i-zdalne-wykonanie-kodu-czesc-i/
     */
    public void wczytaj(ActionEvent actionEvent) {
        ArrayList<Seans> seanseList;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data.obj"))) {
            seanseList = (ArrayList<Seans>) ois.readObject();
            tabelka.getItems().clear();
            tabelka.getItems().addAll(seanseList);
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void synchronizuj(ActionEvent actionEvent) {
        parentController.getTabelaDane().setSeanse(tabelka.getItems());
    }

    public void dodajJesliEnter(KeyEvent keyEvent) throws ParseException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            dodaj(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
        }
    }

        public void aktualizujComboBoxy(){
            filmy.getItems().setAll(getParentController().getTabelaDane().getFilmy());
            sale.getItems().setAll(getParentController().getTabelaDane().getSala());

        }
}