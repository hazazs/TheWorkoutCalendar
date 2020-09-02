package calendar;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainClass extends Application {
    private Parent anchorPane;
    private final String fxml = "WorkoutsMain.fxml";
    @Override
    public void start(Stage stage) {       
        try {
            anchorPane = FXMLLoader.load(getClass().getResource(fxml));
        } catch (IOException ex) {
            System.out.println("Error when trying to load " + fxml);
          }
        Scene scene = new Scene(anchorPane);
        scene.getStylesheets().addAll("css/style.css", "https://bit.ly/2RRFUKc");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/calendarIconWhite.png")));
        stage.setTitle("THE WORKOUT CALENDAR 1.7.0 by hazazs®");
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}