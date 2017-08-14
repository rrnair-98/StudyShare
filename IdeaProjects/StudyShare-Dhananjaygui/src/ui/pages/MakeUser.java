package ui.pages;
//<!--@Autor Dhiren Chotwani-->
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

//This class is the controller class for makeUser.fxml file and this is used to initialize the elements of makeUser.fxml
// the essentials of this groups are:
//1. List view adds the data to DB and  display it to the user
//2.load all the corresponding fxml files into the Stackpane
//3.performs the task of creating and displaying the groups with thier members   using scrollPane

//NOTE- THIS CLASS AND ITS CORRESPONDING FXML WILL BE USED WHEN THE SERVER CLICKS Manage and Create Users->addFROM THE Manage and create group window


public class MakeUser {
    //Supporting objects for all the fxmls
    FXMLLoader loader=new FXMLLoader(getClass().getResource("fxml//manageGroup.fxml"));
    ManageUser mgC=new ManageUser();
    ObservableList<String> items = FXCollections.observableArrayList ();

    // Objects refrenced from FXML file for this controller i.e makeGroups.fxml
    Button spBtn;
    StackPane myStackPane;
    BorderPane tp;

    @FXML
    ListView<String> listView;

    @FXML
    Button addBtn,addNbtn;

    @FXML
    AnchorPane myRoot;
    @FXML
    TextField email;
    @FXML
    PasswordField userPass;
    @FXML
    TextField groupName;
    @FXML
    TextField clientName;

    @FXML protected  void initialize(){
        //populating list view
//        String arr[]={"dhiren","dhiren","dhiren","dhiren","dhiren","dhiren"};
        items.addAll("Enter","your data","in the above","text-field!");
        listView.setItems(items);

    }

    //this method sets the stackpane
    public void setStackParent(StackPane sp)
    {

        myStackPane= sp;

    }
    //this method sets the root anchor pane
    public void setMyRoot(AnchorPane ap)
    {

        myRoot=ap;
    }

    //function that handles all the tasks related to actionEvent
    public void createUser(ActionEvent ae){
        try {
            Button src=(Button)ae.getSource() ;
            if(src==addNbtn) {
                //populates the list view
                if(email.getText().length()<1){

                    email.setPromptText("Please enter something here");

//                    *********************** HANDLE THIS FROM CSS  ***********************
//                    System.out.println("inisde if");
//                    email.setStyle("-fx-background-color:red");
                }

                else {
                    items.add(email.getText());
                    email.setText("");
                    email.setPromptText("someone@something.com");
                }
            }
            else if(src==addBtn) {
                //removes the createUser.fxml from the screen
                myStackPane.getChildren().remove(myRoot);


            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //    setters and getters for this.java file which handles makeGroup.fxml
    public BorderPane getMyBorderPane() {
        return tp;
    }

    public AnchorPane getMyRoot() {
        return myRoot;
    }

    public Button getAddBtn() {
        return addBtn;
    }

    public Button getAddNbtn() {
        return addNbtn;
    }

    public TextField getEmail() {
        return email;
    }

    public TextField getGroupName() {
        return groupName;
    }

    public PasswordField getUserPass() {
        return userPass;
    }

    public TextField getClientName() {
        return clientName;
    }

    public ObservableList<String> getItems() {
        return items;
    }

    public ListView<String> getListView() {
        return listView;
    }


    //    ******************** SETTERS ********************
    public void setAddBtn(Button addBtn) {
        this.addBtn = addBtn;
    }

    public void setAddNbtn(Button addNbtn) {
        this.addNbtn = addNbtn;
    }

    public void setEmail(TextField email) {
        this.email = email;
    }

    public void setGroupName(TextField groupName) {
        this.groupName = groupName;
    }

    public void setClientName(TextField clientName) {
        this.clientName = clientName;
    }

    public void setUserPass(PasswordField userPass) {
        this.userPass = userPass;
    }

    public void setItems(ObservableList<String> items) {
        this.items = items;
    }

    public void setListView(ListView<String> listView) {
        this.listView = listView;
    }

    public void setMyBorderPane(BorderPane tp) {
        this.tp = tp;
    }

}
