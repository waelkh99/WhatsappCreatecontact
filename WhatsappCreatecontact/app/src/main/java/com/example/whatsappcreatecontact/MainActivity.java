package com.example.whatsappcreatecontact;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    EditText number;
    Button create, wallpaper;
    String formattedNumber;
    RadioButton current, coming, busy;
    CountryCodePicker country_picker;
    LocationManager locationManager;
    Double lat,lng;
    String locationLink;
    int permissionCode=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current = (RadioButton) findViewById(R.id.currentRB);
        coming = (RadioButton) findViewById(R.id.comingRB);
        busy = (RadioButton) findViewById(R.id.busyRB);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busy.setChecked(false);
                coming.setChecked(false);
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                   requestPermission();
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        lat=location.getLatitude();
                        lng=location.getLongitude();

                         locationLink="https://www.google.com/maps/@"+lat+","+lng;

                    }
                });

            }
        });

        current.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                current.setChecked(false);
                return true;
            }
        });
        coming.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                coming.setChecked(false);
                return true;
            }
        });
        busy.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                busy.setChecked(false);
                return true;
            }
        });
        coming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busy.setChecked(false);
                current.setChecked(false);
            }
        });
        busy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current.setChecked(false);
                coming.setChecked(false);
            }
        });

        number=(EditText)findViewById(R.id.contactET);
        create=(Button)findViewById(R.id.createBtn);
        country_picker=(CountryCodePicker)findViewById(R.id.country_picker);
        wallpaper=(Button)findViewById(R.id.wallpaperBtn);

        wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Wallpapers.class));
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = number.getText().toString();

                if (input.length() > 15) {
                    Toast.makeText(MainActivity.this, "Wrong Format (too long)", Toast.LENGTH_SHORT).show();
                } else {

                    for (int i = 0; i < input.length(); i++) {
                        if (input.charAt(i) == '+') {
                            input = charRemoveAt(input, i);
                        }

                    


                    }
                    formattedNumber = country_picker.getSelectedCountryCode() + input;

                    if (!current.isChecked()&&!coming.isChecked()&&!busy.isChecked()){
                    createContact(formattedNumber);}

                    else if (current.isChecked()){
                        sendMessage(formattedNumber,locationLink);
                    }
                    else if (coming.isChecked()){
                        sendMessage(formattedNumber,"I'm%20coming");
                    }

                    else if (busy.isChecked()){
                        sendMessage(formattedNumber,"I'm%20busy");
                    }
                }
            }
        });
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("this permission is needed to access your location")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},permissionCode);

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                }
            }).create().show();

        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},permissionCode);
        }
    }

    public static String charRemoveAt(String str, int p) {
        return str.substring(0, p) + str.substring(p + 1);
    }

    private void createContact(String formattedNumber) {
        String link = "https://wa.me/" + formattedNumber;

        Uri uri=Uri.parse(link);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
    private  void sendMessage(String formattedNumber,String message){
        String link = "https://wa.me/" + formattedNumber+"?text="+message;

        Uri uri=Uri.parse(link);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        if (requestCode==permissionCode){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
