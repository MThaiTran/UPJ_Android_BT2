package com.example.android_bt2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miniprojectbt2.R;
import com.example.miniprojectbt2.data.entities.ProductEntity;
import com.example.miniprojectbt2.ui.DrawableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<ProductEntity> items = new ArrayList<>();

    public ProductAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void submitList(List<ProductEntity> products) {
        items.clear();
        items.addAll(products);
        notifyDataSetChanged();
    }

    public ProductEntity getItemAt(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).productId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_product, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductEntity product = items.get(position);
        holder.textName.setText(product.name);
        holder.textDescription.setText(product.description);
        holder.textMeta.setText(String.format(Locale.US, "%,.0f đ • %s • Còn %d", product.price, product.unit, product.stock));
        holder.imageProduct.setImageResource(DrawableUtils.resolve(context, product.imageName, R.drawable.img));
        return convertView;
    }

    private static class ViewHolder {
        final ImageView imageProduct;
        final TextView textName;
        final TextView textDescription;
        final TextView textMeta;

        ViewHolder(View view) {
            imageProduct = view.findViewById(R.id.imageProduct);
            textName = view.findViewById(R.id.textProductNameRow);
            textDescription = view.findViewById(R.id.textProductDescriptionRow);
            textMeta = view.findViewById(R.id.textProductMetaRow);
        }
    }
}

