package com.example.brian.mediaplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.Map;
import java.util.UUID;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PlayerActivity extends AppCompatActivity {
    SimpleExoPlayerView playerView;
    SimpleExoPlayer player;
    private static final String PROXY_SERVER = "https://pure-meadow-83313.herokuapp.com/proxy";
    private static final UUID WIDEVINE_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        Intent intent = getIntent();

        Uri mp4VideoUri = Uri.parse(intent.getStringExtra("url"));

        initPlayer(intent.getBooleanExtra("drm", false));
        preparePlayer(mp4VideoUri);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (player != null) {
            player.release();
        }
    }

    private void initPlayer(boolean drm) {
        // Create default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // Create default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // Create the player
        if (drm == false) {
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
        } else {
            Toast.makeText(this, "This is DRM", Toast.LENGTH_SHORT).show();
            
            try {
                player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl, buildDrmSessionManager(WIDEVINE_UUID, PROXY_SERVER, null));
            } catch (UnsupportedDrmException e) {
                e.printStackTrace();
            }
        }

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

        // start play
        player.setPlayWhenReady(true);
    }

    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(UUID uuid, String licenseUrl, Map<String, String> keyRequestProperties) throws UnsupportedDrmException {
        if (Util.SDK_INT < 18) {
            return null;
        }

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        HttpDataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "MediaPlayer"), bandwidthMeter);

        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, dataSourceFactory, keyRequestProperties);
        return new DefaultDrmSessionManager<>(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback, null, null, null);
    }
}
