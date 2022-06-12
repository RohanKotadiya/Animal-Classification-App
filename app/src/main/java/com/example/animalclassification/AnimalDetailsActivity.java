package com.example.animalclassification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animalclassification.database.AnimalContract;

public class AnimalDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Uri currentUri;
    ImageView imageView;
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);

        imageView = findViewById(R.id.image_details);
        description = findViewById(R.id.animal_description);
        currentUri = getIntent().getData();

        getLoaderManager().initLoader(0, null, this);

        Button editDetails = findViewById(R.id.editDetails);
        editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalDetailsActivity.this, EditorActivity.class);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = new String[]{
                AnimalContract.AnimalEntry.ID,
                AnimalContract.AnimalEntry.COLUMN_ANIMAL_NAME,
                AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS,
                AnimalContract.AnimalEntry.COLUMN_ANIMAL_IMAGE,
                AnimalContract.AnimalEntry.COLUMN_ANIMAL_DESCRIPTION
        };

        return new CursorLoader(this, currentUri,  projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        int imageIndex = data.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_IMAGE);
        int descriptionIndex = data.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_DESCRIPTION);

        if (data != null && data.moveToFirst()){
            String animal_description = data.getString(descriptionIndex);
            description.setText(animal_description);
            byte[] imageByte = data.getBlob(imageIndex);
            Bitmap imageBit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            imageView.setImageBitmap(imageBit);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        description.setText(null);
        imageView.setImageBitmap(null);
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
//        switch (item.getItemId()){
//            case R.id.delete:
//                showDeleteConfirmationDialog();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void deleteAnimal(){
//        int id = getContentResolver().delete(currentUri, null, null);
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
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }
}