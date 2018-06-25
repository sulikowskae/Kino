package sample;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MainController implements HierarchicalController<MainController> {

    public Pane srodek;

    protected TabelaDane tabelaDane;

    public TabelaDane getTabelaDane() {
        return tabelaDane;
    }

    public MainController() {
        this.tabelaDane = new TabelaDane();
    }

    public void film(ActionEvent actionEvent) {
        loadIntoPane("filmy.fxml");
    }

    public void sala(ActionEvent actionEvent) {
        loadIntoPane("sale.fxml");
    }

    public void seans(ActionEvent actionEvent) {
        loadIntoPane("seanse.fxml");
    }

    private void loadIntoPane(String fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        try {
            final BorderPane load = loader.load();
            HierarchicalController<MainController> daneController = loader.getController();
            daneController.setParentController(this);
            srodek.getChildren().clear();
            srodek.getChildren().add(load);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public MainController getParentController() {
        return this;
    }

    @Override
    public void setParentController(MainController parent) {

    }
}