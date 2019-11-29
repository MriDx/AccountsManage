package com.mridx.accountsmanage.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mridx.accountsmanage.NameExtractor;
import com.mridx.accountsmanage.R;
import com.mridx.accountsmanage.activity.TotalExpenses;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends Fragment {

    private AppCompatTextView nameView, totalExpensesHeading, totalToPaidHeading, totalToRecieveHeading, totalExpenses, totalToPaid, totalToReceieve, expensesStatus, toPaidStatus, toReceieveStatus;

    private AppCompatImageView addExpenses, addToPaid, addToReceieve;

    private String type;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private int month;
    private Calendar calendar;

    private CardView card1, card2, card3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dashboard_ui, null);
        setupView(view);
        return view;
    }

    private void setupView(View view) {

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String name = user.getDisplayName();

        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;

        nameView = view.findViewById(R.id.nameView);
        nameView.setText(new NameExtractor().getLastName(user.getDisplayName()));
        totalExpensesHeading = view.findViewById(R.id.totalExpensesHeading);
        totalToPaidHeading = view.findViewById(R.id.totalToPaidHeading);
        totalToRecieveHeading = view.findViewById(R.id.totalToReceivedHeading);

        totalExpenses = view.findViewById(R.id.totalExpenses);
        totalToPaid = view.findViewById(R.id.totalToPaid);
        totalToReceieve = view.findViewById(R.id.totalToReceived);


        expensesStatus = view.findViewById(R.id.expensesStatus);
        toPaidStatus = view.findViewById(R.id.paidStatus);
        toReceieveStatus = view.findViewById(R.id.receiveStatus);

        addExpenses = view.findViewById(R.id.addExpenses);
        addToPaid = view.findViewById(R.id.addToPaid);
        addToReceieve = view.findViewById(R.id.addToReceive);


        card1 = view.findViewById(R.id.card1);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);

        card1.setOnClickListener(v -> startActivity(new Intent(getActivity(), TotalExpenses.class)));


        addExpenses.setOnClickListener(v -> newExpenses());

        getDatas();
    }

    private void getDatas() {

        DocumentReference documentReference = firestore.collection("users").document("uuid")
                .collection("expenses")
                .document("currentMonth");
        documentReference.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Log.d("kaku", "getDatas: " + documentSnapshot.get("month").toString());
                            if (documentSnapshot.get("month").toString().equalsIgnoreCase(String.valueOf(month))) {
                                String expenses = documentSnapshot.get("expenses").toString();
                                totalExpenses.setText(expenses);
                            } else {
                                totalExpenses.setText("0.00");
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Log.d("kaku", "getDatas: Failed"));

        documentReference.addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.d("kaku", "getDatas: Failed to realtime sync");
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                if (documentSnapshot.get("month").toString().equalsIgnoreCase(String.valueOf(month))) {
                    String expenses = documentSnapshot.get("expenses").toString();
                    totalExpenses.setText(expenses);
                } else {
                    totalExpenses.setText("0.00");
                }
            }
        });

    }


    private void newExpenses() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.new_expenses);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        List<String> types = Arrays.asList(getResources().getStringArray(R.array.expenseTypes));
        SmartMaterialSpinner typeSelector = alertDialog.findViewById(R.id.typeSelector);
        typeSelector.setItem(types);
        //addListener(typeSelector, types, alertDialog);
        TextInputEditText amountField = alertDialog.findViewById(R.id.amountField);
        TextInputEditText descriptionField = alertDialog.findViewById(R.id.descriptionField);
        AppCompatButton submitExpense = alertDialog.findViewById(R.id.submitExpenses);
        addTextEditFieldListener(amountField, descriptionField, submitExpense);
        submitExpense.setOnClickListener(v -> {
            uploadExpense(alertDialog);
            hideKeyboard(v);
        });
        typeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeSelector.setHint(null);
                type = types.get(position);
                validateForSubmit(amountField, submitExpense);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addTextEditFieldListener(TextInputEditText amountField, TextInputEditText descriptionField, AppCompatButton submitExpense) {
        amountField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateForSubmit(amountField, submitExpense);
            }
        });
    }

    private void validateForSubmit(TextInputEditText amountField, AppCompatButton submitExpense) {
        if (filled(amountField)) {
            submitExpense.setVisibility(View.VISIBLE);
        } else {
            submitExpense.setVisibility(View.INVISIBLE);
        }
    }

    private boolean filled(TextInputEditText amountField) {
        if (type == null || type.length() == 0) {
            return false;
        } else if (amountField.getText().toString().trim().length() == 0) {
            return false;
        } else {
            return true;
        }

    }

    private void uploadExpense(AlertDialog alertDialog) {

        ProgressBar mainProgressbar = alertDialog.findViewById(R.id.mainProgressbar);
        ConstraintLayout layout1 = alertDialog.findViewById(R.id.layout1);
        ConstraintLayout layout2 = alertDialog.findViewById(R.id.layout2);
        LottieAnimationView successAnim = alertDialog.findViewById(R.id.successAnim);
        layout1.setVisibility(View.GONE);
        mainProgressbar.setVisibility(View.VISIBLE);

        TextInputEditText amountField = alertDialog.findViewById(R.id.amountField);
        String amount = amountField.getText().toString().trim();
        TextInputEditText descriptionField = alertDialog.findViewById(R.id.descriptionField);
        String description = descriptionField.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put("type", type);
        data.put("amount", amount);
        data.put("description", description);
        data.put("timestamp", FieldValue.serverTimestamp());
        data.put("time", String.valueOf(new Date().getTime()));
        firestore.collection("users").document("uuid").collection("expenses")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    mainProgressbar.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    successAnim.playAnimation();

                    alertDialog.setCancelable(true);
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

        updateMonthlyExpenses(amount);
    }

    private void updateMonthlyExpenses(String amount) {
        firestore.collection("users").document("uuid")
                .collection("expenses")
                .document("currentMonth")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                String expenses = documentSnapshot.get("expenses").toString();
                                int uploadableExpenses = 0;
                                if (documentSnapshot.get("month").toString().equalsIgnoreCase(String.valueOf(month))) {
                                    uploadableExpenses = Integer.parseInt(amount) + Integer.parseInt(expenses);
                                } else {
                                    uploadableExpenses = 0;
                                }
                                uploadMonthlyExpenses(uploadableExpenses);
                            } else {
                                uploadMonthlyExpenses(Integer.parseInt(amount));
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed !", Toast.LENGTH_SHORT).show());

    }

    private void uploadMonthlyExpenses(int uploadableExpenses) {
        Map<String, Object> expenses = new HashMap<>();
        expenses.put("expenses", uploadableExpenses);
        expenses.put("month", month);
        firestore.collection("users").document("uuid")
                .collection("expenses")
                .document("currentMonth")
                .set(expenses)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "monthly expenses uploaded !", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show());
    }

    public void hideKeyboard(View v) {
        try {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch(Exception ignored) {
        }
    }

}
