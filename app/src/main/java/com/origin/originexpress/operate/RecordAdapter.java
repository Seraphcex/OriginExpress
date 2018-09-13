package com.origin.originexpress.operate;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.origin.originexpress.R;
import com.origin.originexpress.data.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XunselF on 2018/9/7.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private List<Record> mRecords = new ArrayList<>();

    public  RecordAdapter(){
    }

    public void setRecords(List<Record> records){
        this.mRecords = records;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public View to_View;
        View go_View;
        TextView tv_monthDay;
        TextView tv_time;
        TextView tv_message;

        public ViewHolder(View itemView) {
            super(itemView);
            to_View = itemView.findViewById(R.id.tv_to_view);
            go_View = itemView.findViewById(R.id.tv_go_view);
            tv_monthDay = itemView.findViewById(R.id.tv_month_day);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_message = itemView.findViewById(R.id.tv_message);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record record = mRecords.get(position);
        String monthDay = record.getTime().substring(record.getTime().indexOf("-") + 1, record.getTime().indexOf(" "));
        String time = record.getTime().substring(record.getTime().indexOf(" ") + 1, record.getTime().length());
        String message = getMessage(record);



        holder.tv_monthDay.setText(monthDay);
        holder.tv_time.setText(time);
        holder.tv_message.setText(message);
    }



    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public String getMessage(Record record){
        int status = record.getStatus();
        String manager = record.getManager();
        String phone = record.getPhone();
        String license = record.getLicense();
        String location = record.getLocation();
        String tolocal = record.getTolocal();

        String message = "";

        if(status == 0){
            //被快递员收件
            message = "已被快递员 " + manager + " (" + phone + ")" + " 收件.";
        }else if(status == 1){
            message = "派送员 " + manager + " (" + phone + ")" + " 正在派件.";
        }else if(status == 2){
            message = "该件已被签收.";
        }else if(status == 3){
            message = "包裹已到达 " + location + " ，入库";
        }else if(status == 4){
            message = "包裹已从 " + location + " 出库.";
        }else if(status == 5) {
            message = "包裹已装车，司机为: " + manager  + " (" + phone + ")，车牌为: " + license + " ，" +  "正在发往 " + tolocal + " .";
        }else if(status == 6){
            message = "包裹已在 " + location + " 卸货，司机为: " + manager  + " (" + phone + ")，车牌为: " + license + " ，" +  "开始分拣.";
        }
        return message;
    }
}
