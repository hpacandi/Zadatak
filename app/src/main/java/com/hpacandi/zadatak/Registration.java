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

public class Registration extends AppCompatActivity {

    private EditText name_field;
    private EditText surname_field;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ctx = this;

        SharedPreferences sp = ctx.getSharedPreferences("ZadatakAssecoSEE", Context.MODE_PRIVATE);
        String user_id = sp.getString("user_id", "");
        if(!user_id.equals("")){
            Intent i = new Intent(Registration.this, Login.class);
            startActivity(i);
        }

        name_field = findViewById(R.id.name_field);
        surname_field = findViewById(R.id.surname_field);
        Button enterPIN_btn = findViewById(R.id.btn_next);

        enterPIN_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
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
                if(!isEmpty(name_field) && !isEmpty(surname_field)){
                    if(hasMinFourChars(dialog_enteredPIN)){
                        getData(dialog_enteredPIN.getText().toString());
                    }else{
                        Toast.makeText(ctx, getString(R.string.dialog_error_PIN), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ctx, getString(R.string.dialog_error_inputInvalid), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private boolean isEmpty(EditText editTxt) {
        return editTxt.getText().toString().trim().length() == 0;
    }

    private boolean hasMinFourChars(EditText editTxt) {
        return editTxt.getText().toString().trim().length() >= 4;
    }

    private void getData(final String pin){

        final ProgressDialog pd = new ProgressDialog(Registration.this);
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
                            editor.putString("Ime", name_field.getText().toString());
                            editor.putString("Prezime", surname_field.getText().toString());
                            editor.putString("PIN", pin);
                            editor.putString("user_id", user_id);
                            editor.apply();

                            Intent i = new Intent(Registration.this, MyTransactions.class);
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
