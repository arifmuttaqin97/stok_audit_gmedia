package com.example.arif.barangkonsumen.list_barang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.arif.barangkonsumen.R;
import com.example.arif.barangkonsumen.data_barang.DataBarangActivity;

import java.util.List;

class ListBarangAdapter extends BaseAdapter {

    private final Context context;
    private final List<ListBarangResponseData> customer;
    private final String id_lokasi;
    private final String nama_site;

    ListBarangAdapter(Context context, List<ListBarangResponseData> customer, String id_lokasi, String nama_site) {
        this.context = context;
        this.customer = customer;
        this.id_lokasi = id_lokasi;
        this.nama_site = nama_site;
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

        final ListBarangResponseData listBarangResponseData = customer.get(position);
        textView.setText(listBarangResponseData.nama_barang);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DataBarangActivity.class);
                i.putExtra("Master", listBarangResponseData.nama_barang);
                i.putExtra("KodeMaster", listBarangResponseData.id_header);
                i.putExtra("id_lokasi", id_lokasi);
                i.putExtra("nama_site", nama_site);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    void addMore(List<ListBarangResponseData> moreData) {
        customer.addAll(moreData);
        notifyDataSetChanged();
    }
}
