package controllers;

import classes.Main;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import oracle.jdbc.datasource.OracleDataSource;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.util.Objects;
import java.util.ResourceBundle;

public class adminscreencontroller implements Initializable
{
    @FXML
    private AnchorPane pane1;

    @FXML
    private AnchorPane bighomeanc;

    @FXML
    private AnchorPane homeanch;

    @FXML
    private ImageView hima;

    @FXML
    private AnchorPane bigcaranc;
    @FXML
    private AnchorPane parentContainer;
    @FXML
    private AnchorPane caranc;

    @FXML
    private ImageView cima;

    @FXML
    private AnchorPane bigresanc;

    @FXML
    private AnchorPane resanc;

    @FXML
    private ImageView rima;

    @FXML
    private AnchorPane bigtoranc;

    @FXML
    private AnchorPane toranc;

    @FXML
    private ImageView tima;

    @FXML
    private AnchorPane biguseranc;

    @FXML
    private AnchorPane useranc;

    @FXML
    private ImageView uima;

    @FXML
    private AnchorPane bigreseranc;

    @FXML
    private AnchorPane reseranc;

    @FXML
    private ImageView resima;

    @FXML
    private AnchorPane bigloganc;
    @FXML
    private AnchorPane pane;
    @FXML
    private AnchorPane loganc;

    @FXML
    private ImageView logb;

    @FXML
    private ImageView icon;

    @FXML
    private AnchorPane pane2;

    @FXML
    private Label upname;

    @FXML
    public AnchorPane anch;
    @FXML
    private Button Print;
    @FXML
    private AnchorPane bigcityanc;

    @FXML
    private AnchorPane cityanc;

