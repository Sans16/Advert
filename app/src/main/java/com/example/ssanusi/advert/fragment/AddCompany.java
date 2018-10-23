package com.example.ssanusi.advert.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ssanusi.advert.MainActivity;
import com.example.ssanusi.advert.R;
import com.example.ssanusi.advert.interfaces.Listener;
import com.example.ssanusi.advert.model.Contact;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AddCompany extends Fragment {

    @BindView(R.id.companyLogoAC)ImageView companyLogo;
    @BindView(R.id.companyNameAC)EditText companyName;
    @BindView(R.id.rcAC)EditText rcAC;
    @BindView(R.id.categoryAC)EditText category;
    @BindView(R.id.emailAddressAC)EditText emailAddress;
    @BindView(R.id.caAC)EditText companyAddress;
    @BindView(R.id.pnAC)EditText phoneNumber;
    @BindView(R.id.addNumAC)TextView addNumber;
    @BindView(R.id.addDescriptionAC)EditText addDescription;
    @BindView(R.id.uploadBtnAC)Button uploadBtn;
    private Unbinder unbinder;
    Intent intent ;
    private int id;
    private Contact Contact;
    private String con,con1;

    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSIONS = 1234;
    private static final int REQUEST_CONTACT_STORAGE_PERMISSIONS = 4567;
    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private int choosenAction;
    private AlertDialog dialog;
    private File photoFile = null;
    private boolean pictureChanged = false;
    private String imageUrl;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Listener mListener;

    public AddCompany() {
    }

    // This is the method to call the intent to use camera
    private void photoCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IMAGE_CAMERA_REQUEST);
    }

    // This is the method to call the intent to use camera
    private void photoGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), IMAGE_GALLERY_REQUEST);
    }

    // This is what happens after the intent has been called
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (choosenAction == 0) photoCameraIntent();
                    else photoGalleryIntent();
                } else {
                    dialog.dismiss();
                }
                break;
            case REQUEST_CONTACT_STORAGE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contPick();
                } else {
                    dialog.dismiss();
                }
                break;
        }
    }

    // This is the method that returns the picture Obtained
    private void onCaptureImageResult(Intent data) {
        // uri = data.getData();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        photoFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            photoFile.createNewFile();
            fo = new FileOutputStream(photoFile);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

      Glide.with(this).load(photoFile).into((ImageView) dialog.findViewById(R.id.profile_image));
        ((ImageButton) dialog.findViewById(R.id.acceptImage)).setVisibility(View.VISIBLE);
    }

    // This is the method that returns the picture Obtained from the gallery
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
               Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
               thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                photoFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                photoFile.createNewFile();
                fo = new FileOutputStream(photoFile);
                fo.write(bytes.toByteArray());
                fo.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

      Glide.with(this).load(photoFile).into((ImageView) dialog.findViewById(R.id.profile_image));
       ((ImageButton) dialog.findViewById(R.id.acceptImage)).setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.pick_image_AC)
    public void changePic(){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.popup_picture_picked, null);
        ImageButton camera = mView.findViewById(R.id.pickCameraImage);
        ImageButton gallery = mView.findViewById(R.id.pickGalleryImage);
        ImageButton accept = mView.findViewById(R.id.acceptImage);
        mBuilder.setView(mView);

        dialog = mBuilder.create();
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoFile = null;
                choosenAction = 0;
                boolean result = checkStoragePermission();
                Log.i("TAG","result: "+result);
                if (result) photoCameraIntent();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoFile = null;
                choosenAction = 1;
                boolean result = checkStoragePermission();
                Log.i("TAG","result: "+result);
                if (result) photoGalleryIntent();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (photoFile != null) {
                    Glide.with(getActivity()).load(photoFile)
                            .apply(new RequestOptions().error(R.drawable.ic_person_black_24dp).placeholder(R.drawable.ic_person_black_24dp).fitCenter())
                            .into(companyLogo);
                    pictureChanged = true;
                }
                dialog.dismiss();
            }
        });

        //dialog.setCancelable(false);
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                if (data != null)
                    onSelectFromGalleryResult(data);
            } else if (requestCode == IMAGE_CAMERA_REQUEST) {
                if (data != null)
                    onCaptureImageResult(data);
            }
        }

        switch (requestCode) {
            case (7):
                if (resultCode == Activity.RESULT_OK) {

                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNameHolder, TempNumberHolder, TempContactID, IDresult = "";
                    int IDresultHolder;

                    uri = data.getData();

                    cursor1 = getContext(). getContentResolver().query(uri, null, null, null, null);


                    if (cursor1.moveToFirst()) {

                        TempNameHolder = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        IDresultHolder = Integer.valueOf(IDresult);

                        if (IDresultHolder == 1) {

                            cursor2 = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                                //DbHandler DbHandler = new DbHandler(this);


                                Contact = new Contact(TempNameHolder, TempNumberHolder);

                                con = Contact.getPhoneNumber();

                                //DbHandler.addContact(Contact);
                                //Toast.makeText(this, "Contact Added", Toast.LENGTH_LONG).show();

                                if (id == 1) {
                                    con1 = con;
                                    phoneNumber.setText(con1);
                                }
                            }
                        }
                    }
                    break;
                }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale( Manifest.permission.READ_EXTERNAL_STORAGE)) {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission Required");
            alertBuilder.setMessage("Permision to Read/Write to External storage is required");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                   requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSIONS);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSIONS);
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkContactPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (shouldShowRequestPermissionRationale( Manifest.permission.READ_EXTERNAL_STORAGE)) {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission Required");
            alertBuilder.setMessage("Permision to Read/Write to Contact storage is required");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions( new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_CONTACT_STORAGE_PERMISSIONS);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            requestPermissions( new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_CONTACT_STORAGE_PERMISSIONS);
        }
        return false;
    }

    public static AddCompany newInstance(String param1, String param2) {
        AddCompany fragment = new AddCompany();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.addNumAC)
    public void addNumber (){
        boolean result = checkContactPermission();
        Log.i("TAG","result: "+result);
        if (result) contPick();
        id = 1;
    }

    public void contPick(){
        intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 7);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_company, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
