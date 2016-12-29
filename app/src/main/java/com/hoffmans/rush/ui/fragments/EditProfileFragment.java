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
 * A simple {@link BaseFragment} subclass.
 * Activities that contain this fragment must implement the
  * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener{


    private static final String FILE_PROVIDER="com.example.android.fileprovider";
    private static final int IMAGE_REQUEST_PERMISSION=100;
    private static final int CAMERA_PIC_REQUEST    = 101;
    private static final int GALLERY_PIC_REQUEST   = 102;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText edtname,edtEmail,edtphone,edtPassword,edtConfirmPassword;
    private CircleImageView imgProfilePic;
    private Button btnSave;
    private String mCurrentPhotoPath;

    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        // Inflate the layout for this fragment
        mActivity.initToolBar("Edit Profile",true);
        View editProfileView=inflater.inflate(R.layout.fragment_edit_profile,container,false);
        initViews(editProfileView);
        initListeners();
        return editProfileView;
    }


    @Override
    protected void initViews(View view) {
        edtname=(EditText)view.findViewById(R.id.fEPEdtname);
        edtEmail=(EditText)view.findViewById(R.id.fEPEdtEmail);
        edtphone=(EditText)view.findViewById(R.id.fEPEdtPhone);
        edtPassword=(EditText)view.findViewById(R.id.fEPEdtPassword);
        edtConfirmPassword=(EditText)view.findViewById(R.id.fEPEdtConfirmPassword);
        btnSave=(Button)view.findViewById(R.id.fEPBtnSave);
        imgProfilePic=(CircleImageView)view.findViewById(R.id.fEPImgProfile);
    }

    @Override
    protected void initListeners() {

        imgProfilePic.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.fEPBtnSave:
                validateFields();
                break;

            case R.id.fEPImgProfile:
                checkPermission();
                break;
        }
    }




    private void validateFields() {
        // Store values at the time of the login attempt.
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmpassword = edtConfirmPassword.getText().toString().trim();
        String fullname = edtname.getText().toString().trim();
        String phoneNo = edtphone.getText().toString().trim();
        // Check for a valid email address.
        if (TextUtils.isEmpty(fullname)) {
            mActivity.showSnackbar(getString(R.string.error_empty_name), Toast.LENGTH_SHORT);
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
        if(!password.equals(confirmpassword)){
            mActivity.showSnackbar(getString(R.string.error_pass_not_matched), Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(phoneNo)) {
            mActivity.showSnackbar(getString(R.string.error_empty_Mobile), Toast.LENGTH_SHORT);
            return;
        }
        if (!Validation.isValidMobile(phoneNo)) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_Mobile), Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(mCurrentPhotoPath)) {
            mActivity.showSnackbar(getString(R.string.str_profile_pic), Toast.LENGTH_SHORT);
            return;
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
