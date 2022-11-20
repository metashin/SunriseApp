package com.example.sunriseapp.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunriseapp.R;
import com.example.sunriseapp.adapter.GalleryAdapter;

public class GalleryActivity extends AppCompatActivity {
  private RecyclerView recyclerView;
  private RecyclerView.Adapter nAdapter;
  private RecyclerView.LayoutManager layoutManager;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gallery);
    recyclerView = (RecyclerView) findViewById(R.id.reCycleViewz);
    recyclerView.setHasFixedSize(true);
    layoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(layoutManager);

    String[] mdataSet={"dog","cat","Dragon","nama","mem"};
    nAdapter = new GalleryAdapter(mdataSet);
    recyclerView.setAdapter(nAdapter);

    Log.e("gallset","check");

  }

}
