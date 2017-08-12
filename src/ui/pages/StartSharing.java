//Author@: Dhiren

package ui.pages;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.StatusBar;
import ui.pages.constants.BasicController;

public class StartSharing implements BasicController{
    //Supporting objects
    ObservableList<StatusBar> items = FXCollections.observableArrayList ();

    ////Objects required for this file
    @FXML
    ListView<StatusBar> listView;
    @FXML
    Button startShare;
    @FXML
    Button back;
    @FXML
    AnchorPane myRoot;
    StackPane myStackPane;
    StatusBar sp[] =new StatusBar[20] ;

    //this method is used to initialize all the necessary components before the fxml is displayed
    public void initialize(){

        //populating the status bars in the scrollpane. CHANGE 20 HERE by obtaining the total no of users in that group from db
        for(int i=0;i<20;i++){
            sp[i]=new StatusBar();
            sp[i].setProgress(0.5);
            sp[i].setText("Status Bar number " + (i+1));
            sp[i].getRightItems().add(new Button("x"));
            sp[i].setStyle("fx-padding:0 0 10 0");
        }

        //populating the list view with status bars
        items.addAll(sp);
        listView.setFixedCellSize(50);
        listView.setItems(items);
        listView.setDisable(true);

        //this event handler function displays the user list of the groups that is clicked by the server. For now the list view is disabled clicking
        //this button will enable the sharing and start sharing the files
        startShare.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                listView.setDisable(false);
            }
        });

        //this event handler function hides/removes the user list and shows the groups created by that server. In short the back button used to track down
        //the previous page
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myStackPane.getChildren().remove(myRoot);

            }
        });
    }


//    ********** getters for this file **********


    public Button getStartShare() {
        return startShare;
    }

    public Button getClose() {
        return back;
    }

    public AnchorPane getMyRoot() {
        return myRoot;
    }

    public StackPane getMyStackPane() {
        return myStackPane;
    }

    public StatusBar getStatusBar(String name) {
        int i;
        for( i=0;i<20;i++)
        {
            if(sp[i].getText().equals(name))
                break;
        }
        return sp[i];
    }

    //    ********** setters for this file **********


    public void setStartShare(Button startShare) {
        this.startShare = startShare;
    }

    public void setClose(Button back) {
        this.back = back;
    }

    public void setMyRoot(AnchorPane ap)
    {

        myRoot=ap;
    }
    public void setMyStackPane(StackPane sp)
    {
        myStackPane=sp;
    }

    public Node getRoot(){
        return myRoot;
    }
    public void setPageKeeper(PageKeeper pg){
//        Not required here
    }
    public void refreshPage(){
//        hey code for dhiren

    }
}
