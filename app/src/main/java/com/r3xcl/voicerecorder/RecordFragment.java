package com.r3xcl.voicerecorder;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class RecordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;
    private ImageView btn_list, btn_record;
    private boolean isRecording = false;

    public RecordFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        btn_list = view.findViewById(R.id.btn_record_list);
        btn_list.setOnClickListener(this);

        btn_record = view.findViewById(R.id.btn_record);
        btn_record.setOnClickListener(this);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_record_list:
                navController.navigate(R.id.action_recordFragment_to_audioListFragment);
                break;
            case R.id.btn_record:
                if(isRecording){
                    //Stop
                    btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_dis_rec, null));
                    isRecording = false;
                }else{
                    //Start
                    btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_en_rec, null));
                    isRecording = true;
                }
                break;

        }
    }
}