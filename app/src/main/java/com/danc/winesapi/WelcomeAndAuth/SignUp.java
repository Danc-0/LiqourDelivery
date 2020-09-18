package com.danc.winesapi.WelcomeAndAuth;

import android.content.Intent;
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
import com.danc.winesapi.Models.RegisterUser;
import com.danc.winesapi.R;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignUp";

//    final String uniqueID = UUID.randomUUID().toString();
//
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//
//    private FirebaseDatabase mFirebaseDatabase;
//    private DatabaseReference mDatabaseReference;
//    private StorageReference mStorageReference;
//    private FirebaseStorage mFirebaseStorage;
//    private String currentUserID;

    private ProgressBar mProgressBar;

    EditText edtEmail, edtFirstName, edtLastName, phoneNumber, edtPassword, edtConfirmPassword;
    Button btnSignUp;
    TextView edtSignUp;

    ApiClient apiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mProgressBar = findViewById(R.id.mProgressBar);
        edtEmail = findViewById(R.id.edtEmail);
        edtFirstName = findViewById(R.id.FirstName);
        edtLastName = findViewById(R.id.LastName);
        phoneNumber = findViewById(R.id.edtPhone);

        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtSignUp = findViewById(R.id.edtSignUp);

        btnSignUp.setOnClickListener(this);
        edtSignUp.setOnClickListener(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.12:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

         apiClient = retrofit.create(ApiClient.class);

    }

    private void getNewUser() {
        String email = edtEmail.getText().toString();
        String username = edtFirstName.getText().toString();
        String name = edtFirstName.getText().toString() + " " + edtLastName.getText().toString();
        String phone = phoneNumber.getText().toString();
        String password = edtPassword.getText().toString();

        RegisterUser registerUser = new RegisterUser(email, username, name, phone, password);

        Call<RegisterUser> user = apiClient.userRegistration(registerUser);
        user.enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(SignUp.this, "User Not Created", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Failed " + response.code());
                    return;
                }
                Toast.makeText(SignUp.this, "Thank you User Created Successfully", Toast.LENGTH_SHORT).show();
                clear();
                redirectLoginScreen();
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                Log.d(TAG, "onFailure: Failed to Create " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUp:
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show();
                saveUserProfile();
                break;

            case R.id.edtSignUp:
                Toast.makeText(this, "Logging in Activity", Toast.LENGTH_SHORT).show();
                redirectLoginScreen();
        }

    }

    public void transferAuthDetails(){
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        if (doStringsMatch(password, confirmPassword)){
            Intent intent = new Intent("INTENT_NAME");
            intent.putExtra("SignUp Email", email);
            intent.putExtra("Password", password);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return;
        }
        Toast.makeText(this, "Credentials do not Match", Toast.LENGTH_SHORT).show();

    }

    public void checkAuthDetails(){
        //check for null valued EditText fields
        if (!isEmpty(edtEmail.getText().toString())
                && !isEmpty(edtPassword.getText().toString())
                && !isEmpty(edtConfirmPassword.getText().toString())) {

            mProgressBar.setVisibility(View.INVISIBLE);

            //check if passwords match
            if (doStringsMatch(edtPassword.getText().toString(), edtConfirmPassword.getText().toString())) {
                getNewUser();
                mProgressBar.setVisibility(View.INVISIBLE);

            } else {
                Toast.makeText(SignUp.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                hideDialog();
            }

        } else {
            Toast.makeText(SignUp.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
            hideDialog();
        }
    }

    private void saveUserProfile() {
        showDialog();
        String FirstName = edtFirstName.getText().toString();
        String LastName = edtLastName.getText().toString();
        String EmailAddress = edtEmail.getText().toString();
        String phone = phoneNumber.getText().toString();

        if (isEmpty(FirstName) || (isEmpty(LastName)) || isEmpty(EmailAddress) || isEmpty(phone)) {
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
            hideDialog();

        } else {
            checkAuthDetails();
        }

    }

    private boolean doStringsMatch(String s1, String s2) {
        return s1.equals(s2);
    }

    private void clear() {
        edtFirstName.setText("");
        edtEmail.setText("");
        phoneNumber.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");

    }

    private void showDialog() {
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private void hideDialog() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

    private void redirectLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");

        transferAuthDetails();
        Intent intent = new Intent(SignUp.this, SignIn.class);
        startActivity(intent);
        finish();
    }
}








