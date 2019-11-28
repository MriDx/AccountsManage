package com.mridx.accountsmanage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mridx.accountsmanage.R;
import com.mridx.accountsmanage.activity.LoginUI;
import com.mridx.accountsmanage.activity.LoginUI1;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {

    private AppCompatImageView logoutIcon, editIcon;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private CircleImageView profile_image;
    private AppCompatTextView nameView, emailView, phoneView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.profile_ui, null);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        nameView = view.findViewById(R.id.nameView);
        emailView = view.findViewById(R.id.emailView);
        phoneView = view.findViewById(R.id.phoneView);
        profile_image = view.findViewById(R.id.profile_image);
        logoutIcon = view.findViewById(R.id.logoutIcon);
        editIcon = view.findViewById(R.id.editIcon);
        logoutIcon.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(getActivity(), LoginUI1.class));
            getActivity().finish();
        });

        fillProfile();

    }

    private void fillProfile() {
        nameView.setText(user.getDisplayName());
        emailView.setText(user.getEmail());
        Picasso.get().load(user.getPhotoUrl()).into(profile_image);
        String phone = user.getPhoneNumber();
        if (!phone.equalsIgnoreCase("")) {
            phoneView.setText(phone);
        } else {
            phoneView.setText("+91-XXXXXXXXXX");
        }
    }
}
