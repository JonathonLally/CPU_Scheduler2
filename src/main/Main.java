package main;

import controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {         //Starts Application

    @Override
    public void start(Stage primaryStage) throws Exception{         //Loads GUI, starting with MainView.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();
        MainViewController mainCtrl = loader.getController();
        primaryStage.setTitle("CPU Scheduler");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}