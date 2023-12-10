package classes;

public class Restaurant
{
    private int id;
    private String name;
    private String location;
    private String description;
    private int cid;
    private String pic;

    public Restaurant(int id,String name,String location,String description,int cid,String pic)
    {
        this.setId(id);
        this.setName(name);
        this.setLocation(location);
        this.setDescription(description);
        this.setCid(cid);
        this.setPic(pic);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}