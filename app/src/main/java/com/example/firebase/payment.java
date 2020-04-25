package com.example.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class payment extends AppCompatActivity{

    EditText ettn;
    TextView name,id,head;
    EditText amount;
    Button paybtn;


    final int UPI_PAYMENT=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initializemethod();

        /*name.setText("VRSEC Printer");
        id.setText("chharithaponduru@okicici");*/


        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(amount.getText().toString().trim())){
                    Toast.makeText(payment.this,"Enter AMOUNT!",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(ettn.getText().toString().trim())){
                    Toast.makeText(payment.this,"Enter TOKEN NUMBER!",Toast.LENGTH_SHORT).show();
                }
                else{
                   pay(amount.getText().toString(),ettn.getText().toString(),name.getText().toString(),id.getText().toString());
                }
            }
        });


    }

    void pay( String amount, String ettn,String name,String id ){
       // String GOOGLE_PAY_PACKAGE_NAME="com.google.android.apps.nbu.paisa.user";
        //int GOOGLE_PAY_REQUEST_CODE=123;
        Uri uri= Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", id)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", ettn)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upipay= new Intent(Intent.ACTION_VIEW);
        upipay.setData(uri);
        Intent chooser=Intent.createChooser(upipay,"Pay with");
        if(null!=chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        }
        else{
            Toast.makeText(this,"No Upi App Found!",Toast.LENGTH_SHORT).show();
        }
    }

        private void initializemethod() {
            paybtn=(Button)findViewById(R.id.paybtn);
            ettn=(EditText)findViewById(R.id.ettn);
            name=(TextView)findViewById(R.id.name);
            name.setTextColor(0xff444444);
            id=(TextView)findViewById(R.id.id);
            id.setTextColor(0xff444444);
            amount=(EditText) findViewById(R.id.amount);
            head=(TextView)findViewById(R.id.head);
            head.setTextColor(0xff000000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode || resultCode == 11)) {
                    if (data != null) {
                        String txt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult" + txt);
                        ArrayList<String> dataLst = new ArrayList<>();
                        dataLst.add("Nothing");
                        upipaymentdataoperation(dataLst);
                    }

                    else{
                        Log.d("UPI","onActivityResult:"+"Return data is null");
                        ArrayList<String> dataLst = new ArrayList<>();
                        dataLst.add("Nothing");
                        upipaymentdataoperation(dataLst);

                    }
                }
                else{
                    Log.d("UPI","onActivityResult:"+"Return data is null");
                    ArrayList<String> dataLst = new ArrayList<>();
                    dataLst.add("Nothing");
                    upipaymentdataoperation(dataLst);
                }
                break;
        }
    }

    private void upipaymentdataoperation(ArrayList<String> data) {
        if (isConnectionAvailable(payment.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upipaymentoperation:" + str);
            String paymentCancel = " ";
            if (str == null) str = "discard";
            String status = "";
            String approvalref = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("approval Ref".toLowerCase()) || equalStr[0].toLowerCase().equals("txtRef".toLowerCase())) {
                        approvalref = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user";
                }
            }
                    if (status.equals("success")) {
                        Toast.makeText(this, "Transaction Successful!", Toast.LENGTH_SHORT).show();
                        Log.d("UPI", "responsestr:" + approvalref);
                    } else if ("Payment cancel by user".equals(paymentCancel)) {
                        Toast.makeText(this, "Payment cancel by user", Toast.LENGTH_SHORT).show();
                    } /*else {
                        Toast.makeText(this, "Transaction Failed", Toast.LENGTH_SHORT).show();

                    }*/
                }
                else{
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }


            private boolean isConnectionAvailable (Context context){
                ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
                if(connectivityManager!=null){
                    NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
                    if(networkInfo!=null && networkInfo.isConnected()&&networkInfo.isConnectedOrConnecting()
                    && networkInfo.isAvailable()){
                        return true;
                    }
                }
                return false;

            }
        }
