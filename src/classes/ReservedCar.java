package classes;

public class ReservedCar
{
    private int id;
    private int carID;
    private int rID;

    public ReservedCar(int id,int carID,int rID)
    {
        this.setId(id);
        this.setCarID(carID);
        this.setrID(rID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getrID() {
        return rID;
    }

    public void setrID(int rID) {
        this.rID = rID;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }
}