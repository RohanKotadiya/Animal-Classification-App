package com.example.animalclassification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;

import android.content.ContentUris;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animalclassification.database.AnimalContract;
import com.example.animalclassification.database.AnimalDbHelper;

import java.lang.reflect.Field;

public class AnimalList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri mUri;
    AnimalCursorAdapter mAdapter;
    AnimalDbHelper aDbHelper;

    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int AQUATIC = 2;
    public static final int MAMMALS = 3;
    public static final int BIRDS = 4;
    public static final int REPTILES = 5;
    static {
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH+"/Aquatic", AQUATIC);
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH+"/Mammals", MAMMALS);
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH+"/Birds", BIRDS);
        mUriMatcher.addURI(AnimalContract.AnimalEntry.CONTENT_AUTHORITY, AnimalContract.AnimalEntry.PATH+"/Reptiles", REPTILES);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_list);

        mUri = getIntent().getData();
        int match = mUriMatcher.match(mUri);
        switch (match){
            case AQUATIC:
                setTitle("Aquatic Animals");
                break;
            case MAMMALS:
                setTitle("Mammal Animals");
                break;
            case BIRDS:
                setTitle("Birds");
                break;
            case REPTILES:
                setTitle("Reptile Animals");
                break;
        }
        aDbHelper = new AnimalDbHelper(this);
        ListView list = findViewById(R.id.list);
        mAdapter = new AnimalCursorAdapter(this, null);
        list.setAdapter(mAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itemIntent = new Intent(AnimalList.this, AnimalDetailsActivity.class);
                Uri uri = ContentUris.withAppendedId(AnimalContract.AnimalEntry.CONTENT_URI, id);
                itemIntent.setData(uri);
                startActivity(itemIntent);
            }
        });

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }

//        View emptyView = findViewById(R.id.empty_view);
//        list.setEmptyView(emptyView);

        getLoaderManager().initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = new String[]{
                AnimalContract.AnimalEntry.ID,
                AnimalContract.AnimalEntry.COLUMN_ANIMAL_NAME,
                AnimalContract.AnimalEntry.COLUMN_ANIMAL_LOCATION,
                AnimalContract.AnimalEntry.COLUMN_ANIMAL_IMAGE
        };

        return new CursorLoader(this, mUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.list_menu, menu);
//        return true;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.R)
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.delete:
//                showDeleteConfirmationDialog();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void deleteAnimal() {
//        int id = getContentResolver().delete(mUri, null, null);
//        if (id == 0) {
//            Toast.makeText(this, "Deletion Failed !",
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Deletion Success !",
//                    Toast.LENGTH_SHORT).show();
//        }
//        finish();
//    }
//
//    private void showDeleteConfirmationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(R.string.delete_dialog_msg);
//        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                deleteAnimal();
//            }
//        });
//        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                if (dialog != null) {
//                    dialog.dismiss();
//                }
//            }
//        });
//    }
}