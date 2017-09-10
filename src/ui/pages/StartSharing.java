//Author@: Dhiren

//Author@: Dhiren

package ui.pages;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import org.controlsfx.control.StatusBar;
import ui.core.Server;
import ui.pages.constants.BasicController;
import javafx.geometry.Insets;

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
    StatusBar sp[] =new StatusBar[20];
    Button btn[] = new Button[20];
    String groupName;
    Server server;
    int i;
    float val;
    //this method is used to initialize all the necessary components before the fxml is displayed
    public void initialize(){

        FontAwesomeIconView btn_back = new FontAwesomeIconView(FontAwesomeIcon.MAIL_REPLY);
        back.setGraphic(btn_back);
        back.setTooltip(new Tooltip("Back"));
        btn_back.setFill(Color.web("#1b3737"));
        btn_back.setId("share_btnBackIcon");

        MaterialDesignIconView btn_share = new MaterialDesignIconView(MaterialDesignIcon.RADIO_TOWER);
        startShare.setGraphic(btn_share);
        startShare.setTooltip(new Tooltip("Start Sharing"));
        btn_share.setFill(Color.web("#1b3737"));
        btn_share.setId("share_btnShareIcon");


        //populating the status bars in the scrollpane. CHANGE 20 HERE by obtaining the total no of users in that group from db
        for( i=0;i<20;i++){
            btn[i]=new Button("x");
            btn[i].setId("share_btnCancel");
            sp[i]=new StatusBar();
            sp[i].setProgress(0.5);
            sp[i].setText("Status Bar number " + (i+1));
            sp[i].getRightItems().add(btn[i]);
            sp[i].setId("share_StatusBar");
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


    public  void setAll(String groupName, Server server){
        this.groupName=groupName;
        this.server=server;
    }

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
