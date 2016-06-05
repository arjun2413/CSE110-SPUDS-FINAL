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
/*---------------------------------------------------------------------------
Class Name:                 DatabaseTableSubUser
Description:                Sets up classes for setting up local SQLite database
                            to hold Firebase user info
---------------------------------------------------------------------------*/
public class DatabaseTableSubUser{
    //marker for thread completion
    public static boolean threadDone = false;
    //Strings for SQLite database setup
    private static final String TAG = "DictionaryDatabase";

    //The columns we'll include in the dictionary table
    public static final String COL_USER_NAME = "USER_NAME";
    public static final String COL_USER_ID = "USER_ID";

    //Strings for SQLite database setup
    private static final String DATABASE_NAME = "USERS";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    /*---------------------------------------------------------------------------
    Constructor Name:           DatabaseTableSubUser
    Description:                Singular function: creates instance of DatabaseOpenHelper
    Input:                      Context context, ArrayList<SubUser>
    Output:                     None.
    ---------------------------------------------------------------------------*/
    public DatabaseTableSubUser(Context context, ArrayList<SubUser> users) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context,users);
        //("Create SQLite Table","DatabaseTable ctor called");
    }

    /*---------------------------------------------------------------------------
        Class Name:           DatabaseOpenHelper
        Description:          Creates local table and pulls info from Firebase into it
        ---------------------------------------------------------------------------*/
    private static class DatabaseOpenHelper extends SQLiteOpenHelper {
        //declare SQLite variables
        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;
        private ArrayList<SubUser> mHelperUsersList;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        COL_USER_NAME + ", "+
                        COL_USER_ID+")";
        /*---------------------------------------------------------------------------
        Constructor Name:           DatabaseOpenHelper
        Description:                Helps access Firebase, sets up local table
        Input:                      Context context, ArrayList<SubUser> users
        ---------------------------------------------------------------------------*/
        DatabaseOpenHelper(Context context,ArrayList<SubUser> users) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
            mHelperUsersList = users;
            mHelperContext.deleteDatabase(DATABASE_NAME);
            mDatabase = getWritableDatabase();
        }

        /*---------------------------------------------------------------------------
        Function Name:                onCreate()
        Description:                  actually create the table to the passed in arg
        Input:                        SQLiteDatabase db
        Output:                       None.
        ---------------------------------------------------------------------------*/
        @Override
        public void onCreate(SQLiteDatabase db) {
            System.err.println("Running DbOH's onCreate");
            mDatabase = db;
            //("Create SQLite Table","onCreate for SQLiteDatabase called");
            mDatabase.execSQL(FTS_TABLE_CREATE);
            //("Create SQLite Table","mDatabase initialized");
            loadDictionary();
        }

        /*---------------------------------------------------------------------------
        Function Name:                onUpgrade()
        Description:                  update database data
        Input:                        SQLiteDatabase db, int oldVersion, int newVersion
        Output:                       None.
        ---------------------------------------------------------------------------*/
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.err.println("Running DbOH's onUpgrade");
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        //the ArrayList passed in here should be ALL the users currently on database.
        //Should be run only onCreate, further content is by update.
        /*---------------------------------------------------------------------------
        Function Name:                onloadDictionary()
        Description:                  Calls new thread to load words onto db
        Input:                        None.
        Output:                       None.
        ---------------------------------------------------------------------------*/
        private void loadDictionary() {
            //loop to add SubUsers to arraylist

            //New thread for the purpose of loading all the words into the db
            new Thread(new Runnable() {
                public void run() {
                    try {
                        //call loadwords to iterate through each event and add them
                        loadWords(mHelperUsersList);
                        String db = getTableAsString(mDatabase,FTS_VIRTUAL_TABLE);
                        //end thread
                        threadDone = true;

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();

        }

        /*---------------------------------------------------------------------------
        Function Name:                loadWords()
        Description:                  Iterates through items to add to db one at a time
        Input:                        ArrayList<SubUser>
        Output:                       None.
        ---------------------------------------------------------------------------*/
        private void loadWords(ArrayList<SubUser> ev) throws IOException {
            //final Resources resources = mHelperContext.getResources();

            //Here I will call addWord with correct shit. I need to take in an arraylist of events here.
            for(SubUser e:ev){
                addWord(e.getName(),
                        e.getUserId());
            }

        }

        /*---------------------------------------------------------------------------
        Function Name:                addWord()
        Description:                  Inserts data values of into table
        Input:                        String column_event_name, String column_event_id
        Output:                       None.
        ---------------------------------------------------------------------------*/
        public long addWord(String T_col_user_name,
                            String T_col_user_id) {

            ContentValues initialValues = new ContentValues();
            initialValues.put(COL_USER_NAME,T_col_user_name); //the one searchable query
            initialValues.put(COL_USER_ID,T_col_user_id);

            //("Create SQLite Table","Word Added");

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }


        //end result, mDatabase should be fully formed virtual database

        /*---------------------------------------------------------------------------
        Function Name:                getTableAsString()
        Description:                  Pull from local table and print out values in string form
        Input:                        SQLiteDatabase db, String tableName
        Output:                       String
        ---------------------------------------------------------------------------*/
        public String getTableAsString(SQLiteDatabase db, String tableName) {
            //(TAG, "getTableAsString called");
            String tableString = String.format("Table %s:\n", tableName);
            Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
            //if moveToFirst returns without null error
            if (allRows.moveToFirst() ){
                String[] columnNames = allRows.getColumnNames();
                do {
                    //concatenate String values to String
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
    /*---------------------------------------------------------------------------
    Function Name:                getUserNameMatches()
    Description:                  matches query string to database strings
    Input:                        String query - query to search for
                                  String[] columns - for instructing db
    Output:                       Cursor
    ---------------------------------------------------------------------------*/
    public Cursor getUserNameMatches(String query, String[] columns) {
        String selection = COL_USER_NAME + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }

    /*---------------------------------------------------------------------------
    Function Name:                query()
    Description:                  matches query string to database strings
    Input:                        String selection - used to build cursor
                                  String[] selectionArgs - used to build cursor
                                  String[] columns - used to build cursor
    Output:                       Cursor
    ---------------------------------------------------------------------------*/
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);
        //if cursor built w/ error
        if (cursor == null) {
            return null;
        }
        //if cursor build with no error
        else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}
