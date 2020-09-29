package com.danc.winesapi.Auth.WelcomeAndAuth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.danc.winesapi.Interfaces.ApiClient;
import com.danc.winesapi.Models.LoginUser;
import com.danc.winesapi.ProductActivity;
import com.danc.winesapi.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogIn extends AppCompatActivity {
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
        setContentView(R.layout.activity_log_in);

        mProgressBar = findViewById(R.id.progressBar);
        edtEmail = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtSignIn = findViewById(R.id.edtSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("INTENT_NAME"));

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.12:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient = retrofit.create(ApiClient.class);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        setupFirebaseAuth();

        edtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, Register.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check if the fields are filled out
                if (!isEmpty(edtEmail.getText().toString())
                        && !isEmpty(edtPassword.getText().toString())) {
                    Log.d(TAG, "onClick: attempting to authenticate.");

                    showDialog();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(edtEmail.getText().toString(),
                            edtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        signUser();
                                    }

                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LogIn.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            hideDialog();
                        }
                    });
                } else {
                    hideDialog();
                    Toast.makeText(LogIn.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LogIn.this, ProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                }
            }
        };
    }

    private void signUser() {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        LoginUser loginUser = new LoginUser(email, password);

        Call<LoginUser> userLogin = apiClient.loginNewUser(loginUser);
        userLogin.enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                Log.d(TAG, "onResponse: Signed User " + response.code());
                if (!response.isSuccessful()) {
                    hideDialog();
                    Toast.makeText(LogIn.this, "Failed to Sign in", Toast.LENGTH_SHORT).show();
                    return;
                }
                hideDialog();
                Toast.makeText(LogIn.this, "Thank you, you're Signed in", Toast.LENGTH_SHORT).show();
                sendEmail();
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {
                Log.d(TAG, "onFailure: Error Message " + t.getMessage());
            }
        });
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            usedEmail = intent.getStringExtra("SignUp Email");
            usedPassword = intent.getStringExtra("Password");
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    private void sendEmail(){
        String email = edtEmail.getText().toString();
        Intent intent = new Intent("INTENT_NAME");
        intent.putExtra("User Email", email);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
