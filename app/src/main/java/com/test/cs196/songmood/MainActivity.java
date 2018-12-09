package com.test.cs196.songmood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText artist = findViewById(R.id.artist);
        final EditText song = findViewById(R.id.song_search);
        Button search = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = DisplayInfo.createIntent(MainActivity.this, song.getText().toString(),artist.getText().toString());
                startActivity(intent);
            }
        });
    }
}
