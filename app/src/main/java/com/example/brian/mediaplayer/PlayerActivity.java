package com.example.brian.mediaplayer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlayerActivity extends AppCompatActivity {
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        Uri mp4VideoUri = Uri.parse("http://www.bok.net/dash/tears_of_steel/cleartext/stream.mpd");

        initPlayer();
        preparePlayer(mp4VideoUri);
    }

    private void initPlayer() {
        // Create default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        playerView = (SimpleExoPlayerView) findViewById(R.id.player);
        playerView.setPlayer(player);
    }

    private void preparePlayer(Uri mp4VideoUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "MediaPlayer"), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new DashMediaSource(mp4VideoUri, dataSourceFactory, new DefaultDashChunkSource.Factory(dataSourceFactory), null, null);
        // Prepare the player with the source.
        player.prepare(videoSource);
    }
}
