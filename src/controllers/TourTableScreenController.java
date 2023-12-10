package controllers;

import classes.Main;
import classes.Touristic;
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

public class TourTableScreenController implements Initializable
{
    @FXML
    private TableView<Touristic> tourTable;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<Touristic, Integer> idCol;

    @FXML
    private TableColumn<Touristic, String> nameCol;

    @FXML
    private TableColumn<Touristic, String> locationCol;

    @FXML
    private TableColumn<Touristic, String> descriptionCol;

    @FXML
    private TableColumn<Touristic, Integer> cidCol;

    @FXML
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private Button search;

    @FXML
    void addEvent(ActionEvent event)
    {
        try
        {
            Parent root= FXMLLoader.load(getClass().getResource("/fxml/addTour.fxml"));
            Pane pane=new Pane(root);
            Stage stageDialog=new Stage();
            Image icon = new Image("/images/icon.png");
            stageDialog.setTitle("Add Touristic Place Dialog");
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
            addTourController.stage=stageDialog;
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
    void deleteEvent(ActionEvent event)
    {
        Touristic touristic=this.tourTable.getSelectionModel().getSelectedItem();

        if(touristic!=null)
        {
            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url1="jdbc:oracle:thin:@localhost:1521:xe";
                Connection con = DriverManager.getConnection(url1, "C##sundos_yaqeen", "11223344");

                String query="delete from touristic_places_pictures where tid="+touristic.getId()+"";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                query="delete from touristic_place where touristic_id="+touristic.getId()+"";
                ps=con.prepareStatement(query);
                ps.executeQuery();
                con.close();
                this.fillTableData("select *from touristic_place");
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
            alert.setContentText("No Touristic Place Is Selected.");
            alert.showAndWait();
        }
    }

    @FXML
    void searchEvent(ActionEvent event)
    {
        if(!this.searchField.getText().isBlank())
        {
            this.fillTableData("select *from touristic_place where name='"+this.searchField.getText()+"'");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Image ima = new Image(("/images/searcha.png"));
        ImageView v = new ImageView(ima);
        this.search.setGraphic(v);
        Image ima2= new Image(("/images/add.png"));
        ImageView v2= new ImageView(ima2);
        this.add.setGraphic(v2);
        this.add.setText("Add");
        Image ima3= new Image(("/images/delete.png"));
        ImageView v3= new ImageView(ima3);
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
                    fillTableData("select *from touristic_place");
                }
            }
        });

        this.tourTable.setRowFactory( tv ->
        {
            TableRow<Touristic> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (! row.isEmpty()) )
                {
                    Touristic rowData = row.getItem();

                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(Main.mainStage);
                    alert.setHeaderText("Touristic Place Information");
                    alert.setContentText(
                            "Name: "+rowData.getName()+"\n"+
                                    "Location: "+rowData.getLocation()+"\n"+
                                    "Description: "+rowData.getDescription()
                    );
                    alert.show();
                }
            });
            return row ;
        });

        this.fillTableData("select *from touristic_place");
    }

    public void fillTableData(String query)
    {
        int id;
        String name;
        String location;
        String description;
        int cid;

        Touristic touristic;
        ObservableList<Touristic> touristicObservableList= FXCollections.observableArrayList();

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url1="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url1, "C##sundos_yaqeen", "11223344");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                id=resultSet.getInt(1);
                name=resultSet.getString(2);
                location=resultSet.getString(3);
                description=resultSet.getString(4);
                cid=resultSet.getInt(5);

                touristic=new Touristic( id, name, location, description, cid,null);
                touristicObservableList.add(touristic);
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

        this.idCol.setCellValueFactory(new PropertyValueFactory<Touristic,Integer>("id"));
        this.nameCol.setCellValueFactory(new PropertyValueFactory<Touristic,String>("name"));
        this.locationCol.setCellValueFactory(new PropertyValueFactory<Touristic,String>("location"));
        this.descriptionCol.setCellValueFactory(new PropertyValueFactory<Touristic,String>("description"));
        this.cidCol.setCellValueFactory(new PropertyValueFactory<Touristic,Integer>("cid"));

        this.tourTable.setItems(touristicObservableList);
        this.tourTable.refresh();

        tourTable.widthProperty().addListener((source, oldWidth, newWidth) ->
        {
            TableHeaderRow header = (TableHeaderRow) tourTable.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
    }
}