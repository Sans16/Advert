package com.example.ssanusi.advert.model;

public class AdvertDetails {
    String categories;
    private String Description;
    private int logo;
    private String companyName;
    private String Date;
    private String phoneNumber;

    public AdvertDetails(String categories, String description, int logo, String companyName, String date) {
        this.categories = categories;
        Description = description;
        this.logo = logo;
        this.companyName = companyName;
        Date = date;
    }

    public String getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public String getCategories() { return categories;}
    public void setCategories(String categories) {this.categories = categories;}
    public String getDescription() {return Description;}
    public void setDescription(String description) {Description = description;}
    public int getLogo() {return logo;}
    public void setLogo(int logo) {this.logo = logo;}
    public String getCompanyName() {return companyName;}
    public void setCompanyName(String companyName) {this.companyName = companyName;}
    public String getDate() {return Date;}
    public void setDate(String date) {Date = date;}
}
