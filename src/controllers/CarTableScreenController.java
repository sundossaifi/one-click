package controllers;

import classes.Car;
import classes.Main;
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

public class CarTableScreenController implements Initializable
{
    @FXML
    private TableView<Car> carTable;
    @FXML
    private TableColumn<Car, Integer> idCol;
    @FXML
    private TableColumn<Car, String> nameCol;
    @FXML
    private TableColumn<Car, String> typeCol;
    @FXML
    private TableColumn<Car, String> colorCol;
    @FXML
    private TableColumn<Car, Integer> cityIDCol;
    @FXML
    private TableColumn<Car, Integer> priceCol;
    @FXML
    private Button delete;
    @FXML
    private Button add;
    @FXML
    private TextField searchField;
    @FXML
    private Button search;

    @FXML
    void deleteCar(ActionEvent event)
    {
        Car car=this.carTable.getSelectionModel().getSelectedItem();

        if(car!=null)
        {
            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url1="jdbc:oracle:thin:@localhost:1521:xe";
                Connection con = DriverManager.getConnection(url1, "C##sundos_yaqeen", "11223344");
                String query="delete from car_pictures where caid="+car.getCarID()+"";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                query="delete from reserved_cars where caid="+car.getCarID()+"";
                ps=con.prepareStatement(query);
                ps.executeQuery();

                query="delete from car where car_id="+car.getCarID()+"";
                ps=con.prepareStatement(query);
                ps.executeQuery();

                con.close();
                this.fillTableData("select *from car");
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
            alert.setContentText("No Car Is Selected.");
            alert.showAndWait();
        }
    }

    @FXML
    void searchEvent(ActionEvent event)
    {
        if(!this.searchField.getText().isBlank())
        {
            this.fillTableData("select *from car where name='"+this.searchField.getText()+"'");
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
    void addcar(ActionEvent event)
    {
        try
        {
            Parent root= FXMLLoader.load(getClass().getResource("/fxml/addCar.fxml"));
            Pane pane=new Pane(root);
            Stage stageDialog=new Stage();
            Image icon = new Image("/images/icon.png");
            stageDialog.setTitle("Add Car Dialog");
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
            addCarController.stage=stageDialog;
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
                    fillTableData("select *from car");
                }
            }
        });

        this.carTable.setRowFactory( tv ->
        {
            TableRow<Car> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (! row.isEmpty()) )
                {
                    Car rowData = row.getItem();

                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(Main.mainStage);
                    alert.setHeaderText("Car Information");
                    alert.setContentText(
                            "Name: "+rowData.getCarName()+"\n"+
                                    "Type: "+rowData.getCarType()+"\n"+
                                    "Color: "+rowData.getCarColor()+"\n"+
                                    "Price: "+rowData.getPrice()
                    );
                    alert.show();
                }
            });
            return row ;
        });

        this.fillTableData("select *from car");
    }

    public void fillTableData(String query)
    {
        int carID;
        String carName;
        String carType;
        String carColor;
        int cID;
        int price;

        Car car;
        ObservableList<Car> carObservableList= FXCollections.observableArrayList();

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url1="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url1, "C##sundos_yaqeen", "11223344");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                carID=resultSet.getInt(1);
                carName=resultSet.getString(2);
                carType=resultSet.getString(3);
                carColor=resultSet.getString(4);
                cID=resultSet.getInt(6);
                price=resultSet.getInt(5);

                car=new Car(carID,carName,carType,carColor,cID,price,null);
                carObservableList.add(car);
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
            sqlException.printStackTrace();
            return;
        }

        this.idCol.setCellValueFactory(new PropertyValueFactory<Car,Integer>("carID"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<Car,String>("carName"));
        this.typeCol.setCellValueFactory(new PropertyValueFactory<Car,String>("carType"));
        this.colorCol.setCellValueFactory(new PropertyValueFactory<Car,String>("carColor"));
        this.cityIDCol.setCellValueFactory(new PropertyValueFactory<Car,Integer>("cityID"));
        this.priceCol.setCellValueFactory(new PropertyValueFactory<Car,Integer>("price"));

        this.carTable.setItems(carObservableList);
        this.carTable.refresh();

        carTable.widthProperty().addListener((source, oldWidth, newWidth) ->
        {
            TableHeaderRow header = (TableHeaderRow) carTable.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
    }
}