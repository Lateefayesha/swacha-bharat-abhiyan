package com.appynitty.swachbharatabhiyanlibrary.adapters.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appynitty.swachbharatabhiyanlibrary.R;
import com.appynitty.swachbharatabhiyanlibrary.pojos.EmpWorkHistoryDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.pojos.WorkHistoryDetailPojo;
import com.appynitty.swachbharatabhiyanlibrary.utils.AUtils;

import java.util.List;

/**
 * Created by Ayan Dey on 25/10/18.
 */

public class EmpInflateHistoryDetailsAdapter extends ArrayAdapter<EmpWorkHistoryDetailPojo> {

    private List<EmpWorkHistoryDetailPojo> workHistoryDetailPojoList;
    private Context context;
    private View view;
    private ViewHolder holder;

    public EmpInflateHistoryDetailsAdapter(@NonNull Context context, @NonNull List<EmpWorkHistoryDetailPojo> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.context = context;
        this.workHistoryDetailPojoList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_history_detail_card_emp, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.time = view.findViewById(R.id.history_details_time);
            viewHolder.id = view.findViewById(R.id.history_details_name);

            view.setTag(viewHolder);

        } else {
            view = convertView;
        }
        holder = (ViewHolder) view.getTag();

        if (!AUtils.isNull(workHistoryDetailPojoList) && !workHistoryDetailPojoList.isEmpty()) {
            EmpWorkHistoryDetailPojo workHistoryDetailPojo = workHistoryDetailPojoList.get(position);

            if(workHistoryDetailPojo.getType().equals("2")){
                holder.time.setBackgroundResource(R.drawable.rounded_pink_button);
                holder.time.setPadding(0,0,0,0);
                holder.id.setText(String.format("%s %s", context.getResources().getString(R.string.point_id_txt), workHistoryDetailPojo.getPointNo()));
            }else if(workHistoryDetailPojo.getType().equals("1")){
                holder.time.setBackgroundResource(R.drawable.rounded_blue_button);
                holder.time.setPadding(0,0,0,0);
                holder.id.setText(String.format("%s %s", context.getResources().getString(R.string.house_id_txt), workHistoryDetailPojo.getHouseNo()));
            }else{
                holder.time.setBackgroundResource(R.drawable.rounded_orange_button);
                holder.time.setPadding(0,0,0,0);
                holder.id.setText(String.format("%s %s", context.getResources().getString(R.string.dump_yard_id_txt), workHistoryDetailPojo.getDumpYardNo()));
            }

//            holder.time.setText(workHistoryDetailPojo.getTime());
            holder.time.setText(AUtils.getEmpTimeLineFormat(workHistoryDetailPojo.getTime()));
        }

        return view;
    }

    private class ViewHolder {

        private TextView time;
        private TextView id;
    }


}
