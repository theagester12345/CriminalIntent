package com.example.criminalintent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

public class crimeActivity extends SingleFragmentActivity {
   private static final String EXTRA_CRIME_ID = "crime_id";

    public static Intent newIntent(Context packagecontext, UUID crimeID){

        Intent intent = new Intent(packagecontext,crimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeID);
        return intent;

    }
    @Override
    protected Fragment createFragment() {
       // return new  crimeFragment();
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return crimeFragment.newInstance(crimeId);
    }
}