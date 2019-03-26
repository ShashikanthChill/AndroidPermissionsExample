package com.example.permissionsexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static boolean FINE_LOCATION_FLAG = false;

    static boolean alreadyAsked = false;

    Button btnCheck, btnAsk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCheck = findViewById(R.id.btnCheck);
        btnAsk = findViewById(R.id.btnAsk);

        btnCheck.setOnClickListener(this);
        btnAsk.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCheck: {
                checkPermissions();
            }
            break;

            case R.id.btnAsk: {
                if (!permissionsGranted())
                    askPermissions();
                else
                    Toast.makeText(getApplicationContext(), "Permissions have been already granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean permissionsGranted() {
        return FINE_LOCATION_FLAG;
    }

    private void askPermissions() {
        System.out.println("Inside askPermissions()");
        System.out.println("Already Asked? " + alreadyAsked);

        if (!alreadyAsked) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            alreadyAsked = true;
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            System.out.println("Inside if");
            Toast.makeText(getApplicationContext(), "Need permissions to access fine location", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            Toast.makeText(getApplicationContext(),"Perms forever denied",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
            intent.setData(uri);
            this.startActivity(intent);
        }

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            FINE_LOCATION_FLAG = false;
        } else {
            FINE_LOCATION_FLAG = true;
        }


        Toast.makeText(getApplicationContext(), FINE_LOCATION_FLAG ? "FINE_LOCATION_FLAG permission: Granted" : "FINE_LOCATION_FLAG permission: Denied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.d("ACCESS_FINE_LOCATION", "Permission granted");
                    } else {
                        Log.d("ACCESS_FINE_LOCATION", "Permission denied");
                    }
                } else {
                    Log.e("Grant Results length", String.valueOf(grantResults.length));
                }
                checkPermissions();
            }
            break;
        }
    }
}
