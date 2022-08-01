package com.diplabs.securecodeverifier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.Base64;


public class ResultImage extends AppCompatActivity {
    TextView textViewTokenValid;
    Button buttonExit;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_image);
        Intent intent = getIntent();
        String message = intent.getStringExtra("data");

        initUI();
        analize(message);
    }

    private void initUI() {
        imageView = findViewById(R.id.imageView2);
        buttonExit = findViewById(R.id.buttonScanAgain);
        textViewTokenValid = findViewById(R.id.textViewTokenValid2);

    }

    public void analize(String message)  {

        try {

            //https://stackoverflow.com/questions/57116335/environment-getexternalstoragedirectory-deprecated-in-api-level-29-java

            File file = new File(this.getExternalCacheDir(), "ByteImageTempFile.jpg");
            byte[] decodedBytes = Base64.getDecoder().decode(message);
            org.apache.commons.io.FileUtils.writeByteArrayToFile(file, decodedBytes);


            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());


            imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 350, 350 * bitmap.getHeight() / bitmap.getWidth(), false));
            tokenValid();

//            file.delete();

        } catch (IllegalArgumentException | IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            tokenInvalid();
        }


    }
    public void tokenInvalid(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ResultImage.this, "Code invalid", Toast.LENGTH_SHORT).show();
                textViewTokenValid.setText("CODE INVALID");
                textViewTokenValid.setBackgroundColor(Color.RED);


            }
        });
    }
    public void tokenValid(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ResultImage.this, "Code valid", Toast.LENGTH_SHORT).show();
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