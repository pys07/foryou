package com.example.foryou;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.foryou.Utils.DatabaseHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EmergencyActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_SMS_PERMISSION = 2;

    Button btnD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        String email=UserSession.getUserEmail();

        btnD = findViewById(R.id.btnDanger);
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for SMS sending permission
                if (ContextCompat.checkSelfPermission(EmergencyActivity.this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    // Request SMS sending permission
                    ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{android.Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
                    return;
                }

                if (ContextCompat.checkSelfPermission(EmergencyActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation().addOnSuccessListener(EmergencyActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                Geocoder geocoder = new Geocoder(EmergencyActivity.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                    if (addresses != null && addresses.size() > 0) {
                                        Address address = addresses.get(0);
                                        String locationDescription = String.valueOf(address.getAddressLine(0));

                                        DatabaseHelper dbHelper = new DatabaseHelper(EmergencyActivity.this);
                                        List<String> contactsList = dbHelper.getContacts(email);
                                        dbHelper.insertLocation(email, latitude, longitude);


                                        for (String contactNumber : contactsList) {
                                            String message = "Emergency! I need help. My location: " + locationDescription;
                                            sendSMS(contactNumber, message);
                                        }
                                    } else {
                                        Toast.makeText(EmergencyActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(EmergencyActivity.this, "Failed to obtain location description", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(EmergencyActivity.this, "Location not available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }
            }
        });

    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
