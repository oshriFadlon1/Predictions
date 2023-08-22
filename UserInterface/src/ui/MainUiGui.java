package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainUiGui extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("\\javaFx\\scenes\\sceneMenu1.fxml"));
        primaryStage.setTitle("jWorld");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
