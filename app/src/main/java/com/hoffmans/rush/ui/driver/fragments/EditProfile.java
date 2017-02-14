package com.hoffmans.rush.ui.driver.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.ui.fragments.BaseFragment;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * Created by devesh on 14/2/17.
 */

public class EditProfile extends BaseFragment implements View.OnClickListener{




    private static final String FILE_PROVIDER="com.example.android.fileprovider";
    private static final int IMAGE_REQUEST_PERMISSION=100;
    private static final int CAMERA_PIC_REQUEST    = 101;
    private static final int GALLERY_PIC_REQUEST   = 102;
    private static final String ARG_PARAM1         = "param1";
    private static final String ARG_PARAM2         = "param2";
    private EditText edtname,edtEmail,edtphone,edtoldPassword,edtNewPassword,edtConfirmNewPassword;
    private RelativeLayout linearNewPass,linearConfirmNewPass,linearOldPass,topView;
    private TextView editableName,editableNumber,editablePassword;
    private CircleImageView imgProfilePic;
    private Button btnSave;
    private String mCurrentPhotoPath;
    private boolean isEditablePassClicked,isEditableName,isEditablePhone;



    public EditProfile() {
        // Required empty public constructor
    }



    public static EditProfile newInstance() {
        EditProfile fragment = new EditProfile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity.initToolBar("Edit Profile",true,true);
        View editProfileView=inflater.inflate(R.layout.driver_edit_profile,container,false);
        initViews(editProfileView);
        initListeners();
        getProfile();

        return editProfileView;
    }


    @Override
    protected void initViews(View view) {
        edtname=(EditText)view.findViewById(R.id.fEPEdtname);
        edtEmail=(EditText)view.findViewById(R.id.fEPEdtEmail);
        edtphone=(EditText)view.findViewById(R.id.fEPEdtPhone);
        // edtcc=(EditText)view.findViewById(R.id.fEPEdtCC);
        edtoldPassword=(EditText)view.findViewById(R.id.fEPEdtPassword);
        edtNewPassword=(EditText)view.findViewById(R.id.fEPEdtNewPassword);
        edtConfirmNewPassword=(EditText)view.findViewById(R.id.fEPEdtConfirmPassword);

        linearNewPass=(RelativeLayout) view.findViewById(R.id.linearNewPassword);
        linearConfirmNewPass=(RelativeLayout)view.findViewById(R.id.linearConfirmNewPassword);
        linearOldPass=(RelativeLayout)view.findViewById(R.id.linearOldPass);
        editableName=(TextView)view.findViewById(R.id.editableName);
        editablePassword=(TextView)view.findViewById(R.id.editablePassword);
        editableNumber=(TextView)view.findViewById(R.id.editablePhone);
        btnSave=(Button)view.findViewById(R.id.fEPBtnSave);
        imgProfilePic=(CircleImageView)view.findViewById(R.id.fEPImgProfile);

        topView =(RelativeLayout)view.findViewById(R.id.topRegistration);
        if(appPreference.getUserDetails().isSocialProvider()){
            linearOldPass.setVisibility(View.GONE);
        }


    }

    @Override
    protected void initListeners() {

        imgProfilePic.setOnClickListener(this);
        editableName.setOnClickListener(this);
        editablePassword.setOnClickListener(this);
        editableNumber.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        topView.setOnClickListener(this);

    }



