package ui.pages;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ui.pages.constants.BasicController;
import ui.pages.constants.PageConstants;


public class Login implements PageConstants,BasicController{

    /*Objects fo local fxml*/
    @FXML
    AnchorPane loginRoot;
    @FXML
    Button btnLogin;
    @FXML
    TextField password;
    @FXML
    TextField username;

    /*object of other fxml*/
    PageKeeper pageKeeper;



    /*object used for some use*/
    FXMLLoader fxmlLoader;


    @FXML protected void initialize(){

        JFXButton jfoenixButton = new JFXButton("JFoenix Button");
        JFXButton button = new JFXButton("Raised Button".toUpperCase());
        button.getStyleClass().add("button-raised");
        System.out.println("Start of Login");
        try{
        /*No need to call fxmlLoader.load() as the file is already loaded*/}
        catch(Exception e){
            System.out.println("Initialize of login"+e);
        }
    }

    @FXML protected void validateLogin(ActionEvent ae)
    {
        try {
            if (validateUsername(ae) || validatePassword(ae)) {
            /*Login sucessful*/
                System.out.println("Value of pagekeeper"+pageKeeper);
                pageKeeper.pageManager.setCurrentPageIndex(PageConstants.DASHBOARD_PAGE);
            } else {
                System.out.println("Something is wrong plz check again");
                password.setText("");
                username.setText("");
            /*Plz write the code to show error message @Vishal*/
            }
        }catch(Exception e){
            System.out.println("inside call of button click of login button");
        }

    }
/*chutya return type should be boolean*/
    private boolean validatePassword(ActionEvent ae)
    {
        if((password.getText().length() == 0)||(password.getText().length() < 8))
            return false;
        return true;

    }

    private boolean validateUsername(ActionEvent ae)
    {
        if(username.getText().length() == 0)
            return false;
        return true;
    }

    public Node getRoot(){
        refreshPage();
        return loginRoot;
    }

    public void setPageKeeper(PageKeeper pg){
        pageKeeper=pg;
    }

    public void refreshPage(){

    }
}
