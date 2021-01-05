package com.example.criminalintent.Database;

import android.database.Cursor;
import com.example.criminalintent.*;
import com.example.criminalintent.Database.crimeDBSchema.crimeTable;

import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class crimeCursorWrapper extends CursorWrapper {
    public crimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public crime getCrime (){
        String uuid = getString(getColumnIndex(crimeTable.Cols.uuid));
        String title = getString(getColumnIndex(crimeTable.Cols.title));
        long date = getLong(getColumnIndex(crimeTable.Cols.date));
        int isSolved = getInt(getColumnIndex(crimeTable.Cols.solved));
        String suspect = getString(getColumnIndex(crimeTable.Cols.suspect));

        crime Crime = new crime (UUID.fromString(uuid));
        Crime.setMtitle(title);
        Crime.setmDate(new Date(date));
        Crime.setmSolved(isSolved !=0);
        Crime.setSuspect(suspect);

        return Crime;

    }
}
