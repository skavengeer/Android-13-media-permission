package com.dino.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    String TAG = "Permission";
    Button check;
    boolean is_storage_image_permissions = false;
    boolean is_storage_video_permissions = false;
    boolean is_storage_audio_permissions = false;

    String[] required_permissions   = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check = (Button) findViewById(R.id.button);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!allPermissionResultCheck()){
                     requestPermissionsStorageImages();
                } else {
                    Toast.makeText(MainActivity.this, "OK", Toast.LENGTH_SHORT ).show();
                }
            }
        });
    }

    public boolean allPermissionResultCheck()     {
        return  is_storage_image_permissions
                    && is_storage_video_permissions
                        && is_storage_audio_permissions;
    }

    public void requestPermissionsStorageImages() {
        if(ContextCompat.checkSelfPermission (MainActivity.this,required_permissions[0]) == PackageManager.PERMISSION_GRANTED)    {
            Log.d(TAG,required_permissions[0] + "OK");
            is_storage_image_permissions = true;

            if (!allPermissionResultCheck()) {
                requestPermissionsStorageVideo();
            }
        } else {
            request_permission_launcher_storage.launch(required_permissions[0]);
        }
    }

    private ActivityResultLauncher<String> request_permission_launcher_storage =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if (isGranted){
                    Log.d(TAG,required_permissions[0] + "OK");
                    is_storage_image_permissions = true;
                }else {
                    Log.d(TAG,required_permissions[0] + " Not OK");
                    is_storage_image_permissions = false;
                }

                if (!allPermissionResultCheck()) {
                    requestPermissionsStorageAudio();
                }
            });



    public void requestPermissionsStorageVideo() {
        if(ContextCompat.checkSelfPermission (MainActivity.this,required_permissions[1]) == PackageManager.PERMISSION_GRANTED)    {
            Log.d(TAG,required_permissions[1] + "OK");
            is_storage_video_permissions = true;

            if (!allPermissionResultCheck()) {
                requestPermissionsStorageAudio();
            }
        } else {
            request_permission_launcher_video.launch(required_permissions[0]);
        }
    }

    private ActivityResultLauncher<String> request_permission_launcher_video =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                if (isGranted){
                    Log.d(TAG,required_permissions[1] + "OK");
                    is_storage_video_permissions = true;
                }else {
                    Log.d(TAG,required_permissions[1] + " Not OK");
                    is_storage_video_permissions = false;
                }

            });

    public void requestPermissionsStorageAudio() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, required_permissions[2]) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, required_permissions[2] + "OK");
            is_storage_audio_permissions = true;

            if (!allPermissionResultCheck()) {
                requestPermissionsStorageAudio();
            }
        } else {
            request_permission_launcher_audio.launch(required_permissions[2]);
        }
    }

    private ActivityResultLauncher<String> request_permission_launcher_audio =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted->{
                    if (isGranted){
                        Log.d(TAG,required_permissions[2] + "OK");
                        is_storage_audio_permissions = true;
                    }else {
                        Log.d(TAG,required_permissions[2] + " Not OK");
                        is_storage_audio_permissions = false;
                        sendToSettingDialog();
                    }

                });

    public void sendToSettingDialog() {
        new  AlertDialog.Builder(  MainActivity.this )
                .setTitle("Alert for Permition")
                .setPositiveButton("Setings", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent rInt = new Intent();
                        rInt.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",getPackageName(), null);
                        rInt.setData(uri);
                        startActivity(rInt);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                })
                .show();
    }
}