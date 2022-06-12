package com.example.animalclassification;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animalclassification.database.AnimalContract;

public class AnimalCursorAdapter extends CursorAdapter {
    public AnimalCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.animal_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = view.findViewById(R.id.name);
        TextView animal_location = view.findViewById(R.id.animal_location);
        ImageView image = view.findViewById(R.id.image);

        int nameIndex = cursor.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_NAME);
        int animalLocationIndex = cursor.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_LOCATION);
        int imageIndex = cursor.getColumnIndex(AnimalContract.AnimalEntry.COLUMN_ANIMAL_IMAGE);

        name.setText(cursor.getString(nameIndex));
        animal_location.setText(cursor.getString(animalLocationIndex));

        byte[] imageByte = cursor.getBlob(imageIndex);
        Bitmap imageBit = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
        image.setImageBitmap(imageBit);

    }
}
