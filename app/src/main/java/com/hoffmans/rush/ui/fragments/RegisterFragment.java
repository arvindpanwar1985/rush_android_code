package com.hoffmans.rush.ui.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hoffmans.rush.R;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by devesh on 19/12/16.
 */

public class RegisterFragment extends BaseFragment implements View.OnClickListener {


    private static final String FILE_PROVIDER="com.example.android.fileprovider";

    private EditText edtname,edtEmail,edtphone,edtPassword;
    private Button btnRegister,btnFb,btnGoogle;
    private CircleImageView imgProfilePic;
    private static final int IMAGE_REQUEST_PERMISSION=100;
    private static final int CAMERA_PIC_REQUEST = 101;
    private static final int GALLERY_PIC_REQUEST = 102;
    private String  mCurrentPhotoPath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_register,container,false);
        initViews(view);
        initListeners();
        return view;
    }


    @Override
    protected void initViews(View view) {

        edtname=(EditText)view.findViewById(R.id.frEdtname);
        edtEmail=(EditText)view.findViewById(R.id.frEdtEmail);
        edtphone=(EditText)view.findViewById(R.id.frEdtPhone);
        edtPassword=(EditText)view.findViewById(R.id.frEdtPassword);
        btnRegister=(Button)view.findViewById(R.id.frBtnCreateAccount);
        btnFb=(Button)view.findViewById(R.id.frBtnFacebook);
        btnGoogle=(Button)view.findViewById(R.id.frBtnGoogle);
        imgProfilePic=(CircleImageView)view.findViewById(R.id.frImgProfile);
    }

    @Override
    protected void initListeners() {

        btnRegister.setOnClickListener(this);
        btnFb.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frBtnCreateAccount:
                validateFields();
                break;
            case R.id.frBtnFacebook:
                break;
            case R.id.frBtnGoogle:
                break;
            case R.id.frImgProfile:
                checkPermission();
                break;
        }
    }


    /**
     * check the permission
     */
    private void checkPermission(){
        String [] arrPermission=new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(mActivity.isPermissionGranted(arrPermission)){
            pickImage();

        }else {
              requestPermissions(arrPermission, IMAGE_REQUEST_PERMISSION);
        }
    }


    /**
     * Alert dialog to show Image picker
     */
    private void pickImage(){

        AlertDialog.Builder getImageFrom = new AlertDialog.Builder(mActivity);
        getImageFrom.setTitle(getString(R.string.str_profile_pic));
        final CharSequence[] opsChars = {getString(R.string.str_open_Camera), getString(R.string.str_open_Gallery)};
        getImageFrom.setItems(opsChars, new android.content.DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    dispatchTakePictureIntent();
                }else
                if(which == 1){
                    dispatchSelectPictureIntent();
                }
                dialog.dismiss();
            }
        }).show();
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    /**
     * dispatch intent to select image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
               Uri photoURI = FileProvider.getUriForFile(mActivity,
                        FILE_PROVIDER,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            }
        }
    }

    /**
     * dispatch intent to select image from Gallery
     */

    private void dispatchSelectPictureIntent(){

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType(Constants.IMAGE_TYPE);
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, getString(R.string.str_profile_pic));
        startActivityForResult(chooser, GALLERY_PIC_REQUEST);
    }


    /**
     * set the thumbnail pic to circular imageview
      */
    private void setPic() {
            // Get the dimensions of the View
            int targetW = imgProfilePic.getWidth();
            int targetH = imgProfilePic.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            imgProfilePic.setImageBitmap(bitmap);

    }
    private void validateFields(){
        // Store values at the time of the login attempt.
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String fullname=edtname.getText().toString().trim();
        String phoneNo=edtphone.getText().toString().trim();
        // Check for a valid email address.
        if(TextUtils.isEmpty(fullname)){
            mActivity.showSnackbar(getString(R.string.error_empty_name),Toast.LENGTH_SHORT);
            return;
        }

        if (TextUtils.isEmpty(email)) {
            mActivity.showSnackbar(getString(R.string.error_empty_email), Toast.LENGTH_SHORT);
            return;

        } else if (!Validation.isValidEmail(email)) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_email), Toast.LENGTH_SHORT);
            return;

        }

        if (TextUtils.isEmpty(password.trim())) {
            mActivity.showSnackbar(getString(R.string.error_empty_password), Toast.LENGTH_SHORT);
            return;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !Validation.isValidPassword(password)) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_password), Toast.LENGTH_SHORT);

            return;
        }
        if(TextUtils.isEmpty(phoneNo)){
            mActivity.showSnackbar(getString(R.string.error_empty_Mobile), Toast.LENGTH_SHORT);
            return;
        }
        if(!Validation.isValidMobile(phoneNo)){
            mActivity.showSnackbar(getString(R.string.error_title_invalid_Mobile), Toast.LENGTH_SHORT);
            return;
        }
        createAccount(fullname,email,password,phoneNo);
    }


    private void createAccount(String name,String email,String password,String phone){



    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case IMAGE_REQUEST_PERMISSION:
                if(grantResults.length>2){
                    pickImage();
                }else{
                    Toast.makeText(mActivity,"not granted",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            setPic();

        }else if(requestCode==GALLERY_PIC_REQUEST &&resultCode==RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            mCurrentPhotoPath= Utils.getRealPathFromURI(mActivity,uri);
            setPic();
        }
    }
}
