package com.example.gemini;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DownloadProgressActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView progressText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_download_progress);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        String status = getIntent().getStringExtra("status");
        int progress = getIntent().getIntExtra("progress", 0);

        if ("completed".equals(status)) {
            progressBar.setVisibility(View.GONE);
            progressText.setText("Download Completed");
        } else if ("failed".equals(status)) {
            progressBar.setVisibility(View.GONE);
            progressText.setText("Download Failed");
        } else if ("downloading".equals(status)) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(progress);
            progressText.setText("Downloading: " + progress + "%");
        }
    }
}