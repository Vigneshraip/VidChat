package com.example.vidchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vidchat.R;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class DashboardActivity extends AppCompatActivity {
    private EditText secretCodeBox;
    private Button joinBtn;
    private Button shareBtn;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        secretCodeBox = findViewById(R.id.codeBox);
        joinBtn = findViewById(R.id.joinBtn);
        shareBtn = findViewById(R.id.shareBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String secretCode = secretCodeBox.getText().toString();
                openJitsiMeetActivity(secretCode);
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String secretCode = secretCodeBox.getText().toString();
                shareText(secretCode);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void openJitsiMeetActivity(String secretCode) {
        // Initialize JitsiMeetConferenceOptions and launch the Jitsi Meet activity
        URL serverURL;
        try {
            serverURL = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                .setRoom(secretCode)
                .build();
        JitsiMeetActivity.launch(this, options);
    }

    private void shareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        Intent chooserIntent = Intent.createChooser(shareIntent, "Share via");

        // Verify that at least one app is available to handle the intent
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooserIntent);
        } else {
            Toast.makeText(this, "No app available to handle the share action.", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        // Clear any user session or data if needed

        // Return to the LoginActivity
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
