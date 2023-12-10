package controllers;

import classes.Main;
import classes.Reservation;
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

public class ReserTableScreenController implements Initializable
{
    @FXML
    private TableView<Reservation> tableView;

    @FXML
    private TableColumn<Reservation, String> cityNameCol;

    @FXML
    private TableColumn<Reservation, LocalDate> ResDateCol;

    @FXML
    private TableColumn<Reservation, String> withHotelCol;

    @FXML
    private TableColumn<Reservation, String> withCarCol;

    @FXML
    private TableColumn<Reservation, String> hotelNameCol;

    @FXML
    private TableColumn<Reservation, String> carNameCol;

    @FXML
    private Button SearchUser;

    @FXML
    private DatePicker dateField;

    @FXML
    void searchEvent(ActionEvent event)
    {
        LocalDate date;
        if(this.dateField.getValue()!=null)
        {
            date=this.dateField.getValue();
            String resDate=""+date.getYear()+""+"-"+""+date.getMonthValue()+""+"-"+""+date.getDayOfMonth()+"";
            this.fillTableData("select *from reservation where reservation_date=TO_DATE('"+resDate+"','YYYY-MM-DD')");
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(Main.mainStage);
            alert.setHeaderText(null);
            alert.setContentText("Date Field Is Empty");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Image ima = new Image(("/images/searcha.png"));
        ImageView v = new ImageView(ima);
        this.SearchUser.setGraphic(v);
        this.dateField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent k)
            {
                if (k.getCode().equals(KeyCode.ENTER))
                {
                    dateField.setValue(null);
                    fillTableData("select *from reservation");
                }
            }
        });

        this.tableView.setRowFactory( tv ->
        {
            TableRow<Reservation> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (! row.isEmpty()) )
                {
                    Reservation rowData = row.getItem();

                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.initOwner(Main.mainStage);
                    alert.setHeaderText("Reservation Information");
                    alert.setContentText(
                                    "SSN: "+rowData.getUserSSN()+"\n"+
                                    "City Name: "+rowData.getCityName()+"\n"+
                                    "Reservation Date: "+rowData.getReservationDate().getYear()+"-"+rowData.getReservationDate().getMonthValue()+"-"+rowData.getReservationDate().getDayOfMonth()+"\n"+
                                    "Hotel Name: "+rowData.getHotelName()+"\n"+
                                    "Car Name: "+rowData.getCarName()
                    );
                    alert.show();
                }
            });
            return row ;
        });

        this.fillTableData("select *from reservation");
    }

    public void fillTableData(String query)
    {
        int cityID,ssn,rid,hotelID,carID;
        String hotelFlag,carFlag,rDate,cityName,hotelName,carName,carType;
        LocalDate reservationDate;

        Reservation reservation;
        ObservableList<Reservation> reservationObservableList= FXCollections.observableArrayList();

        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url1="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url1, "C##sundos_yaqeen", "11223344");
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();

            while(resultSet.next())
            {
                rid=resultSet.getInt(1);
                rDate=resultSet.getString(2);
                reservationDate=LocalDate.of(Integer.parseInt(rDate.split(" ")[0].split("-")[0]),Integer.parseInt(rDate.split(" ")[0].split("-")[1]),Integer.parseInt(rDate.split(" ")[0].split("-")[2]));
                cityID=resultSet.getInt(3);
                hotelFlag=resultSet.getString(4);
                carFlag=resultSet.getString(5);
                ssn=resultSet.getInt(6);

                reservation=new Reservation(rid,reservationDate,cityID,hotelFlag,carFlag,ssn,null,null,null);
                reservationObservableList.add(reservation);
            }

            for(int i=0;i<reservationObservableList.size();i++)
            {
                query="select *from city where city_id="+reservationObservableList.get(i).getCityID()+"";
                ps=con.prepareStatement(query);
                resultSet=ps.executeQuery();
                resultSet.next();

                cityName=resultSet.getString(2);
                reservationObservableList.get(i).setCityName(cityName);


                if(reservationObservableList.get(i).getHotelFlag().equals("T"))
                {
                    query="select *from reserved_hotels where rid="+reservationObservableList.get(i).getReservationID()+"";
                    ps=con.prepareStatement(query);
                    resultSet=ps.executeQuery();
                    resultSet.next();
                    hotelID=resultSet.getInt(2);

                    query="select *from hotel where hotel_id="+hotelID+"";
                    ps=con.prepareStatement(query);
                    resultSet=ps.executeQuery();
                    resultSet.next();
                    hotelName=resultSet.getString(2);
                }
                else
                {
                    hotelName="-";
                }

                if(reservationObservableList.get(i).getCarFlag().equals("T"))
                {
                    query="select *from reserved_cars where rid="+reservationObservableList.get(i).getReservationID()+"";
                    ps=con.prepareStatement(query);
                    resultSet=ps.executeQuery();
                    resultSet.next();
                    carID=resultSet.getInt(2);

                    query="select *from car where car_id="+carID+"";
                    ps=con.prepareStatement(query);
                    resultSet=ps.executeQuery();
                    resultSet.next();
                    carName=resultSet.getString(2);
                    carType=resultSet.getString(3);
                    carName+="-";
                }
                else
                {
                    carName="-";
                    carType="";
                }

                reservationObservableList.get(i).setHotelName(hotelName);
                reservationObservableList.get(i).setCarName(carName+carType);
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

        this.cityNameCol.setCellValueFactory(new PropertyValueFactory<Reservation,String>("cityName"));
        this.ResDateCol.setCellValueFactory(new PropertyValueFactory<Reservation,LocalDate>("reservationDate"));
        this.withHotelCol.setCellValueFactory(new PropertyValueFactory<Reservation,String>("hotelFlag"));
        this.withCarCol.setCellValueFactory(new PropertyValueFactory<Reservation,String>("carFlag"));
        this.hotelNameCol.setCellValueFactory(new PropertyValueFactory<Reservation,String>("hotelName"));
        this.carNameCol.setCellValueFactory(new PropertyValueFactory<Reservation,String>("carName"));

        this.tableView.setItems(reservationObservableList);
        this.tableView.refresh();

        tableView.widthProperty().addListener((source, oldWidth, newWidth) ->
        {
            TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });
    }
}