package controllers;

import classes.Main;
import classes.Stuff;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import oracle.jdbc.driver.OracleDriver;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BookDialogController implements Initializable
{
    @FXML
    private ImageView imageview;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        imageview.setImage(new Image("/images/click.gif"));
        Callback<DatePicker, DateCell> dayCellFactory  = this.disableReservedAndPassedDays();
        this.datePicker.setDayCellFactory(dayCellFactory);
    }

    @FXML
    void confirmEvent() throws IOException
    {
        String hotelFlag;
        String carFlag;
        int currentValue;
        if(this.datePicker.getValue()==null)
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.cancelButton.getScene().getWindow());
            alert.setHeaderText(null);
            alert.setContentText("Please Choose Reservation Date");
            alert.show();
        }
        else
        {
            try
            {
                LocalDate localDate=this.datePicker.getValue();
                String rDate=""+localDate.getYear()+""+"-"+""+localDate.getMonthValue()+""+"-"+""+localDate.getDayOfMonth()+"";

                if(Stuff.car!=null)
                {
                    carFlag="T";
                }
                else
                {
                    carFlag="F";
                }

                if(Stuff.hotel!=null)
                {
                    hotelFlag="T";
                }
                else
                {
                    hotelFlag="F";
                }

                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@localhost:1521:xe";
                Connection con = DriverManager.getConnection(url, "C##sundos_yaqeen", "11223344");
                String query="insert into reservation VALUES(ID_SEQ.nextval,TO_DATE('"+rDate+"','YYYY-MM-DD'),"+Stuff.city.getCityID()+",'"+hotelFlag+"','"+carFlag+"',"+Stuff.user.getSSN()+")";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                query="select ID_SEQ.currval from DUAL";
                ps=con.prepareStatement(query);
                ResultSet resultSet=ps.executeQuery();
                resultSet.next();
                currentValue=resultSet.getInt(1);

                if(Stuff.hotel!=null)
                {
                    query="insert into reserved_hotels VALUES(ID_SEQ.nextval,"+Stuff.hotel.getHotelID()+","+currentValue+")";
                    ps=con.prepareStatement(query);
                    ps.executeQuery();
                }

                if(Stuff.car!=null)
                {
                    query="insert into reserved_cars VALUES(ID_SEQ.nextval,"+Stuff.car.getCarID()+","+currentValue+")";
                    ps=con.prepareStatement(query);
                    ps.executeQuery();
                }

                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText(null);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setContentText("Your Reservation Is Assigned Successfully.");
                alert.showAndWait();
                CityOptionsController.stageDialog.close();

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/CityOptionsScreen.fxml"));
                Parent root= fxmlLoader.load();

                Scene scene=new Scene(root);
                Main.mainStage.setScene(scene);

                CityOptionsController cityOptionsController = fxmlLoader.getController();
                cityOptionsController.booked();
            }
            catch(SQLException sqlException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText("Data-Base Connection Error!");
                alert.setContentText(sqlException.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void cancelEvent()
    {
        CityOptionsController.stageDialog.close();
    }

    private Callback<DatePicker, DateCell> disableReservedAndPassedDays()
    {
        ArrayList<LocalDate> localDateList = this.getBookedAppointmentDates();
        return (final DatePicker datePicker) ->
                new DateCell()
                {
                    @Override
                    public void updateItem(LocalDate item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        setDisable(empty || item.compareTo(LocalDate.now()) < 0);
                        if (localDateList.contains(item))
                        {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
    }

    private ArrayList<LocalDate> getBookedAppointmentDates()
    {
        ArrayList<LocalDate>reservedDates=new ArrayList<>();
        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url, "C##sundos_yaqeen", "11223344");
            String query="select reservation_date from reservation where ussn="+Stuff.user.getSSN()+"";
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet resultSet=ps.executeQuery();
            while(resultSet.next())
            {
                String date=resultSet.getString(1);
                LocalDate localDate=LocalDate.of(Integer.parseInt(date.split(" ")[0].split("-")[0]),Integer.parseInt(date.split(" ")[0].split("-")[1]),Integer.parseInt(date.split(" ")[0].split("-")[2]));
                reservedDates.add(localDate);
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
        return reservedDates;
    }
}