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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.firebase.iid.FirebaseInstanceId;
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
import com.hoffmans.rush.ui.activities.LoginActivity;
import com.hoffmans.rush.ui.activities.TermsPolicyActivity;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

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
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.hoffmans.rush.ui.activities.LoginActivity.CAMERA_PIC_REQUEST;
import static com.hoffmans.rush.ui.activities.LoginActivity.GALLERY_PIC_REQUEST;
import static com.hoffmans.rush.ui.activities.LoginActivity.IMAGE_REQUEST_PERMISSION;
import static com.hoffmans.rush.ui.activities.LoginActivity.REQUEST_GOOGLE_SIGNIN;

/**
 * Created by devesh on 19/12/16.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener,FacebookCallback<LoginResult>,AdapterView.OnItemSelectedListener {
    public static final String KEY_TERMS="terms";
    public static final String KEY_URL="url";
    private static final String FILE_PROVIDER="com.example.android.fileprovider";
    private EditText edtname,edtEmail,edtphone,edtPassword,edtConfirmPassword,edtcc;
    private Button btnRegister,btnFb,btnGoogle;
    private CircleImageView imgProfilePic;
    private Spinner spinnerCurrency;
    private ImageView imgFlag;
    private TextView txtCountryCode,txtTerms;
    private CallbackManager callbackManager;
    private String  mCurrentPhotoPath;
    private Currency selectedCurrency;
    private String notificationToken;
    private RelativeLayout topView;
    private View view,viewSelectCountryPicker;
    private List<Currency> currencyList =new ArrayList<>();
    private Fragment fragment;
    private Uri photoURI;
    private CountryPicker countryPicker;
    private CheckBox chekboxtermsCondition;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         if(view==null) {
             fragment = this;
             mActivity.initToolBar("", true,false);
             view = inflater.inflate(R.layout.fragment_register, container, false);
             FacebookSdk.sdkInitialize(mActivity.getApplicationContext());
             callbackManager = CallbackManager.Factory.create();
             initViews(view);
             initListeners();
             getAllCurrency();
             setRetainInstance(true);
             notificationToken =appPreference.getNoticficationToken();
             if(TextUtils.isEmpty(notificationToken)){
                 notificationToken= FirebaseInstanceId.getInstance().getToken();
                 appPreference.setNotificationToken(notificationToken);
             }
         }
        return view;
    }


    @Override
    protected void initViews(View view) {
        edtname=(EditText)view.findViewById(R.id.frEdtname);
        edtEmail=(EditText)view.findViewById(R.id.frEdtEmail);
        edtphone=(EditText)view.findViewById(R.id.frEdtPhone);
       // edtcc=(EditText)view.findViewById(R.id.frEdtCC);
        edtPassword=(EditText)view.findViewById(R.id.frEdtPassword);
        edtConfirmPassword=(EditText)view.findViewById(R.id.frEdtConfirmPassword);
        btnRegister=(Button)view.findViewById(R.id.frBtnCreateAccount);
        btnFb=(Button)view.findViewById(R.id.frBtnFacebook);
        btnGoogle=(Button)view.findViewById(R.id.frBtnGoogle);
        imgProfilePic=(CircleImageView)view.findViewById(R.id.frImgProfile);
        spinnerCurrency=(Spinner)view.findViewById(R.id.spinnerCurrency);
        topView =(RelativeLayout)view.findViewById(R.id.topRegistration);
        viewSelectCountryPicker=(View)view.findViewById(R.id.viewCountryCode);
        imgFlag=(ImageView)view.findViewById(R.id.imgFlag);
        txtCountryCode=(TextView)view.findViewById(R.id.txtCountryCode);
        chekboxtermsCondition=(CheckBox)view.findViewById(R.id.checkboxTermsCondition);
        txtTerms=(TextView)view.findViewById(R.id.txtTermsAndCondition);

        setSpanable(txtTerms);



    }

    @Override
    protected void initListeners() {
        btnRegister.setOnClickListener(this);
        btnFb.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        imgProfilePic.setOnClickListener(this);
        topView.setOnClickListener(this);
        viewSelectCountryPicker.setOnClickListener(this);
        LoginManager.getInstance().registerCallback(callbackManager, this);


    }



    private void setSpanable(TextView tv){
        Spannable word = new SpannableString(getString(R.string.str_terms_part_one));
        word.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity,android.R.color.white)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(word);
        tv.setMovementMethod(LinkMovementMethod.getInstance());

        Spannable wordTwo = new SpannableString(getString(R.string.str_terms_part_two));
        wordTwo.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity,R.color.com_facebook_blue)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordTwo.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                Intent termsIntent = new Intent(mActivity, TermsPolicyActivity.class);
                termsIntent.putExtra(KEY_TERMS,true);
                termsIntent.putExtra(KEY_URL,getLocaleBasedUrl());
                startActivity(termsIntent);
            }
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setUnderlineText(false); // set to false to remove underline
            }
        }, 0, wordTwo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(wordTwo);

        Spannable wordThree = new SpannableString(getString(R.string.str_terms_part_three));
        wordThree.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity,android.R.color.white)), 0, wordThree.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(wordThree);

        Spannable wordFour = new SpannableString(getString(R.string.str_terms_part_four));
        wordFour.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mActivity,R.color.com_facebook_blue)), 0, wordFour.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordFour.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View v) {
                Intent termsIntent = new Intent(mActivity, TermsPolicyActivity.class);
                termsIntent.putExtra(KEY_TERMS,false);
                termsIntent.putExtra(KEY_URL,getLocaleBasedUrl());
                startActivity(termsIntent);
            }
            public void updateDrawState(TextPaint ds) {// override updateDrawState
                ds.setUnderlineText(false); // set to false to remove underline
            }
        }, 0, wordFour.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(wordFour);
    }

    /**
     *
     * @return terms and policy url according to locale
     */
    private String getLocaleBasedUrl(){
        String locale= Locale.getDefault().getLanguage();
        if(locale.equals("es")){
            return "http://192.168.1.210:3000/privacy_policy?locale=es";
        }
        return "http://192.168.1.210:3000/privacy_policy?locale=en";
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.frBtnCreateAccount:
                btnRegister.startAnimation(new AlphaAnimation(1.0f, 0.0f));
                validateFields();
                break;
            case R.id.frBtnFacebook:
                btnFb.startAnimation(new AlphaAnimation(1.0f, 0.0f));
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
                break;
            case R.id.frBtnGoogle:
                btnGoogle.startAnimation(new AlphaAnimation(1.0f, 0.0f));
                mActivity.idGoogleApiclient++;
                googleSignIn();
                break;
            case R.id.frImgProfile:
                imgProfilePic.startAnimation(new AlphaAnimation(1.0f, 0.0f));
                checkPermission();
                break;
            case R.id.topRegistration:
                Utils.hideKeyboard(mActivity);
                break;
            case R.id.viewCountryCode:
                countryPicker = CountryPicker.newInstance("Select Country");
                countryPicker.show(mActivity.getSupportFragmentManager(), "COUNTRY_PICKER");

                countryPicker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        if(flagDrawableResID!=0){imgFlag.setImageResource(flagDrawableResID);}
                        if(dialCode!=null) {
                            txtCountryCode.setText(dialCode);
                        }
                        countryPicker.dismiss();
                        countryPicker=null;
                    }
                });
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
                                socialLogin(socialId,first_name,last_name,email,Constants.FB_PROVIDER,imageUrl,notificationToken,Constants.DEVICE_TYPE,Utils.getTimeZone());
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
           GoogleApiClient googleApiClient=(mActivity.getGoogleApiClient()==null)?mActivity.setGoogleSignInOptions(mActivity.idGoogleApiclient):mActivity.getGoogleApiClient();
            if(googleApiClient!=null) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(signInIntent, LoginActivity.REQUEST_GOOGLE_SIGNIN);
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
                startActivityForResult(takePictureIntent, LoginActivity.CAMERA_PIC_REQUEST);
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
            bmOptions.inJustDecodeBounds =  false;
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
        String confirmpassword = edtConfirmPassword.getText().toString().trim();
        String fullname=edtname.getText().toString().trim();
        String number=edtphone.getText().toString().trim();
        String countrycode=txtCountryCode.getText().toString();//"";//edtcc.getText().toString().trim();
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
        if (!password.equals(confirmpassword)) {
            mActivity.showSnackbar(getString(R.string.error_pass_not_matched), Toast.LENGTH_SHORT);
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

    /**
     * build register params
     * @param email
     * @param password
     * @param phoneNo
     * @param fullname
     */
    private void buildParams(String email,String password,String phoneNo,String fullname){
        try {
            MultipartBody.Part imageFileBody=null;

            if(!TextUtils.isEmpty(mCurrentPhotoPath)){
                File fileToUpload=new File(mCurrentPhotoPath);
                //compress the original file using zetbaitsu/Compressor
                File compressedImageFile = Compressor.getDefault(mActivity).compressToFile(fileToUpload);
                // add different media type to request body for image
                RequestBody requestBody = RequestBody.create(MediaType.parse(Constants.CONTENT_IMAGE), compressedImageFile);
                imageFileBody = MultipartBody.Part.createFormData(Constants.KEY_PIC, fileToUpload.getName(), requestBody);
                // call api to create new account
            }
            Map<String,RequestBody> requestBodyMap=new HashMap<>();
            requestBodyMap.put(Constants.KEY_EMAIL, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),email));
            requestBodyMap.put(Constants.KEY_PASSWORD, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),password));
            requestBodyMap.put(Constants.KEY_PHONE, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),phoneNo));
            requestBodyMap.put(Constants.KEY_NAME, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),fullname));
            requestBodyMap.put(Constants.KEY_TIME_ZONE, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),Utils.getTimeZone()));
            requestBodyMap.put(Constants.KEY_UDID, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),notificationToken));
            requestBodyMap.put(Constants.KEY_TYPE, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),Constants.DEVICE_TYPE));
            requestBodyMap.put(Constants.KEY_CURRENCY, RequestBody.create(MediaType.parse(Constants.TEXT_PLAIN_TYPE),selectedCurrency.getId().toString()));
            createAccount(requestBodyMap,imageFileBody);
        }catch (Exception e){

        }
    }

    /**
     * api call for registering new user.
     * @param requestBodyMap
     * @param imageFileBody
     */
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
     * @param socialId  facebook_id/google_id
     * @param picUrl pic_url in case of fb
     */
    private void socialLogin(String socialId, String first_name, String last_name,String email, String provider, String picUrl, String uuid,String type,String timezone){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.loginViaSocialNetwork(socialId, first_name, last_name, email, provider, picUrl, uuid,type,timezone,new ApiCallback() {
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
        if(user!=null) {
            if(user.getStatus()!=null && user.getStatus().equals(LoginActivity.STATUS_ACTIVE)){
                appPreference.saveUser(user);
                appPreference.setUserLogin(true);
                Intent bookServiceIntent = new Intent(mActivity, BookServiceActivity.class);
                bookServiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(bookServiceIntent);
            }else if(user.getPhone()==null || !user.is_email_verified()) {
                UpdateAccountFragment fragment = UpdateAccountFragment.newInstance(user.getEmail(), user.getPhone(), user.getToken());
                mActivity.replaceFragment(fragment, 0, true);
            }else{
                if (!user.is_card_verfied()) {
                    PaymentMethodFragment paymentMethodFragment = PaymentMethodFragment.newInstance(user);
                    replaceFragment(paymentMethodFragment, true);
                }
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
             //mCurrentPhotoPath= RealPathUtil.getRealPath(mActivity,uri);
            if (mCurrentPhotoPath!=null) {
                setPic();
            }
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

        if (result.isSuccess()){
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String googleId=acct.getId();
            String email=acct.getEmail();
            String name =acct.getDisplayName();
            socialLogin(googleId,name,"",email,Constants.GOOGLE_PROVIDER,"",notificationToken,Constants.DEVICE_TYPE,Utils.getTimeZone());
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
