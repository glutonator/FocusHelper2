package com.filip.focushelper2.AppListPackage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.filip.focushelper2.R;

import java.util.List;

public class AppAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<AppList> listStorage;

    public AppAdapter(Context context, List<AppList> customizedListView) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return listStorage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.installed_app_list, parent, false);
            listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.list_app_name);
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.app_icon);
            listViewHolder.checkBoxInListView = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getName());
        listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());
        listViewHolder.checkBoxInListView.setChecked(listStorage.get(position).isChecked());

        //checkbox listener
        listViewHolder.checkBoxInListView.setOnClickListener((v) -> {
            listStorage.get(position).toogleChecked();
            Log.wtf("AppAdapter", listStorage.get(position).getName());
            Log.wtf("AppAdapter", "" + position);
        });
        return convertView;
    }

    static class ViewHolder {

        TextView textInListView;
        ImageView imageInListView;
        CheckBox checkBoxInListView;
    }
}
