package com.example.sunriseapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunriseapp.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

  private String[] localDataSet;

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public CardView cardViewl;

    public ViewHolder(CardView v) {
      super(v);
      cardViewl = v;
    }

  }

  public GalleryAdapter(String[] dataSet) {
    localDataSet = dataSet;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    CardView view = (CardView)LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_gallery, viewGroup, false);
    final ViewHolder galleryViewHolder = new ViewHolder(view);
    return galleryViewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, final int position) {
    TextView txView = viewHolder.cardViewl.findViewById(R.id.textView3);
    txView.setText(localDataSet[position]);
  }

  @Override
  public int getItemCount() {
    return localDataSet.length;
  }
}
