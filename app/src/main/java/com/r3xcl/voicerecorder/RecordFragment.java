package com.r3xcl.voicerecorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.security.Permission;


public class RecordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;

    private ImageView btn_list, btn_record;

    private boolean isRecording = false;

    private String recPermission = Manifest.permission.RECORD_AUDIO;
    private int codePermission = 21;

    private MediaRecorder mediaRecorder;
    private String recordFile;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
                    stopRecording();

                    btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_dis_rec, null));
                    isRecording = false;
                }else{
                    //Start
                    startRecording();
                    
                    if(checkPermissions()){
                        btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_en_rec, null));
                        isRecording = true;
                    }
                }
                break;

        }
    }

    private void startRecording() {
        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        recordFile = "filename.3gp";

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordPath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    private boolean checkPermissions() {
        if(ActivityCompat.checkSelfPermission(getContext(), recPermission) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            ActivityCompat.requestPermissions(getActivity(), new String[]{recPermission}, codePermission);
            return false;
        }
    }
}