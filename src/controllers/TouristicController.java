package controllers;

import Interface.TouristicListener;
import classes.Main;
import classes.Stuff;
import classes.Touristic;
import javafx.animation.FadeTransition;
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
import javafx.util.Duration;
import oracle.jdbc.driver.OracleDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class TouristicController implements Initializable
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
    private Touristic touristic;
    private List<Touristic> touristics=new ArrayList<>();
    private TouristicListener touristicListener;
    private Random random=new Random();
    public static Thread thread=null;

    private List<Touristic> getdata(String query)
    {
        List<Touristic> touristicList=new ArrayList<>();
        Touristic touristic;

         int id;
         String name;
         String location;
         String description;
         int cid;
         ArrayList<String> pictures;
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

                touristic=new Touristic(id,name,location,description,cid,null);
                touristicList.add(touristic);
            }

            for(int i=0;i<touristicList.size();i++)
            {
                pictures=new ArrayList<>();
                query="select *from TOURISTIC_PLACES_PICTURES where tid="+touristicList.get(i).getId()+"";
                ps=con.prepareStatement(query);
                resultSet=ps.executeQuery();
                while(resultSet.next())
                {
                    pic=resultSet.getString(2);
                    pictures.add(pic);
                }
                touristicList.get(i).setPictures(pictures);
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
        return touristicList;
    }

    private void setChosenItem(Touristic touristic)
    {
        if(this.thread!=null)
        {
            this.thread.stop();
        }
        itemPane.setVisible(true);
        this.touristic=touristic;
        this.itemInformation.setText(
                        "Name: "+touristic.getName()+"\n"+
                        "Location: "+touristic.getLocation()+"\n"+
                        "Description: "+touristic.getLocation()
        );
        AtomicInteger temp = new AtomicInteger();
        AtomicInteger index= new AtomicInteger();
        index.set(random.nextInt(touristic.getPictures().size()));
        this.rectangle.setFill(new ImagePattern(new Image(new File(touristic.getPictures().get(index.get())).toURI().toString())));
        this.thread=new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    int from=1,to=0;
                    while(true)
                    {
                        Thread.sleep(1000);
                        FadeTransition fadeTransition=new FadeTransition();
                        fadeTransition.setNode(rectangle);
                        fadeTransition.setDuration(Duration.millis(800));
                        fadeTransition.setFromValue(from);
                        fadeTransition.setToValue(to);
                        fadeTransition.setOnFinished((ActionEvent)->
                        {
                            temp.set(index.get());
                            index.set(random.nextInt(touristic.getPictures().size()));
                            while(temp.get()==index.get()&&touristic.getPictures().size()!=1)
                            {
                                index.set(random.nextInt(touristic.getPictures().size()));
                            }
                            rectangle.setFill(new ImagePattern(new Image(new File(touristic.getPictures().get(index.get())).toURI().toString())));
                            FadeTransition fadeTransition2=new FadeTransition();
                            fadeTransition2.setNode(rectangle);
                            fadeTransition2.setDuration(Duration.millis(800));
                            fadeTransition2.setFromValue(0);
                            fadeTransition2.setToValue(1);
                            fadeTransition2.play();
                        });
                        fadeTransition.play();
                        Thread.sleep(800);
                    }
                }
                catch(InterruptedException interruptedException)
                {
                    interruptedException.printStackTrace();
                }
            }
        };
        thread.start();
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
                    setData("select *from TOURISTIC_PLACE where cid="+Stuff.city.getCityID()+"");
                }
            }
        });
        this.setData("select *from TOURISTIC_PLACE where cid="+Stuff.city.getCityID()+"");
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
        touristics=new ArrayList<>();
        gridPane.getChildren().clear();
        itemPane.setVisible(false);
        touristic=null;
        this.touristicListener=new TouristicListener()
        {
            @Override
            public void onClickListener(Touristic touristic)
            {
                setChosenItem(touristic);
            }
        };
        try
        {
            this.touristics.addAll(this.getdata(query));
            for(int i=0;i<this.touristics.size();i++)
            {
                FXMLLoader fxmlLoader=new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/TouristicItemController.fxml"));
                AnchorPane anchorPane=fxmlLoader.load();


                TouristicItemController touristicItemController=fxmlLoader.getController();
                touristicItemController.setData(touristics.get(i),this.touristicListener);

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
            alert.setContentText("Please Enter Car Name");
            alert.initOwner(Main.mainStage);
            alert.show();
        }
        else
        {
            this.setData("select *from TOURISTIC_PLACE where cid="+Stuff.city.getCityID()+" and name='"+this.searchField.getText()+"'");
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