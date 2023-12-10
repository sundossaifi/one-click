package controllers;

import classes.Main;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oracle.jdbc.driver.OracleDriver;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class addCarController
{
    static Stage stage;
    private ArrayList<String> carPictures=new ArrayList<>();

    @FXML
    private TextField nameField;

    @FXML
    private TextField typeField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField cityIDField;

    @FXML
    private Button savebutton;

    @FXML
    private Button uploadButton;

    @FXML
    private TextField colorField;

    @FXML
    void saveEvent(ActionEvent event)
    {
        String carName;
        String carType;
        String carColor;
        int cityID;
        int price;
        int id = -1;

        if(this.checkIfDataIsValid())
        {
            carName=this.nameField.getText();
            carType=this.typeField.getText();
            carColor=this.colorField.getText();
            cityID=Integer.parseInt(this.cityIDField.getText());
            price=Integer.parseInt(this.priceField.getText());

            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@localhost:1521:xe";
                Connection con = DriverManager.getConnection(url, "c##sundos_yaqeen", "11223344");

                String query="insert into car values(id_seq.nextVAL,'"+carName+"','"+carType+"','"+carColor+"',"+price+","+cityID+")";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

                query="select id_seq.currval from DUAL";
                ps=con.prepareStatement(query);
                ResultSet resultSet=ps.executeQuery();
                resultSet.next();
                id=resultSet.getInt(1);

                for(int i=0;i<this.carPictures.size();i++)
                {
                    query="insert into car_pictures values(id_seq.nextVAL,'"+this.carPictures.get(i)+"',"+id+")";
                    ps=con.prepareStatement(query);
                    ps.executeQuery();
                }
                con.close();
                savebutton.setText("saved");
                Image image = new Image(("/images/check.png"));
                ImageView view = new ImageView(image);
                savebutton.setGraphic(view);

                Task<Void> sleeper = new Task<Void>()
                {
                    @Override
                    protected Void call()
                    {
                        try
                        {
                            Thread.sleep(1500);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };

                sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>()
                {
                    @Override
                    public void handle(WorkerStateEvent event)
                    {
                        savebutton.setGraphic(null);
                        savebutton.setText("save");
                    }
                });
                new Thread(sleeper).start();

                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(Main.mainStage);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setHeaderText(null);
                alert.setContentText("Car Added Successfully.");
                alert.showAndWait();

                FXMLLoader fxmlLoader1 = new FXMLLoader();
                fxmlLoader1.setLocation(getClass().getResource("/fxml/adminScreen.fxml"));
                Parent root1= fxmlLoader1.load();
                adminscreencontroller Adminscreencontroller = fxmlLoader1.getController();

                FXMLLoader fxmlLoader2 = new FXMLLoader();
                fxmlLoader2.setLocation(getClass().getResource("/fxml/CarTableScreen.fxml"));
                Parent root2= fxmlLoader2.load();
                CarTableScreenController carTableScreenController = fxmlLoader2.getController();
                carTableScreenController.fillTableData("select *from car");

                Adminscreencontroller.anch.getChildren().clear();
                Adminscreencontroller.anch.getChildren().add(root2);

                Scene scene=new Scene(root1);
                Main.mainStage.setScene(scene);
                stage.close();
            }
            catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException)
            {
                this.reverseOnError(id);
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText(null);
                alert.setContentText("Maybe You've Entered Duplicated Car Type, An Existing Image Or The City ID Not Found");
                alert.showAndWait();
            }
            catch (SQLException sqlException)
            {
                this.reverseOnError(id);
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText("Data-Base Connection Error!");
                alert.setContentText(sqlException.getMessage());
                alert.showAndWait();
            }
            catch(NullPointerException | IOException exception)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText("Loading Screen Error!");
                alert.setContentText(exception.getMessage());
                alert.show();
            }
        }
    }

    @FXML
    void addPictureEvent()
    {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        FileChooser.ExtensionFilter extFilterJPEG
                = new FileChooser.ExtensionFilter("JPEG files (*.JPEG)", "*.JPEG");
        FileChooser.ExtensionFilter extFilterjpeg
                = new FileChooser.ExtensionFilter("jpeg files (*.jpeg)", "*.jpeg");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng,extFilterJPEG,extFilterjpeg);

        File file = fileChooser.showOpenDialog(stage);

        if (file != null)
        {
            String imgPath=file.toString();
            this.carPictures.add(imgPath);
        }
    }

    private boolean checkIfDataIsValid()
    {
        String errorMessage="";
        boolean error=false;

        if(this.nameField.getText().isBlank())
        {
            this.nameField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Car Name Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.nameField.setStyle("");
        }

        if(this.typeField.getText().isBlank())
        {
            this.typeField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Car Type Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.typeField.setStyle("");
        }

        if(this.colorField.getText().isBlank())
        {
            this.colorField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Car Color Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.colorField.setStyle("");
        }

        if(this.priceField.getText().isBlank())
        {
            this.priceField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Price Field Is Empty.\n";
            error=true;
        }
        else if(!this.isNumeric(this.priceField.getText()))
        {
            this.priceField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Price Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.priceField.setStyle("");
        }

        if(this.cityIDField.getText().isBlank())
        {
            this.cityIDField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*City ID Field Is Empty.\n";
            error=true;
        }
        else if(!this.isNumeric(this.cityIDField.getText()))
        {
            this.cityIDField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*City ID Is Not Valid.\n";
            error=true;
        }
        else
        {
            this.cityIDField.setStyle("");
        }

        if(this.carPictures.size()==0)
        {
            this.uploadButton.setStyle("-fx-border-color: #FF0000; -fx-focus-color: #FF0000; -fx-border-radius: 30;" +
                    "-fx-border-insets: 0,1,1;");
            errorMessage+="*Please Upload A Car Picture.\n";
            error=true;
        }
        else
        {
            this.uploadButton.setStyle("");
        }

        if(error)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.savebutton.getScene().getWindow());
            alert.setHeaderText("Data Is Invalid!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean isNumeric(String string)
    {
        for(char c:string.toCharArray())
        {
            if(!Character.isDigit(c))
            {
                return false;
            }
        }
        return true;
    }

    private void reverseOnError(int carid)
    {
        try
        {
            DriverManager.registerDriver((new OracleDriver()));
            String url="jdbc:oracle:thin:@localhost:1521:xe";
            Connection con = DriverManager.getConnection(url, "c##sundos_yaqeen", "11223344");
            String query="delete from car_pictures where caid="+carid+"";
            PreparedStatement ps=con.prepareStatement(query);
            ps.executeQuery();

            query="delete from car where car_id="+carid+"";
            ps=con.prepareStatement(query);
            ps.executeQuery();

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
    }
}