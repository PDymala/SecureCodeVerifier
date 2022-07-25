package com.diplabs.securecodeverifier;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Result extends AppCompatActivity {

    TextView textViewTokenValid;
    Button buttonExit;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String message = intent.getStringExtra("data");


        textViewTokenValid = findViewById(R.id.textViewTokenValid);
        buttonExit = findViewById(R.id.button);
        webView = findViewById(R.id.webciew);
        checkCode(message);



    }

    public void checkCode(String message){


        Thread thread = new Thread(new Runnable() {
            private static final String TAG = "TEST";

            @Override
            public void run() {

                try {

                    JSONObject j = new JSONObject(message);

                    if (j.has("fc") && j.has("data") && j.has("token")){

                        Encryption encryption = new Encryption();
                        String encr = encryption.get_SHA_512_SecurePassword(j.get("data").toString());
                        if (j.get("token").toString().equals(encr) ) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    webView.loadData(

                                            " <html><body> <p style=\"word-wrap: break-word;\">" + String.valueOf(j) + "</p></body></html>"

                                            , "text/html; charset=UTF-8", null);
                                }
                            });

                            tokenValid();
                        } else{
                            tokenInvalid();
                        }
                    } else if (j.has("sd") && j.has("id") && j.has("key")){
                        URL url = new URL("https://securecode.diplabs.app/securedata/getdata?id="+j.get("id")); //Enter URL here
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                        conn.setRequestProperty("Accept","application/json");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);

                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("id", j.get("id"));

                        Log.i("JSON", jsonParam.toString());
                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                        os.writeBytes(jsonParam.toString());


                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        Log.i("MSG2" , sb.toString());


//                       decryption
                        Encryption encryption = new Encryption();

                        String encr = encryption.decrypt(sb.toString(),(String)j.get("key"));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webView.loadData(



                                        " <html><body> <p style=\"word-wrap: break-word;\">" + sb.toString() + " <BR> " +
                                                 encr + "</p></body></html>"

                                        , "text/html; charset=UTF-8", null);
                            }
                        });

                        tokenValid();





                    } else {
                        tokenInvalid();
                    }


//
//
//                    Gson g = new Gson();
//
//                    Code p = g.fromJson(message, Code.class);
//                    //looper handler etc.... whatever
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            textViewToken.setText(p.getToken());
//                            textViewIssuer.setText(p.getClient_id());
//                            textViewData.setText(p.getData());
//                        }
//                    });
//
//
//
//
//                    try {
//                        URL url = new URL("https://us-central1-diplabs.cloudfunctions.net/check_token"); //Enter URL here
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setRequestMethod("POST");
//                        conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//                        conn.setRequestProperty("Accept","application/json");
//                        conn.setDoOutput(true);
//                        conn.setDoInput(true);
//
//                        JSONObject jsonParam = new JSONObject();
//                        jsonParam.put("client_id", p.getClient_id());
//                        jsonParam.put("data", p.getData());
//                        jsonParam.put("token", p.getToken());
//
//                        Log.i("JSON", jsonParam.toString());
//                        DataOutputStream os = new DataOutputStream(conn.getOutputStream());
//                        //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
//                        os.writeBytes(jsonParam.toString());
//
//
//
//
//                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                        StringBuilder sb = new StringBuilder();
//                        String line;
//                        while ((line = br.readLine()) != null) {
//                            sb.append(line+"\n");
//                        }
//                        br.close();
//                        Log.i("MSG2" , sb.toString());
//
//
//
//                        Response response = new Gson().fromJson(sb.toString(), Response.class);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (response.isValid()){
//                                    textViewTokenValid.setText("TOKEN VALID");
//                                    textViewTokenValid.setBackgroundColor(Color.GREEN);
//
//                                } else if (!response.isValid()){
//                                    textViewTokenValid.setText("TOKEN INVALID");
//                                    textViewTokenValid.setBackgroundColor(Color.RED);
//
//                                } else {
//                                    textViewTokenValid.setText("TOKEN INVALID OR ERROR");
//                                    textViewTokenValid.setBackgroundColor(Color.YELLOW);
//
//                                }
//                            }
//                        });
//
//
//
//
//                        os.flush();
//                        os.close();
//
//
//                        conn.disconnect();
//
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    } catch (UnknownHostException e) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(Result.this, "No internet connection", Toast.LENGTH_SHORT).show();
//                                textViewTokenValid.setText("ERROR");
//                                textViewTokenValid.setBackgroundColor(Color.YELLOW);
//
//                            }
//                        });
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//


                } catch ( Exception e){
                    //CRAP!
                    tokenInvalid();
                }

            }
        });
        thread.start();



    }
    public void tokenInvalid(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Result.this, "Code invalid", Toast.LENGTH_SHORT).show();
                textViewTokenValid.setText("CODE INVALID");
                textViewTokenValid.setBackgroundColor(Color.RED);


            }
        });
    }
//TODO ZMIENIC z ml kit na innne? cos kilka razy skanuje...
    public void tokenValid(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Result.this, "Code valid", Toast.LENGTH_SHORT).show();
                textViewTokenValid.setText("CODE VALID");
                textViewTokenValid.setBackgroundColor(Color.GREEN);


            }
        });
    }



    public void exit(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}