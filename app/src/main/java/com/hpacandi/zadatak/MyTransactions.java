package com.hpacandi.zadatak;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyTransactions extends AppCompatActivity {

    private final static int DEFAULT_ACCOUNT_ID = 1;
    ArrayList<AccountClass> accounts_list;
    ArrayList<TransactionClass> transactions_list;
    Context ctx;
    ExpandableListView exv;
    AccountClass account_selected;
    ArrayList<TransactionClass> transactions_list_filtered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytransactions);
        ctx = this;
        changeTitle();

        exv = findViewById(R.id.mytransactions_list);
        transactions_list_filtered = new ArrayList<>();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        try{
            accounts_list = (ArrayList<AccountClass>) bundle.getSerializable("accounts");
            transactions_list = (ArrayList<TransactionClass>) bundle.getSerializable("transactions");

            Collections.sort(transactions_list, new Comparator<TransactionClass>() {
                public int compare(TransactionClass o1, TransactionClass o2) {
                    return o1.getDate().compareTo(o2.getDate());
                }
            });

            setAccountData(DEFAULT_ACCOUNT_ID, accounts_list);

        }catch (Exception e){
            e.printStackTrace();
        }

        Button btt_switch_acc = findViewById(R.id.mytrans_btt_switchAcc);
        btt_switch_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        Button btt_logout = findViewById(R.id.mytrans_btt_logout);
        btt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    void setAccountData(int id, ArrayList<AccountClass> acc){

        account_selected = acc.get(id-1);

        TextView mytrans_value_IBAN = findViewById(R.id.mytrans_value_IBAN);
        TextView mytrans_lbl_currency = findViewById(R.id.mytrans_lbl_currency);
        TextView mytrans_value_currency = findViewById(R.id.mytrans_value_currency);
        mytrans_value_IBAN.setText(account_selected.getIBAN());
        mytrans_lbl_currency.setText(account_selected.getCurrency());
        mytrans_value_currency.setText(account_selected.getAmount());

        transactions_list_filtered.clear();
        for(int i = 0; i < transactions_list.size(); i++){
            if(transactions_list.get(i).getAccountID().equals(account_selected.getAccountID())){
                transactions_list_filtered.add(transactions_list.get(i));
            }
        }

        exv.setAdapter(new MyTransactionsListAdapter(ctx, transactions_list_filtered));

    }

    private void showDialog(){

        final Dialog acc_dialog = new Dialog(ctx);
        acc_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        acc_dialog.setContentView(R.layout.account_dialog_layout);
        acc_dialog.show();
        Button cancel_btn = acc_dialog.findViewById(R.id.acc_dialog_btn_back);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acc_dialog.cancel();
            }
        });

        ListView lv = acc_dialog.findViewById(R.id.acc_listview);
        AccountListAdapter adapter = new AccountListAdapter(this, R.layout.list_row, accounts_list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                AccountClass ac = (AccountClass) parent.getItemAtPosition(i);
                acc_dialog.dismiss();
                setAccountData(ac.getAccountID(), accounts_list);
            }
        });

    }

    void changeTitle(){
        SharedPreferences sp = ctx.getSharedPreferences("ZadatakAssecoSEE", Context.MODE_PRIVATE);
        String ime = sp.getString("Ime", "");
        String prezime = sp.getString("Prezime", "");
        getSupportActionBar().setTitle(ime + " " + prezime);

    }

    void logout(){
        SharedPreferences settings = ctx.getSharedPreferences("ZadatakAssecoSEE", Context.MODE_PRIVATE);
        settings.edit().remove("user_id").apply();
        Intent i = new Intent(MyTransactions.this, Login.class);
        startActivity(i);
    }
}
