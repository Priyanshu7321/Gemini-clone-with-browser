package com.example.gemini;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class startPage extends AppCompatActivity {

    TextInputEditText name,email,password,retype;
    TextView login;
    MaterialButton signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        retype=findViewById(R.id.retype);
        signup=findViewById(R.id.signup);
        login=findViewById(R.id.login);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAll()){
                    Intent intent=new Intent(startPage.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(startPage.this,"fill all the details",3000).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(startPage.this, Login.class);
                startActivity(intent);
            }
        });
    }
    public boolean checkAll(){
        if(!name.getText().toString().equals("") || !email.getText().toString().equals("") || !password.getText().toString().equals("") || !retype.getText().toString().equals("")){
            return false;
        }
        return true;
    }
}