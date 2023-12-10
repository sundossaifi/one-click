package controllers;

import Interface.HotelListener;
import classes.Hotel;
import classes.Main;
import classes.Stuff;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import oracle.jdbc.driver.OracleDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HotelController implements Initializable
{
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button backButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private GridPane gridPane;
    @FXML
    private Pane itemPane;
    @FXML
    private ImageView itemPicture;
    @FXML
    private Button prev;
    @FXML
    private Rectangle rectangle;
    @FXML
    private Button next;
    @FXML
    private TextArea itemInformation;
    @FXML
    private Button bookButton;
    private Hotel hotel;
    private List<Hotel> hotels=new ArrayList<>();
    private HotelListener hotelListener;

    private List<Hotel> getdata(String query)
    {
        List<Hotel> hotelList=new ArrayList<>();
        Hotel hotel;

        int hotelID;
        String hotelName;
        int rate;
        String location;
        String pic;
        int cID;

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##sundos_yaqeen", "11223344");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();
            while(resultSet.next())
            {
                hotelID = resultSet.getInt(1);
                hotelName = resultSet.getString(2);
                rate = resultSet.getInt(3);
                location = resultSet.getString(4);
                pic = resultSet.getString(5);
                cID = resultSet.getInt(6);

                hotel=new Hotel(hotelID, hotelName, rate, location, pic, cID);
                hotelList.add(hotel);
            }
            con.close();
        }
        catch (SQLException sqlException)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText("Data-Base Connection Error!");
            alert.setContentText(sqlException.getMessage());
            alert.showAndWait();
        }
        return hotelList;
    }

    private void setChosenItem(Hotel hotel)
    {
        itemPane.setVisible(true);
        this.hotel=hotel;
        this.itemInformation.setText(
                        "Name: "+hotel.getHotelName()+"\n"+
                        "Rate: "+hotel.getRate()+"\n"+
                        "Location: "+hotel.getLocation()
        );
        this.rectangle.setFill(new ImagePattern(new Image(new File(hotel.getPic()).toURI().toString())));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Image image = new Image(("/images/prev.png"));
        ImageView v = new ImageView(image);
        this.backButton.setGraphic(v);
        this.searchField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent k)
            {
                if (k.getCode().equals(KeyCode.ENTER))
                {
                    searchField.setText("");
                    setData("select *from hotel where cid="+Stuff.city.getCityID()+"");
                }
            }
        });
        this.setData("select *from hotel where cid="+Stuff.city.getCityID()+"");
    }

    @FXML
    void backtocityoptions()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/CityOptionsScreen.fxml"));
            Scene scene= new Scene(root);
            Main.mainStage.setScene(scene);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    private void setData(String query)
    {
        int row=1,column=0;
        hotels=new ArrayList<>();
        gridPane.getChildren().clear();
        itemPane.setVisible(false);
        hotel=null;
        this.hotelListener=new HotelListener()
        {
            @Override
            public void onClickListener(Hotel hotel)
            {
                setChosenItem(hotel);
            }
        };
        try
        {
            this.hotels.addAll(this.getdata(query));
            for(int i=0;i<this.hotels.size();i++)
            {
                FXMLLoader fxmlLoader=new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/HotelItem.fxml"));
                AnchorPane anchorPane=fxmlLoader.load();

                HotelItemController hotelItemController=fxmlLoader.getController();
                hotelItemController.setData(hotels.get(i),this.hotelListener);

                gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);

                gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);

                if (column==1){
                    column=0;
                    row++;
                }
                this.gridPane.add(anchorPane,column++,row);
                gridPane.setPadding(new Insets(0, 0, 100, 0));
                gridPane.setMargin(anchorPane,new Insets(20));
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    @FXML
    void bookEvent()
    {
        Stuff.hotel=this.hotel;
        Alert alert=new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.setHeaderText(null);
        alert.setContentText("hotel Booked Successfully");
        alert.initOwner(Main.mainStage);
        alert.show();
    }

    @FXML
    void searchEvent()
    {
        if(this.searchField.getText().isBlank())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Search Field Is Empty!");
            alert.setContentText("Please Enter Hotel Name");
            alert.initOwner(Main.mainStage);
            alert.show();
        }
        else
        {
            this.setData("select *from hotel where cid="+Stuff.city.getCityID()+" and name='"+this.searchField.getText()+"'");
        }
    }

    @FXML
    void changback( ) {
        Image image = new Image(("/images/prev.gif"));
        ImageView view = new ImageView(image);
        this.backButton.setGraphic(view);
    }

    @FXML
    void returnback( ) {
        Image image = new Image(("/images/prev.png"));
        ImageView view = new ImageView(image);
        this.backButton.setGraphic(view);
    }
}