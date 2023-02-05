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

public class MainActivity extends AppCompatActivity {

    // Sukuriamas ekrano vaizdas
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button enterInfoButton = findViewById(R.id.enterInfoButton);
        Button readInfoButton = findViewById(R.id.readInfoButton);
        TextView infoFromFileText = findViewById(R.id.infoFromFileText);

        // Sukuriamas pranešimas
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "default")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle("Informacija issiusta")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Paspaudus enterInfoButton, atidaromas SecondActivity
        enterInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
        
        // Paspaudus readInfoButton, iš failo skaitoma informacija ir išsiunčiamas pranešimas
        readInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    infoFromFileText.setText(readFromFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Failo informacija");
                intent.putExtra(Intent.EXTRA_TEXT, infoFromFileText.getText().toString());
                startActivity(Intent.createChooser(intent, "Siusti"));
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(1, builder.build());
            }
        });
    }

    // Sukuriamas nustatyumų meniu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Aprašomi nustatymų meniu mygtukų veiksmai
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.aboutButton:
            Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
            startActivity(intent);
            return true;
        case R.id.exitButton:
            this.finishAffinity();
            return true;
        }
    
        return(super.onOptionsItemSelected(item));
    }

    // Iš failo nuskaitomas tekstas
    public String readFromFile() throws IOException {

        File path = getFilesDir();
        File file = new File(path, "info.txt");

        int length = (int) file.length();
        byte[] bytes = new byte[length];

        FileInputStream in = new FileInputStream(file);
        try {
            in.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close(); 
        }

        String contents = new String(bytes);

        return contents;
    }

  // Sukuriamas kanalas pranešimams siųsti
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
