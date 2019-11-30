package com.mridx.accountsmanage.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mridx.accountsmanage.R;

import java.util.ArrayList;

public class TotalExpenses extends AppCompatActivity {

    private Toolbar toolbar;
    
    private RecyclerView totalExpensesHolder;

    private ArrayList<String> some = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_expenses);

      /*  toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        
        
        totalExpensesHolder = findViewById(R.id.totalExpensesHolder);
        
        PopulateView();
        
    }

    private void PopulateView() {

        if (geData()) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            totalExpensesHolder.setLayoutManager(layoutManager);
            SomeAdapter someAdapter = new SomeAdapter(some);
            totalExpensesHolder.setAdapter(someAdapter);
        }

    }

    private boolean geData() {

        for (int i = 0; i< 30; i++) {
            String name = "Random Name "+i;
            some.add(name);
        }

        return true;

    }

    class SomeAdapter extends RecyclerView.Adapter<SomeAdapter.MyViewHolder> {

        private ArrayList<String> some = new ArrayList<>();

        public SomeAdapter(ArrayList<String> some) {
            this.some = some;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_view, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            //holder.nameView.setText(some.get(position));
        }

        @Override
        public int getItemCount() {
            return some.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            //private AppCompatTextView nameView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                //nameView = itemView.findViewById(R.id.nameView);
            }
        }
    }

}
