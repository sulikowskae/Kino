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
        tabelaDane = new TabelaDane();
    }

    public void film(ActionEvent actionEvent) {
        loadIntoPane("filmy.fxml");
    }

    public void sala(ActionEvent actionEvent) {
        loadIntoPane("sale.fxml");
    }

    private void loadIntoPane(String fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        try {
            final BorderPane load = loader.load();
            srodek.getChildren().clear();
            srodek.getChildren().add(load);
            HierarchicalController<MainController> daneController = loader.getController();
            daneController.setParentController(this);
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