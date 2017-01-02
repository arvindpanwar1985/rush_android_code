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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link BaseFragment} subclass.
 * Activities that contain this fragment must implement the
  * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener{


    private static final String KEY_NAME              ="user[name]";
    private static final String KEY_PHONE             ="user[phone]";
    private static final String KEY_PIC               ="user[picture]";

    private static final String KEY_CURRENT_PASSWORD  ="user[current_password]";
    private static final String KEY_NEW_PASSWORD      ="user[password]";
    private static final String KEY_PASSWORD_CONFIRMATION="user[password_confirmation]";

    private static final String FILE_PROVIDER="com.example.android.fileprovider";
    private static final int IMAGE_REQUEST_PERMISSION=100;
    private static final int CAMERA_PIC_REQUEST    = 101;
    private static final int GALLERY_PIC_REQUEST   = 102;
    private static final String ARG_PARAM1         = "param1";
    private static final String ARG_PARAM2         = "param2";
    private EditText edtname,edtEmail,edtphone,edtoldPassword,edtNewPassword,edtConfirmNewPassword;
    private LinearLayout linearNewPass,linearConfirmNewPass,linearOldPass;
    private TextView editableName,editableNumber,editablePassword;
    private CircleImageView imgProfilePic;
    private Button btnSave;
    private String mCurrentPhotoPath;
    private boolean isEditablePassClicked,isEditableName,isEditablePhone;

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
        setProfile(appPreference.getUserDetails());
        return editProfileView;
    }


    @Override
    protected void initViews(View view) {
        edtname=(EditText)view.findViewById(R.id.fEPEdtname);
        edtEmail=(EditText)view.findViewById(R.id.fEPEdtEmail);
        edtphone=(EditText)view.findViewById(R.id.fEPEdtPhone);
        edtoldPassword=(EditText)view.findViewById(R.id.fEPEdtPassword);
        edtNewPassword=(EditText)view.findViewById(R.id.fEPEdtNewPassword);
        edtConfirmNewPassword=(EditText)view.findViewById(R.id.fEPEdtConfirmPassword);

        linearNewPass=(LinearLayout)view.findViewById(R.id.linearNewPassword);
        linearConfirmNewPass=(LinearLayout)view.findViewById(R.id.linearConfirmNewPassword);
        linearOldPass=(LinearLayout)view.findViewById(R.id.linearOldPass);
        editableName=(TextView)view.findViewById(R.id.editableName);
        editablePassword=(TextView)view.findViewById(R.id.editablePassword);
        editableNumber=(TextView)view.findViewById(R.id.editablePhone);
        btnSave=(Button)view.findViewById(R.id.fEPBtnSave);
        imgProfilePic=(CircleImageView)view.findViewById(R.id.fEPImgProfile);


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
                     linearNewPass.setVisibility(View.GONE);
                     linearConfirmNewPass.setVisibility(View.GONE);
                 }else{
                     isEditablePassClicked=true;
                     edtoldPassword.setEnabled(true);
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
        }
    }

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
            mActivity.showSnackbar("No change selected",0);
        }else{
            buildParams(fullname,password,newpassword,confirmNewpassword,phoneNo);
        }



    }



    private void buildParams(String name,String oldpass,String newpass,String confirmPass,String phone){

        MultipartBody.Part imageFileBody=null;
        try {

            Map<String,RequestBody> requestBodyMap=new HashMap<String,RequestBody>();
            if(isEditablePhone){
                requestBodyMap.put(KEY_PHONE, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),phone));
            }
            if(isEditableName){
                requestBodyMap.put(KEY_NAME, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),name));
            }
            if(isEditablePassClicked){
                requestBodyMap.put(KEY_CURRENT_PASSWORD, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),oldpass));
                requestBodyMap.put(KEY_NEW_PASSWORD, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),newpass));
                requestBodyMap.put(KEY_PASSWORD_CONFIRMATION, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),confirmPass));
            }
            if(!TextUtils.isEmpty(mCurrentPhotoPath)){
                File fileToUpload=new File(mCurrentPhotoPath);
                RequestBody requestBody = RequestBody.create(MediaType.parse(Constants.CONTENT_TYPE_MULTIPART), fileToUpload);
                imageFileBody = MultipartBody.Part.createFormData(KEY_PIC, fileToUpload.getName(), requestBody);
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


    private void updateuserProfile(Map<String,RequestBody> requestBodyMap, MultipartBody.Part imageFileBody){
        mActivity.showProgress();
        String token=appPreference.getUserDetails().getToken();
        UserRequest userRequest=new UserRequest();
        userRequest.updateUserWithImageData(token,requestBodyMap,imageFileBody, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                mActivity.hideProgress();
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
                mActivity.hideProgress();
                mActivity.showSnackbar(message,0);
            }
        });
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
