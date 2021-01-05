package com.example.criminalintent.Database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.criminalintent.Database.crimeDBSchema.crimeTable;

public class crimeBaseHelper extends SQLiteOpenHelper {
    public static final int version = 1;
    public static final String Db_name = "crime.db";


    public crimeBaseHelper(@Nullable Context context) {
        super(context, Db_name, null, version);
    }





    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ crimeTable.Name+"(_id integer primary key autoincrement,"+crimeTable.Cols.uuid+","+crimeTable.Cols.title+","+crimeTable.Cols.date+","+crimeTable.Cols.solved+","+crimeTable.Cols.suspect+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
