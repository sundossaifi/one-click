package controllers;

import classes.Main;
import classes.Stuff;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class userScreencontroller implements Initializable
{
    @FXML
    private Circle circle;
    @FXML
    private AnchorPane anc;
    @FXML
    private ImageView ima;
    @FXML
    private Label namelabel;
    @FXML
    private Label emaillabel;
    @FXML
    private Label birthdatelabel;
    @FXML
    private Label countrylabel;
    @FXML
    private Button homebutton;
    @FXML
    private Button editbutton;
    @FXML
    private Button tablebutton;
    @FXML
    private Button favbutton;
    @FXML
    private Label ssnlabel;


    @FXML
    void changeToDefault(MouseEvent event)
    {

        Image image = new Image(("/images/home.png"));
        ImageView view = new ImageView(image);
        this.homebutton.setGraphic(view);

    }

    @FXML
    void gotoarr(MouseEvent event)
    {
        this.tablebutton.setText("Go To Your Reservation");
    }

    @FXML
    void Backto(MouseEvent event)
    {
        this.tablebutton.setText("");
    }

    @FXML
    void changeToGIF(MouseEvent event)
    {

        Image image = new Image(("/images/homeg.gif"));
        ImageView view = new ImageView(image);
        this.homebutton.setGraphic(view);
    }

    @FXML
    void changetodefult2(MouseEvent event)
    {
        Image image = new Image(("/images/edit.png"));
        ImageView view = new ImageView(image);
        this.editbutton.setGraphic(view);
    }

    @FXML
    void changetogif2(MouseEvent event)
    {
        Image image = new Image(("/images/editg.gif"));
        ImageView view = new ImageView(image);
        this.editbutton.setGraphic(view);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
      this.circle.setFill(new ImagePattern(new Image(new File(Stuff.user.getPicPath()).toURI().toString())));
      this.namelabel.setText(Stuff.user.getName());
      this.emaillabel.setText(Stuff.user.getEmail());
      this.birthdatelabel.setText(Stuff.user.getDOB().getYear() +"-"+Stuff.user.getDOB().getMonthValue()+"-"+Stuff.user.getDOB().getDayOfMonth());
      this.countrylabel.setText(Stuff.user.getCountry());
      this.ssnlabel.setText(Integer.toString(Stuff.user.getSSN()));

      Image ima4 = new Image(("/images/table.png"));
      ImageView v4 = new ImageView(ima4);
      this.tablebutton.setGraphic(v4);


        Image ima = new Image(("/images/home.png"));
      ImageView v = new ImageView(ima);
      this.homebutton.setGraphic(v);
      Image ima2 = new Image(("/images/edit.png"));
      ImageView v2 = new ImageView(ima2);
      this.editbutton.setGraphic(v2);
      Image ima3 = new Image(("/images/favg.gif"));
      ImageView v3 = new ImageView(ima3);
      this.favbutton.setGraphic(v3);
    }

    @FXML
    void backToHomeScreen() throws IOException
    {
        Parent root= FXMLLoader.load(getClass().getResource("/fxml/CityScreen.fxml"));
        Scene scene=new Scene(root);
        Main.mainStage.setScene(scene);
    }

    @FXML
    void switchToEditProfileScreen() throws IOException
    {
        Parent root= FXMLLoader.load(getClass().getResource("/fxml/editUser.fxml"));
        Scene scene=new Scene(root);
        Main.mainStage.setScene(scene);
    }

    @FXML
    void GoToTable()
    {
        try
        {
            Parent root= FXMLLoader.load(getClass().getResource("/FXML/tableforUser.fxml"));
            Scene scene=new Scene(root);
            Main.mainStage.setScene(scene);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}