//@author: Ashutosh
//it is a contoller class which will create buttons(Groups) which will be further used for sharing files

package ui.pages;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ui.pages.constants.BasicController;

import java.util.ArrayList;


public class Share implements BasicController{

    //Supporting objects
    @FXML
    AnchorPane shareRoot;
    @FXML
    GridPane Grid;

    //method which will create buttons(Groups) which is accepting arraylist
    //will be displaying buttons in column of 3

    public void initialize(){
        System.out.println("Inside init of share of ashutosh");
    }

    public void getData(ArrayList arr) {
        int arraySize = arr.size();
        int z = 0;

        Button[] btn = new Button[arraySize];
        while (z < arraySize) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    btn[z] = new Button();
                    btn[z].setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent event){
                            System.out.println("hey i was clicked");
                        }
                    });
                    btn[z].setId("btn_style");
                    btn[z].setPrefHeight(150);
                    btn[z].setPrefWidth(111);
                    btn[z].setText(arr.get(z) + "");
                    Grid.add(btn[z], i, j);
                    z++;
                }
            }
        }
        Grid.setHgap(20);
        Grid.setVgap(20);
    }

    //temporary arraylist for checking purpose
    @FXML
    public void createButtons(ActionEvent ae) {
        // do your job of creating checkboxes and pass the arraylist as function parameters
        ArrayList myArray = new ArrayList();
        myArray.add("Hello");
        myArray.add("Hey");
        myArray.add("Hi");
        myArray.add("He");
        myArray.add("Hell");
        myArray.add("HeL");
        myArray.add("HELLo");
        myArray.add("LLO");
        myArray.add("LLO");

        this.getData(myArray);
        System.out.println("I was clicked");
    }

    public Node getRoot(){
        refreshPage();
        return  shareRoot;
    }

    public void setPageKeeper(PageKeeper pk){
        /*Not required here*/
    }

    public void refreshPage(){

    }

}




