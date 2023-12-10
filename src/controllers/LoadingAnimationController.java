package controllers;

import animatefx.animation.Bounce;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingAnimationController implements Initializable
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
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        AtomicBoolean flag= new AtomicBoolean(false);
        this.label1.setVisible(false);
        this.label2.setVisible(false);
        this.label3.setVisible(false);

        Bounce bounce1=new Bounce(this.circle1);
        Bounce bounce2=new Bounce(this.circle2);
        Bounce bounce3=new Bounce(this.circle3);
        Bounce bounce4=new Bounce(this.circle4);
        Bounce bounce5=new Bounce(this.circle5);

        bounce5.setOnFinished(actionEvent ->
        {
            flag.set(true);
            LogInScreencontroller.loadingStage.close();
        });

        bounce1.setCycleCount(4).setDelay(Duration.valueOf("500ms")).play();
        bounce2.setCycleCount(4).setDelay(Duration.valueOf("1000ms")).play();
        bounce3.setCycleCount(4).setDelay(Duration.valueOf("1500ms")).play();
        bounce4.setCycleCount(4).setDelay(Duration.valueOf("2000ms")).play();
        bounce5.setCycleCount(4).setDelay(Duration.valueOf("2500ms")).play();

        Task<Void> sleeper = new Task<Void>()
        {
            @Override
            protected Void call()
            {
                try
                {
                    while(true)
                    {
                        Thread.sleep(250);
                        label1.setVisible(true);
                        Thread.sleep(250);
                        label2.setVisible(true);
                        Thread.sleep(250);
                        label3.setVisible(true);
                        Thread.sleep(250);
                        label1.setVisible(false);
                        label2.setVisible(false);
                        label3.setVisible(false);
                        if(flag.get())
                        {
                            break;
                        }
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };

        new Thread(sleeper).start();
    }
}