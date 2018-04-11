package com.hpacandi.zadatak;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Keyboard extends FrameLayout {

    private EditText pinField;

    public Keyboard(Context context) {
        super(context);
        init();
    }

    public Keyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Keyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.keyboard, this);

        pinField = findViewById(R.id.pin_field);
        TextView keyOne = findViewById(R.id.keyboard_key_1);
        TextView keyTwo = findViewById(R.id.keyboard_key_2);
        TextView keyThree = findViewById(R.id.keyboard_key_3);
        TextView keyFour = findViewById(R.id.keyboard_key_4);
        TextView keyFive = findViewById(R.id.keyboard_key_5);
        TextView keySix = findViewById(R.id.keyboard_key_6);
        TextView keySeven = findViewById(R.id.keyboard_key_7);
        TextView keyEight = findViewById(R.id.keyboard_key_8);
        TextView keyNine = findViewById(R.id.keyboard_key_9);
        TextView keyZero = findViewById(R.id.keyboard_key_0);
        TextView keyConfirm = findViewById(R.id.keyboard_key_confirm);
        TextView keyDelete = findViewById(R.id.keyboard_key_backspace);

        keyOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("1");
            }
        });
        keyTwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("2");
            }
        });
        keyThree.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("3");
            }
        });
        keyFour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("4");
            }
        });
        keyFive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("5");
            }
        });
        keySix.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("6");
            }
        });
        keySeven.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("7");
            }
        });
        keyEight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("8");
            }
        });
        keyNine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("9");
            }
        });
        keyZero.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pinField.append("0");
            }
        });
        keyDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = pinField.getText().length();
                if (length > 0) {
                    pinField.getText().delete(length - 1, length);
                }
            }
        });


    }

}
