package com.example.gemini;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

public class BrowserActivity extends AppCompatActivity {

    List<Article> articleList;
    RecyclerView recyclerView;
    recyclerAdapter adapter;
    EditText navToBowser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browser);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        articleList=new ArrayList<>();
        navToBowser=findViewById(R.id.navToBrowser);
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new recyclerAdapter(BrowserActivity.this,articleList);
        recyclerView.setAdapter(adapter);
        navToBowser.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        (event != null || event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||  event.getAction() == KeyEvent.ACTION_DOWN)) {
                    func(navToBowser.getText().toString());
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.optionBt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        findViewById(R.id.pinterest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func("https://in.pinterest.com/login/");
            }
        });
        findViewById(R.id.instagram).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func("https://www.instagram.com/accounts/login/?hl=en");
            }
        });
        findViewById(R.id.reddit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func("https://www.reddit.com/?rdt=55664");
            }
        });
        findViewById(R.id.linkedin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func("https://in.linkedin.com/");
            }
        });
        fetchNews();
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.menu_item_one) {

                    startActivity(new Intent(BrowserActivity.this, MainActivity.class));
                    return true;
                }else if(item.getItemId()==R.id.menu_item_two){
                    Toast.makeText(BrowserActivity.this, "Menu Item Two Clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }
    public void fetchNews(){
        NewsApiClient newsApiClient=new NewsApiClient("7655c47f70c04dd2be18018eebd98d14");
        newsApiClient.getTopHeadlines(new TopHeadlinesRequest.Builder().language("en").build(), new NewsApiClient.ArticlesResponseCallback() {
            @Override
            public void onSuccess(ArticleResponse response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        articleList.addAll(response.getArticles());
                        adapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("failure","failure");
            }
        });
    }
    public void func(String st){
        Intent intent=new Intent(BrowserActivity.this,mainBrowserActivity.class);
        intent.putExtra("searchContent",st);
        startActivity(intent);
    }
}