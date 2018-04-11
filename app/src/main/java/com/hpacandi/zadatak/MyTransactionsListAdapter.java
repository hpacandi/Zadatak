package com.hpacandi.zadatak;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MyTransactionsListAdapter extends BaseExpandableListAdapter {

    Context ctx;
    private ArrayList<TransactionClass> listitem;
    List<String> headers = new ArrayList<>();
    HashMap<String, ArrayList<TransactionClass>> listDataChild;

    public MyTransactionsListAdapter(Context context, ArrayList<TransactionClass> transactions) {
        ctx = context;
        listitem = transactions;
        listDataChild = new HashMap<>();

        SimpleDateFormat format = new SimpleDateFormat("MM.yyyy.");

        for(int i = 0; i < listitem.size(); i++){
            Date d = listitem.get(i).getDate();
            String date = format.format(d);
            if(!headers.contains(date)){
                headers.add(date);
            }
        }

        for(int i = 0; i < headers.size(); i++){
            ArrayList<TransactionClass> children = new ArrayList<>();
            for(int ii = 0; ii < listitem.size(); ii++){
                Date d = listitem.get(ii).getDate();
                String date = format.format(d);
                if(date.equals(headers.get(i))){
                    TransactionClass trans = listitem.get(ii);
                    children.add(trans);
                }
            }
            listDataChild.put(headers.get(i), children);
        }

    }


    @Override
    public int getGroupCount() {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(headers.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(headers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final TransactionClass transInfo = (TransactionClass) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView lblListItemDescription = convertView.findViewById(R.id.lblListItem);
        TextView lblListItemDate = convertView.findViewById(R.id.lblListItemDate);
        TextView lblListItemValue = convertView.findViewById(R.id.lblListItemValue);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
        Date d = transInfo.getDate();
        String date = format.format(d);

        lblListItemDescription.setText(transInfo.getDescription());
        lblListItemDate.setText(date);
        lblListItemValue.setText(transInfo.getTransAmount());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }




}
