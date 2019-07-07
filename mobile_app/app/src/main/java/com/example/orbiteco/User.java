package com.example.orbiteco;

import org.w3c.dom.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class User {
    private String mUsername;
    private String mEMail;
    private int mTokenAmount;
    private String mPassword;
    private HashMap<String, Integer> mInfos;

    private Double lastComputedScore = 0.0;

    public static Double MAX_DISTANCE = 0.02;

    public User(String username) {
        buildUser(username, "nopass", "none", 0);
    }

    public User(String username, String password, String eMail, int tokenAmount) {
        buildUser(username, password, eMail, tokenAmount);
    }

    public void buildUser(String username, String password, String eMail, int tokenAmount) {
        mInfos = new HashMap<>();
        setmInfos("Fast_food", 3);
        setmInfos("Japanese", 3);
        setmInfos("Italian", 3);
        setmInfos("Chinese", 3);
        setmInfos("Bank", 3);
        setmInfos("Shop", 3);
        setmInfos("Laundry", 3);
        setmInfos("Shoe", 3);
        setmInfos("Clothing", 3);

        mUsername = username;
        mTokenAmount = tokenAmount;
        mPassword = password;
        mEMail = eMail;
    }

    public void setmInfos(String key, Integer value) {
        if (value < 1) {
            value = 1;
        } else if (value > 5) {
            value = 5;
        }

        mInfos.put(key, value);
    }

    // The distance is in degree for the moment, so its value varies depending on the latitude
    public Double computeScore(Double distance, Shop shop) {
        Double distanceFactor = 1 - distance/MAX_DISTANCE;

        System.out.println(" --- : " + distanceFactor);
        Double score = 1.0;
        for (Map.Entry<String, Integer> info : mInfos.entrySet()) {
            if (shop.mType == info.getKey()) {
                score += info.getValue();
            }
        }
        score *= distanceFactor;
        shop.score = score;
        return score;
    }

    public Integer getmInfos(String key) {
        return mInfos.get(key);
    }

    public String getUsername() {
        return mUsername;
    }

    public int getTokenAmount() {
        return mTokenAmount;
    }
}
