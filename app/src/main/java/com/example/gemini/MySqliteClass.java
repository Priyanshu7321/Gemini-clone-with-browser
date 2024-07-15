package com.example.gemini;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MySqliteClass extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="chats";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="geminiTable";
    private static final String KEY_ID="id";
    private static final String GEMINI_CHAT="geminiChat";
    private static final String USER_CHAT="userChat";
    public MySqliteClass(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+KEY_ID+" INTEGER ,"+USER_CHAT+" TEXT,"+GEMINI_CHAT+" TEXT"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public void addData(int id,String userChat,String geminiChat){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_ID,id);
        values.put(USER_CHAT,userChat);
        values.put(GEMINI_CHAT,geminiChat);
        db.insert(TABLE_NAME,null,values);
    }
    public ArrayList<ChatModelCalss> showData(int id){
        ArrayList<ChatModelCalss> temp=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM geminiTable WHERE id="+String.valueOf(id),null);
        while(cursor.moveToNext()){
            ChatModelCalss obj=new ChatModelCalss();
            obj.id=cursor.getInt(0);
            obj.userChat=cursor.getString(1);
            obj.geminiChat=cursor.getString(2);
            temp.add(obj);
        }
        cursor.close();
        return temp;
    }
    public ArrayList<ChatModelCalss> CheckId(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ChatModelCalss> arrayList=new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='geminiTable'", null);

        if (cursor.moveToFirst()) {
            cursor.close();

            cursor = db.rawQuery("SELECT MAX(id) FROM geminiTable", null);
            int max = 0;
            if (cursor.moveToFirst()) {
                max = cursor.getInt(0);
            }
            cursor.close();

            for (int i = 1; i <= max; i++) {
                cursor = db.rawQuery("SELECT * FROM geminiTable WHERE id=" + i + " LIMIT 1", null);
                if (cursor.moveToFirst()) {

                    ChatModelCalss obj=new ChatModelCalss();
                    obj.id=cursor.getInt(0);
                    obj.userChat = cursor.getString(1);
                    obj.geminiChat= cursor.getString(2);
                    arrayList.add(obj);

                }
                cursor.close();
            }
        }
        cursor.close();
        return arrayList;
    }
}
