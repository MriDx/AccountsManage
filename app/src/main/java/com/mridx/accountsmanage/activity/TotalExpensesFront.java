package com.mridx.accountsmanage.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.mridx.accountsmanage.R;

public class TotalExpensesFront extends AppCompatActivity {

    private CardView card1, card2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_expenses_front);


        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card1.setOnClickListener(v -> startActivity(new Intent(this, TotalExpensesByDate.class)));


    }
}
