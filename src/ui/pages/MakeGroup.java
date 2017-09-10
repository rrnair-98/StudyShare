package ui.pages;

//<!--@Autor Dhiren Chotwani-->
        import de.jensd.fx.glyphs.emojione.EmojiOne;
        import de.jensd.fx.glyphs.emojione.EmojiOneView;
        import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
        import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
        import de.jensd.fx.glyphs.weathericons.WeatherIcon;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.control.Button;
        import javafx.scene.control.ListView;
        import javafx.scene.control.TextField;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.layout.BorderPane;
        import javafx.scene.layout.StackPane;
        import javafx.scene.paint.Color;
        import org.controlsfx.control.textfield.CustomTextField;
        import org.controlsfx.glyphfont.FontAwesome;

//This class is the controller class for makeGroup.fxml file and this is used to initialize the elements of makeGroup.fxml
// the essentials of this groups are:
//1. List view adds the data to DB and  display it to the user
//2.load all the corresponding fxml files into the Stackpane
//3.performs the task of creating and displaying the groups with the files   using scrollPane

//NOTE- THIS CLASS AND ITS CORRESPONDING FXML WILL BE USED WHEN THE SERVER CLICKS Manage and Create Groups->ADD FROM THE Manage and create group window


public class MakeGroup{

    //Supporting objects for all the fxmls
    FXMLLoader loader=new FXMLLoader(getClass().getResource("fxml//manageGroup.fxml"));
    ManageGroup mgC=new ManageGroup();
    ObservableList<String> items = FXCollections.observableArrayList ();

    // Objects refrenced from FXML file for this controller i.e makeGroups.fxml

    StackPane myStackPane;
    BorderPane tp;

    @FXML
    ListView<String> listView;

    @FXML
    Button addBtn,addNbtn;

    @FXML  AnchorPane myRoot;
    @FXML
    CustomTextField email;
    @FXML
    CustomTextField groupName;

    //Methods that will initialze all the necessary element for manageGroups.fxml
    @FXML protected  void initialize(){

        //populating list view


        System.out.println("hey i am inside dhiren make group");

//        String arr[]={"dhiren","dhiren","dhiren","dhiren","dhiren","dhiren"};
        items.addAll("Enter","your data","in the above","text-field!");

        listView.setItems(items);
        FontAwesomeIconView groupNameIcon=new FontAwesomeIconView(FontAwesomeIcon.GROUP);
        groupNameIcon.setFill(Color.web("white"));

        EmojiOneView emailIcon=new EmojiOneView(EmojiOne.ENVELOPE);
        emailIcon.setFill(Color.web("white"));

        groupName.setRight(groupNameIcon);
        email.setRight(emailIcon);
        try{


            System.out.println(mgC);
        }catch(Exception e){}
//        myStackPane=cnt.getStackPane();
//        System.out.println(myStackPane);
//        myRoot=cnt.getAnchorPane();
//        System.out.println(myRoot);
    }

    //this method sets the stackpane
    public void setStackParent(StackPane sp)
    {
        System.out.println(sp);
        myStackPane= sp;
        System.out.println(myStackPane);
    }
    //this method sets the root anchor pane
    public void setMyRoot(AnchorPane ap)
    {
        System.out.println(ap);
        myRoot=ap;
    }

    //task to be done when ActionEvent takes place
    @FXML public  void random(ActionEvent ae){
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
                //removes the createGroup.fxml from the screen
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

    public void setEmail(CustomTextField email) {
        this.email = email;
    }

    public void setGroupName(CustomTextField groupName) {
        this.groupName = groupName;
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

