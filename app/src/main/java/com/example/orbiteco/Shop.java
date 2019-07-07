package com.example.orbiteco;

public class Shop implements Comparable<Shop>{
    public String mName;
    public String mType;

    public String mPhoneNumber;
    public Double mLatitude;
    public Double mLongitude;

    public Integer mImg;

    public Double score;

    public Shop() {}

    public Shop(String name, String type, Integer img, Double latitude, Double longitude) {
        mName = name;
        mType = type;
        mImg = img;
        mPhoneNumber = "00299692458";
        mLatitude = latitude;
        mLongitude = longitude;
    }

    @Override
    public int compareTo(Shop shop) {
        if (this.score < shop.score) {
            return -1;
        } else if(this.score == shop.score) {
            return 0;
        } else {
            return 1;
        }
    }
}
