package ui.pages;
//<!--@Autor Dhiren Chotwani-->
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.pages.constants.BasicController;

//This class is the controller class for ManageUser.fxml file and this is used to initialize the elements of manageUser.fxml
// the essentials of this groups are:
//1. The main stack pane is controlled in this file for manageUser.fxml and makeUser.fxml
//2.load all the corresponding fxml files into the Stackpane
//3.performs the task of loading and displaying the files in the so created group with thier users using scrollPane

//NOTE- THIS CLASS AND ITS CORRESPONDING FXML WILL BE USED WHEN THE SERVER CLICKS Manage and Create Users FROM THE DASHBOARD

public class ManageUser implements BasicController{
    //Supporting objects for all the fxmls
    FXMLLoader loader=new FXMLLoader(getClass().getResource("fxml//makeUser.fxml"));

    // Objects refrenced from FXML file for this controller i.e ManageGroups.fxml
    @FXML
    StackPane parent;

    AnchorPane myRoot;


    @FXML
    BorderPane borderPane;

    @FXML
    ScrollPane groupsScrollPane;

    @FXML
    ScrollPane usersScrollPane;

    @FXML
    Button addGroup;
    @FXML
    Button edit;
    @FXML
    Button delete;

    @FXML
    Button addUser;

    @FXML
    Button delete1;

    //this method is used to set the stage for this file in order to be used to add elements later to this pane
    @FXML
    private Stage myStage=new Stage();
    @FXML
    public void setStage(Stage stage) {
        myStage = stage;
    }

    //Methods that will initialze all the necessary element for manageUser.fxml
    @FXML protected  void initialize()
    {
        MakeUser muController=null;
        try{
            // This will load the makeGroup.fxml file which is used to create groups
            loader.load();
            muController= loader.getController();

            //method of makeGroupController.java to set the stack pane of the makeGroup.fxml file
            muController.setStackParent(parent);

            //some work to be done before initial functionality begins
//        edit.setDisable(true);
            delete.setDisable(true);

            delete1.setDisable(true);}
        catch (Exception e){
            System.out.println("Hello World");
        }

        //setting content of scrollpane is to be done here
//        groupsScrollPane.setContent(new ImageView(getClass().getResource("..//resources//images(3).jpg").toExternalForm()));
//        usersScrollPane.setContent(new ImageView(getClass().getResource("..//resources//images(3).jpg").toExternalForm()));

        try

        {
            //loading the root of makeGroup.fxml in order to be added on the stack pane
            myRoot = (AnchorPane)loader.getRoot();
            muController.setMyRoot(myRoot);
        }catch (Exception e){}

    }
    @FXML
    //method that controls the tasks to be performed when actionEvent occurs
    public void createUser(ActionEvent ae){
        Button btn=(Button)ae.getSource();
        if(btn==addGroup || btn==edit ){
            borderPane.setStyle("-fx-opacity:0.5");
            parent.getChildren().add(myRoot);
        }
    }

    //setters and getters for manageUserController.java


    public Button getAddGroup() {
        return addGroup;
    }
    public Button getAddUser(){
        return addUser;
    }

    public Button getEdit() {
        return edit;
    }

    public Button getDelete() {
        return delete;
    }

    public Button getDelete1() {
        return delete1;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public ScrollPane getUsersScrollPane() {
        return usersScrollPane;
    }
    public ScrollPane getGroupsScrollPane() {
        return groupsScrollPane;
    }

    public StackPane getParent() {
        return parent;
    }


//    ****************** SETTERS  ******************

    public void setAddGroup(Button addGroup) {
        this.addGroup = addGroup;
    }

    public void setAddUser(Button addUser) {
        this.addUser = addUser;
    }

    public void setEdit(Button edit) {
        this.edit = edit;
    }

    public void setDelete1(Button delete1) {
        this.delete1 = delete1;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public void setUsersScrollPane(ScrollPane usersScrollPane) {
        this.usersScrollPane = usersScrollPane;
    }

    public void setGroupsScrollPane(ScrollPane groupsScrollPane) {
        this.groupsScrollPane = groupsScrollPane;
    }

    public void setParent(StackPane parent) {
        this.parent = parent;
    }

    public Node getRoot(){
        refreshPage();
        return parent;
    }
    public void setPageKeeper(PageKeeper pg){
//        not required
    }

    public void refreshPage(){

    }
}
