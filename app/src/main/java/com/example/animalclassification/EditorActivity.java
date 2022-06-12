package com.example.animalclassification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.animalclassification.database.AnimalContract;
import com.example.animalclassification.database.AnimalDbHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 100;
    Boolean infoItemHasChanged = false;


    Uri currentUri;
    Uri imageUri;
    private Bitmap imageToStore;
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private byte[] imageInByte = new byte[1024];
    private String mTypeAnimal;

    EditText animal_name;
    EditText animal_location;
    EditText animal_life_expectancy;
    EditText description;
    Spinner animal_type;
    ImageView image;
    Button chooseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        animal_name = findViewById(R.id.animal_name);
        animal_location = findViewById(R.id.animal_location);
        animal_life_expectancy = findViewById(R.id.animal_life_expectancy);
        description = findViewById(R.id.description);
        animal_type = findViewById(R.id.spinner_type);
        image = findViewById(R.id.showImage);
        chooseImage = findViewById(R.id.chooseImage);

        setupSpinner();
        AnimalDbHelper aDbHelper = new AnimalDbHelper(this);

        Intent i = getIntent();
        currentUri = i.getData();

        if (currentUri == null){
            setTitle("Add Animal");
            invalidateOptionsMenu();
        }else{
            setTitle("Edit Animal");

        }

        getLoaderManager().initLoader(0, null, this);




        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelector();
                infoItemHasChanged = true;
            }
        });





    }

    View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            infoItemHasChanged = true;
            return false;
        }
    };

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (currentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (currentUri != null){
            String[] projection = {
                    AnimalContract.AnimalEntry.ID,
                    AnimalContract.AnimalEntry.COLUMN_ANIMAL_NAME,
                    AnimalContract.AnimalEntry.COLUMN_ANIMAL_LOCATION,
                    AnimalContract.AnimalEntry.COLUMN_ANIMAL_LIFE_EXPECTANCY,
                    AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS,
                    AnimalContract.AnimalEntry.COLUMN_ANIMAL_DESCRIPTION,
                    AnimalContract.AnimalEntry.COLUMN_ANIMAL_IMAGE
            };
            return new CursorLoader(this,
                    currentUri,
                    projection,
                    null,
                    null,
                    null);
        }else{
            return null;
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null){
           if (data.moveToFirst()) {
               int nameIndex = data.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_NAME);
               int locationIndex = data.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_LOCATION);
               int lifeExpectancyIndex = data.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_LIFE_EXPECTANCY);
               int descriptionIndex = data.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_DESCRIPTION);
               int animalClassIndex = data.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS);
               int imageIndex = data.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_IMAGE);

               animal_name.setText(data.getString(nameIndex));
               animal_location.setText(data.getString(locationIndex));
               animal_life_expectancy.setText(data.getString(lifeExpectancyIndex));
               description.setText(data.getString(descriptionIndex));
               byte[] imageByte = data.getBlob(imageIndex);
               Bitmap imageBit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
               image.setImageBitmap(imageBit);
               image.setVisibility(View.VISIBLE);

               switch (data.getString(animalClassIndex)) {
                   case AnimalContract.AnimalEntry.CLASS_AQUATIC:
                       animal_type.setSelection(0);
                       break;
                   case AnimalContract.AnimalEntry.CLASS_MAMMAL:
                       animal_type.setSelection(1);
                       break;
                   case AnimalContract.AnimalEntry.CLASS_BIRD:
                       animal_type.setSelection(2);
                       break;
                   case AnimalContract.AnimalEntry.CLASS_REPTILE:
                       animal_type.setSelection(3);
                       break;
               }
           }
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader.isReset();
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!infoItemHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAnimal();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_type_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        animal_type.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        animal_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                    if (!TextUtils.isEmpty(selection)){
                        if (selection.equals(getString(R.string.aquatic))) {
                            mTypeAnimal = AnimalContract.AnimalEntry.CLASS_AQUATIC;
                        } else if (selection.equals(getString(R.string.mammals))) {
                            mTypeAnimal = AnimalContract.AnimalEntry.CLASS_MAMMAL;
                        } else if (selection.equals(getString(R.string.birds))) {
                            mTypeAnimal = AnimalContract.AnimalEntry.CLASS_BIRD;
                        }else if(selection.equals(getString(R.string.reptiles))) {
                            mTypeAnimal = AnimalContract.AnimalEntry.CLASS_REPTILE;
                        }else{
                            mTypeAnimal = AnimalContract.AnimalEntry.CLASS_AQUATIC;
                        }
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTypeAnimal = AnimalContract.AnimalEntry.CLASS_AQUATIC;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                insertAnimal();
                finish();
                return true;
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!infoItemHasChanged){
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAnimal(){
        int id = getContentResolver().delete(currentUri, null, null);
        if (id == 0) {
            Toast.makeText(this, "Deletion Failed !",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Deletion Success !",
                    Toast.LENGTH_SHORT).show();
        }
        finish();
    }



    @RequiresApi(api = Build.VERSION_CODES.R)
    private void insertAnimal(){
        long id;

        String name = animal_name.getText().toString().trim();
        String location = animal_location.getText().toString().trim();
        String life_expectancy = animal_life_expectancy.getText().toString().trim();
        String animal_description = description.getText().toString().trim();

        imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageInByte = byteArrayOutputStream.toByteArray();

        Log.v("BLOB", Arrays.toString(imageInByte));

        ContentValues values = new ContentValues();
        values.put(AnimalContract.AnimalEntry.COLUMN_ANIMAL_NAME, name);
        values.put(AnimalContract.AnimalEntry.COLUMN_ANIMAL_LOCATION, location);
        values.put(AnimalContract.AnimalEntry.COLUMN_ANIMAL_LIFE_EXPECTANCY, life_expectancy);
        values.put(AnimalContract.AnimalEntry.COLUMN_ANIMAL_DESCRIPTION, animal_description);
        values.put(AnimalContract.AnimalEntry.COLUMN_ANIMAL_CLASS, mTypeAnimal);
        values.put(AnimalContract.AnimalEntry.COLUMN_ANIMAL_IMAGE, imageInByte);


        if (currentUri != null){
            String selection = AnimalContract.AnimalEntry.ID+"=?";
            String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(currentUri))};
            id = getContentResolver().update(currentUri, values, selection, selectionArgs);
            if (id == 0) {
                Toast.makeText(this, "Updation is failed !",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Updation Successful !",
                        Toast.LENGTH_SHORT).show();
            }
        }else {


            Uri newUri= getContentResolver().insert(AnimalContract.AnimalEntry.CONTENT_URI, values,  null);
            if (newUri != null) {
                Toast.makeText(this, "Insertion success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Insertion Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }



//    public void tryToOpenImageSelector() {
////        if (ContextCompat.checkSelfPermission(this,
////                Manifest.permission.READ_EXTERNAL_STORAGE)
////                != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(this,
////                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
////                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
////            return;
////        }
//        openImageSelector();
//    }

    @SuppressLint("ObsoleteSdkInt")
    private void openImageSelector() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageSelector();
                    // permission was granted
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {


        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && resultData != null && resultData.getData() != null){
            try {
                imageUri = resultData.getData();
                Log.v("ImageUri", String.valueOf(imageUri));
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                image.setImageBitmap(imageToStore);
                image.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}