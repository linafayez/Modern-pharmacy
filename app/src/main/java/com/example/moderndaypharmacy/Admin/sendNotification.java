package com.example.moderndaypharmacy.Admin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.moderndaypharmacy.MainActivity;
import com.example.moderndaypharmacy.R;


public class sendNotification extends Fragment {
Button send;
EditText title, text ;
int i= 0;
    public sendNotification() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        send = view.findViewById(R.id.send);
        title = view.findViewById(R.id.Title);
        text = view.findViewById(R.id.Text);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                send(title.getText().toString(),text.getText().toString());
            }
        });
    }

    private void send(String s, String text) {
//        Intent intent = new Intent(getContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //
//        // Pan
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getActivity().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),"n")
                .setContentTitle(s)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                .setContentText(text);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.notify(i++,builder.build());
    }

}