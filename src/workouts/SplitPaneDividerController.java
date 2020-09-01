package workouts;

import javafx.scene.control.SplitPane;

public interface SplitPaneDividerController {
    public default void disableSplitPaneDivider(SplitPane splitPane, double pos) {
        splitPane.getDividers().get(0).positionProperty().addListener((observable, oldValue, newValue) -> splitPane.getDividers().get(0).setPosition(pos));
    }
}