    private void getProfile(){

        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        UserRequest request=new UserRequest();
        request.getProfile(appPreference.getUserDetails().getToken(), new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();

                UserBean userBean=(UserBean)body;
                if(userBean.getUser()!=null) {
                    setProfile(userBean.getUser());

                }
            }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,0);
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }

            }
        });
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
            case R.id.editableName:
                edtname.setEnabled(true);
                edtname.setFocusable(true);
                isEditableName=true;
                break;
            case R.id.editablePassword:
                edtoldPassword.setFocusable(true);
                if(isEditablePassClicked){
                    editablePassword.setText("edit");
                    isEditablePassClicked=false;
                    edtoldPassword.setEnabled(false);
                    edtoldPassword.setHint(getString(R.string.str_password));
                    linearNewPass.setVisibility(View.GONE);
                    linearConfirmNewPass.setVisibility(View.GONE);
                }else{
                    isEditablePassClicked=true;
                    edtoldPassword.setEnabled(true);
                    edtoldPassword.setHint(getString(R.string.str_hint_password));
                    editablePassword.setText("cancel");
                    linearNewPass.setVisibility(View.VISIBLE);
                    linearConfirmNewPass.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.editablePhone:
                edtphone.setFocusable(true);
                isEditablePhone=true;
                edtphone.setEnabled(true);
                break;
            case R.id.topRegistration:
                Utils.hideKeyboard(mActivity);
                break;
        }
    }

    /**
     * set user profile
     * @param user
     */
    private void setProfile(User user){
        edtname.setText(user.getName());
        edtphone.setText(user.getPhone());
        edtEmail.setText(user.getEmail());
        edtEmail.setEnabled(false);
        edtphone.setEnabled(false);
        edtname.setEnabled(false);
        edtoldPassword.setEnabled(false);

        if(!TextUtils.isEmpty(user.getPic_url()))
            Glide.with(mActivity).load(user.getPic_url()).into(imgProfilePic);

        appPreference.updateUserProfile(user);


    }




    private void validateFields() {
        // Store values at the time of the login attempt.

        String password = edtoldPassword.getText().toString().trim();
        String newpassword = edtNewPassword.getText().toString().trim();
        String confirmNewpassword = edtConfirmNewPassword.getText().toString().trim();
        String fullname = edtname.getText().toString().trim();
        String phoneNo = edtphone.getText().toString().trim();

        // Check for a valid email address.
        if (TextUtils.isEmpty(fullname) && isEditableName) {
            mActivity.showSnackbar(getString(R.string.error_empty_name), Toast.LENGTH_SHORT);
            return;
        }

        if (TextUtils.isEmpty(password.trim()) && isEditablePassClicked) {
            mActivity.showSnackbar(getString(R.string.error_empty_password), Toast.LENGTH_SHORT);
            return;
        }
        // Check for a valid password, if the user entered one.
        if (isEditablePassClicked && !Validation.isValidPassword(password)) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_password), Toast.LENGTH_SHORT);

            return;
        }
        if (isEditablePassClicked && !Validation.isValidPassword(newpassword)) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_password), Toast.LENGTH_SHORT);

            return;
        }
        if(isEditablePassClicked && !newpassword.equals(confirmNewpassword)){
            mActivity.showSnackbar(getString(R.string.error_pass_not_matched), Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(phoneNo) &&isEditablePhone) {
            mActivity.showSnackbar(getString(R.string.error_empty_Mobile), Toast.LENGTH_SHORT);
            return;
        }
        if (!Validation.isValidMobile(phoneNo) && isEditablePhone) {
            mActivity.showSnackbar(getString(R.string.error_title_invalid_Mobile), Toast.LENGTH_SHORT);
            return;
        }

        if(!isEditablePassClicked&&!isEditableName&&!isEditablePhone &&TextUtils.isEmpty(mCurrentPhotoPath)){
            mActivity.showSnackbar(getString(R.string.str_no_changes),0);
        }else{
            buildParams(fullname,password,newpassword,confirmNewpassword,phoneNo);
        }

    }


    /**
     * build the update profile parameters
     * @param name name of user
     * @param oldpass old password of user
     * @param newpass new password of user
     * @param confirmPass new password of user
     * @param phone phone number of user
     */
    private void buildParams(String name,String oldpass,String newpass,String confirmPass,String phone){

        MultipartBody.Part imageFileBody=null;
        try {

            Map<String,RequestBody> requestBodyMap=new HashMap<String,RequestBody>();
            if(isEditablePhone){
                requestBodyMap.put(Constants.KEY_PHONE, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),phone));
            }
            if(isEditableName){
                requestBodyMap.put(Constants.KEY_NAME, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),name));
            }
            if(isEditablePassClicked){
                requestBodyMap.put(Constants.KEY_CURRENT_PASSWORD, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),oldpass));
                requestBodyMap.put(Constants.KEY_NEW_PASSWORD, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),newpass));
                requestBodyMap.put(Constants.KEY_PASSWORD_CONFIRMATION, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),confirmPass));
            }
            //requestBodyMap.put(Constants.KEY_CURRENCY, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),selectedCurrency.getId().toString()));
            if(!TextUtils.isEmpty(mCurrentPhotoPath)){
                File fileToUpload=new File(mCurrentPhotoPath);
                // compress the original file
                File compressedImageFile = Compressor.getDefault(mActivity).compressToFile(fileToUpload);
                // creating request body for image upload
                RequestBody requestBody = RequestBody.create(MediaType.parse(Constants.CONTENT_IMAGE), compressedImageFile);
                imageFileBody = MultipartBody.Part.createFormData(Constants.KEY_PIC, fileToUpload.getName(), requestBody);
            }
            updateuserProfile(requestBodyMap,imageFileBody);
        }catch (Exception e){

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


    /**
     *
     * @return file created using camera request
     * @throws IOException
     */
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
        Bitmap rotatedBitmap=checkOrientation(bitmap);
        if(rotatedBitmap!=null){
            imgProfilePic.setImageBitmap(rotatedBitmap);
        }

    }

    private Bitmap checkOrientation(Bitmap bitmap){

        try {
            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap bitmap1=null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap1=Utils.rotateImage(bitmap, 90);
                    //imgProfilePic.setImageBitmap(bitmap1);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap1=Utils.rotateImage(bitmap, 180);
                    //imgProfilePic.setImageBitmap(bitmap1);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap1=Utils.rotateImage(bitmap, 270);

                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                    bitmap1=bitmap;
                    break;

                case 0:
                    bitmap1=bitmap;

                    break;
            }
            return  bitmap1;

        }catch (IOException e){

        }
        return  null;
    }

    /**
     *
     * @param requestBodyMap map containing image data and text data
     * @param imageFileBody image file multipart
     */
    private void updateuserProfile(Map<String,RequestBody> requestBodyMap, MultipartBody.Part imageFileBody){
        Progress.showprogress(mActivity,"updating profile..",false);
        String token=appPreference.getUserDetails().getToken();
        UserRequest userRequest=new UserRequest();
        userRequest.updateUserWithImageData(token,requestBodyMap,imageFileBody, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                mActivity.showSnackbar(body.getMessage(),0);
                UserBean userBean=(UserBean)body;
                setProfile(userBean.getUser());
                appPreference.updateUserProfile(userBean.getUser());
                isEditablePhone=false;
                isEditableName=false;
                isEditablePassClicked=false;
            }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,0);
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            if(mCurrentPhotoPath!=null) {
                setPic();
            }

        }else if(requestCode==GALLERY_PIC_REQUEST &&resultCode==RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            mCurrentPhotoPath= Utils.getRealPathFromURI(mActivity,uri);
            if(mCurrentPhotoPath!=null) {
                setPic();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case IMAGE_REQUEST_PERMISSION:
                if(mActivity.isPermissionGranted(grantResults)){
                    pickImage();
                }else{
                    Toast.makeText(mActivity,"Permission denied.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}

