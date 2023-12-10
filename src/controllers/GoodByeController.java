package controllers;

import animatefx.animation.Bounce;
import classes.Main;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GoodByeController implements Initializable
{
    @FXML
    Circle circle1;
    @FXML
    Circle circle2;
    @FXML
    Circle circle3;
    @FXML
    Circle circle4;
    @FXML
    Circle circle5;
    @FXML
    AnchorPane parentContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Bounce bounce1=new Bounce(this.circle1);
        Bounce bounce2=new Bounce(this.circle2);
        Bounce bounce3=new Bounce(this.circle3);
        Bounce bounce4=new Bounce(this.circle4);
        Bounce bounce5=new Bounce(this.circle5);

        bounce5.setOnFinished(actionEvent ->
        {
            FadeTransition fadeTransition=new FadeTransition();
            fadeTransition.setNode(Main.mainStage.getScene().getRoot());
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);
            fadeTransition.setDuration(Duration.millis(250));
            fadeTransition.setOnFinished(e ->
            {
                try
                {
                    Parent root= FXMLLoader.load(getClass().getResource("/fxml/LogInScreen.fxml"));
                    Scene scene=new Scene(root);
                    Main.mainStage.setScene(scene);
                }
                catch (IOException ioException)
                {
                    ioException.printStackTrace();
                }
            });
            fadeTransition.play();
        });

        bounce1.setCycleCount(4).setDelay(Duration.valueOf("50ms")).play();
        bounce2.setCycleCount(4).setDelay(Duration.valueOf("100ms")).play();
        bounce3.setCycleCount(4).setDelay(Duration.valueOf("150ms")).play();
        bounce4.setCycleCount(4).setDelay(Duration.valueOf("200ms")).play();
        bounce5.setCycleCount(4).setDelay(Duration.valueOf("250ms")).play();
    }
}