package com.danc.winesapi.WelcomeAndAuth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.danc.winesapi.Interfaces.ApiClient;
import com.danc.winesapi.Models.LoginUser;
import com.danc.winesapi.ProductActivity;
import com.danc.winesapi.R;

import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    EditText edtEmail, edtPassword;
    TextView edtSignIn;
    Button btnSignIn;
    private static final String TAG = "Login";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressBar mProgressBar;

    Retrofit retrofit;
    ApiClient apiClient;

    String usedEmail, usedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mProgressBar = findViewById(R.id.progressBar);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtSignIn = findViewById(R.id.edtSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);

        edtSignIn.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.12:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient = retrofit.create(ApiClient.class);


    }

    private void signUser() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        LoginUser loginUser = new LoginUser(email, password);

        Call<LoginUser> userLogin = apiClient.userLogin(loginUser);
        userLogin.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                Log.d(TAG, "onResponse: Signed User " + response.code());
                if (!response.isSuccessful()) {
                    Toast.makeText(SignIn.this, "Failed to Sign in", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Not Signed In " + response.code());
                    return;
                }
                Log.d(TAG, "onResponse: Data Values " + response.body());
                Toast.makeText(SignIn.this, "Thank you, you're Signed in", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {
                Log.d(TAG, "onFailure: Error Message " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                doMatchEmail();
                break;

            case R.id.edtSignIn:
                redirectToSignUp();
        }
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            usedEmail = intent.getStringExtra("SignUp Email");
            usedPassword = intent.getStringExtra("Password");
        }
    };

    public void doMatchEmail(){
        String currentEmail = edtEmail.getText().toString();
        assert usedEmail != null;
        if (doStringsMatch(currentEmail, usedEmail)){
            passwordMatch();
            Toast.makeText(this, "Check password match to log in", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(this, "Incorrect email used", Toast.LENGTH_SHORT).show();
    }

    public void passwordMatch(){
        String currentPassword = edtPassword.getText().toString();

        assert usedPassword != null;
        if (doStringsMatch(currentPassword, usedPassword)){
            gotToProductActivity();
            Toast.makeText(this, "Log in the User", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean doStringsMatch(String s1, String s2){
       return s1.equals(s2);
    }

    public void gotToProductActivity() {
        Intent intent = new Intent(SignIn.this, ProductActivity.class);
        startActivity(intent);
    }

    public void redirectToSignUp() {
        Intent intent = new Intent(SignIn.this, SignUp.class);
        startActivity(intent);
    }
}


//
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                //check if the fields are filled out
//                if (!isEmpty(edtPhone.getText().toString())
//                        && !isEmpty(edtPassword.getText().toString())) {
//                    Log.d(TAG, "onClick: attempting to authenticate.");
//
//                    showDialog();
//
//                    FirebaseAuth.getInstance().signInWithEmailAndPassword(edtPhone.getText().toString(),
//                            edtPassword.getText().toString())
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//
//                                    if (task.isSuccessful()) {
//                                        hideDialog();
//                                    }
//
//                                }
//
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(SignIn.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
//                            hideDialog();
//                        }
//                    });
//                } else {
//                    Toast.makeText(SignIn.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
//
//    private void showDialog() {
//        mProgressBar.setVisibility(View.VISIBLE);
//
//    }
//
//    private void hideDialog() {
//        if (mProgressBar.getVisibility() == View.VISIBLE) {
//            mProgressBar.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    private void hideSoftKeyboard() {
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//    }
//
//    private boolean isEmpty(String string) {
//        return string.equals("");
//    }
//
//    private void setupFirebaseAuth() {
//        Log.d(TAG, "setupFirebaseAuth: started.");
//
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    init();
////                    Intent intent = new Intent(SignIn.this, MainActivity.class);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                    startActivity(intent);
//                    finish();
//
//                }
//            }
//        };
//    }
//
//    public void init(){
////        MainFragment mainFragment = new MainFragment();
////        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////        transaction.replace(R.id.main_container, mainFragment, getString(R.string.fragment_main));
////        transaction.commit();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
//        }
//    }
//}
