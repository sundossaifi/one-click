package classes;

import java.time.LocalDate;

public class User
{
    private String name;
    private int SSN;
    private LocalDate DOB;
    private String country;
    private String email;
    private String password;
    private String picPath;
    private String type;

    public User(String name, int SSN, LocalDate DOB, String country, String email, String password, String picPath, String type)
    {
        this.setName(name);
        this.setSSN(SSN);
        this.setDOB(DOB);
        this.setCountry(country);
        this.setEmail(email);
        this.setPassword(password);
        this.setPicPath(picPath);
        this.setType(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSSN() {
        return SSN;
    }

    public void setSSN(int SSN) {
        this.SSN = SSN;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}