package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import static main.MainController.log;

public class MainClass extends Application {
    private Parent anchorPane;
    private final String fxml = "Main.fxml";
    @Override
    public void start(Stage stage) {       
        try {
            anchorPane = FXMLLoader.load(getClass().getResource(fxml));
        } catch (IOException ex) {
            log.error("Something went wrong while trying to load the .fxml file: " + ex);
          }
        Scene scene = new Scene(anchorPane);
        scene.getStylesheets().addAll("css/style.css", "https://bit.ly/2RRFUKc");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/calendarIconWhite.png")));
        stage.setTitle("THE WORKOUT CALENDAR 1.7.0 by hazazs®");
        stage.setResizable(false);
        stage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {if (event.getCode() == KeyCode.F12) System.exit(0);});
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}