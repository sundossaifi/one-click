package classes;

public class Hotel
{
    private int hotelID;
    private String hotelName;
    private int rate;
    private String location;
    private String pic;
    private int cityID;

    public Hotel(int hotelID,String hotelName,int rate,String location,String pic,int cityID)
    {
        this.setHotelID(hotelID);
        this.setHotelName(hotelName);
        this.setRate(rate);
        this.setLocation(location);
        this.setPic(pic);
        this.setCityID(cityID);
    }

    public int getHotelID() {
        return hotelID;
    }

    public void setHotelID(int hotelID) {
        this.hotelID = hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
}