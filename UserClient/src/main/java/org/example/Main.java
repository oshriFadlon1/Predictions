package org.example;

import controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
//        String currentDirectory = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//        System.out.println("Current Directory: " + currentDirectory);
        URL mainFXML = getClass().getResource("/scenes/loginpagebasic.fxml");
        loader.setLocation(mainFXML);
        AnchorPane root = loader.load();
        LoginController loginController = loader.getController();

        loginController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Prediction");
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}