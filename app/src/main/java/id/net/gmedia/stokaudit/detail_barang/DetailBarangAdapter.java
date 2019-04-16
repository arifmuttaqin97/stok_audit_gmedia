package id.net.gmedia.stokaudit.detail_barang;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import id.net.gmedia.stokaudit.R;
import id.net.gmedia.stokaudit.detail_serial_barang.DetailSavedActivity;

import java.util.List;

class DetailBarangAdapter extends BaseAdapter {

    private final Context context;
    private final List<DetailBarangResponseData> customer;

    DetailBarangAdapter(Context context, List<DetailBarangResponseData> customer) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.model_with_number, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.textModel);
        TextView textView2 = convertView.findViewById(R.id.numberModel);

        final DetailBarangResponseData detailBarangResponseData = customer.get(position);
        textView.setText(detailBarangResponseData.nama_barang);
        textView2.setText(detailBarangResponseData.total_serial);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailSavedActivity.class);
                i.putExtra("id_barang", detailBarangResponseData.id_barang);
                i.putExtra("id_lokasi", detailBarangResponseData.id_lokasi);
                i.putExtra("nama_barang", detailBarangResponseData.nama_barang);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    void addMore(List<DetailBarangResponseData> moreData) {
        customer.addAll(moreData);
        notifyDataSetChanged();
    }
}
