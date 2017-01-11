package com.hoffmans.rush.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.CurrencyBean;
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.request.AppCurrencyRequest;
import com.hoffmans.rush.http.request.LoginRequest;
import com.hoffmans.rush.http.request.UserRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.Currency;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * Created by devesh on 19/12/16.
 */

public class RegisterFragment extends BaseFragment implements View.OnClickListener,FacebookCallback<LoginResult>,AdapterView.OnItemSelectedListener {

    private static final String FILE_PROVIDER="com.example.android.fileprovider";
    private EditText edtname,edtEmail,edtphone,edtPassword,edtcc;
    private Button btnRegister,btnFb,btnGoogle;
    private CircleImageView imgProfilePic;
    private Spinner spinnerCurrency;
    private static final int IMAGE_REQUEST_PERMISSION=100;
    private static final int CAMERA_PIC_REQUEST    = 101;
    private static final int GALLERY_PIC_REQUEST   = 102;
    private static final int REQUEST_GOOGLE_SIGNIN = 8;
    private CallbackManager callbackManager;
    private String  mCurrentPhotoPath;
    private Currency selectedCurrency;
    private int idGoogleApiclient;
    private RelativeLayout topView;
    private View view;
    private List<Currency> currencyList =new ArrayList<>();
    Fragment fragment;
    private Uri photoURI;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         if(view==null) {
             fragment = this;
             mActivity.initToolBar("", false);
             mActivity.hideToolbar();
             view = inflater.inflate(R.layout.fragment_register, container, false);
             FacebookSdk.sdkInitialize(mActivity.getApplicationContext());
             callbackManager = CallbackManager.Factory.create();
             initViews(view);
             initListeners();
             getAllCurrency();
             setRetainInstance(true);
         }

