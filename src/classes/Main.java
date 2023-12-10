package classes;

import controllers.CarController;
import controllers.TouristicController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application
{
    public static Stage mainStage=new Stage();
    public static void main(String [] args){
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException
    {
        Parent root= FXMLLoader.load(getClass().getResource("/FXML/welcomescreen.fxml"));
        Scene scene=new Scene(root);
        mainStage.setWidth(800);
        mainStage.setHeight(700);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.setTitle("One Click");
        mainStage.getIcons().add(new Image("/images/icon.png"));
        mainStage.setOnCloseRequest(e ->
        {
            Platform.exit();
            if(CarController.thread!=null)
            {
                CarController.thread.stop();
            }
            if(TouristicController.thread!=null)
            {
                TouristicController.thread.stop();
            }
            System.exit(0);
        });
        mainStage.show();
    }
}