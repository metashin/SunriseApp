package com.example.sunriseapp.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sunriseapp.Activity.GalleryActivity;
import com.example.sunriseapp.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

  private ArrayList<String> localDataSet;
  private Activity activity;

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public CardView cardViewl;

    public ViewHolder(CardView v) {
      super(v);
      cardViewl = v;
    }

  }

  public GalleryAdapter(Activity activity, ArrayList<String> dataSet) {
    this.activity = activity;
    localDataSet = dataSet;

  }

  @NonNull
  @Override
  public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
    CardView view = (CardView)LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_gallery, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
    CardView cardView = viewHolder.cardViewl;
    cardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("profilePath",localDataSet.get(viewHolder.getAdapterPosition()));
        activity.setResult(Activity.RESULT_OK,resultIntent);
        activity.finish();
      }
    });

    ImageView imageView = viewHolder.cardViewl.findViewById(R.id.imageView);
    Glide.with( activity).load(localDataSet.get(position)).centerCrop().override(200,150).into(imageView);

  }

  @Override
  public int getItemCount() {
    return localDataSet.size();
  }
}
