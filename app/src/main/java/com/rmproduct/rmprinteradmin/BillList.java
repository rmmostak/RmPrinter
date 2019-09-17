package com.rmproduct.rmprinteradmin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BillList extends ArrayAdapter<DailyData> {

    private Activity context;
    private List<DailyData> dailyDataList;

    public BillList(Activity context, List<DailyData> dailyDataList) {
        super(context, R.layout.update_dialog, dailyDataList);
        this.context = context;
        this.dailyDataList = dailyDataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.update_dialog, null, true);

        final TextView date = listView.findViewById(R.id.date);
        final TextView topic = listView.findViewById(R.id.topic);
        final TextView bill = listView.findViewById(R.id.bill);
        final TextView pay = listView.findViewById(R.id.pay);
        final TextView adv = listView.findViewById(R.id.adv);
        final TextView due = listView.findViewById(R.id.due);

        DailyData dailyData = dailyDataList.get(position);

        date.setText("Date: " + dailyData.getDate());
        topic.setText("Topic: " + dailyData.getTopic());
        bill.setText("Bill: " + dailyData.getBill() + " TK.");
        pay.setText("Pay: " + dailyData.getPay() + " TK.");
        adv.setText("Advance: " + dailyData.getAdv() + " TK.");
        due.setText("Due: " + dailyData.getDue() + " TK.");


        return listView;
    }
}
