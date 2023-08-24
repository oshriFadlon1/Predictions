package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.sceneController.SceneMenu;
import ui.sceneController.detailsController.DetailsController;
import ui.sceneController.newExecutionController.NewExecutionController;

import java.net.URL;

public class MainUiGui extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        URL mainFXML = getClass().getResource("/ui/javaFx/scenes/sceneMenu.fxml");
        loader.setLocation(mainFXML);
        BorderPane root = loader.load();
        SceneMenu sceneMenu = loader.getController();

        sceneMenu.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Oshri ya maniac");
        Scene scene = new Scene(root, 1050, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
