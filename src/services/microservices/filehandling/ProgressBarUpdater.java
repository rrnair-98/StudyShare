package services.microservices.filehandling;

import javafx.scene.control.ProgressBar;

import java.io.IOException;
/*
@author
Interface implemented by any class should set its progress bar reference to the received object's reference
 */
public interface ProgressBarUpdater {
    public void setProgress(ProgressBar pgb) ;
}
