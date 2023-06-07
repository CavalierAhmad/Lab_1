package algonquin.cst2335.alja0062;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private static String tag = "SecondActivity";

    // Declaring views
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        textview = findViewById(R.id.textView);
        textview.setText("Welcome back " + emailAddress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(tag,"In onStart() - The application is now visible on screen.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(tag,"In onResume() - The application is now responding to user input");
        Log.w(tag,"The application is now up and running on the screen. When another application comes up on screen and the" +
                "application disappears beneath, there are other functions that get called:");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(tag,"The application no longer responds to user input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(tag,"The application is no longer visible.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(tag,"Any memory used by the application is freed.");
    }
}