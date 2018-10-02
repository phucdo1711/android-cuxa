package com.example.dell.appcuxa.Login.LoginView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dell.appcuxa.MainPage.MainPageViews.MainPageActivity;
import com.example.dell.appcuxa.Login.Models.ISignIn;
import com.example.dell.appcuxa.ObjectModels.UserModel;
import com.example.dell.appcuxa.Login.Presenters.SignInLogicPresenter;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Login.Adapter.SectionsPagerAdapter;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,LoginView, ISignIn {
    private static final String TAG = "GOOGLE";
    CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    SectionsPagerAdapter adapter;
    public static final int RC_SIGN_IN = 1996;
    SignInLogicPresenter presenterLogin;
    UserModel userModel;
    SharedPreferences pre;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(MainActivity.this.getResources().getString(R.string.client_id))
                .requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        callbackManager = CallbackManager.Factory.create();
        circleIndicator = findViewById(R.id.indicator);
        viewPager = findViewById(R.id.viewPager);
        pre=getSharedPreferences("login_data", MODE_PRIVATE);
        edit=pre.edit();
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        userModel = new UserModel();
        presenterLogin = new SignInLogicPresenter(this,this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.btnLoginFb).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.sign_in_button:
                if(AppUtils.haveNetworkConnection(getApplicationContext())){
                    signOut();
                    loginGoogle();
                }else{
                    Toast.makeText(this, "Đéo có mạng", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnLoginFb:
                if(AppUtils.haveNetworkConnection(getApplicationContext())){
                    loginFaceBook();
                }else{
                    Toast.makeText(this, "Đéo có mạng", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void loginFaceBook() {
        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FACETAG", "facebook:onSuccess:" + loginResult);
                // gọi hàm của thằng ml Phúc.
                String accessToken = loginResult.getAccessToken().getToken();
                 presenterLogin.ThucHienDangNhapFB(MainActivity.this,accessToken);
            }

            @Override
            public void onCancel() {
                Log.d("FACETAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FACETAG", "facebook:onError", error);
            }
        });
    }

    private void loginGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("TAG", "success");
            Log.d("TAGG", account.toJson());
            loginInBackendless(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAGGG", "success");
                    }
                });
    }

    public void updateUI(UserModel userModel) {
        Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
        intent.putExtra("login_user", userModel);
        startActivity(intent);
    }

    private void loginInBackendless(final GoogleSignInAccount acct) {
        Log.d(TAG, "handleSignInResult: try login to backendless");
        final String scopes = "oauth2:" + Scopes.PLUS_LOGIN + " " + Scopes.PLUS_ME + " " + Scopes.PROFILE + " " + Scopes.EMAIL;
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;
                try {
                    token = GoogleAuthUtil.getToken(getApplicationContext(), acct.getAccount(), scopes);
                    GoogleAuthUtil.clearToken(getApplicationContext(), token);
                    presenterLogin.ThucHienDangNhapGG(MainActivity.this,token);
                } catch (UserRecoverableAuthException e) {
                    startActivityForResult(e.getIntent(), RC_SIGN_IN);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return token;
            }
        };

        task.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void signInSuccess() {
        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signInFailure() {
        Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
    }

    /**
     * Hàm nhận về data của login facebook.
     * @param userModel
     */
    @Override
    public void sendBackUserModel(UserModel userModel) {
        if(userModel!=null){
            edit.putString("token",userModel.getToken()); // token cũ
            edit.commit();
            updateUI(userModel);
        }
    }

    /**
     * Đóng ứng dụng
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
