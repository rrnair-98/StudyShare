package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("Manage Users");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("manageUser.fxml"));
        Parent root = loader.load();

//        *********** DHANO SEE THIS ***********
        ManageUserController controller=(ManageUserController)loader.getController();
        controller.setStage(stage);

        Scene scene=new Scene(root, 800, 600);
        scene.getStylesheets().add("ManageUsers.css");

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
