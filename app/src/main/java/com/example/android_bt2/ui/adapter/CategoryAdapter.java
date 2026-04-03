package com.example.android_bt2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.miniprojectbt2.R;
import com.example.miniprojectbt2.data.entities.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final List<CategoryEntity> items = new ArrayList<>();

    public CategoryAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void submitList(List<CategoryEntity> categories) {
        items.clear();
        items.addAll(categories);
        notifyDataSetChanged();
    }

    public CategoryEntity getItemAt(int position) {
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
        return items.get(position).categoryId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_category, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryEntity category = items.get(position);
        holder.textName.setText(category.name);
        holder.textDescription.setText(category.description);
        return convertView;
    }

    private static class ViewHolder {
        final TextView textName;
        final TextView textDescription;

        ViewHolder(View view) {
            textName = view.findViewById(R.id.textCategoryName);
            textDescription = view.findViewById(R.id.textCategoryDescription);
        }
    }
}

