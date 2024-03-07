package com.example.foryou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foryou.Utils.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    EditText name, email, pass, add, contacts;
    Button btnR, btnL;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmailID);
        pass = findViewById(R.id.etPass);
        add = findViewById(R.id.etAddress);
        contacts = findViewById(R.id.etContacts);

        btnR = findViewById(R.id.btnR);
        btnL = findViewById(R.id.btnL);

        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dh = new DatabaseHelper(RegisterActivity.this);
                dh.insertData(
                        name.getText().toString(),
                        email.getText().toString(),
                        pass.getText().toString(),
                        add.getText().toString(),
                        contacts.getText().toString());

                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                UserSession.setUserEmail(email.getText().toString());
                 Intent i = new Intent(getApplicationContext(), EmergencyActivity.class);
                 startActivity(i);
            }
        });

        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}
