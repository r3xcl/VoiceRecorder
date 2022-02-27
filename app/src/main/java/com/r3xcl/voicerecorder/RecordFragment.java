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

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class RecordFragment extends Fragment implements View.OnClickListener {

    private NavController navController;

    private TextView fileNameTV;

    private ImageView btn_list, btn_record;

    private boolean isRecording = false;

    private String recPermission = Manifest.permission.RECORD_AUDIO;
    private int codePermission = 21;

    private MediaRecorder mediaRecorder;
    private String recordFile;

    private Chronometer recordTimer;


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

        recordTimer = view.findViewById(R.id.record_timer);

        fileNameTV = view.findViewById(R.id.rec_filename);
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
                    if(checkPermissions()){
                        startRecording();
                        btn_record.setImageDrawable(getResources().getDrawable(R.drawable.ic_en_rec, null));
                        isRecording = true;
                    }

                }
                break;

        }
    }

    private void startRecording() {
        recordTimer.setBase(SystemClock.elapsedRealtime());
        recordTimer.start();

        SimpleDateFormat date = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss", Locale.ROOT);
        Date now = new Date();


        String recordPath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        recordFile = date.format(now) + ".3gp";

        fileNameTV.setText("Запись, имя файла: " + recordFile);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(recordPath + "/" + recordFile);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    private void stopRecording() {
        recordTimer.setBase(SystemClock.elapsedRealtime());
        recordTimer.stop();

        fileNameTV.setText("Запись завершена, файл сохранен: " + recordFile);

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