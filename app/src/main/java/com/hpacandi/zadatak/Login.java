package com.hpacandi.zadatak;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx = this;
        changeTitle();

        Button login_btt = findViewById(R.id.mytrans_btt_login);
        login_btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

    }

    void changeTitle(){
        SharedPreferences sp = ctx.getSharedPreferences("ZadatakAssecoSEE", Context.MODE_PRIVATE);
        String ime = sp.getString("Ime", "");
        String prezime = sp.getString("Prezime", "");
        getSupportActionBar().setTitle(ime + " " + prezime);

    }

    private void showDialog(){

        final Dialog pin_dialog = new Dialog(ctx, android.R.style.Theme_Light_NoTitleBar);
        pin_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pin_dialog.setContentView(R.layout.pin_dialog_layout);
        pin_dialog.show();
        Button cancel_btn = pin_dialog.findViewById(R.id.dialog_btn_back);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pin_dialog.cancel();
            }
        });
        final EditText dialog_enteredPIN = pin_dialog.findViewById(R.id.pin_field);
        TextView confirm_btn = pin_dialog.findViewById(R.id.keyboard_key_confirm);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = ctx.getSharedPreferences("ZadatakAssecoSEE", Context.MODE_PRIVATE);
                String pin = sp.getString("PIN", "");

                if(pin.equals(dialog_enteredPIN.getText().toString())){
                    getData(dialog_enteredPIN.getText().toString());
                }else{
                    Toast.makeText(ctx, getString(R.string.dialog_error_PIN), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void getData(final String pin){

        final ProgressDialog pd = new ProgressDialog(Login.this);
        pd.setMessage(getString(R.string.registration_progress_text));
        pd.setCancelable(false);
        pd.show();

        String url = "http://mobile.asseco-see.hr/builds/ISBD_public/Zadatak_1.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try
                        {
                            ArrayList<AccountClass> accounts_list = new ArrayList<>();
                            ArrayList<TransactionClass> transactions_list = new ArrayList<>();

                            String user_id = (String) response.get("user_id");
                            JSONArray jsonAccounts = response.getJSONArray("acounts");
                            for(int i = 0; i < jsonAccounts.length(); i++)
                            {
                                JSONObject jobj1 = jsonAccounts.getJSONObject(i);
                                String accID = (String) jobj1.get("id");
                                String accIBAN = (String) jobj1.get("IBAN");
                                String accAmount = (String) jobj1.get("amount");
                                String accCurrency = (String) jobj1.get("currency");

                                AccountClass acc = new AccountClass();
                                acc.setAccountID(Integer.parseInt(accID));
                                acc.setIBAN(accIBAN);
                                acc.setAmount(accAmount);
                                acc.setCurrency(accCurrency);
                                accounts_list.add(acc);

                                JSONArray jr1 = jobj1.getJSONArray("transactions");
                                for(int j = 0; j < jr1.length(); j++)
                                {
                                    JSONObject jobj2 = jr1.getJSONObject(j);
                                    String transID = (String) jobj2.get("id");
                                    String transDate = (String) jobj2.get("date");
                                    String transDesc = (String) jobj2.get("description");
                                    String transAmount = (String) jobj2.get("amount");

                                    TransactionClass trans = new TransactionClass();
                                    trans.setTransactionID(Integer.parseInt(transID));
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
                                    trans.setDate(dateFormat.parse(transDate));
                                    trans.setDescription(transDesc);
                                    trans.setAmount(transAmount);
                                    trans.setAccountID(Integer.parseInt(accID));
                                    transactions_list.add(trans);

                                }
                            }

                            pd.dismiss();

                            SharedPreferences settings = ctx.getSharedPreferences("ZadatakAssecoSEE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("PIN", pin);
                            editor.putString("user_id", user_id);
                            editor.apply();

                            Intent i = new Intent(Login.this, MyTransactions.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("accounts", accounts_list);
                            bundle.putSerializable("transactions", transactions_list);
                            i.putExtras(bundle);

                            startActivity(i);

                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                            pd.cancel();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        pd.cancel();
                        Toast.makeText(ctx, getString(R.string.registration_error_connection), Toast.LENGTH_SHORT).show();
                    }
                });

        JsonRequest.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

}
