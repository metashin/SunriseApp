package com.example.sunriseapp.Activity;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunriseapp.R;
import com.example.sunriseapp.adapter.GalleryAdapter;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
  private RecyclerView recyclerView;
  private RecyclerView.Adapter nAdapter;
  private RecyclerView.LayoutManager layoutManager;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gallery);

    final int numberOfColumns = 3;

    recyclerView = (RecyclerView) findViewById(R.id.reCycleViewz);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

    nAdapter = new GalleryAdapter(this,getImagesPath(this));
    recyclerView.setAdapter(nAdapter);

    Log.e("gallset","check");

  }
  public static ArrayList<String> getImagesPath(Activity activity) {
    Uri uri;
    ArrayList<String> listOfAllImages = new ArrayList<String>();
    Cursor cursor;
    int column_index_data, column_index_folder_name;
    String PathOfImage = null;
    uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    String[] projection = { MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

    cursor = activity.getContentResolver().query(uri, projection, null,
            null, null);

    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
    column_index_folder_name = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
    while (cursor.moveToNext()) {
      PathOfImage = cursor.getString(column_index_data);

      listOfAllImages.add(PathOfImage);
    }
    return listOfAllImages;
  }

}
