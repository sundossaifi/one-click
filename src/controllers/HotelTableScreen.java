package controllers;

import classes.Hotel;
import classes.Main;
import classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import oracle.jdbc.driver.OracleDriver;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HotelTableScreen implements Initializable
{
    @FXML
    private TableView<Hotel> hotelTable;

    @FXML
    private TableColumn<Hotel, Integer> idCol;

    @FXML
    private TableColumn<Hotel, String> nameCol;

    @FXML
    private TableColumn<Hotel, Integer> rateCol;

    @FXML
    private TableColumn<Hotel, String> locationCol;

    @FXML
    private TableColumn<Hotel, Integer> cityIDCol;

    @FXML
    private Button delete;

    @FXML
    private Button add;

    @FXML
    private TextField searchField;

    @FXML
    private Button search;

    @FXML
    void deleteEvent(ActionEvent event)
    {
        Hotel hotel=this.hotelTable.getSelectionModel().getSelectedItem();

        if(hotel!=null)
        {
            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url1="jdbc:oracle:thin:@localhost:1521:xe";
                Connection con = DriverManager.getConnection(url1, "C##sundos_yaqeen", "11223344");

                 String query="delete from reserved_hotels where hid="+hotel.getHotelID()+"";
                 PreparedStatement ps=con.prepareStatement(query);
                 ps.executeQuery();

                 query="delete from hotel where hotel_id="+hotel.getHotelID()+"";
                 ps=con.prepareStatement(query);
                 ps.executeQuery();
                 con.close();
                 this.fillTableData("select *from hotel");
            }
            catch (SQLException sqlException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText("Data-Base Connection Error!");
                alert.setContentText(sqlException.getMessage());
                alert.showAndWait();
            }
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText(null);
            alert.setContentText("No Hotel Is Selected.");
            alert.showAndWait();
        }
    }

    @FXML
    void searchEvent(ActionEvent event)
    {
        if(!this.searchField.getText().isBlank())
        {
            this.fillTableData("select *from hotel where name='"+this.searchField.getText()+"'");
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText(null);
            alert.setContentText("Search Field Is Empty");
            alert.showAndWait();
        }
    }

    @FXML
    void addHotel(ActionEvent event)
    {
        try
        {
            Parent root= FXMLLoader.load(getClass().getResource("/fxml/addHotel.fxml"));
            Pane pane=new Pane(root);
            Stage stageDialog=new Stage();
            Image icon = new Image("/images/icon.png");
            stageDialog.setTitle("Add Hotel Dialog");
            stageDialog.setResizable(false);
            stageDialog.getIcons().add(icon);
            Scene scene = new Scene(pane);
            stageDialog.setScene(scene);
            double centerXPosition = Main.mainStage.getX() + Main.mainStage.getWidth()/2d;
            double centerYPosition = Main.mainStage.getY() + Main.mainStage.getHeight()/2d;
            stageDialog.setOnShowing(ev -> stageDialog.hide());
            stageDialog.setOnShown(ev ->
            {
                stageDialog.setX(centerXPosition - stageDialog.getWidth()/2d);
                stageDialog.setY(centerYPosition - stageDialog.getHeight()/2d);
                stageDialog.show();
            });
            addHotelController.stage=stageDialog;
            stageDialog.show();
        }
        catch(NullPointerException | IOException exception)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText("Loading Dialog Error!");
            alert.setContentText(exception.getMessage());
            alert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Image ima = new Image(("/images/searcha.png"));
        ImageView v = new ImageView(ima);
        this.search.setGraphic(v);
        Image ima2 = new Image(("/images/add.png"));
        ImageView v2 = new ImageView(ima2);
        this.add.setGraphic(v2);
        this.add.setText("Add");
        Image ima3 = new Image(("/images/delete.png"));
        ImageView v3 = new ImageView(ima3);
        this.delete.setGraphic(v3);
        this.delete.setText("Delete");

        this.searchField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent k)
            {
                if (k.getCode().equals(KeyCode.ENTER))
                {
                    searchField.setText("");
                    fillTableData("select *from hotel");
                }
            }
        });

        this.hotelTable.setRowFactory( tv ->
        {
            TableRow<Hotel> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (! row.isEmpty()) )
                {
                    Hotel rowData = row.getItem();

                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(Main.mainStage);
                    alert.setHeaderText("Hotel Information");
                    alert.setContentText(
                                    "Name: "+rowData.getHotelName()+"\n"+
                                    "Rate: "+rowData.getRate()+"\n"+
                                    "Location: "+rowData.getLocation()
                    );
                    alert.show();
                }
            });
            return row ;
        });

        this.fillTableData("select *from hotel");
    }

    public void fillTableData(String query)
    {
        int hotelID;
        String hotelName;
        int rate;
        String location;
        String pic;
        int cID;

        Hotel hotel;
        ObservableList<Hotel> hotelObservableList= FXCollections.observableArrayList();

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url1="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url1, "C##sundos_yaqeen", "11223344");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                hotelID=resultSet.getInt(1);
                hotelName=resultSet.getString(2);
                rate=resultSet.getInt(3);
                location=resultSet.getString(4);
                pic=resultSet.getString(5);
                cID=resultSet.getInt(6);

                hotel=new Hotel(hotelID,hotelName,rate,location,pic,cID);
                hotelObservableList.add(hotel);
            }
            con.close();
        }
        catch(SQLException sqlException)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText("Data-Base Connection Error!");
            alert.setContentText(sqlException.getMessage());
            alert.showAndWait();
            return;
        }

        this.idCol.setCellValueFactory(new PropertyValueFactory<Hotel,Integer>("hotelID"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<Hotel,String>("hotelName"));
        this.rateCol.setCellValueFactory(new PropertyValueFactory<Hotel,Integer>("rate"));
        this.locationCol.setCellValueFactory(new PropertyValueFactory<Hotel,String>("location"));
        this.cityIDCol.setCellValueFactory(new PropertyValueFactory<Hotel,Integer>("cityID"));

        this.hotelTable.setItems(hotelObservableList);
        this.hotelTable.refresh();

        hotelTable.widthProperty().addListener((source, oldWidth, newWidth) ->
        {
            TableHeaderRow header = (TableHeaderRow) hotelTable.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
    }
}