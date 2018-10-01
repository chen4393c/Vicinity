package com.chen4393c.vicinity.model;

public class User {

    private double mUserTimestamp;
    private String mUserAccount;
    private String mUserPassword;

    public double getUserTimestamp() {
        return mUserTimestamp;
    }

    public void setUserTimestamp(double userTimestamp) {
        this.mUserTimestamp = userTimestamp;
    }

    public String getUserAccount() {
        return mUserAccount;
    }

    public void setUserAccount(String userAccount) {
        this.mUserAccount = userAccount;
    }

    public String getUserPassword() {
        return mUserPassword;
    }

    public void setUserPassword(String userPassword) {
        this.mUserPassword = userPassword;
    }
}
