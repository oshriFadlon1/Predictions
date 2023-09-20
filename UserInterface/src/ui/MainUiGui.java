package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.sceneController.SceneMenu;
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
        VBox root = loader.load();
        SceneMenu sceneMenu = loader.getController();

        sceneMenu.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Prediction");
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add("/ui/cssDesign/homePageDesign.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
