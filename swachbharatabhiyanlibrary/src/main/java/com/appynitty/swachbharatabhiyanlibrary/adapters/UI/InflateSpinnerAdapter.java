package com.appynitty.swachbharatabhiyanlibrary.adapters.UI;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Ayan Dey on 25/10/18.
 */

public class InflateSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private int resId;
    private ArrayList mObject;

    public InflateSpinnerAdapter(Context context, int res, ArrayList object) {
        this.mContext = context;
        this.resId = res;
        this.mObject = object;
    }

    @Override
    public int getCount() {
        return mObject.size();
    }

    @Override
    public Object getItem(int i) {
        return mObject.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return createItemView(position, view, viewGroup);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        return null;
    }
}
