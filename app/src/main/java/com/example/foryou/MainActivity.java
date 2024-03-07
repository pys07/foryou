package com.example.foryou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foryou.Utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper dh;
    Cursor cursor;
    EditText email, pass;
    Button btnLogin,btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin=findViewById(R.id.btnLogin);
        btnRegister=findViewById(R.id.btnRegister);
        email=findViewById(R.id.etEmail);
        pass=findViewById(R.id.etPassword);

        dh = new DatabaseHelper(this);  // Initialize DatabaseHelper


        btnLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String eid = email.getText().toString();
                String pswrd = pass.getText().toString();


                UserSession.setUserEmail(eid);
                cursor = dh.getData(eid); // Retrieve user data based on email

                if (cursor != null && cursor.moveToFirst()) {
                    String emailid = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EMAIL));
                    String password = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_PASSWORD));

                    if (eid.equals(emailid) && pswrd.equals(password)){
                        Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    }
                    Intent i = new Intent(getApplicationContext(), EmergencyActivity.class);
                    startActivity(i);
                    cursor.close();
                } else {
                    Toast.makeText(getApplicationContext(),"User not found. Please Register.",Toast.LENGTH_SHORT).show();

                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}