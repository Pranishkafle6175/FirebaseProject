package com.example.firebase1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainPage extends AppCompatActivity implements View.OnClickListener{

    private Button logoutbutton;

    private DatabaseReference reference;
    private FirebaseUser user;
    private String userid;

    private TextView mainpagename,mainpageemail,mainpageage;
    private String name,email,age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        logoutbutton=(Button) findViewById(R.id.logoutbutton);
        logoutbutton.setOnClickListener(this);

        mainpagename= (TextView)findViewById(R.id.mainpagename);
        mainpageemail=(TextView)findViewById(R.id.mainpageemail);
        mainpageage=(TextView)findViewById(R.id.mainpageage);

        user= FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usersinfo user = snapshot.getValue(Usersinfo.class);
                if(user != null){

                    name=user.username;
                    email = user.useremail;
                    age = user.userage;

                    mainpagename.setText(name);
                    mainpageemail.setText(email);
                    mainpageage.setText(age);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainPage.this, "Some wrong Happened", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.logoutbutton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainPage.this, MainActivity.class));
        }

    }
}