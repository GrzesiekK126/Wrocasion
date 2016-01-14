package app.wrocasion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseHelper extends SQLiteOpenHelper{


    public BaseHelper(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table userdata(" + "nr integer primary key autoincrement," + "username text);" + "");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addUserToDatabase(String username){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        db.insertOrThrow("userdata", null, contentValues);
    }

    public Cursor getUserFromDatabase(){
        String[] columns = {"nr", "username"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("userdata", columns, null, null, null, null, null);
        return cursor;
    }

    public void deleteUserFromDatabase(String username){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("userdata","username='"+username+"'",null);
    }
}
