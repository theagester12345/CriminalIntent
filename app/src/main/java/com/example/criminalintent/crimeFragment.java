package com.example.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import javax.security.auth.callback.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link crimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class crimeFragment extends Fragment {
    private crime mCrime;
    private AppCompatEditText mTitle;
    private AppCompatButton date_btn,delete_btn,send_report,choose_suspect,confront;
    private AppCompatCheckBox solveCheckBox;
    private static final String ARG_CRIME_ID="crime_id";
    private static final String DIALOG_DATE="DialogDate";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_CONTACT=1;
    private static final int REQUEST_CAMERA=2;
    private PackageManager packageManager;
    private AppCompatImageView mImageView;
    private AppCompatImageButton mPhotobutton;
    private File mPhotoFile;
    private crimeFragment.Callbacks mCallback;



    public static crimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        crimeFragment fragment = new crimeFragment();
        fragment.setArguments(args);
        return fragment;

    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public crimeFragment() {
        // Required empty public constructor

    }



    /**
    * Required for hosting activites
     **/
    public interface Callbacks {
        void onCrimeUpdate(crime Crime);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallback = (crimeFragment.Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

      //  UUID crimeid = (UUID) getActivity().getIntent().getSerializableExtra(crimeActivity.EXTRA_CRIME_ID);
        UUID crimeid = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        //Use search crime method in crimelab
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeid);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitle= view.findViewById(R.id.crime_title);
        mTitle.setText(mCrime.getMtitle());
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setMtitle(charSequence.toString());
                updateCrime();
            }

            @Override 
            public void afterTextChanged(Editable editable) {

            }
        });

        delete_btn = view.findViewById(R.id.fragment_crime_delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.deleteCrime(mCrime);
                getActivity().finish();
            }
        });

        date_btn = view.findViewById(R.id.crime_date);
       // date_btn.setText(mCrime.getmDate());
        updateDate();
        date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate_date());
                dialog.setTargetFragment(crimeFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });


        solveCheckBox = view.findViewById(R.id.crime_solved);
        solveCheckBox.setChecked(mCrime.ismSolved());
        solveCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //set the crime solved property
                mCrime.setmSolved(b);
                updateCrime();
            }
        });

        //Confront the suspect button
        //Dial suspect phone number
        /*confront = view.findViewById(R.id.fragment_crime_confront_btn);
        confront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if suspect exist
                if (mCrime.getSuspect()==null){
                    Toast.makeText(getActivity(),"No suspect Found",Toast.LENGTH_SHORT).show();
                }else {
                    //Get suspect number
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mCrime.getSuspect_no()));
                    startActivity(i);

                }
            }
        });*/


        mImageView = view.findViewById(R.id.crime_photo);
        updatePhotoView();

        
        send_report = view.findViewById(R.id.fragment_crime_sendReport_btn);
        send_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create implicit
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                i = Intent.createChooser(i,getString(R.string.crime_send_report_text));
                Intent y = new Intent(Intent.ACTION_SEND);

                //using shared compat intent builder
                y = ShareCompat.IntentBuilder.from(getActivity()).setType("text/plain").setSubject(getString(R.string.crime_report_subject)).setType("").setChooserTitle(R.string.crime_send_report_text).setText(getReport()).createChooserIntent();
                startActivity(y);
            }
        });


        final Intent pickContact = new Intent (Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        choose_suspect = view.findViewById(R.id.fragment_crime_chooseSus_btn);
        choose_suspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact,REQUEST_CONTACT);


            }
        });

        if (mCrime.getSuspect()!=null){
            choose_suspect.setText(mCrime.getSuspect());
        }

        //Check if the application has an application that can view contacts
            packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)==null){
            Toast.makeText(getActivity(),"You do not have a contact app installed",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(),"Contact app Found",Toast.LENGTH_SHORT).show();
        }


        //Camera button
        mPhotobutton = view.findViewById(R.id.crime_camera);
        //Get camera intent
        final Intent captureImg = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //Condition to take picture
        //1. Photfile must not be null
        //2. App must exist to take picture on device

        boolean canTakepic = mPhotoFile !=null && captureImg.resolveActivity(packageManager)!=null;
        //Disable or enable camera button depending on result
        mPhotobutton.setEnabled(canTakepic);

        if (canTakepic){
            //get uri for captured picture container or file
            //Uri uri = Uri.fromFile(mPhotoFile);
            Uri uri = FileProvider.getUriForFile(getActivity(),getActivity().getApplicationContext().getPackageName() + ".provider",mPhotoFile);
            //pass uri to inteent extra as a way to tell where to save the captured image to
            captureImg.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }

        mPhotobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(captureImg,REQUEST_CAMERA);
            }
        });




        //ImageView

        return view;
    }

    private void updateDate (){
        date_btn.setText(mCrime.getmDate());
    }

    private void updatePhotoView (){
        //check file exist and is not null
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mImageView.setImageBitmap(null);
        }else {
            //Get scaled image from pictureUtils class
            Bitmap scaledImage = pictureUtils.getScale(mPhotoFile.getPath(),getActivity());
            //Pass scaled image to image view
            mImageView.setImageBitmap(scaledImage);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode==REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            //Log.d("date",date.toString());
            updateCrime();
            updateDate();
            date_btn.setText(mCrime.getmDate());
        }else if (requestCode==REQUEST_CONTACT && data !=null){
            //Get location of selected contact
            Uri contactUri = data.getData();
            Uri main = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

            //Query fields
            //Field or colum of interest from contact db
            String[] queryField = new String[]{
                    ContactsContract.Data.DATA1

            };
            Log.d("queryField_txt",queryField.toString());

            //Query Contact DB
            //Using content Resolver
            Cursor c = getActivity().getContentResolver().query(contactUri,queryField,null,null,null);

            //Get suspect from content
            if (c.getCount()==0){
                return;
            }

            try {
                c.moveToFirst();
                String sus = c.getString(0);
                mCrime.setSuspect(sus);
                updateCrime();
                choose_suspect.setText(sus);
                //c.moveToNext();
                //String sus_no = c.getString(1);
                //Log.d("sus_no",sus_no);
                //mCrime.setSuspect_no(sus_no);
            }finally {
                c.close();
            }





        }else if (requestCode==REQUEST_CAMERA){
            updateCrime();
            updatePhotoView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private String getReport(){
        //Get suspect
        String suspect;
        if (mCrime.getSuspect()==null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else {
            suspect = getString(R.string.crime_report_suspect, mCrime.getSuspect());
        }

        //Get isSolved
        String isSolved=null;
        if (mCrime.ismSolved()){
            isSolved = getString(R.string.crime_report_solved);
        }else {
            isSolved = getString(R.string.crime_report_unsolved);
        }

        //Get date
        String dateFormat = "EEE, MMM dd";
        String date = DateFormat.format(dateFormat,mCrime.getDate_date()).toString();

        //Build report
        String report = getString(R.string.crime_report,mCrime.getMtitle(),date,isSolved,suspect);

        return report;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback=null;
    }

    private void updateCrime(){
        CrimeLab .get(getActivity()).updateCrime(mCrime);
        mCallback.onCrimeUpdate(mCrime);
    }
}