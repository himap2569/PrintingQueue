package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class homepage extends AppCompatActivity implements View.OnClickListener{
    private TextView tvLogin;
    private TextView tvRegister;
    private  TextView pay;
    private TextView title,caption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        tvLogin=(TextView)findViewById(R.id.tvLogin);
        tvRegister=(TextView)findViewById(R.id.tvRegister);
        pay=(TextView)findViewById(R.id.paybtn);
        title=(TextView)findViewById(R.id.title);
        caption=(TextView)findViewById(R.id.caption);
        title.setTextColor(0xff000000);
        caption.setTextColor(0xff444444);

        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        pay.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        if(view==tvLogin){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        if(view==tvRegister){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

        if(view==pay){
           /* Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setData(Uri.parse("https://paytm.com/"));
            startActivity(intent);*/
           startActivity(new Intent(this,payment.class));
            //Intent shareIntent=new Intent();
            //shareIntent.setAction(Intent.ACTION_SEND);
           // shareIntent.putExtra(Intent.EXTRA_TEXT,result);
            //shareIntent.setType("");
            //startActivity(shareIntent.createChooser(shareIntent,"Pay with"));
        }
    }
    }





