package com.example.animalclassification.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class AnimalContract {
    public AnimalContract(){}

    public static class AnimalEntry implements BaseColumns{
        public final static String TABLE_NAME = "animals";

        public final static String ID = BaseColumns._ID;
        public final static String COLUMN_ANIMAL_NAME = "name";
        public final static String COLUMN_ANIMAL_CLASS = "class";
        public final static String COLUMN_ANIMAL_LOCATION = "location";
        public final static String COLUMN_ANIMAL_LIFE_EXPECTANCY = "life_expectancy";
        public final static String COLUMN_ANIMAL_DESCRIPTION = "description";
        public final static String COLUMN_ANIMAL_IMAGE = "image";

        public final static String CLASS_AQUATIC = "Aquatic";
        public final static String CLASS_MAMMAL = "Mammals";
        public final static String CLASS_BIRD = "Birds";
        public final static String CLASS_REPTILE = "Reptiles";

        public static final String CONTENT_AUTHORITY = "com.example.animalclassification.database";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

        public static final String PATH = "animals";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);
    }
}
