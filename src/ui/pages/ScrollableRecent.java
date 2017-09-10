package ui.pages;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.ScrollPane;
import ui.pages.constants.BasicController;

import java.util.ArrayList;

public class ScrollableRecent implements BasicController {

    //Supporting objects
    @FXML ScrollPane sp;
    @FXML Button btnClick;
    @FXML FlowPane fp;
    @FXML AnchorPane ap;

    ArrayList myArray = new ArrayList();

    //   Disabling scrollbars
    public void initialize(){
      //  sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    //recents tab
    //method which will create buttons(Groups) which is accepting arraylist
    public void getNumberOfRecentGroups(ArrayList arr) throws Exception {
        int arraySize = arr.size();
        int z = 0;

        Button[] btn = new Button[arraySize];
        while (z < arraySize) {
            btn[z] = new Button();

            //Anonymous method
            btn[z].setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    System.out.println("hey i was clicked");
                }
            });
            btn[z].setId("btnRecent");
            btn[z].setPrefHeight(118);
            btn[z].setPrefWidth(118);
            btn[z].setText(arr.get(z) + "");
            fp.getChildren().add(btn[z]);
            z++;
        }
        fp.setHgap(10);
    }

    //will perform acion when the button will get cicked
    @FXML
    public void createRecentsButtons(ActionEvent ae) throws  Exception {

        // do your job of creating Buttons and pass the arraylist as function parameters
        for(int k=0; k<11; k++)
            myArray.add(k);
        this.getNumberOfRecentGroups(myArray);
        System.out.println("I was clicked");
    }

    public Node getRoot(){
        refreshPage();
        return ap;
    }
    public void setPageKeeper(PageKeeper pg){
     /*Not required */
    }
    public void refreshPage(){
//        plz refresh it
    }


}





