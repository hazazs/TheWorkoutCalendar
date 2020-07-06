package workouts;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FileUtils;

public class WorkoutsMainController implements Initializable {
    private final DataBase dataBase = new DataBase();
    private boolean first = true;
    private String logoName, selectedMenuItem;
    @FXML
    private StackPane logoPane, menuPane, workoutsMenuPane;
    private final List<Pane> panes = new ArrayList<>();
    @FXML
    private Label labelName, labelLength, hintLabel;
    @FXML
    private VBox workoutFields;
    @FXML
    private ImageView hintIconA, hintIconB;
    @FXML
    private Pane calendarPane, workoutsPane, statisticsPane;
    private TreeView<String> treeViewWorkouts;
    @FXML
    private Button deleteButton;
    private Map<String, Integer> workouts;
    @FXML
    private TextField workoutName, workoutLength;
    private boolean isNew;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private SplitPane splitPane, workoutsSplitPane;
    private StackPane alertPane;
    public void setLogo() {
        try {
            if (first) logoName = FileUtils.readFileToString(new File("default.txt"), StandardCharsets.UTF_8);
            List<String> logos = new ArrayList<>();
            for (File file : new File("logos").listFiles())
                logos.add(file.getName());
            if (!logos.isEmpty()) {
                int index = logos.indexOf(logoName);
                if (!first && index + 1 != logos.size()) logoName = logos.get(index + 1);
                else if (!first || index == -1) logoName = logos.get(0);
            }
            FileWriter fw = new FileWriter(new File("default.txt"), false);
            fw.write(logos.isEmpty() ? "" : logoName);
            fw.close();
            ImageView iw = new ImageView(new Image(logos.isEmpty() ? "logo.png" : "file:logos/" + logoName));
            iw.setPreserveRatio(true);
            if (iw.getImage().getHeight() > 104.0) iw.setFitHeight(104.0);
            if (iw.getImage().getWidth() > 1334.0) iw.setFitWidth(1334.0);
            logoPane.getChildren().clear();
            logoPane.getChildren().add(iw);
            if (first) first = false;
        } catch (Exception ex) {
            System.out.println(ex);
          }
    }
    public void disableSplitPaneDivider(SplitPane splitPane, double pos) {
        splitPane.getDividers().get(0).positionProperty().addListener((observable, oldValue, newValue) -> splitPane.getDividers().get(0).setPosition(pos));
    }
    public ImageView icon(String icon) {
        return new ImageView(new Image(getClass().getResourceAsStream(icon)));
    }
    public void setUpActivePane(Pane pane) {
        panes.forEach(p -> p.setVisible(p == pane ? true : false));
    }
    public void setVisibility(boolean b) {
        labelName.setVisible(b);
        labelLength.setVisible(b);
        workoutFields.setVisible(b);
        hintIconA.setVisible(b);
        hintIconB.setVisible(b);
    }
    public void setUpMainMenu() {
        TreeItem<String> mainMenu = new TreeItem<>("Menu");
        TreeView<String> treeViewMain = new TreeView<>(mainMenu);
        treeViewMain.setShowRoot(false);
        treeViewMain.setStyle("-fx-font-size:30");
        treeViewMain.setFocusTraversable(false);
        treeViewMain.setPadding(new Insets(50, 0, 0, 0));
        TreeItem<String> calendarMenu = new TreeItem<>("Calendar", icon("/calendarIcon.png"));
        panes.add(calendarPane);
        TreeItem<String> workoutsMenu = new TreeItem<>("Workouts", icon("/workoutsIcon.png"));
        panes.add(workoutsPane);
        TreeItem<String> statisticsMenu = new TreeItem<>("Statistics", icon("/statisticsIcon.png"));
        panes.add(statisticsPane);
        TreeItem<String> exitMenu = new TreeItem<>("Exit", icon("/exitIcon.png"));
        mainMenu.getChildren().addAll(calendarMenu, workoutsMenu, exitMenu);
        menuPane.getChildren().add(treeViewMain);
        treeViewMain.getSelectionModel().selectFirst();
        treeViewMain.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getValue()) {
                case "Calendar" :
                    setUpActivePane(calendarPane);
                    break;
                case "Workouts" :
                    setUpActivePane(workoutsPane);
                    treeViewWorkouts.getSelectionModel().clearSelection();
                    deleteButton.setDisable(true);
                    setVisibility(false);
                    break;
                case "Statistics" :
                    setUpActivePane(statisticsPane);
                    break;
                case "Exit" :
                    System.exit(0);
                    break;
            }
        });
    }
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
            anchorPane.requestFocus();
        });
    }
    public void setUpTabKeyPress(TextField from, TextField to) {
        from.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                to.requestFocus();
                event.consume();
            }
        });
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
                } else anchorPane.requestFocus();
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
    public void btnAction(Button btn) {
        btn.setOnAction(event -> {
            splitPane.setDisable(false);
            splitPane.setOpacity(1);
            alertPane.setVisible(false);
            if (btn.getText().equals("YES")) {
                dataBase.cruD(selectedMenuItem);
                setVisibility(false);
                deleteButton.setDisable(true);
                setUpWorkoutsMenu();
            }
            event.consume();
        });
    }
    public void popUp(String popUp) {
        splitPane.setDisable(true);
        splitPane.setOpacity(0.3);
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
        vBox.setStyle("-fx-border-color:#767676");
        alertPane = new StackPane(vBox);
        anchorPane.getChildren().add(alertPane);
        anchorPane.setLeftAnchor(alertPane, 240.0);
        anchorPane.setRightAnchor(alertPane, 0.0);
        anchorPane.setTopAnchor(alertPane, 0.0);
        anchorPane.setBottomAnchor(alertPane, 0.0);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setLogo();
        disableSplitPaneDivider(splitPane, 0.1525);
        disableSplitPaneDivider(workoutsSplitPane, 0.177);
        setUpMainMenu();
        setUpWorkoutsMenu();
        setUpTabKeyPress(workoutName, workoutLength);
        setUpTabKeyPress(workoutLength, workoutName);
    }
}