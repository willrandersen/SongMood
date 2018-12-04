package com.songmood.cs125.songmood;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchResults extends AppCompatActivity {

    static int count = 0;
    LinearLayout layout;
    String songSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        layout = findViewById(R.id.linearLayout);
        extractPrimitives();

       // new APISearch().execute(songSearch);
        addLayoutSection(songSearch, 0 + "");
    }

    public void extractPrimitives() {
        Intent myIntent = getIntent();
        songSearch = myIntent.getStringExtra("Song");

    }

    public void addLayoutSection(final String header, String subheader) {
        LinearLayout anEvent = new LinearLayout(SearchResults.this);
        anEvent.setOrientation(LinearLayout.VERTICAL);
        TextView mainText = new TextView(SearchResults.this);
        mainText.setTextSize(20);
        mainText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        mainText.setText(header);
        anEvent.addView(mainText);

        TextView subText = new TextView(SearchResults.this);
        subText.setTextSize(16);
        subText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        subText.setText(subheader);
        anEvent.addView(subText);

        TextView divider = new TextView(SearchResults.this);
        divider.setTextSize(16);
        divider.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        divider.setText("______________________________________________");
        anEvent.addView(divider);

        anEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            count++;
            addLayoutSection(header, count + "");
            }
        });

        layout.addView(anEvent);
    }

    public static Intent createIntent(Context previousActivity, String song)
    {
        Intent change = new Intent(previousActivity,SearchResults.class);
        change.putExtra("Song",song);
        return change;
    }

    private class APISearch extends AsyncTask<String, Integer, String[]> {
        @Override
        protected String[] doInBackground(String... strings) {
            //Search for songs with title and process them into array
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

        }
    }
}
