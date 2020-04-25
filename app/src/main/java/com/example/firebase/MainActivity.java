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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button buttonRegister;
    private EditText editTextEmail,editTextPassword;
    private TextView textViewSignin;
    private FirebaseAuth firebaseAuth;
    private TextView gohome;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){//Returns currently signed in firebaseUser
            //profile activity
            finish();
            startActivity(new Intent(MainActivity.this,email.class));
        }


        progressDialog= new ProgressDialog(this);
        buttonRegister=(Button)findViewById(R.id.buttonRegister);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        textViewSignin=(TextView)findViewById(R.id.textViewSignin);
        gohome=(TextView)findViewById(R.id.gohome);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        gohome.setOnClickListener(this);

    }
private void registerUser(){
        String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter an e-mail Address!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter a Password!",Toast.LENGTH_SHORT).show();
            return;
    }
        //
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Registered Successfully!",Toast.LENGTH_SHORT).show();
                    if(firebaseAuth.getCurrentUser()!=null){
                        //profile activity
                        finish();
                        startActivity(new Intent(MainActivity.this,email.class));
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Error, Please try again! :(",Toast.LENGTH_SHORT).show();
                }
            }
        });

}
    @Override
    public void onClick(View view) {
        if(view==buttonRegister){
            registerUser();
        }
        if(view==textViewSignin){
            //will open loginpage
            startActivity(new Intent(this, LoginActivity.class));

        }
        if(view==gohome){
            finish();
            startActivity(new Intent(this,homepage.class));
        }

    }
}
