package com.spuds.eventapp.Shared;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by youngjinyun on 5/20/16.
 */
public class DatabaseTableSubUser{

    public static boolean threadDone = false;

    private static final String TAG = "DictionaryDatabase";

    //The columns we'll include in the dictionary table
    public static final String COL_USER_NAME = "USER_NAME";
    public static final String COL_USER_ID = "USER_ID";

    private static final String DATABASE_NAME = "USERS";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public DatabaseTableSubUser(Context context, ArrayList<SubUser> users) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context,users);
        Log.d("Create SQLite Table","DatabaseTable ctor called");
    }

    /**/

    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;
        private ArrayList<SubUser> mHelperUsersList;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_USER_NAME + ", "+
                        COL_USER_ID+")";

        DatabaseOpenHelper(Context context,ArrayList<SubUser> users) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
            mHelperUsersList = users;
            mHelperContext.deleteDatabase(DATABASE_NAME);
            mDatabase = getWritableDatabase();
            Log.d("Create SQLite Table","DatabaseOpenHelper ctor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.err.println("Running DbOH's onCreate");
            mDatabase = db;
            Log.d("Create SQLite Table","onCreate for SQLiteDatabase called");
            mDatabase.execSQL(FTS_TABLE_CREATE);
            Log.d("Create SQLite Table","mDatabase initialized");
            loadDictionary();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.err.println("Running DbOH's onUpgrade");
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        //This code supposedly for loading from .txt file
        //MOSTLY METHODS

        //the ArrayList passed in here should be ALL the users currently on database.
        //Should be run only onCreate, further shit is by update.
        private void loadDictionary() {
            System.err.println("Running DbOH's loadDictionary");
            Log.d("Create SQLite Table","loadDictionary called");
            //final ArrayList<SubUser> ev = mHelperUsersList;
            //TODO: Populate using UsersFirebase.java iterator
            //loop to add shit to arraylist


            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadWords(mHelperUsersList);

                        String db = getTableAsString(mDatabase,FTS_VIRTUAL_TABLE);
                        System.err.println(db);

                        threadDone = true;

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

        }

        private void loadWords(ArrayList<SubUser> ev) throws IOException {
            //final Resources resources = mHelperContext.getResources();

            //Here I will call addWord with correct shit. I need to take in an arraylist of events here.
            for(SubUser e:ev){
                addWord(e.getName(),
                        e.getUserId());
            }

            //Note 1***
        }

        public long addWord(String T_col_user_name,
                            String T_col_user_id) {

            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_USER_NAME,T_col_user_name); //the one searchable query
            initialValues.put(COL_USER_ID,T_col_user_id);

            Log.d("Create SQLite Table","Word Added");

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }


        //end result, mDatabase should be fully formed virtual database

        public String getTableAsString(SQLiteDatabase db, String tableName) {
            Log.d(TAG, "getTableAsString called");
            String tableString = String.format("Table %s:\n", tableName);
            Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
            if (allRows.moveToFirst() ){
                String[] columnNames = allRows.getColumnNames();
                do {
                    for (String name: columnNames) {
                        tableString += String.format("%s: %s\n", name,
                                allRows.getString(allRows.getColumnIndex(name)));
                    }
                    tableString += "\n";

                } while (allRows.moveToNext());
            }

            return tableString;
        }
    }




    //Start of SEARCH METHODs
    public Cursor getUserNameMatches(String query, String[] columns) {
        String selection = COL_USER_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            System.err.println("returning null 1");
            return null;
        } else if (!cursor.moveToFirst()) {
            System.err.println("returning null 2");
            cursor.close();
            return null;
        }
        return cursor;
    }
}
