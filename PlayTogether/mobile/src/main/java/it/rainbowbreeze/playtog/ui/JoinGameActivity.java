package it.rainbowbreeze.playtog.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import it.rainbowbreeze.playtog.R;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.common.MyApp;

/**
 * Created by alfredomorresi on 07/01/15.
 */
public class JoinGameActivity extends ActionBarActivity {
    private static final String LOG_TAG = JoinGameActivity.class.getSimpleName();

    public static final String EXTRA_PLAYER_NAME = "PlayerName";
    public static final String EXTRA_PLAYER_PICTURE_URL = "PlayerPictureUrl";
    public static final String EXTRA_GAME_ID = "GameID";

    @Inject ILogFacility mLogFacility;
    private String mGameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplicationContext()).inject(this);
        mLogFacility.logStartOfActivity(LOG_TAG, JoinGameActivity.class, savedInstanceState);

        setContentView(R.layout.act_joingame);

        String playerName = getIntent().getStringExtra(EXTRA_PLAYER_NAME);
        String playerProfileUrl = getIntent().getStringExtra(EXTRA_PLAYER_PICTURE_URL);
        mGameId = getIntent().getStringExtra(EXTRA_GAME_ID);

        TextView lblMessage = (TextView) findViewById(R.id.joingame_lblMessage);
        String message = String.format(getString(R.string.joingame_lblMessage), playerName, "Fosball 4th floor");
        mLogFacility.v(LOG_TAG, message);
        lblMessage.setText(message);

        CircleImageView imgProfile = (CircleImageView) findViewById(R.id.joingame_imgProfile);
        if (TextUtils.isEmpty(playerProfileUrl)) {
            imgProfile.setVisibility(View.GONE);
        } else {
            Picasso.with(getApplicationContext())
                    .load(playerProfileUrl)
                    .noFade()  // Required by CircleImageView
                    .into(imgProfile);
        }

        Button button;
        button = (Button) findViewById(R.id.joingame_btnAccept);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JoinGameActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
            }
        });
        button = (Button) findViewById(R.id.joingame_btnRefuse);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(JoinGameActivity.this, "Refused", Toast.LENGTH_SHORT).show();
            }
        });

        if (null == savedInstanceState) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        /*
        if(Build.VERSION.SDK_INT >= 21) {
            AudioAttributes.Builder attrs = new AudioAttributes.Builder();
            attrs.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            attrs.setUsage(AudioAttributes.USAGE_ALARM);
            vibrator.vibrate(1000, attrs.build());
        } else {
            vibrator.vibrate(1000);
        }
         */
        }
    }
}
