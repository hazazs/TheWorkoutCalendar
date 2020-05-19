package insanity;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

public class InsanityViewController implements Initializable {
    private final DataBase dataBase = new DataBase();
    private final ArrayList<Pane> panes = new ArrayList<Pane>();
    private TreeView<String> treeView2;
    private String selectedMenuItem;
    private boolean isNew;
    private StackPane alertPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private SplitPane splitPane, workoutsSplitPane;
    @FXML
    private StackPane menuPane, workoutsMenuPane;
    @FXML
    private Pane calendarPane, workoutsPane, statisticsPane;
    @FXML
    private Button deleteButton;
    @FXML
    private VBox workoutFields;
    @FXML
    private Label labelName, labelLength, hintName, hintLength;
    @FXML
    private TextField workoutName, workoutLength;
    @FXML
    private ImageView hintIconA, hintIconB;
    public void disableSplitPaneDivider(SplitPane fSplitPane, double dPos) {
        fSplitPane.getDividers().get(0).positionProperty().addListener((observable, oldValue, newValue) -> fSplitPane.getDividers().get(0).setPosition(dPos));
    }
    public ImageView icon(String fIcon) {
        return new ImageView(new Image(getClass().getResourceAsStream(fIcon)));
    }
    public void setUpActivePane(Pane pane) {
        for (Pane p : panes)
            p.setVisible(p == pane ? true : false);
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
        TreeView<String> treeView = new TreeView<>(mainMenu);
        treeView.setShowRoot(false);
        treeView.getStylesheets().add("https://bit.ly/2RRFUKc");
        treeView.setStyle("-fx-font-size:30");
        treeView.setFocusTraversable(false);
        treeView.setPadding(new Insets(50, 0, 0, 0));
        TreeItem<String> calendarMenu = new TreeItem<>("Calendar", icon("/calendarIcon.png"));
        panes.add(calendarPane);
        TreeItem<String> workoutsMenu = new TreeItem<>("Workouts", icon("/workoutsIcon.png"));
        panes.add(workoutsPane);
        TreeItem<String> statisticsMenu = new TreeItem<>("Statistics", icon("/statisticsIcon.png"));
        panes.add(statisticsPane);
        TreeItem<String> exitMenu = new TreeItem<>("Exit", icon("/exitIcon.png"));
        mainMenu.getChildren().addAll(calendarMenu, workoutsMenu, statisticsMenu, exitMenu);
        menuPane.getChildren().add(treeView);
        treeView.getSelectionModel().selectFirst();
        treeView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<TreeItem<String>>() {
                @Override
                public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                    switch (newValue.getValue()) {
                        case "Calendar" :
                            setUpActivePane(calendarPane);
                            break;
                        case "Workouts" :
                            setUpActivePane(workoutsPane);
                            treeView2.getSelectionModel().clearSelection();
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
                }
            }
        );
    }
    public void setUpWorkoutsMenu() {
        if (!workoutsMenuPane.getChildren().isEmpty())
            workoutsMenuPane.getChildren().remove(0);
        TreeItem<String> workoutsMenu = new TreeItem<>("Workouts Menu");
        treeView2 = new TreeView<>(workoutsMenu);
        treeView2.setShowRoot(false);
        treeView2.getStylesheets().add("https://bit.ly/2RRFUKc");
        treeView2.setStyle("-fx-font-size:13");
        treeView2.setFocusTraversable(false);
        ArrayList<String> workouts = dataBase.cRud();
        for (int i = 0; i < workouts.size(); i++)
            workoutsMenu.getChildren().add(new TreeItem<>(workouts.get(i)));
        workoutsMenuPane.getChildren().add(treeView2);
        treeView2.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<TreeItem<String>>() {
                @Override
                public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                    try {
                    selectedMenuItem = newValue.getValue();
                    workoutName.setText(selectedMenuItem);
                    workoutLength.setText(dataBase.getLength(selectedMenuItem));
                    } catch (Exception ex) {
                      }
                    isNew = false;
                    deleteButton.setDisable(false);
                    workoutName.setStyle("-fx-border-color: c8c8c8");
                    workoutLength.setStyle("-fx-border-color: c8c8c8");
                    setVisibility(true);
                    anchorPane.requestFocus();
                }
            }
        );
    }
    public void setUpTabKeyPress(TextField fFrom, TextField fTo) {
        fFrom.addEventFilter(
            KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.TAB) {
                        fTo.requestFocus();
                        event.consume();
                    }
                }
            }
        );
    }
    public void showNameHint() {
        hintName.setVisible(true);
    }
    public void hideNameHint() {
        hintName.setVisible(false);
    }
    public void showLengthHint() {
        hintLength.setVisible(true);
    }
    public void hideLengthHint() {
        hintLength.setVisible(false);
    }
    public void newButton() { 
        treeView2.getSelectionModel().clearSelection();
        isNew = true;
        deleteButton.setDisable(true);
        workoutName.setStyle("-fx-border-color: c8c8c8");
        workoutLength.setStyle("-fx-border-color: c8c8c8");
        setVisibility(true);
        workoutName.clear();
        workoutLength.clear();
        workoutName.requestFocus();
    }
    public void btnAction(Button btn) {
        btn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    splitPane.setDisable(false);
                    splitPane.setOpacity(1);
                    alertPane.setVisible(false);
                    if (btn.getText().equals("YES")) {
                        deleteButton.setDisable(true);
                        setVisibility(false);
                        dataBase.cruD(selectedMenuItem);
                        setUpWorkoutsMenu();
                    }
                }
            }
        );
    }
    public void popUp(String fPopUp) {
        splitPane.setDisable(true);
        splitPane.setOpacity(0.3);
        Label lbl = new Label();
        lbl.setMinSize(304, 26);
        lbl.setStyle("-fx-background-color:#767676");
        lbl.setGraphic(fPopUp.equals("delete") ? new ImageView(new Image(getClass().getResourceAsStream("/deleteIcon.png"))) : new ImageView(new Image(getClass().getResourceAsStream("/updateIcon.png"))));
        Button btnY = new Button("YES");
        Button btnN = new Button("NO");
        Button btnO = new Button("OK");
        HBox hBox = new HBox(btnY, btnN);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(30);
        VBox vBox = fPopUp.equals("delete") ? new VBox(lbl, new Label("Are you sure you want to delete this workout?"), hBox) : new VBox(lbl, new Label("Workout is successfully updated."), btnO);
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
        btnAction(btnY);
        btnAction(btnN);
        btnAction(btnO);
    }
    public void deleteButton() {
        popUp("delete");
    }
    public void saveButton() {
        workoutName.setStyle("-fx-border-color: c8c8c8");
        workoutLength.setStyle("-fx-border-color: c8c8c8");
        boolean isInteger = true;
        try {
            int intCheck = Integer.parseInt(workoutLength.getText());
            if (intCheck < 1)
                isInteger = false;
        } catch (Exception ex) {
            isInteger = false;
        }
        ArrayList<String> workouts = dataBase.cRud();
        int i = 0;
        while (i < workouts.size() && (!isNew || !workoutName.getText().equals(workouts.get(i))) && (isNew || !workoutName.getText().equals(workouts.get(i)) || workoutName.getText().equals(selectedMenuItem)))
            i++;
        if (workoutName.getText().length() > 0 && i == workouts.size() && isInteger) {
            if (isNew) {
                setVisibility(false);
                dataBase.Crud(workoutName.getText(), Integer.parseInt(workoutLength.getText()));
                setUpWorkoutsMenu();
            } else {
                if (!selectedMenuItem.equals(workoutName.getText()) || !dataBase.getLength(selectedMenuItem).equals(workoutLength.getText())) {
                    int index = treeView2.getSelectionModel().getSelectedIndex();
                    dataBase.crUd(selectedMenuItem, workoutName.getText(), Integer.parseInt(workoutLength.getText()));
                    setUpWorkoutsMenu();
                    treeView2.getSelectionModel().select(index);
                    popUp("update");
                } else anchorPane.requestFocus();
              }
        } else {
            if (!isInteger) {
                workoutLength.setStyle("-fx-border-color: red");
                workoutLength.requestFocus();
            }
            if (workoutName.getText().length() == 0 || i < workouts.size()) {
                workoutName.setStyle("-fx-border-color: red");
                workoutName.requestFocus();
            }
          }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableSplitPaneDivider(splitPane, 0.1525);
        disableSplitPaneDivider(workoutsSplitPane, 0.177);
        setUpMainMenu();
        setUpWorkoutsMenu();
        setUpTabKeyPress(workoutName, workoutLength);
        setUpTabKeyPress(workoutLength, workoutName);
    }
}