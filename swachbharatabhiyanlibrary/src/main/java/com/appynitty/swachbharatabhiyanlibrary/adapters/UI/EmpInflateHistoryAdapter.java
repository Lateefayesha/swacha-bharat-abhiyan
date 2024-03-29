package com.appynitty.swachbharatabhiyanlibrary.adapters.UI;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.pojos.TableDataCountPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.List;

/**
 * Created by Ayan Dey on 25/10/18.
 */

public class EmpInflateHistoryAdapter extends ArrayAdapter<TableDataCountPojo.WorkHistory> {

    private List<TableDataCountPojo.WorkHistory> historyPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;

    public EmpInflateHistoryAdapter(@NonNull Context context, @NonNull List<TableDataCountPojo.WorkHistory> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.historyPojoList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_history_card, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.date = view.findViewById(R.id.history_date_txt);
            viewHolder.month = view.findViewById(R.id.history_month_txt);
            viewHolder.houseCollection = view.findViewById(R.id.house_collection);
            viewHolder.gpCollection = view.findViewById(R.id.gp_collection);
            viewHolder.dyCollection = view.findViewById(R.id.dy_collection);
            view.setTag(viewHolder);

        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(historyPojoList) && !historyPojoList.isEmpty()) {
            TableDataCountPojo.WorkHistory workHistoryPojo = historyPojoList.get(position);
            holder.date.setText(String.valueOf(AUtils.extractDateEmp(workHistoryPojo.getDate())));
            holder.month.setText(String.valueOf(AUtils.extractMonthEmp(workHistoryPojo.getDate())));
            holder.houseCollection.setText(workHistoryPojo.getHouseCollection());
            holder.gpCollection.setText(workHistoryPojo.getPointCollection());
            holder.dyCollection.setText(workHistoryPojo.getDumpYardCollection());
        }

        return view;
    }

    private class ViewHolder {

        private TextView date;
        private TextView month;
        private TextView houseCollection;
        private TextView gpCollection;
        private TextView dyCollection;
    }


}
