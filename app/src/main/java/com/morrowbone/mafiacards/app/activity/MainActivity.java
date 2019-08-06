package com.morrowbone.mafiacards.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.application.MafiaApp;
import com.morrowbone.mafiacards.app.constants.StatisticConstants;
import com.morrowbone.mafiacards.app.database.SystemDatabaseHelper;
import com.morrowbone.mafiacards.app.utils.Constants;
import com.morrowbone.mafiacards.app.utils.StatisticUtils;

public class MainActivity extends FragmentActivity implements StatisticConstants {

    private Button prevGameBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Mafia");

        initPrevGameBtn();

        initPlayBtn();

        initCreatorBtn();

        initRulesBtn();

        // Get tracker.
        Tracker t = ((MafiaApp) getApplication()).getTracker(
                MafiaApp.TrackerName.APP_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("Main Screen");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreviousGameInfoActivity.mDeck != null) {
            prevGameBtn.setVisibility(View.VISIBLE);
        } else {
            prevGameBtn.setVisibility(View.GONE);
        }
    }

    private void initPrevGameBtn() {
        prevGameBtn = findViewById(R.id.prev_game);
        prevGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticUtils.sendActionInfo(BUTTON_CATEGORY, "Prev game");
                Intent intent = new Intent(MainActivity.this, PreviousGameInfoActivity.class);
                startActivity(intent);
            }
        });
    }


    private void initRulesBtn() {
        Button rulesBtn = (Button) findViewById(R.id.rules_btn);
        rulesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticUtils.sendActionInfo(BUTTON_CATEGORY, "Start roles");
                Intent intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent);

            }
        });
    }


    private void initCreatorBtn() {
        Button creatorBtn = (Button) findViewById(R.id.creator_btn);
        creatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatisticUtils.sendActionInfo(BUTTON_CATEGORY, "Start creator");
                Intent intent = new Intent(MainActivity.this, CreatorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initPlayBtn() {
        Button playBtn = (Button) findViewById(R.id.play_btn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatisticUtils.sendActionInfo(BUTTON_CATEGORY, "Play in main");
                try {
                    SystemDatabaseHelper.Initialize(MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SystemDatabaseHelper databaseHelper = new SystemDatabaseHelper(MainActivity.this);
                int max = databaseHelper.getMaxNumberOfPlayer();
                final int min = databaseHelper.getMinNumberOfPlayer();

                View dialogContent = getLayoutInflater().inflate(R.layout.dialog_num_of_player, null);
                final EditText editText = (EditText) dialogContent.findViewById(R.id.input_field);
                Integer startValue = 6;
                editText.setText(startValue.toString());

                final SeekBar seekBar = (SeekBar) dialogContent.findViewById(R.id.search_bar);

                seekBar.setMax(max - min);
                seekBar.setProgress(startValue - min);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        Integer cardCount = seekBar.getProgress() + min;
                        editText.setText(cardCount.toString());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String value = editText.getText().toString();
                        if (!value.equals("")) {
                            Integer progress = Integer.valueOf(editText.getText().toString()) - min;
                            seekBar.setProgress(progress);
                        }
                    }


                });
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_number_of_player);
                builder.setView(dialogContent).setCancelable(true);
                builder.setPositiveButton(R.string.dialog_number_of_player_positive_btn, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (!editText.getText().toString().equals("")) {

                            Integer cartCount = Integer.valueOf(editText.getText().toString());
                            if (cartCount < min) {
                                showMessage(R.string.error, R.string.wrong_player_count);
                            } else {
                                StatisticUtils.sendActionInfo(BUTTON_CATEGORY, "Play in dialog");
                                Intent intent = new Intent(MainActivity.this, ShowUserCartActivity.class);
                                intent.putExtra(Constants.EXTRA_CART_COUNT, cartCount);
                                startActivity(intent);
                            }

                        }
                    }
                });

                builder.show();

            }
        });
    }

    private void showMessage(int titleResId, int messageResId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(titleResId).setMessage(messageResId).setCancelable(false);
        builder.setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        builder.show();
    }
}
