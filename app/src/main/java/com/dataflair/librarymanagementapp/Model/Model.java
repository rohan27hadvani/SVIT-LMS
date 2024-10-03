package com.dataflair.librarymanagementapp.Model;

public class Model {

    String name, city, profilepic, phoneNumber, address,userId,pincode,CategoryImage,category;

    String bookLocation;
    String bookName;
    String booksCount;
    String imageUrl;
    String pushKey;
    String approve_status;
    public Model(String name, String city, String profilepic, String phoneNumber, String address, String userId, String pincode, String categoryImage, String category, String bookLocation, String bookName, String booksCount, String imageUrl, String pushKey, String approve_status, String notification) {
        this.name = name;
        this.city = city;
        this.profilepic = profilepic;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userId = userId;
        this.pincode = pincode;
        CategoryImage = categoryImage;
        this.category = category;
        this.bookLocation = bookLocation;
        this.bookName = bookName;
        this.booksCount = booksCount;
        this.imageUrl = imageUrl;
        this.pushKey = pushKey;
        this.approve_status = approve_status;
        this.notification = notification;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }



    String notification;

    public Model() {}

    public Model(String name, String city, String profilepic, String phoneNumber, String address, String userId, String pincode, String categoryImage, String category, String bookLocation, String bookName, String booksCount, String imageUrl, String pushKey, String notification) {
        this.name = name;
        this.city = city;
        this.profilepic = profilepic;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.userId = userId;
        this.pincode = pincode;
        CategoryImage = categoryImage;
        this.category = category;
        this.bookLocation = bookLocation;
        this.bookName = bookName;
        this.booksCount = booksCount;
        this.imageUrl = imageUrl;
        this.pushKey = pushKey;
        this.notification = notification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(String bookLocation) {
        this.bookLocation = bookLocation;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBooksCount() {
        return booksCount;
    }

    public void setBooksCount(String booksCount) {
        this.booksCount = booksCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
