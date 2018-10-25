package com.appynitty.swachbharatabhiyanlibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.pojos.MenuListPojo;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Ayan Dey on 25/10/18.
 */

public class InflateMenuAdapter extends ArrayAdapter<MenuListPojo> {

    private List<MenuListPojo> menuList;
    private Context context;
    private View view;
    private ViewHolder holder;

    public InflateMenuAdapter(@NonNull Context context, @NonNull List<MenuListPojo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.menuList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_menu_card, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.menuNameTextView = view.findViewById(R.id.menu_title);
            viewHolder.menuImageView = view.findViewById(R.id.menu_icon);
            view.setTag(viewHolder);

        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        return view;
    }

    class ViewHolder {

        private TextView menuNameTextView;
        private ImageView menuImageView;
    }


}
