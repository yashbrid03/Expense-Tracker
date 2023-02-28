package com.example.expensetracker;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class MainActivity extends AppCompatActivity  {

    // Create the object of TextView and PieChart class
    PieChart pieChart;
    TextView income,  expense, name;
    Button month, overall;
    ImageView dp;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase db;
    DatabaseReference reference, userref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link those objects with their respective
// id's that we have given in .XML file
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("users");
        userref = reference.child(firebaseUser.getUid());
        name = (TextView) findViewById(R.id.textView8);
        dp = (ImageView) findViewById(R.id.imageView3);

        //Greet User dynamic name
        name.setText(String.valueOf(firebaseUser.getDisplayName()));
        //setimage
        setImage();
        setData();


        pieChart = findViewById(R.id.piechart);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            // do stuff
            switch (item.getItemId()){
                case R.id.nav_account:
                    startActivity(new Intent(getApplicationContext(),Account.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.nav_home:
                    return true;

                case R.id.nav_add:
                    startActivity(new Intent(getApplicationContext(),ManageExpense.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return true;
        });

        income = (TextView)findViewById(R.id.incomeData);
        expense = (TextView)findViewById(R.id.expenseData);
        setChartData();
        month = (Button)findViewById(R.id.button2);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMonthlyData();
            }
        });
        overall = (Button)findViewById(R.id.button3);
        overall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOverallData();
            }
        });
        setMonthlyData();

    }

    private void setData() {
//
//        DatabaseReference ref =  userref.child("MonthlyItemList").child(String.valueOf(java.time.LocalDate.now()))
    }

    private void setImage() {
        DatabaseReference imgref = userref.child("profilepic");
        imgref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String link = snapshot.getValue(String.class);
                        Picasso.get().load(link).into(dp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void setMonthlyData() {
        income.setText("₹ 2,00,000");
        expense.setText("₹ 41,580");
        int nextColor = Color.parseColor("#FCC2FC");
        month.setBackgroundTintList(ColorStateList.valueOf(nextColor));
        overall.setBackgroundTintList(null);
//        overall.setTextColor(Color.BLACK);
//        month.setTextColor(Color.WHITE);
    }

    private void setOverallData(){
        income.setText("₹ 20,00,000");
        expense.setText("₹ 1,15,937");
        int nextColor = Color.parseColor("#FCC2FC");
        overall.setBackgroundTintList(ColorStateList.valueOf(nextColor));
        month.setBackgroundTintList(null);
//        month.setTextColor(Color.BLACK);
//        overall.setTextColor(Color.WHITE);
    }

    private void setChartData() {
        pieChart.addPieSlice(
                new PieModel(
                        "R",
                        10,
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Python",
                        20,
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "C++",
                        30,
                        Color.parseColor("#EF5350")));
        pieChart.addPieSlice(
                new PieModel(
                        "Java",
                        40,
                        Color.parseColor("#29B6F6")));

        // To animate the pie chart
        pieChart.startAnimation();
    }

    @Override
    public void onBackPressed() {
        Log.i("inside", "back pressed");
        overridePendingTransition(0,0);
        finishAffinity();
    }

}