package insanity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainClass extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("InsanityView.fxml")); //without "/" it will search the resource among the .class files
        //getResource() returns a URL
        Scene scene = new Scene(anchorPane);
        scene.getStylesheets().add("Insanity.css");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/InsanityLogoWhite.png"))); //with "/" it will search the resource in the /classes folder
        //getResourceAsStream() return an InputStream
        //it basically invokes getResource() to get the URL, and then opens that URL's contents as an InputStream (getResource().openStream())
        stage.setTitle("INSANITY 1.7.0 by hazazs®");
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}