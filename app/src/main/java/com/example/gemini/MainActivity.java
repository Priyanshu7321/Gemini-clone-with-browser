package com.example.gemini;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Calendar;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.BoxStoreBuilder;

public class MainActivity extends AppCompatActivity {

    LinearLayoutCompat sv;
    ImageButton flbt,sideBar;
    int firstTime=0;
    boolean check=false;
    GenerativeModel gm;
    GenerativeModelFutures model;
    TextView textView,textView2,tvcv1,tvcv2,tvcv3;
    CardView cardView,cardView2,cv1,cv2,cv3;
    ImageButton imbt1,refresh;
    ScrollView scrollView;
    EditText edt1;
    TextView gt;
    Model obj;
    DrawerLayout drawerLayout;


    Model1 obj1;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> arrayList;
    MySqliteClass dbobj;
    SQLiteDatabase globdb;
    boolean checkNav=true;
    String userChat;
    int id=1;
    int firstmsg=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sv=findViewById(R.id.sv);
        sideBar=findViewById(R.id.sideBar);
        flbt=findViewById(R.id.sendMsg);
        edt1=findViewById(R.id.edt1);
        imbt1=findViewById(R.id.menuButton);
        drawerLayout=findViewById(R.id.drawarLayout);
        cv1=findViewById(R.id.cv1);
        cv2=findViewById(R.id.cv2);
        cv3=findViewById(R.id.cv3);
        tvcv1=findViewById(R.id.tvcv1);
        tvcv2=findViewById(R.id.tvcv2);
        tvcv3=findViewById(R.id.tvcv3);
        scrollView=findViewById(R.id.scrollView);
        refresh=findViewById(R.id.refresh);
        listView=findViewById(R.id.chatList);
        dbobj=new MySqliteClass(this);

        String apiKey="AIzaSyBLrR40_KCNVZokFI-RxsRlCsb-yAJFKxA";
        gm = new GenerativeModel( "gemini-pro", apiKey);
        model = GenerativeModelFutures.from(gm);
        arrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        obj=new Model();
        obj1=new Model1();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set click listener for the sidebar button
        sideBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the drawer when the sidebar button is clicked
                if(checkNav==true) {
                    drawerLayout.openDrawer(GravityCompat.START);
                    checkNav=false;
                }else{
                    drawerLayout.closeDrawer(GravityCompat.START);
                    checkNav=true;
                }
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,BrowserActivity.class));
                /*sv.removeAllViews();
                id+=1;
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tv=new TextView(MainActivity.this);
                tv.setLayoutParams(layoutParams);
                tv.setText("New Chat! Ask me anything");
                firstTime=0;
                firstmsg+=1;
                tv.setTextSize(20f);
                sv.addView(tv);*/
            }
        });

        flbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstTime==0){
                    arrayList.add(edt1.getText().toString());
                    firstTime=1;
                    arrayAdapter.notifyDataSetChanged();
                    sv.removeAllViews();
                }
                try {
                    createChat(1,edt1.getText().toString());
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        imbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.inflate(R.menu.menufile);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id=item.getItemId();
                        Intent intent;
                        if(id==R.id.about){
                            intent=new Intent(MainActivity.this, about.class);
                            startActivity(intent);
                        }
                        Toast.makeText(MainActivity.this,"menu item is click",3000).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prevChat(position);
            }
        });
        checkDb();
        cardFunc();

    }
    public void cardFunc(){
        cv1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (firstTime==0){
                    arrayList.add(tvcv1.getText().toString());
                    firstTime=1;
                    arrayAdapter.notifyDataSetChanged();
                    sv.removeAllViews();
                }
                try {
                    createChat(2,tvcv1.getText().toString());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstTime==0){
                    arrayList.add(tvcv2.getText().toString());
                    firstTime=1;
                    arrayAdapter.notifyDataSetChanged();
                    sv.removeAllViews();
                }
                try {
                    createChat(3,tvcv2.getText().toString());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstTime==0){
                    arrayList.add(tvcv3.getText().toString());
                    firstTime=1;
                    arrayAdapter.notifyDataSetChanged();
                    sv.removeAllViews();
                }
                try {
                    createChat(4,tvcv3.getText().toString());
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    public void createChat(int x,String s) throws ExecutionException, InterruptedException {

        View uv=getLayoutInflater().inflate(R.layout.user_chat_box,null);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        uv.setLayoutParams(layoutParams);

        TextView ut=uv.findViewById(R.id.userTex);
        ut.setText(s);

        View gv=getLayoutInflater().inflate(R.layout.gemini_chat_box,null);
        LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        gv.setLayoutParams(layoutParams1);

        gt=gv.findViewById(R.id.gemTex);
        sv.addView(uv);
        sv.addView(gv);

        if(x==1){
            userChat=ut.getText().toString();
        }else{
            if(x==2){
                userChat=tvcv1.getText().toString();
            }else if(x==3){
                userChat=tvcv2.getText().toString();
            }else{
                userChat=tvcv3.getText().toString();
            }
        }
        scrollView.post(() -> scrollView.smoothScrollTo(0, sv.getBottom()));

        if(!edt1.getText().toString().isEmpty()){
            ut.setText(s);
            edt1.setText("");
        }

        firstmsg+=1;
        response(s);
    }
     public void response(String inp) throws ExecutionException, InterruptedException {
        // For text-only input, use the gemini-pro model
        String st = inp;
        Content content = new Content.Builder()
                .addText(st)
                .build();

        // Create an ExecutorService (adjust number of threads as needed)
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit the task and obtain a ListenableFuture
        ListenableFuture<GenerateContentResponse> future = executor.submit(() -> model.generateContent(content)).get();

        // Optional: Register a callback for asynchronous processing
        future.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    GenerateContentResponse response = model.generateContent(content).get();
                    handleResponse(inp,response);
                   // Log.d("response", Objects.requireNonNull(response.getText()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, executor); // Use the same executor for callback execution
    }

    private void handleResponse(String inp,GenerateContentResponse response) {
        String resultText = response.getText();
        runOnUiThread(() -> {
            if (gt != null) {
                gt.setText(resultText);
                dbobj.addData(id,userChat,resultText);
            } else {
                Log.e("handleResponse", "textView is null");
            }
        });
    }

    public void prevChat(int position){
        sv.removeAllViews();
        id=position+1;
        firstTime=1;
        Toast.makeText(this,String.valueOf(id),3000).show();
        ArrayList<ChatModelCalss> chatModelCalssArrayList=new ArrayList<>();
        chatModelCalssArrayList=dbobj.showData(position+1);
        for (int i = 0; i < chatModelCalssArrayList.size(); i++) {
            View uv = getLayoutInflater().inflate(R.layout.user_chat_box, null);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            View gv = getLayoutInflater().inflate(R.layout.gemini_chat_box, null);
            LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            uv.setLayoutParams(layoutParams);
            gv.setLayoutParams(layoutParams1);
            TextView ut = uv.findViewById(R.id.userTex);
            TextView gt = gv.findViewById(R.id.gemTex);

            ut.setText(chatModelCalssArrayList.get(i).userChat);
            gt.setText(chatModelCalssArrayList.get(i).geminiChat);

            sv.addView(uv);
            sv.addView(gv);
        }

    }
    public void checkDb(){
        ArrayList<ChatModelCalss> checklist=dbobj.CheckId();
        id=checklist.size()+1;
        for(int i=0;i<checklist.size();i++){
            arrayList.add(checklist.get(i).userChat);
        }
        arrayAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}