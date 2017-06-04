package com.example.swets.mytestdatabaseapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.example.swets.mytestdatabaseapplication.DataBaseConstants.*;

/**
 * Created by swets on 07-01-2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gwl.db";
    private static final int DATABASE_VERSION = 7;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DataBase", "Table will be created");
        String CREATE_TABLE = "CREATE TABLE " +
                DATABASE_TABLE_NAME +
                "(" +
                ID +
                " INTEGER primary key," +
                NAME +
                " text," +
                ADDRESS +
                " text" +
                ")";
        Log.d("DataBase", "" + CREATE_TABLE);
        try {
            db.execSQL(CREATE_TABLE);
            Log.d("DataBase", "Table Created");
        } catch (Exception ex) {
            Log.d("Accounts", "Error in DBHelper.onCreate() : " + ex.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    void addRow(Model model, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, model.getId());
        values.put(NAME, model.getName());
        values.put(ADDRESS, model.getAddress());
        try {
            db.insertOrThrow(DATABASE_TABLE_NAME, null, values);
            // Toast.makeText(context, "Row Inserted", Toast.LENGTH_SHORT).show();
            ((MainActivity) context).showSnackBar("Row Inserted", model.getId());
            db.close();
        } catch (SQLiteConstraintException sqliteException) {
            ((MainActivity) context).showSnackbar("Duplicate Entry");
            if (db.update(DATABASE_TABLE_NAME, values, ID + "=" + model.getId(), null) > 0)
                ((MainActivity) context).showSnackbar("Row updated");
            else ((MainActivity) context).showSnackbar("Row ID not found");
            Log.d("Constraint Exception", sqliteException.getLocalizedMessage());
        }

    }

    void updateRow(Model model, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, model.getId());
        values.put(NAME, model.getName());
        values.put(ADDRESS, model.getAddress());
        if (db.update(DATABASE_TABLE_NAME, values, ID + "=" + model.getId(), null) > 0)
            ((MainActivity) context).showSnackbar("Row updated");

        else ((MainActivity) context).showSnackbar("Row ID not found");
    }

    public List<Model> getAllRows() {
        List<Model> modelArrayList = new ArrayList<Model>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model model = new Model();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setName(cursor.getString(1));
                model.setAddress(cursor.getString(2));
                // Adding contact to list
                modelArrayList.add(model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return modelArrayList;
    }

    //---deletes a particular title---
    public boolean deleteRow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DATABASE_TABLE_NAME, ID + "=" + id, null) > 0;
    }

}
