package id.net.gmedia.stokaudit.site;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import id.net.gmedia.stokaudit.R;
import id.net.gmedia.stokaudit.detail_barang.SavedActivity;

import java.util.List;

class SiteAdapter extends BaseAdapter {

    private final Context context;
    private final List<SiteResponseData> customer;

    SiteAdapter(Context context, List<SiteResponseData> customer) {
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

        final SiteResponseData siteResponseData = customer.get(position);
        textView.setText(siteResponseData.nama_site);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SavedActivity.class);
                i.putExtra("Site", siteResponseData.id_site);
                i.putExtra("Nama_Site", siteResponseData.nama_site);
                context.startActivity(i);
            }
        });

        return convertView;
    }

    void addMore(List<SiteResponseData> moreData) {
        customer.addAll(moreData);
        notifyDataSetChanged();
    }
}
