package com.example.callerthing;

public class item {
    private int mProfilePic;
    private String mDescription;
    private String mNumber;

    public item(int profilePic, String description, String number){
        mProfilePic=profilePic;
        mDescription=description;
        mNumber=number;
    }

    public int getProfilePic() {
        return  mProfilePic;
    }

    public String getDescription(){
        return mDescription;
    }

    public  String getNumber(){
        return mNumber;
    }
}
