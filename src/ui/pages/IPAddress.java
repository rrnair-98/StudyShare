package ui.pages;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ui.core.Comms;
import ui.pages.constants.BasicController;
import ui.pages.constants.PageConstants;
import ui.pages.utilities.ObjectCacher;

import java.net.Socket;

public class IPAddress implements BasicController {

    @FXML
    AnchorPane root;

    @FXML
    Button btn_Go;

    @FXML
    TextField txt_Address;
    /**********************************************/
    PageKeeper pageKeeper;

    @FXML  protected void initialize(){
        btn_Go.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*Validate IpAddress*/
                try{
                    Socket user=new Socket(txt_Address.getText(),44444);
                    Comms c = new Comms(user.getInetAddress());
                    ObjectCacher.getObjectCacher().put(Comms.class,c);
                    pageKeeper.pageManager.setCurrentPageIndex(PageConstants.LOGIN_PAGE);
                }catch(Exception e){System.out.println("This is exception of comms");}
                pageKeeper.pageManager.setCurrentPageIndex(PageConstants.LOGIN_PAGE);
            }
        });
    }

    @Override
    public Node getRoot() {
        return root;
    }

    @Override
    public void setPageKeeper(PageKeeper pg) {
        pageKeeper=pg;
    }

    @Override
    public void refreshPage() {

    }
}