        return view;
    }


    @Override
    protected void initViews(View view) {

        edtname=(EditText)view.findViewById(R.id.frEdtname);
        edtEmail=(EditText)view.findViewById(R.id.frEdtEmail);
        edtphone=(EditText)view.findViewById(R.id.frEdtPhone);
        edtcc=(EditText)view.findViewById(R.id.frEdtCC);
        edtPassword=(EditText)view.findViewById(R.id.frEdtPassword);
        btnRegister=(Button)view.findViewById(R.id.frBtnCreateAccount);
        btnFb=(Button)view.findViewById(R.id.frBtnFacebook);
        btnGoogle=(Button)view.findViewById(R.id.frBtnGoogle);
        imgProfilePic=(CircleImageView)view.findViewById(R.id.frImgProfile);
        spinnerCurrency=(Spinner)view.findViewById(R.id.spinnerCurrency);
        topView =(RelativeLayout)view.findViewById(R.id.topRegistration);
    }

    @Override
    protected void initListeners() {
        btnRegister.setOnClickListener(this);
        btnFb.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);
        topView.setOnClickListener(this);
        LoginManager.getInstance().registerCallback(callbackManager, this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frBtnCreateAccount:
                validateFields();
                break;
            case R.id.frBtnFacebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
                break;
            case R.id.frBtnGoogle:
                idGoogleApiclient++;
                googleSignIn();
                break;
            case R.id.frImgProfile:
                checkPermission();
                break;
            case R.id.topRegistration:
                Utils.hideKeyboard(mActivity);
                break;
        }

    }


    /**
     * graph api caller to get user detail from Facebook account.
     */
    private void callGraphRequestFb(String accessToken) {
        final GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            if(object!=null) {
                                String first_name = object.getString(Constants.FBCONTANTS.FB_FIRST_NAME) ;
                                String last_name  = object.getString(Constants.FBCONTANTS.FB_LAST_NAME);
                                String email = "";
                                if (object.has(Constants.FBCONTANTS.FB_EMAIL)) {
                                    email = object.getString(Constants.FBCONTANTS.FB_EMAIL);
                                }
                                String socialId = object.getString(Constants.FBCONTANTS.FB_ID);
                                String imageUrl = Constants.FBCONTANTS.FB_IMG_URL+socialId+"/picture?type=large";

                                socialLogin(Constants.FB_PROVIDER,first_name,last_name,email,socialId,imageUrl,"adf-dad-fad98097",Constants.DEVICE_TYPE,Utils.getTimeZone());
                                //UpdateAccountFragment fragment=UpdateAccountFragment.newInstance("");
                                //mActivity.replaceFragment(fragment,0,false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }catch (Exception e){

                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    /**
     * sign in through google
     */
    private void googleSignIn(){
           GoogleApiClient googleApiClient=(mActivity.getGoogleApiClient()==null)?mActivity.setGoogleSignInOptions(idGoogleApiclient):mActivity.getGoogleApiClient();
            if(googleApiClient!=null) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, REQUEST_GOOGLE_SIGNIN);
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
            //takePictureIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
            //Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
               photoURI = FileProvider.getUriForFile(mActivity,
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





    private void validateFields(){
        // Store values at the time of the login attempt.
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String fullname=edtname.getText().toString().trim();
        String number=edtphone.getText().toString().trim();
        String countrycode=edtcc.getText().toString().trim();
        String phoneNo=countrycode+number;
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
        if(TextUtils.isEmpty(mCurrentPhotoPath)){
            mActivity.showSnackbar(getString(R.string.str_profile_pic), Toast.LENGTH_SHORT);
            return;
        }
        if(selectedCurrency==null){
           // mActivity.showSnackbar(getString(R.string.str_select_currency), Toast.LENGTH_SHORT);
            Snackbar.make(getView(),"Currency data not found!",Snackbar.LENGTH_LONG)
                    .setAction("Try again", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAllCurrency();
                        }
                    }).show();
            return;
        }

        buildParams(email,password,phoneNo,fullname);

    }


    private void buildParams(String email,String password,String phoneNo,String fullname){
        try {
            File fileToUpload=new File(mCurrentPhotoPath);
            Map<String,RequestBody> requestBodyMap=new HashMap<>();
            requestBodyMap.put(Constants.KEY_EMAIL, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),email));
            requestBodyMap.put(Constants.KEY_PASSWORD, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),password));
            requestBodyMap.put(Constants.KEY_PHONE, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),phoneNo));
            requestBodyMap.put(Constants.KEY_NAME, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),fullname));
            requestBodyMap.put(Constants.KEY_TIME_ZONE, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),Utils.getTimeZone()));
            //TODO add the original notification token
            requestBodyMap.put(Constants.KEY_UDID, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),"adddf -dadf -adsfasd-d8773"));
            requestBodyMap.put(Constants.KEY_TYPE, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),Constants.DEVICE_TYPE));
            requestBodyMap.put(Constants.KEY_CURRENCY, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),selectedCurrency.getId().toString()));
            //compress the original file using zetbaitsu/Compressor
            File compressedImageFile = Compressor.getDefault(mActivity).compressToFile(fileToUpload);
            // add different media type to request body for image
            RequestBody requestBody = RequestBody.create(MediaType.parse(Constants.CONTENT_IMAGE), compressedImageFile);
            MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData(Constants.KEY_PIC, fileToUpload.getName(), requestBody);
            // call api to create new account
            createAccount(requestBodyMap,imageFileBody);
        }catch (Exception e){

        }
    }

    private void createAccount(Map<String,RequestBody> requestBodyMap, MultipartBody.Part imageFileBody){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        UserRequest request=new UserRequest();
        request.createUser(requestBodyMap, imageFileBody,new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean baseBean) {
                Progress.dismissProgress();
                UserBean bean=(UserBean) baseBean;
                Utils.showAlertDialog(mActivity,bean.getMessage());
                mActivity.getSupportFragmentManager().popBackStackImmediate();
                //mActivity.finish();
              }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,Toast.LENGTH_LONG);
            }
        });
    }


    /**
     * login via google and facebook
     * @param provider facebook/google
     * @param first_name
     * @param last_name
     * @param email
     * @param uid  facebook_id/google_id
     * @param picUrl pic_url in case of fb
     */
    private void socialLogin(String provider,String first_name,String last_name,String email,String uid,String picUrl,String uuid,String type,String timezone){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.loginViaSocialNetwork(uid, first_name, last_name, email, provider, picUrl, uuid,type,timezone,new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                UserBean userBean=(UserBean)body;
                User user=userBean.getUser();
                handleUserRegistrationCases(user);
            }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,Toast.LENGTH_LONG);
            }
        });
    }




    private void handleUserRegistrationCases(User user){

        if(!user.is_email_verified()){
            UpdateAccountFragment fragment=UpdateAccountFragment.newInstance(user.getEmail(),user.getPhone(),user.getToken(),user.is_email_verified());
            mActivity.replaceFragment(fragment,0,true);
        }
       else{
            if(!user.is_card_verfied()){
                PaymentMethodFragment paymentMethodFragment=PaymentMethodFragment.newInstance(user);
                replaceFragment(paymentMethodFragment,true);
            }else{
                  appPreference.saveUser(user);
                  appPreference.setUserLogin(true);
                  Intent bookServiceIntent=new Intent(mActivity, BookServiceActivity.class);
                  bookServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(bookServiceIntent);

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



    private void getAllCurrency(){

        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        AppCurrencyRequest appCurrencyRequest=new AppCurrencyRequest();
        appCurrencyRequest.getCurrency(new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {

                Progress.dismissProgress();
                CurrencyBean currencyBean=(CurrencyBean)body;
                currencyList.clear();
                currencyList =currencyBean.getCurrencies();
                List<String> currencyListString =new ArrayList<String>();
                currencyListString.add(getString(R.string.str_select_currency));
                if(currencyList.size()!=0){
                    for(Currency currency:currencyList){
                        currencyListString.add(currency.getCurrencyName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.texview_spinner,currencyListString);
                    spinnerCurrency.setAdapter(adapter);
                    spinnerCurrency.setSelection(0,false);
                    setSpinnerListner();
                }
            }

            @Override
            public void onRequestFailed(String message)
            {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            if(mCurrentPhotoPath!=null) {

                setPic();

            }
        }else if(requestCode==GALLERY_PIC_REQUEST &&resultCode==RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            mCurrentPhotoPath= Utils.getRealPathFromURI(mActivity,uri);
            setPic();
        }else if(requestCode ==REQUEST_GOOGLE_SIGNIN && resultCode == RESULT_OK){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else if(FacebookSdk.isFacebookRequestCode(requestCode)){
            try {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     *
     * @param result Result from google + login
     */
    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String googleId=acct.getId();
            String email=acct.getEmail();
            String name =acct.getDisplayName();
            socialLogin(Constants.GOOGLE_PROVIDER,name,email,null,googleId,"","asd-dad-dad-45f4","ANDROID",Utils.getTimeZone());
        }
    }


    @Override
    public void onSuccess(LoginResult loginResult) {

        String accessToken=loginResult.getAccessToken().getToken();
        callGraphRequestFb(accessToken);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {
     mActivity.showSnackbar(error.getMessage(),0);
        if (error instanceof FacebookAuthorizationException) {
            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
        }
    }



    private  void setSpinnerListner(){
        spinnerCurrency.setOnItemSelectedListener(this);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int selectedPosition, long l) {
        Log.e("pos",selectedPosition+"");
        if(currencyList!=null && selectedPosition!=0) {
            selectedPosition=selectedPosition-1;
            selectedCurrency = currencyList.get(selectedPosition);
        }else{
            selectedCurrency=null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if ( Progress.mprogressDialog!=null && Progress.mprogressDialog.isShowing() ){
            Progress.mprogressDialog.cancel();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
}
