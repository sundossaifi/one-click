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

public class addCityController
{
    static Stage stage;
    @FXML
    private TextField nameField;

    @FXML
    private TextField DescField;

    @FXML
    private Button savebutton;

    @FXML
    private Button uploadButton;

    @FXML
    private Button uploadGifButton;

    private String picPath=null;
    private String gifPath=null;


    @FXML
    void addGifEvent(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterGIF
                = new FileChooser.ExtensionFilter("GIF files (*.GIF)", "*.GIF");
        FileChooser.ExtensionFilter extFiltergif
                = new FileChooser.ExtensionFilter("gif files (*.gif)", "*.gif");
        fileChooser.getExtensionFilters()
                .addAll(extFilterGIF,extFiltergif);
        File file = fileChooser.showOpenDialog(stage);

        if (file != null)
        {
            this.gifPath = file.toString();
        }
        else
        {
            this.gifPath = null;
        }
    }

    @FXML
    void addPictureEvent(ActionEvent event)
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
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng, extFilterJPEG, extFilterjpeg);

        File file = fileChooser.showOpenDialog(stage);

        if (file != null)
        {
            this.picPath = file.toString();
        }
        else
        {
            this.picPath = null;
        }
    }

    @FXML
    void saveEvent(ActionEvent event)
    {
        String name;
        String description;


        if(this.checkIfDataIsValid())
        {
            name=this.nameField.getText();
            description=this.DescField.getText();

            try
            {
                DriverManager.registerDriver((new OracleDriver()));
                String url="jdbc:oracle:thin:@localhost:1521:xe";
                Connection con = DriverManager.getConnection(url, "c##sundos_yaqeen", "11223344");

                String query="insert into city values(id_seq.nextVAL,'"+name+"','"+this.gifPath+"','"+description+"','"+this.picPath+"')";
                PreparedStatement ps=con.prepareStatement(query);
                ps.executeQuery();

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
                    public void handle(WorkerStateEvent event) {
                        savebutton.setGraphic(null);
                        savebutton.setText("save");
                    }
                });
                new Thread(sleeper).start();

                Alert alert=new Alert(Alert.AlertType.NONE);
                alert.initOwner(Main.mainStage);
                alert.getButtonTypes().add(ButtonType.OK);
                alert.setHeaderText(null);
                alert.setContentText("City Added Successfully.");
                alert.showAndWait();

                FXMLLoader fxmlLoader1 = new FXMLLoader();
                fxmlLoader1.setLocation(getClass().getResource("/fxml/adminScreen.fxml"));
                Parent root1= fxmlLoader1.load();
                adminscreencontroller Adminscreencontroller = fxmlLoader1.getController();

                FXMLLoader fxmlLoader2 = new FXMLLoader();
                fxmlLoader2.setLocation(getClass().getResource("/fxml/CityTableScreen.fxml"));
                Parent root2= fxmlLoader2.load();
                CityTableController cityTableController = fxmlLoader2.getController();
                cityTableController.fillTableData("select *from city");

                Adminscreencontroller.anch.getChildren().clear();
                Adminscreencontroller.anch.getChildren().add(root2);

                Scene scene=new Scene(root1);
                Main.mainStage.setScene(scene);
                stage.close();
            }
            catch(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException)
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.initOwner(Main.mainStage);
                alert.setHeaderText(null);
                alert.setContentText("Maybe You've Entered An Existing Image, Gif Or Name");
                alert.showAndWait();
            }
            catch (SQLException sqlException)
            {
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

    private boolean checkIfDataIsValid()
    {
        String errorMessage="";
        boolean error=false;

        if(this.nameField.getText().isBlank())
        {
            this.nameField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*City Name Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.nameField.setStyle("");
        }

        if(this.DescField.getText().isBlank())
        {
            this.DescField.setStyle("-fx-text-box-border: #FF0000; -fx-focus-color: #FF0000;");
            errorMessage+="*Description Field Is Empty.\n";
            error=true;
        }
        else
        {
            this.DescField.setStyle("");
        }

        if(this.picPath==null)
        {
            this.uploadButton.setStyle("-fx-border-color: #FF0000; -fx-focus-color: #FF0000; -fx-border-radius: 30;" +
                    "-fx-border-insets: 0,1,1;");
            errorMessage+="*Please Upload A City Picture.\n";
            error=true;
        }
        else
        {
            this.uploadButton.setStyle("");
        }
        if(this.gifPath==null)
        {
            this.uploadGifButton.setStyle("-fx-border-color: #FF0000; -fx-focus-color: #FF0000; -fx-border-radius: 30;" +
                    "-fx-border-insets: 0,1,1;");
            errorMessage+="*Please Upload A City Gif.\n";
            error=true;
        }
        else
        {
            this.uploadGifButton.setStyle("");
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
}