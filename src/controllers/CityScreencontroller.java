package controllers;

import Interface.CityListener;
import classes.City;
import classes.Main;
import classes.Stuff;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.util.Duration;
import oracle.jdbc.driver.OracleDriver;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CityScreencontroller implements Initializable
{
    private List<City> cities;
    private CityListener cityListener;
    @FXML
    private GridPane gridPane;
    @FXML
    private HBox hbox;
    @FXML
    private TextField searchField;
    @FXML
    private Button logoutbutton;
    @FXML
    Pane pane;
    @FXML
    private Button userbu;
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private Button searchbutton;
    @FXML
    void def4()
    {
        Image image = new Image(("/images/search.png"));
        ImageView view = new ImageView(image);
        this.searchbutton.setGraphic(view);
    }

    @FXML
    void def5()
    {
        Image image = new Image(("/images/user.png"));
        ImageView view = new ImageView(image);
        this.userbu.setGraphic(view);
    }

    @FXML
    void def6()
    {
        Image image = new Image(("/images/signout.png"));
        ImageView view = new ImageView(image);
        this.logoutbutton.setGraphic(view);
    }

    @FXML
    void gif4()
    {
        Image image = new Image(("/images/search.gif"));
        ImageView view = new ImageView(image);
        this.searchbutton.setGraphic(view);
    }

    @FXML
    void gif5()
    {
        Image image = new Image(("/images/user.gif"));
        ImageView view = new ImageView(image);
        this.userbu.setGraphic(view);
    }

    @FXML
    void gif6()
    {
        Image image = new Image(("/images/signout.gif"));
        ImageView view = new ImageView(image);
        this.logoutbutton.setGraphic(view);
    }

    private List<City> getData(String query)
    {
        List<City> cityList=new ArrayList<>();
        City city;

        int cityID;
        String name,picPath,gifPath,description;

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##sundos_yaqeen", "11223344");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();
            while(resultSet.next())
            {
                cityID = resultSet.getInt(1);
                name = resultSet.getString(2);
                gifPath = resultSet.getString(3);
                description = resultSet.getString(4);
                picPath = resultSet.getString(5);

                city=new City(cityID,name,picPath,gifPath,description);
                cityList.add(city);
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
        return cityList;
    }

    private void switchToCityOptions(City city)
    {
        try
        {
            Stuff.city=city;
            Parent root=FXMLLoader.load(getClass().getResource("/fxml/CityOptionsScreen.fxml"));
            Scene scene=new Scene(root);
            Main.mainStage.setScene(scene);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Image ima = new Image(("/images/search.png"));
        ImageView v = new ImageView(ima);
        this.searchbutton.setGraphic(v);
        Image ima1 = new Image(("/images/user.png"));
        ImageView v1 = new ImageView(ima1);
        this.userbu.setGraphic(v1);
        Image ima2 = new Image(("/images/signout.png"));
        ImageView v2 = new ImageView(ima2);
        this.logoutbutton.setGraphic(v2);
        this.setData("select *from city");

        this.searchField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent k)
            {
                if (k.getCode().equals(KeyCode.ENTER))
                {
                    searchField.setText("");
                    setData("select *from city");
                }
            }
        });
    }

    private void setData(String query)
    {
        int row=1,column=0;
        this.cityListener=new CityListener()
        {
            @Override
            public void onClickListener(City city)
            {
                switchToCityOptions(city);
            }
        };
        try
        {
            this.cities=new ArrayList<>();
            this.cities.addAll(this.getData(query));
            for(int i=0;i<this.cities.size();i++)
            {
                FXMLLoader fxmlLoader=new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/FXML/item1.fxml"));
                AnchorPane anchorPane=fxmlLoader.load();

                item1controller itemController=fxmlLoader.getController();
                itemController.setData(cities.get(i),this.cityListener);

                gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);

                gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);

                if (column==2){
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
    void logoutEvent()
    {

        Stuff.user=null;
        Stuff.city=null;
        Stuff.hotel=null;
        Stuff.car=null;

        FadeTransition fadeTransition=new FadeTransition();
        fadeTransition.setNode(this.pane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0.3);
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setOnFinished(e->{
            try
            {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/goodbye.fxml")));
                Scene scene = this.hbox.getScene();
                root.translateXProperty().set(scene.getWidth());
                this.parentContainer = (AnchorPane) this.hbox.getScene().getRoot();
                this.parentContainer.getChildren().add(root);
                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(this::handle);
                timeline.play();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        });
        fadeTransition.play();
    }

    private void handle(ActionEvent t)
    {
        this.parentContainer.getChildren().remove(this.parentContainer);
    }

    @FXML
    void searchEvent()
    {
        if(this.searchField.getText().isBlank())
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Search Field Is Empty!");
            alert.setContentText("Please Enter City Name");
            alert.initOwner(Main.mainStage);
            alert.show();
        }
        else
        {
            this.gridPane.getChildren().clear();
            this.setData("select *from city where name='"+this.searchField.getText()+"'");
        }
    }

    @FXML
    void switchToProfileScreen() throws IOException
    {
        Parent root=FXMLLoader.load(getClass().getResource("/fxml/userScreen.fxml"));
        Scene scene=new Scene(root);
        Main.mainStage.setScene(scene);
    }
}