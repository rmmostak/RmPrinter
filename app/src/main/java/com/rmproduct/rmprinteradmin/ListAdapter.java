package com.rmproduct.rmprinteradmin;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

class ListAdapter extends ArrayAdapter<UserInfo> {
    private Activity context;
    private List<UserInfo> userInfoList;

    public ListAdapter(Activity context, List<UserInfo> userInfoList) {
        super(context, R.layout.pay_list_layout, userInfoList);
        this.context = context;
        this.userInfoList = userInfoList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.pay_list_layout, null, true);


        TextView name=listViewItem.findViewById(R.id.studentName);
        TextView roll=listViewItem.findViewById(R.id.studentId);
        TextView session=listViewItem.findViewById(R.id.studentSession);

        UserInfo userInfo=userInfoList.get(position);
        name.setText(userInfo.getName());
        roll.setText(userInfo.getRoll());
        session.setText(userInfo.getSession());

        return listViewItem;
    }

}