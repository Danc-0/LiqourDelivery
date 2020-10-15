package com.danc.winesapi.Auth.WelcomeAndAuth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.danc.winesapi.Interfaces.ApiClient;
import com.danc.winesapi.Models.RegisterUser;
import com.danc.winesapi.ProductActivity;
import com.danc.winesapi.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    private static final String TAG = "SignUp";

    final String uniqueID = UUID.randomUUID().toString();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private FirebaseStorage mFirebaseStorage;
    private String currentUserID;

    private ProgressBar mProgressBar;

    EditText edtEmail, edtFirstName, edtLastName, edtName, phoneNumber, edtPassword, edtConfirmPassword;
    Button btnSignUp;
    TextView edtSignUp;

    ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        setupFirebaseAuth();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiClient = retrofit.create(ApiClient.class);


        edtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LogIn.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: attempting to register.");

                //check for null valued EditText fields
                if (!isEmpty(edtEmail.getText().toString())
                        && !isEmpty(edtPassword.getText().toString())
                        && !isEmpty(edtConfirmPassword.getText().toString())) {

                    mProgressBar.setVisibility(View.INVISIBLE);

                    //check if passwords match
                    if (doStringsMatch(edtPassword.getText().toString(), edtConfirmPassword.getText().toString())) {
                        //Initiate registration task
                        registerNewEmail(edtEmail.getText().toString(), edtPassword.getText().toString());
                        mProgressBar.setVisibility(View.INVISIBLE);

                    } else {
                        Toast.makeText(Register.this, "Passwords do not Match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Register.this, "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
                saveUserProfile();
                clear();
            }
        });

        hideSoftKeyboard();

    }

    private void init() {
        mProgressBar = findViewById(R.id.mProgressBar);
        edtFirstName = findViewById(R.id.FirstName);
        edtLastName = findViewById(R.id.LastName);
        edtEmail = findViewById(R.id.edtEmail);
//        edtName = findViewById(R.id.Name);
        phoneNumber = findViewById(R.id.edtPhone);

        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        edtSignUp = findViewById(R.id.edtSignUp);
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
                if (!response.isSuccessful()) {
                    Toast.makeText(Register.this, "User Not Created", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Failed " + response.code());
                    return;
                }
                Toast.makeText(Register.this, "Thank you User Created Successfully", Toast.LENGTH_SHORT).show();
                clear();
                redirectLoginScreen();
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                hideDialog();
                Log.d(TAG, "onFailure: Failed to Create " + t.getMessage());
            }
        });
    }

    public void registerNewEmail(final String email, String password) {
        showDialog();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            getNewUser();

                        } else {
                            Toast.makeText(Register.this, "Unable to Register", Toast.LENGTH_SHORT).show();
                        }
                        hideDialog();
                    }
                });
    }

    private void saveUserProfile() {
        showDialog();
        String name = edtFirstName.getText().toString() + " " + edtLastName.getText().toString();
        String EmailAddress = edtEmail.getText().toString();
        String phone = phoneNumber.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(EmailAddress) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
            hideDialog();
        } else {
            HashMap ProfileDetails = new HashMap();
            ProfileDetails.put("Name", name);
            ProfileDetails.put("EmailAddress", EmailAddress);
            ProfileDetails.put("Phone", phone);

            mDatabaseReference.child(uniqueID).updateChildren(ProfileDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        getNewUser();

                    } else {
                        Toast.makeText(Register.this, "Check Your Details", Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                }
            });
        }
    }

    private void clear() {
        edtFirstName.setText("");
        edtLastName.setText("");
        edtEmail.setText("");
        phoneNumber.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");
        hideDialog();

    }

    /**
     * Redirects the user to the login screen
     */
    private void redirectLoginScreen() {
        Log.d(TAG, "redirectLoginScreen: redirecting to login screen.");
        Intent intent = new Intent(Register.this, LogIn.class);
        startActivity(intent);
        finish();
    }


    private boolean doStringsMatch(String s1, String s2) {
        return s1.equals(s2);
    }


    private boolean isEmpty(String string) {
        return string.equals("");
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

    private void setupFirebaseAuth() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(Register.this, ProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(Register.this, "Check your credentials", Toast.LENGTH_SHORT).show();

                }

            }
        };
    }


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
}