    @FXML
    private ImageView citypic;
    @FXML
    void printr(ActionEvent event) {
        try
        {
            OracleDataSource ods=new oracle.jdbc.pool.OracleDataSource();

            ods.setURL("jdbc:oracle:thin:@localhost:1521:xe");
            ods.setUser("C##sundos_yaqeen");
            ods.setPassword("11223344");
            Connection con= ods.getConnection();
            InputStream inputStream=new FileInputStream(new File("src/report/Reservations.jrxml"));
            JasperDesign jd= JRXmlLoader.load(inputStream);
            JasperReport jr= JasperCompileManager.compileReport(jd);
            JasperPrint jp= JasperFillManager.fillReport(jr,null,con);
            OutputStream output=new FileOutputStream(new File("Report.pdf"));
            JasperExportManager.exportReportToPdfStream(jp,output);
            output.close();
            inputStream.close();
            con.close();

            Alert alert=new Alert(Alert.AlertType.NONE);
            alert.getButtonTypes().add(ButtonType.OK);
            alert.setHeaderText(null);
            alert.initOwner(Main.mainStage);
            alert.setContentText("Report Generated Successfully");
            alert.showAndWait();
            JasperViewer.viewReport(jp, false);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    @FXML
void returnColor(MouseEvent event) {
        this.homeanch.setStyle("-fx-background-color: #6F4C5B;");
        this.hima.setImage(new Image("/images/hicon.png"));
    }
    @FXML
    void changeColor(MouseEvent event) {
this.homeanch.setStyle("-fx-background-color: #C8C6C6;");
        this.hima.setImage(new Image("/images/hicon2.png"));
    }



    @FXML
    void carchange(MouseEvent event)
    {
        this.caranc.setStyle("-fx-background-color: #C8C6C6;");
        this.cima.setImage(new Image("/images/cicon.png"));
    }


    @FXML
    void returncar(MouseEvent event)
    {
        this.caranc.setStyle("-fx-background-color: #6F4C5B;");
        this.cima.setImage(new Image("/images/cicon2.png"));
    }

    @FXML
    void changeres(MouseEvent event)
    {
        this.resanc.setStyle("-fx-background-color: #C8C6C6;");
        this.rima.setImage(new Image("/images/ricon.png"));
    }

    @FXML
    void returnres(MouseEvent event)
    {
        this.resanc.setStyle("-fx-background-color: #6F4C5B;");
        this.rima.setImage(new Image("/images/ricon2.png"));
    }
    @FXML
    void changetor(MouseEvent event) {
        this.toranc.setStyle("-fx-background-color: #C8C6C6;");
        this.tima.setImage(new Image("/images/ticon.png"));
    }
    @FXML
    void returntor(MouseEvent event) {
        this.toranc.setStyle("-fx-background-color: #6F4C5B;");
        this.tima.setImage(new Image("/images/ticon2.png"));
    }
    @FXML
    void changeuser(MouseEvent event) {
        this.useranc.setStyle("-fx-background-color: #C8C6C6;");
        this.uima.setImage(new Image("/images/user2.png"));
    }
    @FXML
    void returnuser(MouseEvent event) {
        this.useranc.setStyle("-fx-background-color: #6F4C5B;");
        this.uima.setImage(new Image("/images/user1.png"));
    }
    @FXML
    void changereser(MouseEvent event) {
        this.reseranc.setStyle("-fx-background-color: #C8C6C6;");
        this.resima.setImage(new Image("/images/book2.png"));
    }
    @FXML
    void returnreser(MouseEvent event) {
        this.reseranc.setStyle("-fx-background-color: #6F4C5B;");
        this.resima.setImage(new Image("/images/book1.png"));
    }
    @FXML
    void cityEvent(MouseEvent event) {
        this.upname.setText("City");

        try {
            Node node =(Node)FXMLLoader.load(getClass().getResource("/fxml/CityTableScreen.fxml"));
            this.anch.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void citychange(MouseEvent event) {
        this.cityanc.setStyle("-fx-background-color: #C8C6C6;");
        this.citypic.setImage(new Image("/images/city2.png"));
    }
    @FXML
    void returncity(MouseEvent event) {
        this.cityanc.setStyle("-fx-background-color: #6F4C5B;");
        this.citypic.setImage(new Image("/images/city1.png"));
    }


    @FXML
    void hotel(MouseEvent event) {
        this.upname.setText("Hotel");

        try {
            Node node =(Node)FXMLLoader.load(getClass().getResource("/fxml/HotelTableScreen.fxml"));
            this.anch.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void rest(MouseEvent event) {
        this.upname.setText("Restaurant");

        try {
            Node node =(Node)FXMLLoader.load(getClass().getResource("/fxml/RestTableScreen.fxml"));
            this.anch.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void tour(MouseEvent event) {
        this.upname.setText("Touristic Places");

        try {
            Node node =(Node)FXMLLoader.load(getClass().getResource("/fxml/TourTableScreen.fxml"));
            this.anch.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void cars(MouseEvent event) {
this.upname.setText("Cars");

   try {
            Node node =(Node)FXMLLoader.load(getClass().getResource("/fxml/CarTableScreen.fxml"));
            this.anch.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
   }

    }
    @FXML
    void user(MouseEvent event) {
        this.upname.setText("Users");
        try {
            Node node =(Node)FXMLLoader.load(getClass().getResource("/fxml/UserTableScreen.fxml"));
            this.anch.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void reser(MouseEvent event) {
        this.upname.setText("Reservations");
        try {
            Node node =(Node)FXMLLoader.load(getClass().getResource("/fxml/ReserTableScreen.fxml"));
            this.anch.getChildren().setAll(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loggif(MouseEvent event) {
        this.loganc.setStyle("-fx-background-color: #C8C6C6;");
        this.logb.setImage(new Image("/images/logout2.png"));
    }

    @FXML
    void logpic(MouseEvent event) {
        this.loganc.setStyle("-fx-background-color: #6F4C5B;");
        this.logb.setImage(new Image("/images/logout1.png"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image1 = new Image("/images/print.png");
        ImageView v = new ImageView(image1);
        this.Print.setGraphic(v);


        this.hima.setImage(new Image("/images/hicon.png"));
        this.cima.setImage(new Image("/images/cicon2.png"));
        this.rima.setImage(new Image("/images/ricon2.png"));
        this.tima.setImage(new Image("/images/ticon2.png"));
        this.icon.setImage(new Image("/images/icon.png"));
        this.logb.setImage(new Image("/images/logout1.png"));
        this.uima.setImage(new Image("/images/user1.png"));
        this.resima.setImage(new Image("/images/book1.png"));
        this.citypic.setImage(new Image("/images/city1.png"));
    }

    @FXML
    void logoutEvent()
    {
        FadeTransition fadeTransition=new FadeTransition();
        fadeTransition.setNode(this.pane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0.3);
        fadeTransition.setDuration(Duration.millis(1000));
        fadeTransition.setOnFinished(e->{
            try
            {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/goodbye.fxml")));
                Scene scene = this.anch.getScene();
                root.translateXProperty().set(scene.getWidth());
                this.parentContainer = (AnchorPane) this.anch.getScene().getRoot();
                this.parentContainer.getChildren().add(root);
                Timeline timeline = new Timeline();
                KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
                KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
                timeline.getKeyFrames().add(kf);
                timeline.setOnFinished(this::handle);
                timeline.play();
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        });
        fadeTransition.play();
    }
    private void handle(ActionEvent t)
    {
        this.parentContainer.getChildren().remove(this.parentContainer);
    }
}