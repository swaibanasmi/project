package com.example.hppc.myapplication.models;

/**
 * Created by nasmi on 30/6/17.
 */

public class userprofile {

    private String image;
    private String name;
    private String designation;
    private String phone;
    private String email;
    private String buttonimage;
    private String company;
    private String profilelink;

    public void setname(String nam){this.name=nam;}
    public void setDesignation(String designati){this.designation=designati;}
    public String getname() {
        return name;
    }
    public String getdesignation() {
        return designation;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setphone(String phone){this.phone=phone;}
    public String getPhone() {
        return phone;
    }
    public void setEmail(String email){this.email=email;}
    public String getEmail() {
        return email;
    }
    public void setCompany(String company){this.company=company;}
    public String getcompany(){return company;}
    public void setProfilelink(String profilelink){
        this.profilelink=profilelink;
    }
    public String getProfilelink(){return profilelink;}
    public void setbutton()
    { this.buttonimage="R.drawable.default_image";}
    public String getButtonimage(){
        return buttonimage;

    }


}


