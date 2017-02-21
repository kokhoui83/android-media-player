package com.example.brian.mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button playbtn1;
    Button playbtn2;
    Button playbtn3;

    private static final String URL1 = "http://www.bok.net/dash/tears_of_steel/cleartext/stream.mpd";
    private static final String URL2 = "https://s3-ap-southeast-1.amazonaws.com/brian-stream-test/trailers/clear-starwars-rouge-one.mpd";
    private static final String URL3 = "https://s3-ap-southeast-1.amazonaws.com/brian-stream-test/trailers/encrypt-starwars-rouge-one.mpd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playbtn1 = (Button) findViewById(R.id.play_tears_of_steel);

        playbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("url", URL1);
                intent.putExtra("drm", false);
                startActivity(intent);
            }
        });

        playbtn2 = (Button) findViewById(R.id.play_starwars_rouge_one);

        playbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("url", URL2);
                intent.putExtra("drm", false);
                startActivity(intent);
            }
        });

        playbtn3 = (Button) findViewById(R.id.play_starwars_rouge_one_drm);

        playbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                intent.putExtra("url", URL3);
                intent.putExtra("drm", true);
                startActivity(intent);
            }
        });
    }
}
