package controllers;

import classes.Main;
import classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import oracle.jdbc.driver.OracleDriver;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UserTableScreenController implements Initializable
{
    @FXML
    private Button search;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Integer> SSNCol;

    @FXML
    private TableColumn<User, String> NameCol;

    @FXML
    private TableColumn<User, String> EmailCol;

    @FXML
    private TableColumn<User, String> TypeCol;

    @FXML
    private TableColumn<User, String> CountryCol;

    @FXML
    private TableColumn<User, LocalDate> BDCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Image ima = new Image(("/images/searcha.png"));
        ImageView v = new ImageView(ima);
        this.search.setGraphic(v);

        this.searchField.setOnKeyPressed(k -> {
            if (k.getCode().equals(KeyCode.ENTER))
            {
                searchField.setText("");
                fillTableData("select *from USERS");
            }
        });

        this.tableView.setRowFactory( tv ->
        {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (! row.isEmpty()) )
                {
                    User rowData = row.getItem();

                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(Main.mainStage);
                    alert.setHeaderText("User Information");
                    alert.setContentText(
                            "Name: "+rowData.getName()+"\n"+
                                    "SSN: "+rowData.getSSN()+"\n"+
                                    "Date Of Birth: "+rowData.getDOB().getYear()+"-"+rowData.getDOB().getMonthValue()+"-"+rowData.getDOB().getDayOfMonth()+"\n"+
                                    "Country: "+rowData.getCountry()+"\n"+
                                    "Email: "+rowData.getEmail()+"\n"+
                                    "Type: "+rowData.getType()+"\n"
                    );
                    alert.show();
                }
            });
            return row ;
        });

        this.fillTableData("select *from USERS");
    }

    @FXML
    void searchEvent(ActionEvent event)
    {
        if(!this.searchField.getText().isBlank())
        {
            this.fillTableData("select *from USERS where name='"+this.searchField.getText()+"'");
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

    public void fillTableData(String query)
    {
        int ssn;
        String name,country,type,email,dob;
        LocalDate dobirth;
        User user;
        ObservableList<User> userObservableList= FXCollections.observableArrayList();
        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url1="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url1, "C##sundos_yaqeen", "11223344");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                ssn = resultSet.getInt(2);
                name = resultSet.getString(1);
                email = resultSet.getString(3);
                type = resultSet.getString(6);
                country = resultSet.getString(7);
                dob=resultSet.getString(8);
                dobirth= LocalDate.of(Integer.parseInt(dob.split(" ")[0].split("-")[0]),Integer.parseInt(dob.split(" ")[0].split("-")[1]),Integer.parseInt(dob.split(" ")[0].split("-")[2]));

                user = new User(name,ssn,dobirth,country,email,null,null,type);
                userObservableList.add(user);
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
            sqlException.printStackTrace();
            return;
        }

        this.NameCol.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        this.SSNCol.setCellValueFactory(new PropertyValueFactory<User,Integer>("SSN"));
        this.BDCol.setCellValueFactory(new PropertyValueFactory<User,LocalDate>("DOB"));
        this.CountryCol.setCellValueFactory(new PropertyValueFactory<User,String>("country"));
        this.EmailCol.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        this.TypeCol.setCellValueFactory(new PropertyValueFactory<User,String>("type"));



        this.tableView.setItems(userObservableList);
        this.tableView.refresh();

        tableView.widthProperty().addListener((source, oldWidth, newWidth) ->
        {
            TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
    }
}