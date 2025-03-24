package com.zodiacgroup.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file for the main view
        Parent root = FXMLLoader.load(getClass().getResource("/com/zodiacgroup/view/dashboard.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Zodiac Group - Customer Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}