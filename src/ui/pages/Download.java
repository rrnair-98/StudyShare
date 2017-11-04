package ui.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import ui.pages.constants.BasicController;
import ui.resources.CustomDirectory;


public class Download implements BasicController {
    @FXML
    AnchorPane parent;

    @FXML
    ScrollPane scrollPane;

    @FXML
    Button downloadBtn;

    CustomDirectory customDirectory;
    String path;


    @FXML protected void startDownload(ActionEvent actionEvent){
        System.out.println("Hello i am clicked");
    }

    @FXML protected void initialize(){
        initIt();
    }

    void initIt(){
        if(customDirectory!=null)
            parent.getChildren().remove(customDirectory);
        try{
            customDirectory=new CustomDirectory(path);
            scrollPane.setContent(customDirectory);
            parent.getChildren().addAll(customDirectory);
            customDirectory.setPrefSize(500,400);
        }
        catch(Exception e){
            System.out.println("Inside try of initpage "+e);
        }
    }
    public Node getRoot(){
        initIt();
        return parent;
    }

    public Node getRoot(String path){
        this.path=path;
        initIt();
        return parent;
    }

    public void setPageKeeper(PageKeeper pg){
//        Not required here
    }

    public void refreshPage(){

    }
}
