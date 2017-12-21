package ui.pages;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ui.core.Comms;
import ui.pages.constants.BasicController;
import ui.pages.constants.PageConstants;
import ui.pages.utilities.ObjectCacher;

import java.net.ConnectException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddress implements BasicController {

    @FXML
    AnchorPane root;

    @FXML
    Button btn_Go;

    @FXML
    TextField txt_Address;
    /**********************************************/
    PageKeeper pageKeeper;

    @FXML  protected void initialize() {

        txt_Address.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                btn_Go.fire();
            }
        });

        btn_Go.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                /*Validate IpAddress*/
                if (validateAddress()) {
                    try {
                        Socket user = new Socket(txt_Address.getText(), 44444);
                        Comms c = new Comms(user.getInetAddress());
                        ObjectCacher.getObjectCacher().put(Comms.class, c);
                        pageKeeper.pageManager.setCurrentPageIndex(PageConstants.LOGIN_PAGE);
                    } catch (ConnectException connect) {
                        System.out.println("Opps connect Exception");
                        System.out.println("Something is wrong");
                        txt_Address.setText("");
                    } catch (Exception e) {
                        System.out.println("This is exception of comms");
                        new Alert(Alert.AlertType.ERROR).showAndWait();
                        txt_Address.setText("");

                    }
                    pageKeeper.pageManager.setCurrentPageIndex(PageConstants.LOGIN_PAGE);
                }
                else {
                    System.out.println("No ip address is wrong");
                    new Alert(Alert.AlertType.ERROR).showAndWait();
                    txt_Address.setText("");
                }
            }
        });
    }


    private boolean validateAddress(){
        Pattern pattern=Pattern.compile( "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
        Matcher matcher=pattern.matcher(txt_Address.getText());
        if(matcher.find())
            return true;
        return false;
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
