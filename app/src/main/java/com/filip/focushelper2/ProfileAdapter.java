package com.filip.focushelper2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProfileAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<ProfileList> listStorage;

    public ProfileAdapter(Context context, List<ProfileList> customizedListView) {
        layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        ViewHolder2 listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder2();
            convertView = layoutInflater.inflate(R.layout.profiles_list, parent, false);

            listViewHolder.textInListView = (TextView)convertView.findViewById(R.id.list_profile_name);
            listViewHolder.textBlockedApps= (TextView)convertView.findViewById(R.id.textView6);
//            listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.app_icon);
//            //
//            listViewHolder.checkBoxInListView =(CheckBox)convertView.findViewById(R.id.checkBox);
            //
            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder2) convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getProfileName());
        listViewHolder.textBlockedApps.setText(listStorage.get(position).getBlockedApps());

//        listViewHolder.textInListView.setText(listStorage.get(position).getName());


//        listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());
//        //
//
//        listViewHolder.checkBoxInListView.setChecked(listStorage.get(position).isChecked());

//        //checkbox listener
//        listViewHolder.checkBoxInListView.setOnClickListener((v) ->{
//            listStorage.get(position).toogleChecked();
//            Log.wtf("AppAdapter",listStorage.get(position).getName());
//            Log.wtf("AppAdapter",""+position);
//            //todo: tutaj trzeba cos pokombinowac
//            //chyba już działa
//            //teraz trzeba jeszcze ogarnąć zapisywanie tej listy do pliku...tu i w tym drugim pliku
//        });


        return convertView;
    }

    static class ViewHolder2{

        TextView textInListView;
        TextView textBlockedApps;
//        ImageView imageInListView;
//        CheckBox checkBoxInListView;
    }


}
