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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private TextView banner;
    private Button registerbutton;
    private EditText registerfullname,registerage,registeremailaddress,registerpassword;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mauth= FirebaseAuth.getInstance();

        banner=(TextView) findViewById(R.id.chatapp2);
        banner.setOnClickListener(this);

        registerbutton = (Button)findViewById(R.id.registerbutton);
        registerbutton.setOnClickListener(this);

        registerage=(EditText) findViewById(R.id.registerage);
        registerfullname =(EditText)findViewById(R.id.registerfullname);
        registeremailaddress =(EditText) findViewById(R.id.registeremailaddress);
        registerpassword=(EditText) findViewById(R.id.registerpassword);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case(R.id.chatapp2):
                startActivity(new Intent(this,MainActivity.class));
                break;

            case(R.id.registerbutton):
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = registeremailaddress.getText().toString().trim();
        String fullname = registerfullname.getText().toString().trim();
        String age =registerage.getText().toString().trim();
        String password = registerpassword.getText().toString().trim();


        if(fullname.isEmpty()){
            registerfullname.setError("NAME IS COMPULSORY");
            registerfullname.requestFocus();

        }
        if(age.isEmpty()){
            registerage.setError("AGE IS COMPULSARY");
            registerage.requestFocus();
        }
        if(email.isEmpty()){
            registeremailaddress.setError("Please fill email");
            registeremailaddress.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registeremailaddress.setError("PLEASE PUT THE VALID EMAIL ADDRESS");
            registeremailaddress.requestFocus();
        }
        if(password.isEmpty()){
            registerpassword.setError("PASSWORD IS COMPULSORY");
            registerpassword.requestFocus();
        }
        //firebase needs password greater then 6 length
        if(password.length()<6){
            registerpassword.setError("Password Is small ");
            registerpassword.requestFocus();
        }else {

            mauth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //  CREATE A OBJECT OF USER TO PROVIDE FIREBASE AN OBJECT
                                Usersinfo user = new Usersinfo(fullname, age, email);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(Register.this, "Registered Sucessfully", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(Register.this, "Unsucessful Registration", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }
}