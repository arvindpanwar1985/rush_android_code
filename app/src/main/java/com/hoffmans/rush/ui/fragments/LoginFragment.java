package com.hoffmans.rush.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.hoffmans.rush.bean.UserBean;
import com.hoffmans.rush.http.request.LoginRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.model.User;
import com.hoffmans.rush.ui.activities.BookServiceActivity;
import com.hoffmans.rush.ui.activities.ForgotPassActivity;
import com.hoffmans.rush.ui.activities.LoginActivity;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;
import com.hoffmans.rush.utils.Utils;
import com.hoffmans.rush.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.app.Activity.RESULT_OK;
import static com.hoffmans.rush.ui.activities.LoginActivity.REQUEST_GOOGLE_SIGNIN;

/**
 * Created by devesh on 19/12/16.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener,FacebookCallback<LoginResult> {


    private View loginView;
    private final String login_as="user";
    private TextView txtCreateAccount,txtForgotPassword;
    private Button btnLogin;
    private EditText edtEmail,edtPassword;
    private Button btnFb,btnGoogle;
    private String notificationToken;
    private CallbackManager callbackManager;
    public LoginFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginView=inflater.inflate(com.hoffmans.rush.R.layout.fragment_login,container,false);
        mActivity.initToolBar("", true,false);
        initViews(loginView);
        FacebookSdk.sdkInitialize(mActivity.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        initListeners();
        notificationToken =appPreference.getNoticficationToken();
        if(TextUtils.isEmpty(notificationToken)){
           notificationToken= FirebaseInstanceId.getInstance().getToken();
           appPreference.setNotificationToken(notificationToken);
        }
        return loginView;
    }

    @Override
    protected void initViews(View view) {
        txtCreateAccount=(TextView) view.findViewById(com.hoffmans.rush.R.id.flCreateAccount);
        txtForgotPassword=(TextView) view.findViewById(com.hoffmans.rush.R.id.flForgotPass);
        btnLogin=(Button)view.findViewById(com.hoffmans.rush.R.id.flBtnLogin);
        edtPassword=(EditText)view.findViewById(com.hoffmans.rush.R.id.flPassword);
        edtEmail=(EditText)view.findViewById(com.hoffmans.rush.R.id.flUsername);
        btnFb=(Button)view.findViewById(R.id.frBtnFacebook);
        btnGoogle=(Button)view.findViewById(R.id.frBtnGoogle);

 
       }
    @Override
    protected void initListeners() {
     txtCreateAccount.setOnClickListener(this);
     txtForgotPassword.setOnClickListener(this);
     btnLogin.setOnClickListener(this);
     btnFb.setOnClickListener(this);
     btnGoogle.setOnClickListener(this);
     LoginManager.getInstance().registerCallback(callbackManager, this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case com.hoffmans.rush.R.id.flCreateAccount:
                Fragment fragment=new RegisterFragment();
                replaceFragment(fragment,true);
                break;
            case com.hoffmans.rush.R.id.flForgotPass:
                Intent forgotPassIntent=new Intent(mActivity, ForgotPassActivity.class);
                forgotPassIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(forgotPassIntent);
                break;
            case com.hoffmans.rush.R.id.flBtnLogin:
                validateFields();
                break;
            case R.id.frBtnFacebook:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
                break;
            case R.id.frBtnGoogle:
                mActivity.idGoogleApiclient++;
                googleSignIn();
                break;
        }
    }

    /**
     * Validate the login page
     */
    private void validateFields(){
        // Store values at the time of the login attempt.
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mActivity.showSnackbar(getString(com.hoffmans.rush.R.string.error_empty_email), Toast.LENGTH_SHORT);
            return;
        } else if (!Validation.isValidEmail(email)) {
            mActivity.showSnackbar(getString(com.hoffmans.rush.R.string.error_title_invalid_email), Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(password.trim())) {
            mActivity.showSnackbar(getString(com.hoffmans.rush.R.string.error_empty_password), Toast.LENGTH_SHORT);
            return;
        }
        if (TextUtils.isEmpty(password) ) {
            mActivity.showSnackbar(getString(com.hoffmans.rush.R.string.error_title_invalid_password), Toast.LENGTH_SHORT);
            return;
        }
        proceedToLogin(email,password);
    }

    /**
     *
     * @param email email of user
     * @param password password of user
     */
    private void proceedToLogin(String email,String password){


        mActivity.showProgress();
        LoginRequest loginRequest =new LoginRequest();
        loginRequest.loginUser(email, password,login_as,notificationToken,Constants.DEVICE_TYPE,Utils.getTimeZone(), new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                //Progress.dismissProgress();
                mActivity.hideProgress();
                UserBean bean=(UserBean)body;
                User user=bean.getUser();
                if(user!=null && user.getRole()!=null &&user.getRole().equals(com.hoffmans.rush.ui.driver.fragments.LoginFragment.ROLE_CUST)) {

                    handleLoginResult(user);
                }else{
                    mActivity.showSnackbar(getString(R.string.str_invalid_credentials),0);
                }

            }

            @Override
            public void onRequestFailed(String message) {
                mActivity.hideProgress();
                //Progress.dismissProgress();
                Utils.showAlertDialog(mActivity,message);
            }
        });

    }

    private void handleLoginResult(User user){
       if(!user.is_email_verified()){
          showAlertDialog(getString(R.string.str_verify_text));
        }else if(!user.is_card_verfied()){
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
                                String imageUrl = Constants.FBCONTANTS.FB_IMG_URL+socialId+Constants.FBCONTANTS.FB_IMAGE;

                                // socialLogin(Constants.FB_PROVIDER,first_name,last_name,email,socialId,imageUrl,notificationToken,Constants.DEVICE_TYPE,Utils.getTimeZone());
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


    public void showAlertDialog(String message) {
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.app_name)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    }).create().show();
        }catch (Exception e){

        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==REQUEST_GOOGLE_SIGNIN && resultCode == RESULT_OK){
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO uncomment this when implemneted
        //mActivity.stopService(new Intent(mActivity,TrackingService.class));
    }
}
