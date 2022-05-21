package com.diplabs.securecodeverifier;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.controls.Audio;
import com.otaliastudios.cameraview.controls.Engine;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.size.Size;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    BarcodeScannerOptions options =
            new BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                            Barcode.FORMAT_QR_CODE
                    )
                    .build();

    CameraView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initalizeCamera();
        camListener();



    }

    private void camListener() {

        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            @WorkerThread
            public void process(@NonNull Frame frame) {
                long time = frame.getTime();
                Size size = frame.getSize();
                int format = frame.getFormat();
                int userRotation = frame.getRotationToUser();
                int viewRotation = frame.getRotationToView();
                if (frame.getDataClass() == byte[].class) {
                    byte[] data = frame.getData();
                    // Process byte array...


                    InputImage image = InputImage.fromByteArray(data,
                            /* image width */ size.getWidth(),
                            /* image height */ size.getHeight(),
                            userRotation,
                            InputImage.IMAGE_FORMAT_NV21 // or IMAGE_FORMAT_YV12
                    );

                    BarcodeScanner scanner = BarcodeScanning.getClient(options);


                    Task<List<Barcode>> result = scanner.process(image)
                            .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                                @Override
                                public void onSuccess(List<Barcode> barcodes) {



                                    for (Barcode barcode: barcodes) {
                                        Rect bounds = barcode.getBoundingBox();
                                        Point[] corners = barcode.getCornerPoints();

                                        String rawValue = barcode.getRawValue();

                                        int valueType = barcode.getValueType();
                                        // See API reference for complete list of supported types
                                        switch (valueType) {
                                            case Barcode.TYPE_TEXT:
                                                String ssid = barcode.getRawValue();

//                                                Intent returnIntent = new Intent();
//                                                returnIntent.putExtra("result", ssid);
//                                                setResult(Activity.RESULT_OK, returnIntent);
//                                                finish();

                                                    resultShow(ssid);
                                                break;
                                            default:
                                                break;
                                        }
                                    }



                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    // ...
                                }
                            });



//



                } else if (frame.getDataClass() == Image.class) {
                    Image data = frame.getData();
                    // Process android.media.Image...
                }
            }
        });
    }

    public void resultShow(String string){
        cameraView.close();
        Intent intent = new Intent(MainActivity.this, Result.class);
        intent.putExtra("data", string);
        startActivity(intent);


    }

    public void initalizeCamera() {
        cameraView = findViewById(R.id.camera);
        cameraView.setLifecycleOwner(this);
        cameraView.setEngine(Engine.CAMERA2);
        cameraView.setAudio(Audio.OFF);
        cameraView.setRequestPermissions(true);

    }


    @Override
    protected void onStart() {
        super.onStart();
        cameraView.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.open();
    }

    @Override
    protected void onPause() {

        super.onPause();
        cameraView.close();

    }


    @Override
    protected void onStop() {

        super.onStop();
        cameraView.close();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        cameraView.destroy();
    }




}