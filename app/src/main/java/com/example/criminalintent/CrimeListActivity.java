package com.example.criminalintent;

import android.content.Intent;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks,crimeFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getResourceID() {
        return R.layout.activity_masterDetail;
    }

    @Override
    public void onCrimeSelected(crime Crime) {
        //detect detail_fragContain is present in layout
        //indicating the device is tablet if true and phone is false
        if (findViewById(R.id.detail_fragContainer)==null){
            //Phone device
            Intent intent = crimePagerActivity.newIntent(this,Crime.getmId());
            startActivity(intent);
        }else {
            //Tablet device
            Fragment detailPane_frag = crimeFragment.newInstance(Crime.getmId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragContainer,detailPane_frag)
                    .commit();
        }

    }

    @Override
    public void onCrimeUpdate(crime Crime) {
        CrimeListFragment frag = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        frag.updateUI();
    }
}
