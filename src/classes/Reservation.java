package classes;

import java.time.LocalDate;

public class Reservation
{
    private int reservationID;
    private LocalDate reservationDate;
    private int cityID;
    private String hotelFlag;
    private String carFlag;
    private int userSSN;
    private String cityName;
    private String hotelName;
    private String carName;

    public Reservation(int reservationID,LocalDate reservationDate,int cityID,String hotelFlag,String carFlag,int userSSN,String cityName,String hotelName,String carName)
    {
        this.setReservationID(reservationID);
        this.setReservationDate(reservationDate);
        this.setCityID(cityID);
        this.setHotelFlag(hotelFlag);
        this.setCarFlag(carFlag);
        this.setUserSSN(userSSN);
        this.setCityName(cityName);
        this.setHotelName(hotelName);
        this.setCarName(carName);
    }


    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getHotelFlag() {
        return hotelFlag;
    }

    public void setHotelFlag(String hotelFlag) {
        this.hotelFlag = hotelFlag;
    }

    public String getCarFlag() {
        return carFlag;
    }

    public void setCarFlag(String carFlag) {
        this.carFlag = carFlag;
    }

    public int getUserSSN() {
        return userSSN;
    }

    public void setUserSSN(int userSSN) {
        this.userSSN = userSSN;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }
}