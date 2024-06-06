package com.iverify;

import okhttp3.Request;
import okhttp3.FormBody;
import okhttp3.Response;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import org.json.JSONObject;
import java.io.IOException;
import android.app.Activity;
import okhttp3.OkHttpClient;
import android.os.AsyncTask;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.annotation.SuppressLint;

public class MainActivity extends Activity {

    public Button verifyButton, scanButton;
    public TextView resultTextView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView  = findViewById(R.id.resultTextViewID);
        verifyButton    = findViewById(R.id.verifyButtonID);
        scanButton      = findViewById(R.id.scanButtonID);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Scanner.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        if ( intent != null && intent.hasExtra("result") ){
            String result = intent.getStringExtra("result");
            if (result != null){
                resultTextView.setText(result);
                verifyButton.setVisibility(View.VISIBLE);
                scanButton.setVisibility(View.GONE);
            }
        }

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Verify verify = new Verify();
                verify.execute();
            }
        });


    }


    public class Verify extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String string = resultTextView.getText().toString();

            if ( Utils.isInternetAvailable(getApplicationContext()) ){

                String device_id = Utils.getDeviceID(getApplicationContext());

                OkHttpClient client = new OkHttpClient();
                FormBody requestBody = new FormBody.Builder()
                        .add("encrypted_uuid", string)
                        .add("device_id", device_id)
                        .build();

                Request request = new Request.Builder().url( Constants.VERIFY_URL ).post(requestBody).build();
                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (IOException e) {e.printStackTrace();}

                return "Server error";
            }

            return "Internet not available";
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            String display = "";
            if ( response.equals("Internet not available") || response.equals("Server error")){
                Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_SHORT).show();
                return;
            }

            try{

                response  = new String(Base64.decode(response, Base64.DEFAULT));


                JSONObject jsonObject = new JSONObject(response);
                String message = (String) jsonObject.get("message");
                String details = (String) jsonObject.get("details");
                String name    = (String) jsonObject.get("name");
                String category = (String) jsonObject.get("category");
                String description = (String) jsonObject.get("description");
                String expiry = (String) jsonObject.get("expiry");


                display += message + "\n\n";

                try{
                    String device_id = Utils.getDeviceID(getApplicationContext());
                    String verification_identity = (String) jsonObject.get("verification_identity");
                    if ( device_id.equals(verification_identity)){
                        display += "Verifier:\nYou" + "\n\n";
                    }else{
                        display += "Verifier:\nSomeone else has verified the product, and this is probably counterfeit." + "\n\n";
                    }
                }catch (Exception e){
                    display += "Verifier: You" + "\n\n";
                }

                display += "Verification Time:\n" + details + "\n\n";
                display += "Name:\n" + name + "\n\n";
                display += "Category:\n" + category + "\n\n";
                display += "Description:\n" + description + "\n\n";
                display += "Expiry:\n" + expiry + "\n\n";

            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
            }

            resultTextView.setText(display);
            verifyButton.setVisibility(View.GONE);
            scanButton.setVisibility(View.VISIBLE);
        }
    }



}