package main.workouts;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import static main.MainController.dataBase;

public class WorkoutsController implements Initializable {
    @FXML
    public SplitPane workoutsSplitPane;
    @FXML
    public Pane workoutsPane;
    @FXML
    private StackPane workoutsMenuPane, alertBackgroundPane, alertPane;
    public TreeView<String> treeViewWorkouts;
    private Map<String, Integer> workouts;
    private String selectedMenuItem;
    @FXML
    private TextField workoutName, workoutLength;
    private boolean isNew;
    @FXML
    public Button deleteButton;
    @FXML
    private Label labelName, labelLength, hintLabel;
    @FXML
    private VBox workoutFields;
    @FXML
    private ImageView hintIconA, hintIconB;
    public void setUpWorkoutsMenu() {
        if (!workoutsMenuPane.getChildren().isEmpty())
            workoutsMenuPane.getChildren().remove(0);
        TreeItem<String> workoutsMenu = new TreeItem<>("Workouts Menu");
        treeViewWorkouts = new TreeView<>(workoutsMenu);
        treeViewWorkouts.setShowRoot(false);
        treeViewWorkouts.setStyle("-fx-font-size:13");
        treeViewWorkouts.setFocusTraversable(false);
        workouts = new LinkedHashMap<>(dataBase.cRud());
        workouts.forEach((name, length) -> workoutsMenu.getChildren().add(new TreeItem<>(name)));
        treeViewWorkouts.setMaxHeight(4 + treeViewWorkouts.getExpandedItemCount() * 25);
        workoutsMenuPane.getChildren().add(treeViewWorkouts);
        treeViewWorkouts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                selectedMenuItem = newValue.getValue();
                workoutName.setText(selectedMenuItem);
                workoutLength.setText(String.valueOf(workouts.get(selectedMenuItem)));
            } catch (NullPointerException ex) {
              }
            isNew = false;
            deleteButton.setDisable(false);
            workoutName.setStyle("-fx-border-color: c8c8c8");
            workoutLength.setStyle("-fx-border-color: c8c8c8");
            setVisibility(true);
            workoutsMenuPane.requestFocus();
        });
    }
    public void setVisibility(boolean b) {
        labelName.setVisible(b);
        labelLength.setVisible(b);
        workoutFields.setVisible(b);
        hintIconA.setVisible(b);
        hintIconB.setVisible(b);
    }
    public void setUpTabKeyPress(TextField tf1, TextField tf2) {
        tf1.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                tf2.requestFocus();
                event.consume();
            }
        });
        tf2.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                tf1.requestFocus();
                event.consume();
            }
        });
    }
    public void newButton() {
        treeViewWorkouts.getSelectionModel().clearSelection();
        isNew = true;
        deleteButton.setDisable(true);
        workoutName.setStyle("-fx-border-color: c8c8c8");
        workoutLength.setStyle("-fx-border-color: c8c8c8");
        setVisibility(true);
        workoutName.clear();
        workoutLength.clear();
        workoutName.requestFocus();
    }
    public void deleteButton() {
        popUp("delete");
    }
    public void popUp(String popUp) {
        VBox vBox;
        Label lbl = new Label();
        lbl.setMinSize(304, 26);
        lbl.setStyle("-fx-background-color:#767676");
        if (popUp.equals("delete")) {
            lbl.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/deleteIcon.png"))));
            Button btnY = new Button("YES");
            Button btnN = new Button("NO");
            HBox hBox = new HBox(btnY, btnN);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(30);
            vBox = new VBox(lbl, new Label("Are you sure you want to delete this workout?"), hBox);
            btnAction(btnY);
            btnAction(btnN);
        } else {
            lbl.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/updateIcon.png"))));
            Button btnO = new Button("OK");
            vBox = new VBox(lbl, new Label("Workout is successfully updated."), btnO);
            btnAction(btnO);
          }
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(0, 0, 15, 0));
        vBox.setAlignment(Pos.CENTER);
        vBox.setMaxSize(300, 105);
        vBox.setStyle("-fx-background-color:#f4f4f4; -fx-border-color:#767676");
        alertPane.getChildren().clear();
        alertPane.getChildren().add(vBox);
        setAlert(true);
    }
    public void btnAction(Button btn) {
        btn.setOnAction(event -> {
            setAlert(false);
            if (btn.getText().equals("YES")) {
                dataBase.cruD(selectedMenuItem);
                setVisibility(false);
                deleteButton.setDisable(true);
                setUpWorkoutsMenu();
            }
            event.consume();
        });
    }
    public void setAlert(boolean b) {
        alertBackgroundPane.setVisible(b);
        alertPane.setVisible(b);
    }
    public void saveButton() {
        workoutName.setStyle("-fx-border-color: c8c8c8");
        workoutLength.setStyle("-fx-border-color: c8c8c8");
        if (validName() && validLength()) {
            if (isNew) {
                dataBase.Crud(workoutName.getText(), Integer.parseInt(workoutLength.getText()));
                setVisibility(false);
                setUpWorkoutsMenu();
            } else {
                if (!(selectedMenuItem.equals(workoutName.getText()) && String.valueOf(workouts.get(selectedMenuItem)).equals(workoutLength.getText()))) {
                    dataBase.crUd(selectedMenuItem, workoutName.getText(), Integer.parseInt(workoutLength.getText()));
                    setUpWorkoutsMenu();
                    treeViewWorkouts.getSelectionModel().select(new ArrayList<>(workouts.keySet()).indexOf(workoutName.getText()));
                    popUp("update");
                } else workoutsMenuPane.requestFocus();
              }
        } else {
            if (!validName()) {
                workoutName.setStyle("-fx-border-color: red");
                workoutName.requestFocus();
                if (!validLength())
                    workoutLength.setStyle("-fx-border-color: red");
            } else {
                workoutLength.setStyle("-fx-border-color: red");
                workoutLength.requestFocus();
              }
          }
    }
    public boolean validName() {
        boolean duplicate = workouts.containsKey(workoutName.getText());
        return !(workoutName.getText().length() == 0 || (isNew && duplicate) || (!isNew && duplicate && !workoutName.getText().equals(selectedMenuItem)));
    }
    public boolean validLength() {
        try {
            return Integer.parseInt(workoutLength.getText()) >= 1;
        } catch (NumberFormatException ex) {
            return false;
          }
    }
    public void showNameHint() {
        hintLabel.setText("Workout name must be unique and at least one character.");
        hintLabel.setVisible(true);
    }
    public void showLengthHint() {
        hintLabel.setText("Workout length must be numeric and at least one minute.");
        hintLabel.setVisible(true);
    }
    public void hideHint() {
        hintLabel.setVisible(false);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setUpWorkoutsMenu();
        setUpTabKeyPress(workoutName, workoutLength);
    }
}