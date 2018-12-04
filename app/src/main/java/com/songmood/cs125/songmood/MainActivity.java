package com.songmood.cs125.songmood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText song_search;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        song_search = (EditText)(findViewById(R.id.song_search));
        submit = (Button)(findViewById(R.id.button));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SearchResults.createIntent(MainActivity.this, song_search.getText().toString());
                startActivity(intent);
            }
        });
    }
}
