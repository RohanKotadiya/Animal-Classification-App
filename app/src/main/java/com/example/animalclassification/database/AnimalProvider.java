package com.example.animalclassification.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnimalProvider extends ContentProvider {

    AnimalDbHelper aDbHelper;
    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int ANIMAL = 0;
    public static final int ANIMAL_ID = 1;
    public static final int AQUATIC = 2;
    public static final int MAMMALS = 3;
    public static final int BIRDS = 4;
    public static final int REPTILES = 5;
    static {
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH, ANIMAL);
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH+"/#", ANIMAL_ID);
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH+"/Aquatic", AQUATIC);
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH+"/Mammals", MAMMALS);
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH+"/Birds", BIRDS);
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH+"/Reptiles", REPTILES);

       }

    @Override
    public boolean onCreate() {
        aDbHelper = new AnimalDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = aDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = mUriMatcher.match(uri);
        switch (match){
            case ANIMAL:
                cursor = database.query(AnimalContract.AnimalEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ANIMAL_ID:
                selection = AnimalContract.AnimalEntry.ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(AnimalContract.AnimalEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case AQUATIC:
                selection = AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS+"=?";
                selectionArgs = new String[]{"Aquatic"};
                cursor = database.query(AnimalContract.AnimalEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MAMMALS:
                selection = AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS+"=?";
                selectionArgs = new String[]{"Mammals"};
                cursor = database.query(AnimalContract.AnimalEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BIRDS:
                selection = AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS+"=?";
                selectionArgs = new String[]{"Birds"};
                cursor = database.query(AnimalContract.AnimalEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case REPTILES:
                selection = AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS+"=?";
                selectionArgs = new String[]{"Reptiles"};
                cursor = database.query(AnimalContract.AnimalEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Invalid uri"+ uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = mUriMatcher.match(uri);
        switch (match){
            case ANIMAL:
                return insertAnimal(uri, values);
            default:
                throw new IllegalArgumentException("Invalid URI "+ uri);
        }
    }

    private Uri insertAnimal(Uri uri, ContentValues values){
        SQLiteDatabase database = aDbHelper.getWritableDatabase();

        long id = database.insert(AnimalContract.AnimalEntry.TABLE_NAME, null, values);
            if (id == -1){
                Log.e(String.valueOf(getContext().getClass()), "Insertion Failed !");
                return null;
            }
            Uri mUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(mUri, null);
            Log.v("Insertion", "insertion success");
            return mUri;
        }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
       int match = mUriMatcher.match(uri);
       switch (match){
           case ANIMAL:
               return deleteAnimal(uri, selection, selectionArgs);
           case ANIMAL_ID:
               selection = AnimalContract.AnimalEntry.ID+"=?";
               selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
               return deleteAnimal(uri, selection, selectionArgs);
           case AQUATIC:
               selection = AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS+"=?";
               selectionArgs = new String[]{"Aquatic"};
               return deleteAnimal(uri, selection, selectionArgs);
           case MAMMALS:
               selection = AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS+"=?";
               selectionArgs = new String[]{"Mammals"};
               return deleteAnimal(uri, selection, selectionArgs);
           case BIRDS:
               selection = AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS+"=?";
               selectionArgs = new String[]{"Birds"};
               return deleteAnimal(uri, selection, selectionArgs);
           case REPTILES:
               selection = AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS+"=?";
               selectionArgs = new String[]{"Reptiles"};
               return deleteAnimal(uri, selection, selectionArgs);
           default:
               throw new IllegalArgumentException("Invalid URI "+uri);
       }
    }
    private int deleteAnimal(Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase database = aDbHelper.getWritableDatabase();
        int rowCount = database.delete(AnimalContract.AnimalEntry.TABLE_NAME, selection, selectionArgs);
        if (rowCount != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match){
            case ANIMAL_ID:
                selection = AnimalContract.AnimalEntry.ID+"=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateAnimal(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Invalid URI "+uri);
        }
    }
    private int updateAnimal(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase database = aDbHelper.getWritableDatabase();
            int id = database.update(AnimalContract.AnimalEntry.TABLE_NAME, values, selection, selectionArgs);
            if (id != 0){
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return id;
        }
    }
//    private boolean checkForNull(ContentValues contentValues){
//        String name = contentValues.getAsString(AnimalContract.AnimalEntry.COLUMN_ANIMAL_NAME);
//        String location = contentValues.getAsString(AnimalContract.AnimalEntry.COLUMN_ANIMAL_LOCATION);
//        String description = contentValues.getAsString(AnimalContract.AnimalEntry.COLUMN_ANIMAL_DESCRIPTION);
//        String life_expectancy = contentValues.getAsString(AnimalContract.AnimalEntry.COLUMN_ANIMAL_LIFE_EXPECTANCY);
//        byte[] image = contentValues.getAsByteArray(AnimalContract.AnimalEntry.COLUMN_ANIMAL_IMAGE);
//        int animal_class = contentValues.getAsInteger(AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS);
//        if (name == null || location == null || description == null || life_expectancy == null || image.length == 0 || animal_class < 1){
//            Log.v("Null", "True");
//            return false;
//        }else{
//            Log.v("Null", "False");
//            return true;
//        }
//    }
