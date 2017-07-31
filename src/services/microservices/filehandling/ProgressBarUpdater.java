package services.microservices.filehandling;

import javafx.scene.control.ProgressBar;

import java.io.IOException;

public interface ProgressBarUpdater {
    public void setProgress(ProgressBar pgb) ;
}
