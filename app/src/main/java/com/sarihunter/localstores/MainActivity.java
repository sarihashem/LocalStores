package com.sarihunter.localstores;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sarihunter.localstores.classes.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    DatabaseReference users;
    private static final int RC_SIGN_IN = 123;
    TextInputEditText email, password;
    Button sign, register;
    ImageView googleSign;
    String userIDinDatabase;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private GoogleSignInClient mgsc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //start next Activity for testing

        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mgsc = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        sign = findViewById(R.id.signIn);

        register = findViewById(R.id.register);

        googleSign = findViewById(R.id.googleSignin);

        register.setOnClickListener(v -> {


            String email1 = email.getText().toString();
            String password1 = password.getText().toString();

            if (!isValid(email1, password1)) {
                Toast.makeText(MainActivity.this, "not valid password", Toast.LENGTH_LONG).show();

            } else {

                toggleProgress(true);

                mAuth.createUserWithEmailAndPassword(email1, password1)
                        .addOnCompleteListener(this, task ->  {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    user = mAuth.getCurrentUser();
                                    //Toast.makeText(MainActivity.this, "registered successfully\n" + user.getEmail(), Toast.LENGTH_LONG).show();
                                    // register user in realtime database
                                    registerToRealTimeDatabase(user);
                                    toggleProgress(false);
                                    updateUI();
                                } else {
                                    toggleProgress(false);
                                    // If sign in fails, display a message to the user.
                                    showError("can't register you right now, check your network");
                                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                        });
            }
        });

        sign.setOnClickListener(v -> {


            String email1 = email.getText().toString();
            String password1 = password.getText().toString();

            if (!isValid(email1, password1)) {
                Toast.makeText(MainActivity.this, "not valid password", Toast.LENGTH_LONG).show();
                return;
            } else {

                toggleProgress(true);
                mAuth.signInWithEmailAndPassword(email1, password1)
                        .addOnCompleteListener(this, task -> {

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    toggleProgress(false);
                                    //Toast.makeText(MainActivity.this, " Signing you in\n" + user.getEmail(), Toast.LENGTH_LONG).show();

                                    updateUI();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    toggleProgress(false);
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    showError("wrong details");
                                    //updateUI(null);
                                }

                        });
            }

        });

        googleSign.setOnClickListener(v -> {

            signInGoogle();


        });


    }
    @NotNull
    private void registerToRealTimeDatabase(@NotNull FirebaseUser user) {
        users = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        User userToAdd = new User(user.getUid(), "", user.getEmail(), "","", "", "",
                0.0, 0, 0, 0, 0.0);
        users.setValue(userToAdd).addOnSuccessListener(aVoid -> {
            Toast.makeText(this,user.getEmail() + " have been registered successfully",Toast.LENGTH_SHORT).show();

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        toggleProgress(true);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                toggleProgress(false);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //showError("google sign in failed");
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        toggleProgress(true);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task ->  {


                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");

                            if(task.getResult().getAdditionalUserInfo().isNewUser()){

                                registerToRealTimeDatabase(user);

                            }

                            Toast.makeText(MainActivity.this, user.getEmail() + " logged in successfully", Toast.LENGTH_LONG).show();
                            toggleProgress(false);
                            updateUI();
                        } else {
                            toggleProgress(false);
                            showError("try again later, or check your internet connnectivity");
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }



                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            updateUI();
        }else{
            //stay here
        }
        //updateUI(currentUser);
    }

    private boolean isValid(String uEmail, String uPassowrd) {
        if (TextUtils.isEmpty(uEmail)) {
            email.setError("please fill the Email address");
            System.out.println("Email: " + uEmail);
            return false;
        } else if (!uEmail.contains("@")) {
            email.setError("please fill the Email address correctly");
            return false;
        } else if (!uEmail.endsWith(".com")) {
            email.setError("please fill the Email address correctly");
            return false;
        } else if (TextUtils.isEmpty(uPassowrd) || uPassowrd.length() < 8) {
            password.setError("please fill the password field with at least 8 characters");
            return false;
        } else {
            email.setError(null);
            password.setError(null);
        }

        return Patterns.EMAIL_ADDRESS.matcher(uEmail).matches();
    }

    ProgressDialog pb;

    private void toggleProgress(boolean show) {

        if (pb == null) {
            pb = new ProgressDialog(this);
            pb.setTitle("Logging you in");
            pb.setMessage("please wait");
        }

        if (show)
            pb.show();
        else
            pb.dismiss();

    }

    private void showError(String msg) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .show();
    }

    private void signInGoogle() {
        Intent signInIntent = mgsc.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI() {

        if(mAuth.getCurrentUser() != null){

            startActivity(new Intent(MainActivity.this, Main3Activity.class).putExtra("userID", userIDinDatabase));
        }


    }


}
