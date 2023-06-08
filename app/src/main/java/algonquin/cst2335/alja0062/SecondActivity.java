package algonquin.cst2335.alja0062;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private static String tag = "SecondActivity";

    // Declaring views
    private TextView textview;
    private EditText edittext;
    private Button callBtn;
    private ImageView imageView;
    private Button pictureBtn;

    // for permission
    private Intent cameraIntent; // Declare cameraIntent as a member variable

    // Declare the ActivityResultLauncher as a member variable
    private ActivityResultLauncher<Intent> cameraResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Boilerplate
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_second);

        // Email address sent from first page
            Intent fromPrevious = getIntent();
            String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        // Find views by ID
            textview = findViewById(R.id.textView);
            edittext = findViewById(R.id.editTextPhone);
            callBtn = findViewById(R.id.callBtn);
            imageView = findViewById(R.id.imageView);
            pictureBtn = findViewById(R.id.pictureBtn);

        // Storing string entered in first page to display in second page
            textview.setText("Welcome back " + emailAddress);

        // Register the ActivityResultLauncher outside the click listener
        cameraResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            imageView.setImageBitmap(thumbnail);
                            FileOutputStream fOut = null;
                            try {
                                fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            }
                            catch (FileNotFoundException e)
                            {
                                e.printStackTrace();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        );

        // EVENTS
            // Send number to phone
                callBtn.setOnClickListener( clk-> {
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    String phoneNumber = edittext.getText().toString();
                    call.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(call);
                });
            // Change picture
                pictureBtn.setOnClickListener( clk-> {
                    cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                            // Launch the camera activity using the ActivityResultLauncher
                            cameraResultLauncher.launch(cameraIntent);
                        else
                            requestPermissions(new String[] {Manifest.permission.CAMERA}, 20);
                    }
                });

        File file = new File(getFilesDir(), "Picture.png");
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(theImage);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the camera intent
                startActivity(cameraIntent);
            } else {
                // Permission denied, show a message or take alternative action
            }
        }
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