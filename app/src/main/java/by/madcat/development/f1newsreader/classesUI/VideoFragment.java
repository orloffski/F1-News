package by.madcat.development.f1newsreader.classesUI;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class VideoFragment extends YouTubePlayerSupportFragment {

    private static final String TOU_TUBE_API_KEY = "218786016769-31crjvmu00iictdkmelo7t9tdtp9metj.apps.googleusercontent.com";

    public VideoFragment() { }

    public static VideoFragment newInstance(String url) {

        VideoFragment f = new VideoFragment();

        Bundle b = new Bundle();
        b.putString("url", url);

        f.setArguments(b);
        f.init();

        return f;
    }

    private void init() {

        initialize("TOU_TUBE_API_KEY", new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                Toast.makeText(getContext(), "player error: " + arg1.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
                player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                if (!wasRestored) {
                    player.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                        @Override
                        public void onPlaying() {
                            if(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("fullscreen_video", false))

                                player.setFullscreen(true);
                        }

                        @Override
                        public void onPaused() {

                        }

                        @Override
                        public void onStopped() {

                        }

                        @Override
                        public void onBuffering(boolean b) {

                        }

                        @Override
                        public void onSeekTo(int i) {

                        }
                    });
                    player.cueVideo(getArguments().getString("url"));
                }
            }
        });
    }
}
