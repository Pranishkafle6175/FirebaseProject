package com.example.firebase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private EditText mainemail,mainpassword;
    private Button loginbutton;
    TextView registertextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registertextview =(TextView) findViewById(R.id.register);
        registertextview.setOnClickListener(this);

        mainemail=(EditText)findViewById(R.id.email);
        mainpassword=(EditText)findViewById(R.id.password);
        loginbutton=(Button)findViewById(R.id.button);
        loginbutton.setOnClickListener(this);

        // ...
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this,Register.class));
                break;

            case R.id.button:
                Loginuser();
                break;
        }
    }

    private void Loginuser() {

        String email = mainemail.getText().toString().trim();
        String password = mainpassword.getText().toString().trim();

        if (email.isEmpty()) {
            mainemail.setError("Email is Compulsary");
            mainemail.requestFocus();

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mainemail.setError("Please Insert Valid Email");
            mainemail.requestFocus();
        }
        if (password.isEmpty()) {
            mainpassword.setError("Password is Compulsary");
            mainpassword.requestFocus();

        } else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        // if this if loops runs that means the user has signed in with the email and password sucessfully
                        //so we can verify if thr current user that is loged has verifyed their account or not

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.isEmailVerified()) {

                            startActivity(new Intent(MainActivity.this, MainPage.class));
                            Toast.makeText(MainActivity.this, "Logged In sucessfully", Toast.LENGTH_LONG).show();
                        } else {
                            user.sendEmailVerification();
                            Toast.makeText(MainActivity.this, "PLEASE CHECK YOUR EMAIL", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
    }
}