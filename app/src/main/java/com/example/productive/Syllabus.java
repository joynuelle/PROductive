package com.example.productive;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;

        import android.app.Activity;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;


public class Syllabus extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private ImageView imageView;

    DBPhotoHelper dbPhotoHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        imageView = (ImageView) findViewById(R.id.result);
        dbPhotoHelper = new DBPhotoHelper(getApplicationContext());
        final ArrayList<Image> photos = dbPhotoHelper.getImages();

        ArrayList<String> displayImages = new ArrayList<>();
        for (Image curImg : photos) {
            displayImages.add(String.format("Title: %s\n", curImg.getName().toString()));
        }
        ListView listView = findViewById(R.id.imagelist);

        final ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, photos);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(Syllabus.this);
                adb.setTitle("Delete?");
                final Image curimg = photos.get(position);
                DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
                imageView.setImageBitmap(dbBitmapUtility.getImage(curimg.getImage()));
                adb.setMessage("Are you sure you want to delete this image?");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbPhotoHelper.deleteEntry(curimg.getName());
                        adapter.notifyDataSetChanged();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }});
                adb.show();
            }
        });
    }

    public void onClick(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            try {
                // recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
                bitmapArray.add(bitmap); // Add a bitmap
                DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
                byte[] imgbyte = dbBitmapUtility.getBytes(bitmap);

                EditText imagename = findViewById(R.id.imgname);
                String name = imagename.getText().toString();
                Image image = new Image(name, imgbyte);
                dbPhotoHelper.addEntry(image);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
        }


    }
}