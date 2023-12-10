package classes;

public class ReservedHotel
{
    private int id;
    private int hotelID;
    private int rID;

    public ReservedHotel(int id,int hotelID,int rID)
    {
        this.setId(id);
        this.setHotelID(hotelID);
        this.setrID(rID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHotelID() {
        return hotelID;
    }

    public void setHotelID(int hotelID) {
        this.hotelID = hotelID;
    }

    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }
}