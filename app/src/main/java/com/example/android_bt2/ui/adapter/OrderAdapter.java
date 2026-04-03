package com.example.android_bt2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miniprojectbt2.R;
import com.example.miniprojectbt2.data.model.OrderSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<OrderSummary> items = new ArrayList<>();

    public OrderAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void submitList(List<OrderSummary> orders) {
        items.clear();
        items.addAll(orders);
        notifyDataSetChanged();
    }

    public OrderSummary getItemAt(int position) {
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
        return items.get(position).orderId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        OrderSummary order = items.get(position);
        holder.textTitle.setText(String.format(Locale.US, "Order #%d", order.orderId));
        holder.textMeta.setText(order.createdAt + " • " + order.status);
        holder.textTotal.setText(String.format(Locale.US, "Tổng: %,.0f đ", order.total));
        return convertView;
    }

    private static class ViewHolder {
        final TextView textTitle;
        final TextView textMeta;
        final TextView textTotal;

        ViewHolder(View view) {
            textTitle = view.findViewById(R.id.textOrderTitle);
            textMeta = view.findViewById(R.id.textOrderMeta);
            textTotal = view.findViewById(R.id.textOrderTotal);
        }
    }
}

