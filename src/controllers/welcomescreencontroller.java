package controllers;

import classes.Main;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class welcomescreencontroller implements Initializable
{
    private Parent root;
    private Scene scene;
    @FXML
    private AnchorPane parentContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        this.parentContainer.setOpacity(0);
        this.fadeTransition(0,1,false);
    }

    private void fadeTransition(int start,int end,boolean state)
    {
        FadeTransition fadeTransition=new FadeTransition();
        fadeTransition.setNode(this.parentContainer);
        fadeTransition.setDuration(Duration.millis(1500));
        fadeTransition.setFromValue(start);
        fadeTransition.setToValue(end);
        fadeTransition.setOnFinished((ActionEvent)->
        {
            if(state==true)
            {
                try
                {
                    root= FXMLLoader.load(getClass().getResource("/FXML/LogInScreen.fxml"));
                    scene=new Scene(root);
                    Main.mainStage.setScene(scene);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                this.fadeTransition(1,0,true);
            }
        });
        fadeTransition.play();
    }
}