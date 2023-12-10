package controllers;

import Interface.RestaurantListener;
import classes.Main;
import classes.Restaurant;
import classes.Stuff;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class RestaurantController implements Initializable
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
    private Restaurant restaurant;
    private List<Restaurant> restaurants=new ArrayList<>();
    private RestaurantListener restaurantListener;

    private List<Restaurant> getdata(String query)
    {
        List<Restaurant> restaurantList=new ArrayList<>();
        Restaurant restaurant;

         int id;
         String name;
         String location;
         String description;
         int cid;
         String pic;

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##sundos_yaqeen", "11223344");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();
            while(resultSet.next())
            {
                id = resultSet.getInt(1);
                name = resultSet.getString(2);
                location = resultSet.getString(3);
                description = resultSet.getString(4);
                cid = resultSet.getInt(5);
                pic = resultSet.getString(6);

                restaurant=new Restaurant(id,name,location,description,cid,pic);
                restaurantList.add(restaurant);
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
        return restaurantList;
    }

    private void setChosenItem(Restaurant restaurant)
    {
        itemPane.setVisible(true);
        this.restaurant=restaurant;
        this.itemInformation.setText(
                        "Name: "+restaurant.getName()+"\n"+
                        "Location: "+restaurant.getLocation()+"\n"+
                                "Description: "+restaurant.getDescription()
        );
        this.rectangle.setFill(new ImagePattern(new Image(new File(restaurant.getPic()).toURI().toString())));
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
                    setData("select *from restaurants where cid="+Stuff.city.getCityID()+"");
                }
            }
        });
        this.setData("select *from restaurants where cid="+Stuff.city.getCityID()+"");
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
        restaurants=new ArrayList<>();
        gridPane.getChildren().clear();
        itemPane.setVisible(false);
        restaurant=null;
        this.restaurantListener=new RestaurantListener()
        {
            @Override
            public void onClickListener(Restaurant restaurant)
            {
                setChosenItem(restaurant);
            }
        };
        try
        {
            this.restaurants.addAll(this.getdata(query));
            for(int i=0;i<this.restaurants.size();i++)
            {
                FXMLLoader fxmlLoader=new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/RestaurantItem.fxml"));
                AnchorPane anchorPane=fxmlLoader.load();

                RestaurantItemController restaurantItemController=fxmlLoader.getController();
                restaurantItemController.setData(restaurants.get(i),this.restaurantListener);

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
    void searchEvent()
    {
        if(this.searchField.getText().isBlank())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Search Field Is Empty!");
            alert.setContentText("Please Enter Restaurant Name");
            alert.initOwner(Main.mainStage);
            alert.show();
        }
        else
        {
            this.setData("select *from restaurants where cid="+Stuff.city.getCityID()+" and name='"+this.searchField.getText()+"'");
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