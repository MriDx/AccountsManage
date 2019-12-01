package com.mridx.accountsmanage.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mridx.accountsmanage.R;
import com.mridx.accountsmanage.dataholder.ExpensesData;
import com.mridx.accountsmanage.lib.PercentageCalculator;
import com.mridx.accountsmanage.lib.TimesAgo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TotalExpensesByDate extends AppCompatActivity {

    private CardView topCard;

    private RecyclerView totalExpensesHolder;
    private ArrayList<String> some = new ArrayList<>();
    private ArrayList<ExpensesData> expensesList = new ArrayList<>();
    private ConstraintLayout startDateSelector, endDateSelector;
    private AppCompatTextView startDateView, endDateView;
    private AppCompatTextView totalSummery;

    private Calendar calendar;
    private String startDate = null, endDate = null;
    private Timestamp start, end;
    private int mYear, mMonth, mDay;
    private long minDate, maxDate;

    private FirebaseFirestore firestore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses_by_date);

        topCard = findViewById(R.id.topcard);
        topCard.setOnClickListener(v -> Toast.makeText(this, "Card Cliked !", Toast.LENGTH_SHORT).show());
        totalExpensesHolder = findViewById(R.id.totalExpensesHolder);
        startDateSelector = findViewById(R.id.startDateSelector);
        endDateSelector = findViewById(R.id.endDateSelector);
        startDateView = findViewById(R.id.startDateView);
        endDateView = findViewById(R.id.endDateView);
        totalSummery = findViewById(R.id.totalSummery);


        firestore = FirebaseFirestore.getInstance();

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        startDateSelector.setOnClickListener(v -> getStartDate());
        endDateSelector.setOnClickListener(v -> getEndDate());

        PopulateView();

    }

    private void getStartDate() {

        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    String sDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year + " 00:00:00";
                    if (convertToMilis(dayOfMonth, monthOfYear, year)) {
                        //startDate.setText(sDate);
                    }

                    String toshow = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(sDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long s = date.getTime();
                    Log.d("mridx", "timestamp: " + s);
                    startDate = String.valueOf(s);
                    start = new Timestamp(date);

                    if (getFirestoreDate(start, end)) {
                        loadData();
                    } else {
                        Toast.makeText(TotalExpensesByDate.this, "Select End Date", Toast.LENGTH_SHORT).show();
                    }
                    startDateView.setText(toshow);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

       // }
    }

    private boolean convertToMilis(int dayOfMonth, int monthOfYear, int year) {
        String myDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth + " 00:00:00";
        Log.d("mridx", "This is my min date before " + myDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = null;
        try {
            date = sdf.parse(myDate);
            Log.d("mridx", "this is my min date before conv " + date);
            minDate = date.getTime();
            //startDate = String.valueOf(minDate);
            Log.d("mridx", "This is my min mili " + minDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean getFirestoreDate(Timestamp sDate, Timestamp endDate) {

        if (sDate == null) {
            return false;
        } else if (endDate == null) {
            return false;
        } else {
            return true;
        }
    }

    private void getEndDate() {

       // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    String eDate = (dayOfMonth + 1) + "/" + (monthOfYear + 1) + "/" + year + " 00:00:00";
                    String toshow = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(eDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long s = date.getTime();
                    endDate = String.valueOf(s);

                    end = new Timestamp(date);
                    //n = new Timestamp(date);

                    if (getFirestoreDate(start, end)) {
                        loadData();
                    } else {
                        Toast.makeText(TotalExpensesByDate.this, "Select Start Date", Toast.LENGTH_SHORT).show();
                    }
                    endDateView.setText(toshow);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(minDate);
            datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
            datePickerDialog.show();
            Log.d("mridx", "This is " + calendar.getTimeInMillis());

       // }
    }

    private void loadData() {
        Query query = firestore.collection("users").document("uuid")
                .collection("expenses")
                .whereGreaterThanOrEqualTo("timestamp", start)
                .whereLessThanOrEqualTo("timestamp", end)
                .orderBy("timestamp", Query.Direction.DESCENDING);
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        float total = 0;
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Log.d("kaku", "loadData: " + snapshot.getData());
                            String amount = snapshot.get("amount").toString();
                            amount = new PercentageCalculator().getDecimial(amount);
                            String type = snapshot.get("type").toString();
                            String time = snapshot.get("time").toString();
                            time = new TimesAgo().getTime(time);
                            Timestamp timestamp = snapshot.getTimestamp("timestamp");
                            Date d = timestamp.toDate();
                            String date = DateFormat.getDateInstance(DateFormat.MEDIUM).format(d);
                            ExpensesData data = new ExpensesData(time, date, type, amount);
                            expensesList.add(data);
                            total = total + Float.parseFloat(amount);
                        }
                        PopulateView();
                        ShowTotalSummery(total);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to read data !", Toast.LENGTH_SHORT).show());
    }

    private void ShowTotalSummery(float total) {
        totalSummery.setVisibility(View.VISIBLE);
        totalSummery.setText("Total Expenses During The Range is " + "\u20B9" + total);
    }


    private void PopulateView() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        totalExpensesHolder.setLayoutManager(layoutManager);
        ExpensesAdapter expensesAdapter = new ExpensesAdapter(expensesList);
        totalExpensesHolder.setAdapter(expensesAdapter);


    }


    class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.MyViewHolder> {

        private ArrayList<ExpensesData> expensesList = new ArrayList<>();

        public ExpensesAdapter(ArrayList<ExpensesData> expensesList) {
            this.expensesList = expensesList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_view, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ExpensesData expensesData = expensesList.get(position);
            holder.dateView.setText(expensesData.getDate() + "\n" + expensesData.getTime());
            holder.typeView.setText(expensesData.getType());
            holder.amountView.setText("\u20B9" + expensesData.getAmount());
        }

        @Override
        public int getItemCount() {
            return expensesList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private AppCompatTextView dateView, typeView, amountView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                dateView = itemView.findViewById(R.id.dateView);
                typeView = itemView.findViewById(R.id.typeView);
                amountView = itemView.findViewById(R.id.amountView);
            }
        }
    }

    public void back(View v) {
        onBackPressed();
    }

}
