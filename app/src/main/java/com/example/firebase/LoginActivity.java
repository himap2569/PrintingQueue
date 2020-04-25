package com.example.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSignin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView gohome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            //profile activity
            finish();
            startActivity(new Intent(LoginActivity.this,email.class));
        }

        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        buttonSignin=(Button)findViewById(R.id.buttonSignin);
        textViewSignup=(TextView)findViewById(R.id.textViewSignUp);
        progressDialog=new ProgressDialog(this);
        gohome=(TextView)findViewById(R.id.gohome);

        buttonSignin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        gohome.setOnClickListener(this);
    }

    private void userLogin(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an e-mail Address!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter a Password!",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()){
                    //start profile activity
                    finish();
                    Toast.makeText(LoginActivity.this, "Welcome! :)",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,email.class));
                }
                else{
                    Toast.makeText(LoginActivity.this, "Invalid Credentials.",Toast.LENGTH_SHORT).show();
                }

            }
        });
        }



    @Override
    public void onClick(View view) {
        if(view==buttonSignin){
            userLogin();
        }
        if(view==textViewSignup){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
        if(view==gohome){
            finish();
            startActivity(new Intent(this,homepage.class));
        }
    }
}
