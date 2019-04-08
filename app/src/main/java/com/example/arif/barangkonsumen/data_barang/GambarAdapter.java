package com.example.arif.barangkonsumen.data_barang;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.arif.barangkonsumen.R;

import java.util.ArrayList;

public class GambarAdapter extends RecyclerView.Adapter<GambarAdapter.ViewHolder> {

    private ArrayList<String> arrayGambar;

    GambarAdapter(ArrayList<String> arrayGambar) {
        this.arrayGambar = arrayGambar;
    }

    @NonNull
    @Override
    public GambarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gambar, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GambarAdapter.ViewHolder viewHolder, int i) {
        viewHolder.gambar.setImageURI(Uri.parse(arrayGambar.get(i)));
    }

    @Override
    public int getItemCount() {
        return arrayGambar.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;

        ViewHolder(View v) {
            super(v);
            gambar = v.findViewById(R.id.gambar);
        }
    }
}
