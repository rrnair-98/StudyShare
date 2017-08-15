package ui.pages;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.icons525.Icons525;
import de.jensd.fx.glyphs.icons525.Icons525View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import ui.pages.constants.BasicController;
import ui.pages.constants.PageConstants;


public class Login implements PageConstants,BasicController{

    /*Objects fo local fxml*/
    @FXML
    AnchorPane loginRoot;
    @FXML
    Button btnLogin;
    @FXML
    CustomPasswordField password;
    @FXML
    CustomTextField username;

    /*object of other fxml*/
    PageKeeper pageKeeper;



    /*object used for some use*/
    FXMLLoader fxmlLoader;


    @FXML protected void initialize(){
        Icons525View lockIcon = new Icons525View(Icons525.LOCK);
        //lockIcon.setStyle("-fx-fill: #8A0808;");
        lockIcon.setFill(Color.web("#41464b"));
        lockIcon.setStyle("-glyph-size:35px;");
        FontAwesomeIconView userIcon = new FontAwesomeIconView(FontAwesomeIcon.USER);
        userIcon.setStyle("-glyph-size:35px;");
        userIcon.setFill(Color.web("#41464b"));
        JFXButton jfoenixButton = new JFXButton("JFoenix Button");
        password.setRight(lockIcon);
        username.setRight(userIcon);
        JFXButton button = new JFXButton("Raised Button".toUpperCase());
        button.getStyleClass().add("button-raised");
        System.out.println("Start of Login");

        try{
        /*No need to call fxmlLoader.load() as the file is already loaded*/}
        catch(Exception e){
            System.out.println("Initialize of login"+e);
        }
        /*CustomTextField customTextField=new CustomTextField();
        customTextField.setRight(icon);
        AnchorPane.setLeftAnchor(customTextField,new Double("100"));
        AnchorPane.setTopAnchor(customTextField,new Double("100"));
        loginRoot.getChildren().add(customTextField);*/
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
