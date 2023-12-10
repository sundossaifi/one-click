package controllers;

import classes.Main;
import classes.Stuff;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CityOptionsController implements Initializable
{
    static Stage stageDialog;

    @FXML
    private Button homebutton;

    @FXML
    private ImageView hotelview;

    @FXML
    private Button hotelButton;

    @FXML
    private Button carsButton;

    @FXML
    private ImageView resturentview;

    @FXML
    private Button resButton;

    @FXML
    private ImageView touristicview;

    @FXML
    private Button tourButton;

    @FXML
    private Button bookbutton;

    @FXML
    private ImageView discoverview;

    @FXML
    private ImageView carsview;

    @FXML
    private ImageView bookview;

    @FXML
    void backToCityScreen()
    {
        Stuff.city=null;
        Stuff.hotel=null;
        Stuff.car=null;
        this.switchScreen("/fxml/CityScreen.fxml");
    }

    @FXML
    void switchToCarsScreen()
    {
        this.switchScreen("/fxml/CarScreen.fxml");
    }

    @FXML
    void switchToHotelScreen()
    {
        this.switchScreen("/fxml/HotelScreen.fxml");
    }

    @FXML
    void switchToResScreen()
    {
        this.switchScreen("/fxml/RestaurantScreen.fxml");
    }

    @FXML
    void switchToTourScreen()
    {
        this.switchScreen("/fxml/TouristicScreen.fxml");
    }

    @FXML
    void bookEvent()
    {
        try
        {
            Parent root=FXMLLoader.load(getClass().getResource("/fxml/BookDialogScreen.fxml"));
            Pane pane=new Pane(root);
            stageDialog=new Stage();
            Image icon = new Image("/Images/icon.png");
            stageDialog.setTitle("Assign an Reservation");
            stageDialog.setResizable(false);
            stageDialog.initOwner(this.bookbutton.getScene().getWindow());
            stageDialog.getIcons().add(icon);
            Scene scene = new Scene(pane);
            stageDialog.setScene(scene);
            double centerXPosition = this.bookbutton.getScene().getWindow().getX() + this.bookbutton.getScene().getWindow().getWidth()/2d;
            double centerYPosition = this.bookbutton.getScene().getWindow().getY() + this.bookbutton.getScene().getWindow().getHeight()/2d;
            stageDialog.setOnShowing(ev -> stageDialog.hide());
            stageDialog.setOnShown(ev ->
            {
                stageDialog.setX(centerXPosition - stageDialog.getWidth()/2d);
                stageDialog.setY(centerYPosition - stageDialog.getHeight()/2d);
                stageDialog.show();
            });
            stageDialog.show();
        }
        catch(NullPointerException | IOException exception)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText(null);
            alert.setContentText(exception.getMessage());
            alert.show();
        }
    }

    public void initialize(URL url, ResourceBundle rb)
    {
        discoverview.setImage(new Image("/images/discover.gif"));
        hotelview.setImage(new Image("/images/hotel.png"));
        resturentview.setImage(new Image("/images/resturents.png"));
        touristicview.setImage(new Image("/images/places.png"));
        carsview.setImage(new Image("/images/cars.png"));
        bookview.setImage(new Image("/images/book.png"));
        Image image2 = new Image(("/images/home.png"));
        ImageView v1= new ImageView(image2);
        this.homebutton.setGraphic(v1);
    }

    @FXML
    void changehome()
    {
        Image image = new Image(("/images/homeg.gif"));
        ImageView view = new ImageView(image);
        this.homebutton.setGraphic(view);
    }

    @FXML
    void returnhome()
    {
        Image image = new Image(("/images/home.png"));
        ImageView view = new ImageView(image);
        this.homebutton.setGraphic(view);
    }

    public void booked()
    {
        bookbutton.setText("Booked");
        Image image = new Image(("/images/check.png"));
        ImageView view = new ImageView(image);
        bookbutton.setGraphic(view);

        Task<Void> sleeper = new Task<Void>()
        {
            @Override
            protected Void call()
            {
                try
                {
                    Thread.sleep(1500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        };

        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>()
        {
            @Override
            public void handle(WorkerStateEvent event)
            {
                bookbutton.setGraphic(null);
                bookbutton.setText("Book");
            }
        });
        new Thread(sleeper).start();
    }

    private void switchScreen(String screen)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource(screen));
            Scene scene= new Scene(root);
            Main.mainStage.setScene(scene);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
}