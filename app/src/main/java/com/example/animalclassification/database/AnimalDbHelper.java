package com.example.animalclassification.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.animalclassification.database.AnimalContract.AnimalEntry;

import androidx.annotation.Nullable;

public class AnimalDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "animals.db";
    private static final int DATABASE_VERSION = 1;
    public AnimalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "+AnimalEntry.TABLE_NAME+"("
                +AnimalEntry.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +AnimalEntry.COLUMN_ANIMAL_NAME+" TEXT NOT NULL, "
                +AnimalEntry.COLUMN_ANIMAL_CLASS+" TEXT NOT NULL, "
                +AnimalEntry.COLUMN_ANIMAL_LOCATION+" TEXT NOT NULL, "
                +AnimalEntry.COLUMN_ANIMAL_LIFE_EXPECTANCY+" TEXT NOT NULL, "
                +AnimalEntry.COLUMN_ANIMAL_DESCRIPTION+" TEXT NOT NULL, "
                +AnimalEntry.COLUMN_ANIMAL_IMAGE+" BLOB NOT NULL);";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
