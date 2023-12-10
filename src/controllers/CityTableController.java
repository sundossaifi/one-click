package controllers;

import classes.City;
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

public class CityTableController implements Initializable
{
    @FXML
    private TableView<City> cityTable;

    @FXML
    private TableColumn<City, Integer> idCol;

    @FXML
    private TableColumn<City, String> nameCol;

    @FXML
    private TableColumn<City, String> Description;

    @FXML
    private Button delete;

    @FXML
    private Button add;

    @FXML
    private TextField searchField;

    @FXML
    private Button search;

    @FXML
    void addcity(ActionEvent event)
    {
        try
        {
            Parent root= FXMLLoader.load(getClass().getResource("/fxml/addCity.fxml"));
            Pane pane=new Pane(root);
            Stage stageDialog=new Stage();
            Image icon = new Image("/images/icon.png");
            stageDialog.setTitle("Add City Dialog");
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
            addCityController.stage=stageDialog;
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

    @FXML
    void searchEvent(ActionEvent event) {
        if (!this.searchField.getText().isBlank()) {
            this.fillTableData("select *from city where name='" + this.searchField.getText() + "'");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText(null);
            alert.setContentText("Search Field Is Empty");
            alert.showAndWait();
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

        this.searchField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent k)
            {
                if (k.getCode().equals(KeyCode.ENTER))
                {
                    searchField.setText("");
                    fillTableData("select *from city");
                }
            }
        });

        this.cityTable.setRowFactory( tv ->
        {
            TableRow<City> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (! row.isEmpty()) )
                {
                    City rowData = row.getItem();

                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(Main.mainStage);
                    alert.setHeaderText("City Information");
                    alert.setContentText(
                                    "Name: "+rowData.getName()+"\n"+
                                    "Description: "+rowData.getDescription()

                    );
                    alert.show();
                }
            });
            return row ;
        });
        this.fillTableData("select *from city");
    }
    public void fillTableData(String query)
    {
        int cityID;
        String cityName;
        String citydesc;

        City city;
        ObservableList<City> cityObservableList= FXCollections.observableArrayList();

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url1="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url1, "C##sundos_yaqeen", "11223344");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                cityID=resultSet.getInt(1);
                cityName=resultSet.getString(2);
                citydesc=resultSet.getString(4);


                city=new City(cityID,cityName,null,null,citydesc);
                cityObservableList.add(city);
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

        this.idCol.setCellValueFactory(new PropertyValueFactory<City,Integer>("cityID"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<City,String>("name"));
        this.Description.setCellValueFactory(new PropertyValueFactory<City,String>("description"));


        this.cityTable.setItems(cityObservableList);
        this.cityTable.refresh();

        cityTable.widthProperty().addListener((source, oldWidth, newWidth) ->
        {
            TableHeaderRow header = (TableHeaderRow) cityTable.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
    }
}