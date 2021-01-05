package com.example.criminalintent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class crime {
    private String mtitle;
    private UUID mId;
    private Date mDate;
    private boolean mSolved;
    private String suspect;
    private String suspect_no;

    public String getSuspect_no() {
        return suspect_no;
    }

    public void setSuspect_no(String suspect_no) {
        this.suspect_no = suspect_no;
    }



    public String getmDate() {
        //Format date
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate = format.format(mDate);
        return formattedDate;
    }
    public Date getDate_date(){
        return mDate;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean ismSolved() {
        return mSolved;
    }

    public void setmSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    public String getMtitle() {
        return mtitle;
    }

    public void setMtitle(String mtitle) {
        this.mtitle = mtitle;
    }


    public UUID getmId() {
        return mId;
    }



    public crime(){
        //generate unique identifier
        this(UUID.randomUUID());
        //mId = UUID.randomUUID();
        //mDate=new Date();
    }
    public crime(UUID id){
        mId = id;
        mDate = new Date();
    }

    public String getPhotoName(){
        return "IMG_"+getmId().toString()+".jpg";
    }
}
