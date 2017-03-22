package com.example.joshiyogesh.stackoverflow_bear;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Joshi Yogesh on 21/03/2017 at 20:57 IST.
 */

public class QuestionDatabase {
    private static final String TAG = "QuestionDatabase";
    public static final String TABLE_NAME = "question";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_VOTES = "votes";
    private static final String COLUMN_SEARCH = "search";
    private static final String KEY_PRIMARY = "pk";

    /*making object of database wrapper class*/
    private DatabaseWrapper databaseWrapper;

    SQLiteDatabase sqLiteDatabase;

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + KEY_PRIMARY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_ID + " TEXT, "
                    + COLUMN_TITLE + " TEXT, "
                    + COLUMN_AUTHOR + " TEXT, "
                    + COLUMN_VOTES + " TEXT, "
                    + COLUMN_SEARCH + " TEXT UNIQUE)";
    //ensures that a particular search query can only be present once in the database.
    //and writing command for creating database


    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    /*ensures that if there would be any SQL Table already exist than it would be drop*/


    public int insertQuestion(Context context , String ids , String titles , String authors , String votes , String search){
        databaseWrapper = new DatabaseWrapper(context);
        sqLiteDatabase = databaseWrapper.getWritableDatabase();

        long questionId = 0;
        if (isDatabaseOpened()){
            /*this class is used to return value that ContentResolver can process |
            * i.e ContentValues*/
            ContentValues contentValues = new ContentValues();
            contentValues.put(QuestionDatabase.COLUMN_ID , ids);
            contentValues.put(QuestionDatabase.COLUMN_TITLE , titles);
            contentValues.put(QuestionDatabase.COLUMN_AUTHOR , authors);
            contentValues.put(QuestionDatabase.COLUMN_VOTES, votes);
            contentValues.put(QuestionDatabase.COLUMN_SEARCH , search);

            /*inserting entries into DataBase using insert query*/
            questionId = sqLiteDatabase.insert(QuestionDatabase.TABLE_NAME , "null" ,contentValues);


            /*for testing purpose*/
            Log.e(TAG, "Inserted new Question with ID: " + questionId);

            /*closing database*/
            sqLiteDatabase.close();
        }
        return (int)questionId ;
    }

    public boolean isDatabaseOpened(){
        /*this method checks whether database is opened or not and return boolean value regarding this*/

        if (sqLiteDatabase == null){
            return false;
        }
        else {
            return sqLiteDatabase.isOpen();
        }
    }

    /*checks whether particular string exist in DataBase*/
    public boolean doesExist(Context context , String search){
        databaseWrapper = new DatabaseWrapper(context);
        sqLiteDatabase = databaseWrapper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM question WHERE search" + "= '" +
                search.trim() + "'" + " COLLATE NOCASE", null);
        /*COLLATE NOCASE ... ensure that there shouldnt be any insensetive search*/
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*gets an array list of query corresponding to particular search items*/

    public ArrayList<String> getTitleDetails(Context context , String search ) throws JSONException {

        databaseWrapper = new DatabaseWrapper(context);
        sqLiteDatabase = databaseWrapper.getWritableDatabase();

        ArrayList<String> items = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM question WHERE search"
                + "= '" + search.trim() + "'"+ " COLLATE NOCASE", null);

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            if(cursor.getString(5).trim().equalsIgnoreCase(search.trim())) {
                JSONObject json = new JSONObject(cursor.getString(2));
                JSONArray jarr = json.optJSONArray("uniqueTitles");
                for (int i = 0; i < jarr.length(); i++) {
                    items.add(jarr.getString(i));
                }
            }
        }
        cursor.moveToFirst();
        return items;




    }

    /*gets an array list corresponding to author when search query is made*/
    public ArrayList<String> getAuthorDetails(Context context , String search)throws JSONException{
        databaseWrapper = new DatabaseWrapper(context);
        sqLiteDatabase = databaseWrapper.getWritableDatabase();

        ArrayList<String> items = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM question WHERE search" + "= '" + search.trim() + "'"+ " COLLATE NOCASE", null);

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            if(cursor.getString(5).trim().equalsIgnoreCase(search.trim())) {
                JSONObject json = new JSONObject(cursor.getString(3));
                JSONArray jarr = json.optJSONArray("uniqueAuthors");
                for (int i = 0; i < jarr.length(); i++) {
                    items.add(jarr.getString(i));
                }
            }
        }
        cursor.moveToFirst();
        return items;


    }

    /*gets an array list of votes corresponding to particular search query*/
    public ArrayList<String> getVoteDetails(Context context , String search) throws JSONException{

        databaseWrapper = new DatabaseWrapper(context);
        sqLiteDatabase = databaseWrapper.getWritableDatabase();

        ArrayList<String>items = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM question WHERE search" + "= '" + search.trim() + "'"+ " COLLATE NOCASE", null);

        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            if (cursor.getString(5).trim().equalsIgnoreCase(search.trim())) {
                JSONObject json = new JSONObject(cursor.getString(4));
                JSONArray jarr = json.optJSONArray("uniqueVotes");
                for (int i = 0; i < jarr.length(); i++) {
                    items.add(jarr.getString(i));
                }
            }
        }
        cursor.moveToFirst();
        return items;
    }

    /*gets an array list of questions ids corresponding to particular search query */
    public ArrayList<String> getIDDetails(Context context , String search) throws JSONException{
        databaseWrapper = new DatabaseWrapper(context);
        sqLiteDatabase = databaseWrapper.getWritableDatabase();

        ArrayList<String>items = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM question WHERE search" + "= '" + search.trim() + "'"+ " COLLATE NOCASE", null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            if (cursor.getString(5).trim().equalsIgnoreCase(search.trim())) {
                JSONObject json = new JSONObject(cursor.getString(4));
                JSONArray jarr = json.optJSONArray("uniqueVotes");
                for (int i = 0; i < jarr.length(); i++) {
                    items.add(jarr.getString(i));
                }
            }
        }
        cursor.moveToFirst();
        return items;
    }

}
