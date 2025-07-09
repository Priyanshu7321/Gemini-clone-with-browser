package com.example.gemini;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class mainBrowserActivity extends AppCompatActivity {

    WebView webView;
    EditText searchEditText;
    ImageButton homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_browser);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        webView = findViewById(R.id.webview);
        searchEditText=findViewById(R.id.searchEditText);
        homeButton=findViewById(R.id.home);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.loadUrl(request.getUrl().toString());
                return true;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // Start download
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimetype);
                request.addRequestHeader("User-Agent", userAgent);
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.setDescription("Downloading file...");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    registerReceiver(new DownloadReceiver(), filter, Context.RECEIVER_NOT_EXPORTED);
                }else{
                    registerReceiver(new DownloadReceiver(), filter);
                }
                Toast.makeText(mainBrowserActivity.this, "Downloading file...", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.downloadButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainBrowserActivity.this,DownloadProgressActivity.class));
            }
        });
        Intent intent=getIntent();
        String browsContent=intent.getStringExtra("searchContent");
        if(browsContent!=null){
            performSearch(browsContent);
        }
        homeButton.setOnClickListener(v -> startActivity(new Intent(mainBrowserActivity.this,BrowserActivity.class)));
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null || event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getAction() == KeyEvent.ACTION_DOWN)) {
                Toast.makeText(mainBrowserActivity.this,"working",3000).show();
                Log.d("EditorActionListener", "Performing search");
                searchEditText.clearFocus();
                performSearch(searchEditText.getText().toString());

                return true;
            }
            return false;
        });


    }
    private boolean isValidUrl(String url) {
        return Patterns.WEB_URL.matcher(url).matches();
    }
    private void performSearch(String st) {
        String searchTerm = st;
        if (!TextUtils.isEmpty(searchTerm)) {
            if (isValidUrl(searchTerm)) {
                webView.loadUrl(searchTerm);
                searchEditText.setText(searchTerm);
                searchEditText.clearFocus();
            } else {
                try {
                    String searchUrl = "https://www.google.com/search?q=" + URLEncoder.encode(searchTerm, "UTF-8");
                    searchEditText.setText(searchUrl);
                    searchEditText.clearFocus();
                    webView.loadUrl(searchUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed(); // This will close the activity
        }
    }
}