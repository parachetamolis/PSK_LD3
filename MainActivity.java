package com.example.egzas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Pagr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = findViewById(R.id.enterInfoButton);
        Button button2 = findViewById(R.id.readInfoButton);
        TextView infoFIle = findViewById(R.id.infoFromFileText);
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Pagr.this, "default").setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("Informacija issiusta").setPriority(NotificationCompat.PRIORITY_DEFAULT);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Pagr.this, SecondActivity.class);
                startActivity(intent); }});
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    infoFIle.setText(file_read());
                } catch (IOException e){
                    e.printStackTrace(); }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Failo informacija");
                intent.putExtra(Intent.EXTRA_TEXT, infoFIle.getText().toString());
                startActivity(Intent.createChooser(intent, "Siusti"));
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Pagr.this);
                notificationManager.notify(1, builder.build());



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {switch(item.getItemId()) {
        case R.id.aboutButton:
            Intent intent = new Intent(Pagr.this, ThirdActivity.class);
            startActivity(intent);
            return(true);
        case R.id.exitButton:
            this.finishAffinity();
            return(true);
    } return(super.onOptionsItemSelected(item));
    }

    public String file_read() throws IOException{

        File path = getFilesDir();
        File file = new File(path, "info.txt");

        int x = (int) file.length();
        byte[] y = new byte[x];

        FileInputStream in = new FileInputStream(file);
        try {
            in.read(y);
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            in.close(); }

        String contents = new String(y);

        return contents;
    }

  // notif
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "default";
            String description = "default";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
