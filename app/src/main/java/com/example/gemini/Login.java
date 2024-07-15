package com.example.gemini;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextInputEditText email,password;
    MaterialButton signin;
    TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signin=findViewById(R.id.signin);
        signup=findViewById(R.id.signup);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAll()){
                    Intent intent =new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(Login.this,"fill all the details",3000).show();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Login.this,startPage.class);
                startActivity(intent);
            }
        });
    }
    public boolean checkAll(){
        if(!email.getText().toString().equals("") || !password.getText().toString().equals("") ){
            return false;
        }
        return true;
    }


}