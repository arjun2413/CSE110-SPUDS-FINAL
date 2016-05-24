package com.spuds.eventapp.Shared;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.spuds.eventapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by youngjinyun on 5/20/16.
 */
public class DatabaseTable {

    private static final String TAG = "DictionaryDatabase";

    //The columns we'll include in the dictionary table
    public static final String COL_EVENT_NAME = "EVENT_NAME";
    public static final String COL_EVENT_ID = "EVENT_ID";

    private static final String DATABASE_NAME = "EVENTS";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public DatabaseTable(Context context,ArrayList<Event> events) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context,events);
        Log.d("Create SQLite Table","DatabaseTable ctor called");
    }

    /**/

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;
        private ArrayList<Event> mHelperEventsList;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_EVENT_NAME + ", "+
                        COL_EVENT_ID+")";

        DatabaseOpenHelper(Context context,ArrayList<Event> events) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
            mHelperEventsList = events;
            Log.d("Create SQLite Table","DatabaseOpenHelper ctor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            Log.d("Create SQLite Table","onCreate for SQLiteDatabase called");
            mDatabase.execSQL(FTS_TABLE_CREATE);
            Log.d("Create SQLite Table","mDatabase initialized");
            loadDictionary();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        //This code supposedly for loading from .txt file
        //MOSTLY METHODS

        //the ArrayList passed in here should be ALL the events currently on database.
        //Should be run only onCreate, further shit is by update.
        private void loadDictionary() {
            Log.d("Create SQLite Table","loadDictionary called");
            final ArrayList<Event> ev = mHelperEventsList;
            //TODO: Populate using EventsFirebase.java iterator
            //loop to add shit to arraylist


            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadWords(ev);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void loadWords(ArrayList<Event> ev) throws IOException {
            //final Resources resources = mHelperContext.getResources();

            //Here I will call addWord with correct shit. I need to take in an arraylist of events here.
            for(Event e:ev){
                addWord(e.getEventName(),
                        e.getEventId());
            }

            //Note 1***
        }

        public long addWord(String T_col_event_name,
                            String T_col_event_id) {

            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_EVENT_NAME,T_col_event_name); //the one searchable query
            initialValues.put(COL_EVENT_ID,T_col_event_id);

            Log.d("Create SQLite Table","Word Added");

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }
        //end result, mDatabase should be fully formed virtual database
    }




    //Start of SEARCH METHODs
    public Cursor getEventNameMatches(String query, String[] columns) {
        String selection = COL_EVENT_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}


/****
 Note 1: Option to read in using .txt file

 //Option to use Firebase's InputStream import
 //no need
 //InputStream inputStream = resources.openRawResource(R.raw.definitions);
 //no need
 //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

 //read user input line -> can convert to parsing event
 //String based parsing
 /*
 try {


 String line;
 while ((line = reader.readLine()) != null) {
 String[] strings = TextUtils.split(line, "-");
 if (strings.length < 2) continue;
 long id = addWord(strings[0].trim(), strings[1].trim());
 if (id < 0) {
 Log.e(TAG, "unable to add word: " + strings[0].trim());
 }
 }
 } finally {
 reader.close();
 }
 ***/
