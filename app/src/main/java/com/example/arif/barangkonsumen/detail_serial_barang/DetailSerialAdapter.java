package com.example.arif.barangkonsumen.detail_serial_barang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.arif.barangkonsumen.R;
import com.example.arif.barangkonsumen.detail_gambar.DetailGambarActivity;

import java.util.List;

public class DetailSerialAdapter extends BaseAdapter {

    private Context context;
    private List<DetailSerialResponseData> customer;

    DetailSerialAdapter(Context context, List<DetailSerialResponseData> customer) {
        this.context = context;
        this.customer = customer;
    }

    @Override
    public int getCount() {
        return customer.size();
    }

    @Override
    public Object getItem(int position) {
        return customer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.model, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textModel);

        final DetailSerialResponseData detailSerialResponseData = customer.get(position);
        textView.setText(detailSerialResponseData.serial);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailGambarActivity.class);
                i.putExtra("id_serial", detailSerialResponseData.id_serial);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    void addMore(List<DetailSerialResponseData> moreData) {
        customer.addAll(moreData);
        notifyDataSetChanged();
    }
}
