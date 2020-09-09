package main;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.apache.commons.io.FileUtils;

public class MainController implements Initializable {
    protected static final DataBase dataBase = new DataBase();
    @FXML
    private CalendarController calendarMenuController;
    @FXML
    private WorkoutsController workoutsMenuController;
    @FXML
    private StatisticsController statisticsMenuController;
    @FXML
    private SplitPane splitPane;
    private final List<Pane> panes = new ArrayList<>();
    @FXML
    private StackPane menuPane, logoPane;
    private boolean first = true;
    private String logoName;
    public void disableSplitPaneDivider(SplitPane splitPane, double pos) {
        splitPane.getDividers().get(0).positionProperty().addListener((observable, oldValue, newValue) -> splitPane.getDividers().get(0).setPosition(pos));
    }
    public void setUpMainMenu() {
        TreeItem<String> mainMenu = new TreeItem<>("Menu");
        TreeView<String> treeViewMain = new TreeView<>(mainMenu);
        treeViewMain.setShowRoot(false);
        treeViewMain.setStyle("-fx-font-size:30");
        treeViewMain.setFocusTraversable(false);
        treeViewMain.setPadding(new Insets(50, 0, 0, 0));
        TreeItem<String> calendarMenu = new TreeItem<>("Calendar", icon("/calendarIcon.png"));
        panes.add(calendarMenuController.calendarPane);
        TreeItem<String> workoutsMenu = new TreeItem<>("Workouts", icon("/workoutsIcon.png"));
        panes.add(workoutsMenuController.workoutsPane);
        TreeItem<String> statisticsMenu = new TreeItem<>("Statistics", icon("/statisticsIcon.png"));
        panes.add(statisticsMenuController.statisticsPane);
        TreeItem<String> exitMenu = new TreeItem<>("Exit", icon("/exitIcon.png"));
        mainMenu.getChildren().addAll(calendarMenu, workoutsMenu, statisticsMenu, exitMenu);
        menuPane.getChildren().add(treeViewMain);
        treeViewMain.getSelectionModel().selectFirst();
        treeViewMain.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getValue()) {
                case "Calendar" :
                    setUpActivePane(calendarMenuController.calendarPane);
                    break;
                case "Workouts" :
                    workoutsMenuController.setAlert(false);
                    setUpActivePane(workoutsMenuController.workoutsPane);
                    workoutsMenuController.treeViewWorkouts.getSelectionModel().clearSelection();
                    workoutsMenuController.deleteButton.setDisable(true);
                    workoutsMenuController.setVisibility(false);
                    break;
                case "Statistics" :
                    setUpActivePane(statisticsMenuController.statisticsPane);
                    break;
                case "Exit" :
                    System.exit(0);
                    break;
            }
        });
    }
    public ImageView icon(String icon) {
        return new ImageView(new Image(getClass().getResourceAsStream(icon)));
    }
    public void setUpActivePane(Pane pane) {
        panes.forEach(p -> p.setVisible(p == pane ? true : false));
    }
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableSplitPaneDivider(splitPane, 0.1525);
        setUpMainMenu();
        setLogo();
        disableSplitPaneDivider(workoutsMenuController.workoutsSplitPane, 0.177);
    }
}