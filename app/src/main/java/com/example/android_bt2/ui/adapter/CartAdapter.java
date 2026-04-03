package com.example.android_bt2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miniprojectbt2.R;
import com.example.miniprojectbt2.data.model.CartItem;
import com.example.miniprojectbt2.ui.DrawableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<CartItem> items = new ArrayList<>();

    public CartAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void submitList(List<CartItem> cartItems) {
        items.clear();
        items.addAll(cartItems);
        notifyDataSetChanged();
    }

    public List<CartItem> getItems() {
        return items;
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_cart, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem item = items.get(position);
        holder.textName.setText(item.productName);
        holder.textQuantity.setText(String.format(Locale.US, "x%d", item.quantity));
        holder.textSubtotal.setText(String.format(Locale.US, "Tạm tính: %,.0f đ", item.subtotal));
        holder.imageProduct.setImageResource(DrawableUtils.resolve(context, item.imageName, R.drawable.img));
        return convertView;
    }

    private static class ViewHolder {
        final ImageView imageProduct;
        final TextView textName;
        final TextView textQuantity;
        final TextView textSubtotal;

        ViewHolder(View view) {
            imageProduct = view.findViewById(R.id.imageCartProduct);
            textName = view.findViewById(R.id.textCartProductName);
            textQuantity = view.findViewById(R.id.textCartQuantity);
            textSubtotal = view.findViewById(R.id.textCartSubtotal);
        }
    }
}

