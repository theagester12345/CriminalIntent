package com.example.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.criminalintent.Database.*;
import com.example.criminalintent.Database.crimeDBSchema.crimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab crimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    //Creating list to hold crime objects
    private List<crime> mCrimes;

    public List<crime> getmCrimes() {
       // return mCrimes;
        //return new ArrayList<>();
        List<crime> Crimes = new ArrayList<>();

        //Run query for all data in sqldb
        crimeCursorWrapper cursor_wrapper = queryCrime(null,null);

        //Loop through query and add to list
        try {
            cursor_wrapper.moveToFirst();
            //While cursor is not at last row
            while(!cursor_wrapper.isAfterLast()){
                Crimes.add(cursor_wrapper.getCrime());
                cursor_wrapper.moveToNext();
            }
        }finally {
            cursor_wrapper.close();
        }

        return Crimes;


    }


    //Method creates the crimelab object
    //Lol had to explain but something like a self creating storage unit
    public static CrimeLab get(Context context){
        if (crimeLab==null){
            crimeLab = new CrimeLab(context);
        }
        return crimeLab;
    }

    private CrimeLab(Context context){
       // mCrimes = new ArrayList<>();
        mContext = context.getApplicationContext();
        mDatabase= new crimeBaseHelper(mContext).getWritableDatabase();



        //Generate 100 crimes
        //Put 100 crimes in List
        /*for (int i=0;i<100;i++){
            crime Ocrime = new crime();
            Ocrime.setMtitle("Case #"+i);
            Ocrime.setmSolved(i%2==0);
            mCrimes.add(Ocrime);
        }*/

    }

    //Getting uuid from crime object passed to list
    //searching for a crime
    public crime getCrime (UUID id ){
        //Loop through the list of crimes objects
        //Id equal to the crime id return crime object
        // searching for crime through Id input
        /*for (crime Lcrime: mCrimes){
            if (Lcrime.getmId().equals(id)){
                return Lcrime;
            }
        }*/

        //return null;
        //SEarch for crime with uuid
        crimeCursorWrapper cursorWrapper = queryCrime(crimeTable.Cols.uuid+"=?",new String[] {id.toString()});

        try {
            if (cursorWrapper.getCount()==0){
                return null;

            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();




        }finally {
            cursorWrapper.close();
        }


    }

    public void addCrime(crime c){
        //Add crime to SQL db
        ContentValues values = getContentValues(c);
        mDatabase.insert(crimeTable.Name,null,values);

        //mCrimes.add(c);
    }
    private static ContentValues getContentValues (crime crime){
        ContentValues values = new ContentValues();
        values.put(crimeTable.Cols.uuid,crime.getmId().toString());
        values.put(crimeTable.Cols.title,crime.getMtitle());
        values.put(crimeTable.Cols.date,crime.getDate_date().getTime());
        values.put(crimeTable.Cols.solved,crime.ismSolved() ? 1 : 0);
        values.put(crimeTable.Cols.suspect,crime.getSuspect());

        return values;

    }
     public void updateCrime (crime c){
        String uuid = c.getmId().toString();
        ContentValues values = getContentValues(c);
        mDatabase.update(crimeTable.Name,values,crimeTable.Cols.uuid+"=?",new String[]{uuid});

     }

     private crimeCursorWrapper queryCrime (String whereClause, String[] whereArgs){

        Cursor cursor = mDatabase.query(
                crimeTable.Name,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new crimeCursorWrapper(cursor);
    }

    public void deleteCrime (crime c){
       // mCrimes.remove(c);

        //Delete specific crime
        //Get uuid id of crime
        String uuid = c.getmId().toString();

        mDatabase.delete(crimeTable.Name,crimeTable.Cols.uuid+"=?",new String[]{uuid});
    }

    public File getPhotoFile (crime Crime){
        File externalfileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalfileDir==null){
            return null;
        }

        return new File(externalfileDir,Crime.getPhotoName());
    }
}
