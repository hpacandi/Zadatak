package com.hpacandi.zadatak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AccountListAdapter extends ArrayAdapter<AccountClass> {

    public AccountListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    AccountListAdapter(Context context, int resource, ArrayList<AccountClass> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_row, null);
        }

        AccountClass ac = getItem(position);

        try{

            TextView lblListRowIBAN = v.findViewById(R.id.lblListRowIBAN);
            TextView lblListRowCurrency = v.findViewById(R.id.lblListRowCurrency);
            lblListRowIBAN.setText(ac.getIBAN());
            lblListRowCurrency.setText(ac.getCurrency());

        }catch(Exception e){
           e.printStackTrace();
        }

        return v;
    }

}
