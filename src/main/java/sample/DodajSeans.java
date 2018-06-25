package sample;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DodajSeans implements HierarchicalController<MainController> {


    public ComboBox<Film> filmy = new ComboBox<>();
    public ComboBox<Sala> sale = new ComboBox<>();
    public TextField godzina;
    public TextField minuty;
    public TextField data;

    public TableView<Seans> tabelka;
    private MainController parentController;

   public Date stworzDate() {
       String string = data.getText();
       DateFormat format = new SimpleDateFormat("d.MM.yyyy", new Locale("pl"));
       Date date = null;
       try {
           date = format.parse(string);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       Integer h = Integer.parseInt(godzina.getText());
       Integer m = Integer.parseInt(minuty.getText());
       if (0<=h && h<24) {
           date.setHours(h);
       } else {

       }
       if (0<=m && m<60) {
           date.setMinutes(m);
       }
       System.out.println(date); //
       return date;
    }

    public void dodaj(ActionEvent actionEvent) {
        Date data = stworzDate();
        Seans seans = new Seans(filmy.getValue(),sale.getValue(), data);
        dodajDoBazy(data);
        tabelka.getItems().add(seans);
    }

    private void dodajDoBazy(Seans sn) {
        try (Session ses = parentController.getTabelaDane().getSessionFactory().openSession()) {
            ses.beginTransaction();
            ses.persist(sn);
            ses.getTransaction().commit();
        } catch (HibernateException e) {
            Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
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

        godzina.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    godzina.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });

        minuty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    minuty.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }

        });


        for (TableColumn<Seans, ?> salaTableColumn : tabelka.getColumns()) {
            if ("film".equals(salaTableColumn.getId())) {
                salaTableColumn.setCellValueFactory(new PropertyValueFactory<>("film"));
            }  else if ("data".equals(salaTableColumn.getId())) {
                salaTableColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
            } else if ("sala".equals(salaTableColumn.getId())) {
                salaTableColumn.setCellValueFactory(new PropertyValueFactory<>("sala"));
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

    public void dodajJesliEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            dodaj(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
        }
    }
    public void aktualizujComboBoxy(){
        filmy.setConverter(new StringConverter<Film>() {

            @Override
            public String toString(Film object) {
                return object.getNazwa();
            }

            @Override
            public Film fromString(String string) {
                return null;
            }
        });

        if (parentController.getTabelaDane().getFilmy()!= null) {
            filmy.setItems(parentController.getTabelaDane().getFilmy());
        }
        if (parentController.getTabelaDane().getSala()!= null) {
            sale.setItems(parentController.getTabelaDane().getSala());
        }
    }
}
