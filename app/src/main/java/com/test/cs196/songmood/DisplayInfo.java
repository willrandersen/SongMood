package com.test.cs196.songmood;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.service.exception.RequestTooLargeException;
import com.ibm.watson.developer_cloud.service.exception.ServiceResponseException;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import org.apache.commons.lang3.StringUtils;
import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.MusixMatchException;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DisplayInfo extends AppCompatActivity {

    LinearLayout layout;
    String lyrics;
    String searchedSong;
    String searchedArtist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_info);

        layout = findViewById(R.id.displayLayout);

        extractPrimitives();

        new APISearch().execute(searchedSong, searchedArtist);
    }

    public static Intent createIntent(Context previousActivity, String song_search, String artist_search)
    {
        Intent change = new Intent(previousActivity,DisplayInfo.class);
        change.putExtra("Song",song_search);
        change.putExtra("Artist",artist_search);
        return change;
    }
    public void extractPrimitives() {
        Intent myIntent = getIntent();
        searchedSong = myIntent.getStringExtra("Song");
        searchedArtist = myIntent.getStringExtra("Artist");


    }



    private class APISearch extends AsyncTask<String, String, Object> {
        @Override
        protected Object doInBackground(String... strings) {
            //Search for songs with title and process them into array
            String API_code = "17360750fee90ecd201eb5fde8a4b6d7";
            MusixMatch musicCaller = new MusixMatch(API_code);
            try {
                Track searchResults = musicCaller.getMatchingTrack(strings[0],strings[1]);
                Lyrics l = musicCaller.getLyrics(searchResults.getTrack().getTrackId());

                publishProgress(searchResults.getTrack().getTrackName(), searchResults.getTrack().getArtistName(),l.getLyricsBody());

                IamOptions options = new IamOptions.Builder()
                        .apiKey("ZWE1fkMnnTf6KhEiorTfyAWHZ21iCSgAJVU741-zzVra")
                        .build();
                ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21", options);
                toneAnalyzer.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");


                ToneOptions toneOptions = new ToneOptions.Builder()
                        .text(l.getLyricsBody())
                        .build();

                ToneAnalysis toneAnalysis = toneAnalyzer.tone(toneOptions).execute();

                return toneAnalysis;

            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            TextView Title = findViewById(R.id.Title);
            Title.setText(values[0]);

            TextView Artist = findViewById(R.id.artist_subtitle);
            Artist.setText(values[1]);
            Artist.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            TextView mood = findViewById(R.id.Mood_Title);
            mood.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            mood.setText("Loading Lyrics and Mood Data...");
            lyrics = values[2];
        }

        @Override
        protected void onPostExecute(Object mood) {
            super.onPostExecute(mood);
            if (mood instanceof Exception) {
                if (mood instanceof MusixMatchException) {
                    TextView Title = findViewById(R.id.Title);
                    Title.setText("Unable to Find Song");
                    return;
                }
            }
            TextView mood_line = findViewById(R.id.Mood_Title);
            mood_line.setText("Mood:");
            parsePrintJson(mood.toString());
            addToLinearLayout("Lyrics:", 18);
            addToLinearLayout(lyrics, 18);
            //new SentimentSearch().execute(l);
        }
        public void addToLinearLayout(String text, int size) {
            TextView mainText = new TextView(DisplayInfo.this);
            mainText.setTextSize(size);
            mainText.setText(text);
            mainText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            layout.addView(mainText);
        }

        public void parsePrintJson(String json) {
            if (json.contains("\"sentences_tone\"")) {
                json = json.substring(0, json.indexOf("\"sentences_tone\""));
            }
            // HashMap<String, Integer> moods = new HashMap<String, Integer>();
            int count = StringUtils.countMatches(json, "score");
            if (count == 0) {
                addToLinearLayout("No Moods were detected", 18);
            }
            for (int x = 0; x < count; x++ ) {
                String str = json.substring(json.indexOf("\"score\""), json.indexOf("}"));
                String scoreStr = str.substring(9, str.indexOf(","));
                str = json.substring(json.indexOf("\"tone_name\"") + 14);
                String toneStr = str.substring(0, str.indexOf("\""));
                json = json.substring(json.indexOf("},") + 2);

                int score = (int) (100* Double.parseDouble(scoreStr));
                //moods.put(toneStr,score);
                addToLinearLayout(toneStr + " --> " + score + " %", 18);
            }
            // return moods;
        }
    }

    /**
    private class SentimentSearch extends AsyncTask<String, Integer, Object> {
        @Override
        protected Object doInBackground(String... strings) {
            try{
                IamOptions options = new IamOptions.Builder()
                        .apiKey("ZWE1fkMnnTf6KhEiorTfyAWHZ21iCSgAJVU741-zzVra")
                        .build();
                ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21", options);
                toneAnalyzer.setEndPoint("https://gateway.watsonplatform.net/tone-analyzer/api");


                ToneOptions toneOptions = new ToneOptions.Builder()
                        .text(strings[0])
                        .build();

                ToneAnalysis toneAnalysis = toneAnalyzer.tone(toneOptions).execute();

                return toneAnalysis;

            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object tone) {
            super.onPostExecute(tone);
            if (tone instanceof Exception) {
                //Add Error Handling
            }
            //HashMap<String,Integer> moods = parseFormatJson((String) tone);
           // Set<String> keys = moods.keySet();
            parsePrintJson((String) tone);
            addToLinearLayout(lyrics, 12);
        }
    }
*/

}
