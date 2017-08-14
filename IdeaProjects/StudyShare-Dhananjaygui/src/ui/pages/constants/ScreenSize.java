package ui.pages.constants;

import javafx.stage.Screen;

public interface ScreenSize {
    static final double MAX_SCREEN_WIDTH=Screen.getPrimary().getBounds().getWidth();
    static final double MAX_SCREEN_HEIGHT=Screen.getPrimary().getBounds().getHeight();
}
