package com.knoweverything.remoteconfig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private String text = " ";
    public String text1;
    private final Object LOCK = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        final FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        synchronized (LOCK) {
            mFirebaseRemoteConfig.setDefaultsAsync(R.xml.keys);
        }
        synchronized (LOCK){
            mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Fetch success", Toast.LENGTH_SHORT).show();
                        text = FirebaseRemoteConfig.getInstance().getString("main");
                        textView.setText(text);
                    } else {
                        Toast.makeText(MainActivity.this, "Fetch failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}

