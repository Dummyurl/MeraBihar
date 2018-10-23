package app.zingo.merabihar.UI.ActivityScreen.AccountScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import app.zingo.merabihar.Model.UserProfile;
import app.zingo.merabihar.Model.UserRole;
import app.zingo.merabihar.R;
import app.zingo.merabihar.UI.ActivityScreen.LandingScreen.LandingScreenActivity;
import app.zingo.merabihar.UI.ActivityScreen.MainTabHostActivity.TabMainActivity;
import app.zingo.merabihar.Util.Constants;
import app.zingo.merabihar.Util.PreferenceHandler;
import app.zingo.merabihar.Util.ThreadExecuter;
import app.zingo.merabihar.Util.Util;
import app.zingo.merabihar.WebApi.ProfileAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private static ImageView mBack;
    private static TextInputEditText mEmail,mPassword;
    private static AppCompatButton mLogin,mFaceBook,mGoogle;
    private static AppCompatTextView mSignUp;

    //Google Signin
    private static final String TAG = LoginScreen.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    //Facebook
    CallbackManager callbackManager;
    private static final String EMAIL = "email";
    LoginManager mFbLoginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            //Facebook
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this);
            setContentView(R.layout.activity_login_screen);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

            mBack = (ImageView)findViewById(R.id.back);

            mEmail = (TextInputEditText)findViewById(R.id.email);
            mPassword = (TextInputEditText)findViewById(R.id.password);

            mLogin = (AppCompatButton)findViewById(R.id.loginAccount);
            mFaceBook = (AppCompatButton)findViewById(R.id.facebook);
            mGoogle = (AppCompatButton)findViewById(R.id.google);

            mSignUp = (AppCompatTextView)findViewById(R.id.signup);

            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LoginScreen.this.finish();
                }
            });



            mLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    validate();
                }
            });

            mSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent login = new Intent(LoginScreen.this,SignUpScreen.class);
                    startActivity(login);

                }
            });

            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

            googleInit();


            mGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });


            // mFaceBook.setReadPermissions(Arrays.asList(EMAIL));
            mFbLoginManager = LoginManager.getInstance();
            callbackManager = CallbackManager.Factory.create();


            mFbLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    // Toast.makeText(SignUpScreen.this, "Facebook", Toast.LENGTH_SHORT).show();
                    System.out.println("Facebook "+loginResult.toString());

                    final AccessToken accessToken = AccessToken.getCurrentAccessToken();
                   // LoginManager.getInstance().logInWithPublishPermissions(LoginScreen.this, Arrays.asList("publish_action"));
                 //   LoginManager.getInstance().logInWithPublishPermissions(LoginScreen.this, Arrays.asList( "publish_action"));
                    try{


                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        String name=null,email=null,id=null;

                                        try {

                                            if (object.has("email")) {

                                                if(object.getString("email")!=null){

                                                    email = object.getString("email");
                                                }
                                            }


                                            name = object.getString("name");
                                            id = object.getString("id");
                                            String birthday = object.getString("birthday"); // 01/31/1980 format
                                           // System.out.println("Email "+email+" "+birthday);
                                            //Toast.makeText(SignUpScreen.this, "Email "+email+" "+birthday, Toast.LENGTH_SHORT).show();


                                            UserProfile profiles = new UserProfile();
                                            if(name!=null){
                                                profiles.setFullName(name);
                                            }
                                            if(email!=null){
                                                profiles.setEmail(email);
                                            }
                                            if(id!=null){
                                                profiles.setProfilePhoto("https://graph.facebook.com/" + id+ "/picture?type=large");
                                            }

                                            // profiles.setPhoneNumber(mobile);
                                            profiles.setUserRoleId(1);

                                            profiles.setStatus("Active");
                                            profiles.setAuthType("Facebook");
                                            profiles.setAuthId(""+id);

                                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                                            profiles.setSignUpDate(sdf.format(new Date()));

                                            //postProfile(profiles);

                                            if(email!=null){
                                                checkUserByAuthId(profiles,"Facebook");
                                            }else{
                                                postProfile(profiles);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            System.out.println("Error email");
                                            //Toast.makeText(LoginScreen.this, "Email No  ", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();




                    }catch (Exception e){
                        e.printStackTrace();
                    }




                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException e) {

                }
            });


            mFaceBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mFbLoginManager.logInWithReadPermissions(LoginScreen.this, Arrays.asList("email", "public_profile", "user_birthday"));


                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void validate(){

        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if(email==null||email.isEmpty()){

            Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();

        }else if(password==null||password.isEmpty()){

            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();

        }else{

            UserProfile profiles = new UserProfile();
            profiles.setPassword(password);

            profiles.setEmail(email);
            loginProfile(profiles);

        }


    }

    private void loginProfile( final UserProfile p){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);


                Call<ArrayList<UserProfile>> call = apiService.getProfileforLogin(p);

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        if (statusCode == 200 || statusCode == 201) {

                            ArrayList<UserProfile> dto1 = response.body();//-------------------should not be list------------
                            if (dto1!=null && dto1.size()!=0) {
                                UserProfile dto = dto1.get(0);


                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
                                SharedPreferences.Editor spe = sp.edit();
                                spe.putInt(Constants.USER_ID, dto.getProfileId());
                                PreferenceHandler.getInstance(LoginScreen.this).setUserId(dto.getProfileId());
                                PreferenceHandler.getInstance(LoginScreen.this).setUserName(dto.getEmail());
                                PreferenceHandler.getInstance(LoginScreen.this).setUserFullName(dto.getFullName());
                                spe.putString("FullName", dto.getFullName());
                                spe.putString("Password", dto.getPassword());
                                spe.putString("Email", dto.getEmail());
                                spe.putString("PhoneNumber", dto.getPhoneNumber());
                                spe.putInt("UserRoleId", dto.getUserRoleId());
                                spe.apply();



                                UserRole userRole = dto.getUserRoles();
                                if(userRole != null)
                                {
                                    System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                                    PreferenceHandler.getInstance(LoginScreen.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                }

                                Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginScreen.this, TabMainActivity.class);
                                i.putExtra("Profile",dto);
                                startActivity(i);
                                finish();


                            }else{
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                Toast.makeText(LoginScreen.this, "Login credentials are wrong..", Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            Toast.makeText(LoginScreen.this, "Login failed due to status code:"+statusCode, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed
                        if (progressDialog!=null)
                            progressDialog.dismiss();
                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = null;

            if(acct.getPhotoUrl()!=null){
                personPhotoUrl = acct.getPhotoUrl().toString();
            }

            String email = acct.getEmail();
            String id = acct.getId();
            //String id = acct.getId();


            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            System.out.println("Google = "+"Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl+ ", Id: " + id);

            UserProfile profiles = new UserProfile();
            profiles.setFullName(personName);
            //  profiles.setPassword(password);
            /*if(mMale.isChecked()){
                profiles.setGender("Male");
            }else if(mFemale.isChecked()){
                profiles.setGender("Female");
            }else if(mOther.isChecked()){
                profiles.setGender("Others");
            }*/
            profiles.setEmail(email);
            // profiles.setPhoneNumber(mobile);
            profiles.setUserRoleId(1);

            profiles.setStatus("Active");
            profiles.setAuthType("Google");
            profiles.setAuthId(""+id);

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            profiles.setSignUpDate(sdf.format(new Date()));

            if(personPhotoUrl!=null&&!personPhotoUrl.isEmpty()){
                profiles.setProfilePhoto(personPhotoUrl);
            }

            //checkUserByEmailId(profiles,"Google");
            checkUserByAuthId(profiles,"Google");

            //UserProfile userProfile = new U

            /*txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);

            updateUI(true);*/
        } else {
            // Signed out, show unauthenticated UI.
           // updateUI(false);
            //Toast.makeText(this, "Some thing went wrong", Toast.LENGTH_SHORT).show();

            System.out.println("Google sign in = "+result.getStatus()+"/"+result.toString());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{

            if (FacebookSdk.isFacebookRequestCode(requestCode)) {
                //Facebook activity result
                //Do your stuff here
                //Further you can also check if it's login or Share etc by using
                //CallbackManagerImpl as explained by rajath's answer here
                if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
                    //login
                    callbackManager.onActivityResult(requestCode, resultCode, data);
                }
                else if (requestCode == CallbackManagerImpl.RequestCodeOffset.Share.toRequestCode()){
                    //share
                }
            }
        }
    }

   /* @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvab error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    public void googleInit(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void postProfile(final UserProfile userProfile) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Please wait..");
        dialog.show();



        //System.out.println(sub.getCategoriesName()+","+sub.getDescription()+","+sub.getOrderNo()+","+sub.getCityId());

        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {
                ProfileAPI auditApi = Util.getClient().create(ProfileAPI.class);
                Call<UserProfile> response = auditApi.postProfile(userProfile);
                response.enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        System.out.println(response.code());

                        if(response.code() == 201)
                        {

                            if(response.body()!=null){

                                UserProfile dto = response.body();
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
                                SharedPreferences.Editor spe = sp.edit();
                                spe.putInt(Constants.USER_ID, dto.getProfileId());
                                PreferenceHandler.getInstance(LoginScreen.this).setUserId(dto.getProfileId());
                                PreferenceHandler.getInstance(LoginScreen.this).setUserName(dto.getEmail());
                                PreferenceHandler.getInstance(LoginScreen.this).setUserFullName(dto.getFullName());
                                spe.putString("FullName", dto.getFullName());
                                spe.putString("Password", dto.getPassword());
                                spe.putString("Email", dto.getEmail());
                                spe.putString("PhoneNumber", dto.getPhoneNumber());
                                spe.putInt("UserRoleId", dto.getUserRoleId());
                                spe.apply();



                                UserRole userRole = dto.getUserRoles();
                                if(userRole != null)
                                {
                                    System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                                    PreferenceHandler.getInstance(LoginScreen.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                }

                                if(mGoogleApiClient!=null){

                                    if (mGoogleApiClient.isConnected()) {
                                        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                        mGoogleApiClient.disconnect();
                                        mGoogleApiClient.connect();
                                    }
                                }

                               /* if(mFbLoginManager!=null){

                                    AccessToken accessToken = AccessToken.getCurrentAccessToken();

                                    if(accessToken!=null){

                                        mFbLoginManager.logOut();
                                    }
                                }*/


                                Toast.makeText(LoginScreen.this,"Profile created Successfull",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginScreen.this, TabMainActivity.class);
                                startActivity(intent);
                                LoginScreen.this.finish();

                            }

                        }
                        else
                        {
                            Toast.makeText(LoginScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {
                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }
                        Toast.makeText(LoginScreen.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    private void checkUserByAuthId(final UserProfile userProfile,final String type){


        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Please wait..");
        dialog.show();


        new ThreadExecuter().execute(new Runnable() {
            @Override
            public void run() {


                ProfileAPI apiService =
                        Util.getClient().create(ProfileAPI.class);

                Call<ArrayList<UserProfile>> call = apiService.getUserByAuthId(userProfile.getAuthId());

                call.enqueue(new Callback<ArrayList<UserProfile>>() {
                    @Override
                    public void onResponse(Call<ArrayList<UserProfile>> call, Response<ArrayList<UserProfile>> response) {
//                List<RouteDTO.Routes> list = new ArrayList<RouteDTO.Routes>();
                        int statusCode = response.code();

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        if(statusCode == 200 || statusCode == 204)
                        {

                            ArrayList<UserProfile> responseProfile = response.body();
                            if(responseProfile != null && responseProfile.size()!=0 )
                            {

                                System.out.println("Response size = "+responseProfile.size());

                                if(type!=null&&!type.isEmpty()){
                                    if(type.equalsIgnoreCase("Google")){
                                        //Toast.makeText(LoginScreen.this, "Email already registered with us", Toast.LENGTH_SHORT).show();

                                        if(mGoogleApiClient!=null){

                                            if (mGoogleApiClient.isConnected()) {
                                                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                                mGoogleApiClient.disconnect();
                                                mGoogleApiClient.connect();
                                            }
                                        }
                                    }else if(type.equalsIgnoreCase("Facebook")){
                                        if(mFbLoginManager!=null){

                                            AccessToken accessToken = AccessToken.getCurrentAccessToken();

                                            if(accessToken!=null){

                                                mFbLoginManager.logOut();
                                            }


                                        }

                                        //Toast.makeText(LoginScreen.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                                    }

                                    UserProfile dto = responseProfile.get(0);


                                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginScreen.this);
                                    SharedPreferences.Editor spe = sp.edit();
                                    spe.putInt(Constants.USER_ID, dto.getProfileId());
                                    PreferenceHandler.getInstance(LoginScreen.this).setUserId(dto.getProfileId());
                                    PreferenceHandler.getInstance(LoginScreen.this).setUserName(dto.getEmail());
                                    PreferenceHandler.getInstance(LoginScreen.this).setUserFullName(dto.getFullName());
                                    spe.putString("FullName", dto.getFullName());
                                    spe.putString("Password", dto.getPassword());
                                    spe.putString("Email", dto.getEmail());
                                    spe.putString("PhoneNumber", dto.getPhoneNumber());
                                    spe.putInt("UserRoleId", dto.getUserRoleId());
                                    spe.apply();



                                    UserRole userRole = dto.getUserRoles();
                                    if(userRole != null)
                                    {
                                        System.out.println("Unique id = "+userRole.getUserRoleUniqueId());
                                        PreferenceHandler.getInstance(LoginScreen.this).setUserRoleUniqueID(userRole.getUserRoleUniqueId());
                                    }

                                    Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginScreen.this, TabMainActivity.class);
                                    i.putExtra("Profile",dto);
                                    startActivity(i);
                                    finish();
                                }

                            }
                            else
                            {
                                    postProfile(userProfile);

                            }
                        }
                        else
                        {

                            Toast.makeText(LoginScreen.this,response.message(),Toast.LENGTH_SHORT).show();
                        }
//                callGetStartEnd();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<UserProfile>> call, Throwable t) {
                        // Log error here since request failed

                        if(dialog != null)
                        {
                            dialog.dismiss();
                        }

                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }



}
