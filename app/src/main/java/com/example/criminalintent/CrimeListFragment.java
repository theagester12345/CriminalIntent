package com.example.criminalintent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView CrimeRecyclerView;
    private CrimeAdapter crimeAdapter;
    private static final int REQUEST_CRIME=1;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private Callbacks mCallback;



    /**
     *
     * Require interface for hosting activities
     */
    public interface Callbacks{
        void onCrimeSelected(crime Crime);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (Callbacks) context;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        CrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        CrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();


        return view;
    }

    public void updateUI() {
        CrimeLab mcrimeLab = CrimeLab.get(getActivity());
        List<crime> mCrimes = mcrimeLab.getmCrimes();

        if (crimeAdapter==null){
            Log.d("check","adapter is null");
            crimeAdapter = new CrimeAdapter(mCrimes);
            CrimeRecyclerView.setAdapter(crimeAdapter);
        }else {
            crimeAdapter.notifyDataSetChanged();
        }

        updateSubtitles();


    }



    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //public TextView titleTextView;
        private AppCompatTextView title,date;
        private CheckBox checkBox;
        private crime mCrime;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);
         //   titleTextView = (TextView) itemView;
            title = itemView.findViewById(R.id.custom_list_title);
            date = itemView.findViewById(R.id.custom_list_date);
            checkBox = itemView.findViewById(R.id.custom_list_checkbox);

            itemView.setOnClickListener(this);


        }

        public void bindCrime (crime crime){
            mCrime = crime;
            title.setText(mCrime.getMtitle());
            date.setText(mCrime.getmDate());
            checkBox.setChecked(mCrime.ismSolved());

        }

        @Override
        public void onClick(View view) {
           // Toast.makeText(getActivity(),mCrime.getMtitle()+" clicked!",Toast.LENGTH_SHORT).show();
            //Intent i = crimePagerActivity.newIntent(getActivity(),mCrime.getmId());

          //  i.putExtra("ID",)
            //startActivityForResult(i,REQUEST_CRIME);
            mCallback.onCrimeSelected(mCrime);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode== REQUEST_CRIME){
            //handle result

        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<crime> Crimes;

        public CrimeAdapter(List<crime> crimes){
            Crimes = crimes;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);

            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            crime mcrime = Crimes.get(position);
           // holder.titleTextView.setText(mcrime.getMtitle());
            holder.bindCrime(mcrime);



        }

        @Override
        public int getItemCount() {
            return Crimes.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fraggment_crime_list,menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);

        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                crime mcrime = new crime();
                CrimeLab.get(getActivity()).addCrime(mcrime);
                //Intent s = crimePagerActivity.newIntent(getActivity(),crime.getmId());
                //startActivity(s);
                updateUI();
                mCallback.onCrimeSelected(mcrime);

                return true;
            case R.id.menu_item_show_subtitle:
                //Negate the boolean
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitles();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



    }

    private void updateSubtitles(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int count = crimeLab.getmCrimes().size();
      //  @SuppressLint("StringFormatMatches") String subtitle = getString(R.string.subtitle_format,count);
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural,count,count);
        if(!mSubtitleVisible){
            subtitle=null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback=null;
    }
}
