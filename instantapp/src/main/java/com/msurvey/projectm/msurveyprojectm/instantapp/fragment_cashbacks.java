package com.msurvey.projectm.msurveyprojectm.instantapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragment_cashbacks extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View cashbackFragmentView = inflater.inflate(R.layout.fragment_cashback, container, false);


        return cashbackFragmentView;
    }
}
