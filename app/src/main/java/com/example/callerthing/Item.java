package com.example.callerthing;

public class Item {
    private int mProfilePic;
    private String mDescription;
    private String mNumber;

    public Item(int profilePic, String description, String number) {
        mProfilePic = profilePic;
        mDescription = description;
        mNumber = number;
    }

    public int getProfilePic() {
        return mProfilePic;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getNumber() {
        return mNumber;
    }
}