package com.iglin.lab4_maps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iglin.lab4_maps.db.JourneyContentProvider;
import com.iglin.lab4_maps.model.Picture;
import com.iglin.lab4_maps.model.Point;

import java.util.ArrayList;
import java.util.List;

public class NewPointActivity extends AppCompatActivity {

    private JourneyContentProvider contentProvider;
    private static final  String[] icons = new String[] { "Default", "Human", "Heart", "Plane" };

    private double lat;
    private double lng;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_point);

        lat = getIntent().getDoubleExtra("lat", 0);
        lng = getIntent().getDoubleExtra("lng", 0);

        contentProvider = new JourneyContentProvider(getApplicationContext());

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, icons);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            LinearLayout layout = (LinearLayout)findViewById(R.id.listPics);
            ImageView image = new ImageView(this);
            //image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
            // image.setMaxHeight(20);
            //image.setMaxWidth(20);
            image.setImageBitmap(imageBitmap);
            layout.addView(image);
        }
    }

    private Integer resolveIconId(String id) {
        int i;
        for (i = 0; i < icons.length; i++) {
            if (id.equals(icons[i])) break;
        }
        switch (i) {
            case 0:
                return null;
            case 1:
                return R.drawable.ic_accessibility_black_24dp;
            case 2:
                return R.drawable.ic_favorite_black_24dp;
            case 3:
                return R.drawable.ic_flight_land_black_24dp;
        }
        return null;
    }

    public void savePoint(View view) {
        EditText textEdit = (EditText) findViewById(R.id.editTextTitle);
        if (textEdit.getText() == null || textEdit.getText().length() == 0) {
            Toast.makeText(getApplicationContext(), "Title must not be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        Point point = new Point();
        point.setTitle(textEdit.getText().toString());
        textEdit = (EditText) findViewById(R.id.editTextDescr);
        point.setDescription(textEdit.getText().toString());
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        point.setIconId(resolveIconId(String.valueOf(spinner.getSelectedItem())));

        LinearLayout layout = (LinearLayout)findViewById(R.id.listPics);
        List<Picture> list = new ArrayList<>(layout.getChildCount());
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof ImageView) {
                Bitmap bitmap = ((BitmapDrawable) ((ImageView) v).getDrawable()).getBitmap();
                list.add(new Picture(bitmap));
            }
        }
        if (!list.isEmpty()) {
            point.setPics(list);
        }

        point.setLng(lng);
        point.setLat(lat);

        point = contentProvider.insertPoint(point);

        Intent intent = new Intent();
        intent.putExtra("id", point.getId());
        setResult(RESULT_OK, intent);
        onBackPressed();
    }
}
