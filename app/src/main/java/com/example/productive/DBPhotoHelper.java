package com.example.productive;

        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import java.lang.reflect.Array;
        import java.util.ArrayList;
        import java.util.HashMap;

        import android.content.Context;
        import android.content.ContentValues;
        import android.graphics.Bitmap;

public class DBPhotoHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "photos";

    // Table Names
    private static final String DB_TABLE = "table_image";

    // column names
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";

    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE IF NOT EXISTS " + DB_TABLE + "("+
            KEY_NAME + " TEXT," +
            KEY_IMAGE + " BLOB);";

    public DBPhotoHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating table
        db.execSQL(CREATE_TABLE_IMAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        // create new table
        onCreate(db);
    }

    public void addEntry(Image image){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put(KEY_NAME,    image.getName());
        cv.put(KEY_IMAGE,   image.getImage());
        database.insert( DB_TABLE, null, cv );
    }

    public void deleteEntry(String image){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("table_image", "image_name = ?", new String[]{image});
    }


    public ArrayList<Image> getImages() {
        String selectQuery = "SELECT * FROM table_image";
        ArrayList<Image> photos = new ArrayList<Image>();


        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        int descriptionIndex = cursor.getColumnIndex("image_name");
        if (cursor.moveToFirst()) {
            do {
                byte[] image = cursor.getBlob(1);
                String imagename = cursor.getString(descriptionIndex);
                Image image1 = new Image(imagename, image);
                photos.add(image1);
            }
            while (cursor.moveToNext());
        }

        return photos;
    }
}