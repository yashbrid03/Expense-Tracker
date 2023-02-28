package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;


public class Signup extends AppCompatActivity {
    public void goToLogin (View view){
        Intent intent = new Intent (this, Login.class);
        startActivity(intent);
    }

    Button Signup;
    EditText email, password, name;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase db;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        name = findViewById(R.id.editTextTextPersonName);
        firebaseAuth = FirebaseAuth.getInstance();
        LocalDate currentdate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentdate = LocalDate.now();
        }
        System.out.println(currentdate);
        Month month = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            month = currentdate.getMonth();
        }
        final Month month1 = month;
        Log.d("date", "onClick: "+month.toString());
        Year year = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            year = Year.of(currentdate.getYear());
        }
        final Year year1 = year;
        Log.d("date", "onClick: "+year+""+month);
        Signup = (Button) findViewById(R.id.signup);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || name.getText().toString().isEmpty())
                {
                    Toast.makeText(Signup.this,"Please fill all details correctly",Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener((task)->{
                        if(task.isSuccessful()){
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(String.valueOf(name.getText()))
                                    .build();
                            Log.d("date", "onClick: before date");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            db = FirebaseDatabase.getInstance();
                            reference = db.getReference("users");
                            MonthlyDataOverall mdo = new MonthlyDataOverall(0,0);
                            Overall overall = new Overall(0,0);
                            DatabaseReference userref = reference.child((user.getUid()));
                            userref.child("MonthlyData").child(year1.toString()+""+month1.toString()).setValue(mdo);
                            userref.child("Overall").setValue(overall);
                            userref.child("profilepic").setValue("https://avatars.githubusercontent.com/u/65955929?v=4");
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener((task2)-> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Signup.this,"Signed up Successfully!!",Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finishAffinity();
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(Signup.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Log.i("inside", "back pressed");
        startActivity(new Intent(getApplicationContext(),Login.class));
        overridePendingTransition(0,0);
        finishAffinity();
    }
}