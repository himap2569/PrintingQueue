package com.example.firebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Random;

public class email extends AppCompatActivity implements View.OnClickListener {
    EditText etMsg;
    TextView etSub,gohome,tvtn;
    Button snd, buttonLogout,token;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    FirebaseStorage storage;
    User userdb;
    String emailid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();


        etSub = (TextView) findViewById(R.id.tn);
        etMsg = (EditText) findViewById(R.id.nc);
        snd = (Button) findViewById(R.id.snd);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);
        gohome = (TextView) findViewById(R.id.gohome);
        gohome.setOnClickListener(this);
        token=(Button)findViewById(R.id.token);
        tvtn=(TextView)findViewById(R.id.tvtn);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference("User");
        userdb=new User();
        firebaseUser=firebaseAuth.getInstance().getCurrentUser();



        snd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto: " + "himap.2569@gmail.com"));
                insertData();
                intent.putExtra(Intent.EXTRA_SUBJECT,"Token Number: "+etSub.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, "Number of Copies needed: "+etMsg.getText().toString());
                startActivity(intent);

               /* Intent intent1=new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                // Set the type of data i.e 'text/plain'
                intent1.setType("text/plain");
                // Launches the activity; Open 'Text editor' if you set it as default app to handle Text
                startActivity(intent1);*/
            }
        });

        token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random(); //instance of random class
                int upperbound = 10000;
                //generate random values from 0-10000
                int int_random = rand.nextInt(upperbound);
                final String fileName= new String(String.valueOf(int_random));
                tvtn.setText("Token Number:"+fileName);
                etSub.setText(fileName);
            }
        });


    }



    private  void  getValues(){
        userdb.setNo_of_copies(etMsg.getText().toString());
        emailid= firebaseUser.getEmail();
        userdb.setEmail(emailid);
    }

    private void insertData() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getValues();
                ref.child(etSub.getText().toString()).setValue(userdb);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v==buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,homepage.class));
        }
        if (v == gohome) {
            finish();
            startActivity(new Intent(this,homepage.class));
        }
    }
}
