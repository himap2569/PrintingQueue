package com.example.firebase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class PQ extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    Uri pdfUri;//URLs meant for local storage
    //ProgressDialog progressDialog;
    private TextView token;
    String fileName;
    private TextView gohome;

    private Button selectFile, upload;
    private TextView notification;
    FirebaseStorage storage;//used for uploading files like pdf
    FirebaseDatabase database;//used to store urls of uploaded files




    public PQ() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pq);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(this);
        gohome=(TextView)findViewById(R.id.gohome);
        gohome.setOnClickListener(this);
        token=(TextView)findViewById(R.id.token);

        storage = FirebaseStorage.getInstance();//Returns object of firebase storage
        database = FirebaseDatabase.getInstance();//fb db

        selectFile = (Button) findViewById(R.id.selectFile);
        upload = (Button) findViewById(R.id.upload);
        notification = (TextView) findViewById(R.id.notification);

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PQ.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPDF();

                } else {
                    ActivityCompat.requestPermissions(PQ.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });
        /*selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PQ.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectPDF();

                } else {
                    ActivityCompat.requestPermissions(PQ.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }

        });*/

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri!=null) {
                    uploadFile(pdfUri);
                }
                else
                {
                    Toast.makeText(PQ.this,"Select a file",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void uploadFile(Uri pdfUri)
    {
       /* progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File");
        progressDialog.setProgress(0);
        progressDialog.show();*/


        Random rand = new Random(); //instance of random class
        //int upperbound = 10000;
        //generate random values from 0-10000
        //int int_random = rand.nextInt(upperbound);

        //final String fileName= new String(String.valueOf(int_random));
        final String name=System.currentTimeMillis()+"";
        StorageReference storageReference= storage.getReference();//returns root path
        storageReference.child("Uploads").child(name).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String url= String.valueOf(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                //store this url in rt db
                DatabaseReference reference=database.getReference();
                reference.child(fileName).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                           // progressDialog.dismiss();
                            Toast.makeText(PQ.this,"File Successfully Uploaded!",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            //progressDialog.dismiss();
                           Toast.makeText(PQ.this,"Error Uploading File.",Toast.LENGTH_SHORT).show();                      }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

               // Toast.makeText(PQ.this,"Error Uploading File",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                //track progress of upload
                int currentProgress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
               // progressDialog.setProgress(currentProgress);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectPDF();

        } else {
            Toast.makeText(PQ.this, "Please give permission", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectPDF() {
        //Allow user to select a file using file manager
        //uses intent

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);//to fetch files
        startActivityForResult(intent, 69);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //check whether user has selected a file or not
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();//returns uri of selected file
            notification.setText("File selected:"+data.getData().getLastPathSegment());
            Random rand = new Random(); //instance of random class
            int upperbound = 10000;
            //generate random values from 0-10000
            int int_random = rand.nextInt(upperbound);
            final String fileName= new String(String.valueOf(int_random));
            token.setText("Token Number:"+fileName);

        } else {
            Toast.makeText(PQ.this, "Please select a file", Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void onClick(View view) {
    if(view==buttonLogout){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this,homepage.class));
    }
    if(view==gohome){
        finish();
        startActivity(new Intent(this,homepage.class));
    }
    }
}
