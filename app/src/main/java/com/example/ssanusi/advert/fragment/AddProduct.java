package com.example.ssanusi.advert.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ssanusi.advert.R;
import com.example.ssanusi.advert.interfaces.Listener;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class AddProduct extends Fragment {
    @BindView(R.id.im1)
    ImageView im1;
    @BindView(R.id.im2AP)
    ImageView im2;
    @BindView(R.id.companyNameSpinner)
    Spinner companyNameSpinner;
    @BindView(R.id.productNameAP)
    EditText productNameET;
    @BindView(R.id.categoryAP)
    EditText categoryET;
    @BindView(R.id.emailAddresAP)
    EditText emailAddressET;
    @BindView(R.id.contactAddresAP)
    EditText contactAddressET;
    @BindView(R.id.phoneNumberAP)
    EditText phoneNumberET;
    @BindView(R.id.addDescriptionAP)
    EditText descriptionET;

    private String companyName, productName, category, email, contactAddress, phoneNumber, description;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSIONS = 1234;
    private static final int REQUEST_CONTACT_STORAGE_PERMISSIONS = 4567;
    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private int choosenAction;
    private AlertDialog dialog;
    private int id;
    private File photoFile = null;
    private int maxNumber = 2;
    private HashMap<Integer, String> pictures = new HashMap<>(maxNumber);
    private String mParam1;
    private String mParam2;
    private Listener mListener;
    private Unbinder unbinder;

    @OnClick(R.id.addBtnAP)
    public void addFirstImage() {
        id = 0;
        putImage(im1);
    }

    @OnClick(R.id.add2BtnAP)
    public void addSecondImage() {
        id = 1;
        putImage(im2);
    }

    public void putImage(ImageView img) {
        final ImageView imageView = img;
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
                Log.i("TAG", "result: " + result);
                if (result) photoCameraIntent();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoFile = null;
                choosenAction = 1;
                boolean result = checkStoragePermission();
                Log.i("TAG", "result: " + result);
                if (result) photoGalleryIntent();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoFile != null) {
                    Glide.with(getActivity()).load(photoFile)
                            .apply(new RequestOptions().error(R.drawable.ic_person_black_24dp).placeholder(R.drawable.ic_person_black_24dp).fitCenter())
                            .into(imageView);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
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
        }
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
    }
    String[] path = new String[2];
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

        switch (id) {
            case 0:
                String path = photoFile.getAbsolutePath();
                pictures.put(0, path);
                Log.i("TAG", "This is the first path " + path);
                break;

            case 1:
                String path2 = photoFile.getAbsolutePath();
                pictures.put(1, path2);
                Log.i("TAG", "This is the second path " + path2);
                break;
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

            switch (id) {
                case 0:
                    String path = photoFile.getAbsolutePath();
                    pictures.put(0, path);
                    Log.i("TAG", "This is the gallery first path " + path);
                    break;

                case 1:
                    String path2 = photoFile.getAbsolutePath();
                    pictures.put(1, path2);
                    Log.i("TAG", "This is the gallery second path " + path2);
                    break;
            }
        }

        Glide.with(this).load(photoFile).into((ImageView) dialog.findViewById(R.id.profile_image));
        ((ImageButton) dialog.findViewById(R.id.acceptImage)).setVisibility(View.VISIBLE);
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
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission Required");
            alertBuilder.setMessage("Permision to Read/Write to External storage is required");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSIONS);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSIONS);
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
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission Required");
            alertBuilder.setMessage("Permision to Read/Write to Contact storage is required");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_CONTACT_STORAGE_PERMISSIONS);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, REQUEST_CONTACT_STORAGE_PERMISSIONS);
        }
        return false;
    }

    public AddProduct() {
    }

    public static AddProduct newInstance(String param1, String param2) {
        AddProduct fragment = new AddProduct();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public boolean validated() {
        assignment();
        int remaining = maxNumber - pictures.size();
        if (pictures.size() < maxNumber) {
            Toast.makeText(getActivity(), remaining + "images empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(productName)) {
            productNameET.setError("The field is essential");
            productNameET.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(category)) {
            categoryET.setError("The field is essential");
            categoryET.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddressET.setError("Invalid email");
            emailAddressET.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(contactAddress)) {
            contactAddressET.setError("Invalid email");
            contactAddressET.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(phoneNumber) || !Patterns.PHONE.matcher(email).matches() ||
                              !isNumberValid("234", phoneNumber)) {
            phoneNumberET.setError("Invalid phone number");
            phoneNumberET.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            descriptionET.setError("Invalid email");
            descriptionET.requestFocus();
            return false;
        }
        return true;
    }

    @OnClick(R.id.uploadBtnAP)
    public void publish() {
        if (!validated()) return;
    }

    public void assignment() {
        contactAddress = contactAddressET.getText().toString().trim();
        productName = productNameET.getText().toString().trim();
        companyName = String.valueOf(companyNameSpinner.getSelectedItem());
        email = emailAddressET.getText().toString().trim();
        phoneNumber = phoneNumberET.getText().toString().trim();
        category = categoryET.getText().toString().trim();
        description = descriptionET.getText().toString().trim();
    }

    public boolean isNumberValid(String countryCode, String phone) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
        boolean isValid = false;
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phone, isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return isValid;
    }

    @OnItemSelected(R.id.companyNameSpinner)
    public void makeTextVisible() {
        assignment();
        Log.i("TAG", "This is value selected = " + companyName);
        if (!TextUtils.isEmpty(companyNameSpinner.getSelectedItem().toString())) {
            categoryET.setVisibility(View.VISIBLE);
        }
    }

    List<String> listOfCompanies = new ArrayList<>();
    public void setCOmpanies() {
        listOfCompanies.add("");
        listOfCompanies.add("Atele");
        listOfCompanies.add("google");
        listOfCompanies.add("Microsoft");
        ArrayAdapter<String> companiesAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, listOfCompanies);
        companyNameSpinner.setAdapter(companiesAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCOmpanies();
    }
}