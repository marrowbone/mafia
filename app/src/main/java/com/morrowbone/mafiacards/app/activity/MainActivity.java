package com.morrowbone.mafiacards.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.database.DatabaseHelper;
import com.morrowbone.mafiacards.app.utils.Constants;

public class MainActivity extends FragmentActivity {

    private Typeface mTypeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTypeFace = Typeface.createFromAsset(getAssets(), Constants.frontPathInAssets);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Mafia");
        title.setTypeface(mTypeFace);

        Button playBtn = (Button) findViewById(R.id.play_btn);
        playBtn.setTypeface(mTypeFace);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DatabaseHelper.Initialize(MainActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
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
                        } else {
                            // TODO: show massage
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
                                // TODO: show massage
                            } else {
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
