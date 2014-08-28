package com.morrowbone.mafiacards.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.CreateDeckArrayAdapter;
import com.morrowbone.mafiacards.app.database.DatabaseHelper;
import com.morrowbone.mafiacards.app.model.Card;
import com.morrowbone.mafiacards.app.model.Deck;
import com.morrowbone.mafiacards.app.utils.Constants;

import java.util.List;

public class CreatorActivity extends Activity implements View.OnClickListener {
    private static int mCardCount = 0;
    private Button mSaveButton;
    private TextView mCardCountTextView;
    private EditText mEditText;
    private ListView mListView;
    private ArrayAdapter mArrayAdapter;
    private List<Card> mRolesList;
    private Typeface mTypeFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        mTypeFace = Typeface.createFromAsset(getAssets(), Constants.getTypeFacePath());

        mRolesList = Constants.getRoles();
        mArrayAdapter = new CreateDeckArrayAdapter(this, mRolesList);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(mArrayAdapter);

        mCardCountTextView = (TextView) findViewById(R.id.card_count_textview);
        mCardCountTextView.setTypeface(mTypeFace);
        mCardCountTextView.setText(String.valueOf(mCardCount));

        mEditText = (EditText) findViewById(R.id.deck_name);
        mEditText.setTypeface(mTypeFace);

        TextView view = (TextView) findViewById(R.id.text_above_card_count);
        view.setTypeface(mTypeFace);
        view = (TextView) findViewById(R.id.text_below_card_count);
        view.setTypeface(mTypeFace);

        mSaveButton = (Button) findViewById(R.id.save_btn);
        mSaveButton.setTypeface(mTypeFace);
        mSaveButton.setOnClickListener(this);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.creator, menu);
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
    public void setCardCount(int cardCount) {
        mCardCount = cardCount;
        mCardCountTextView.setText(String.valueOf(cardCount));
    }

    @Override
    public void onClick(View v) {
        String nameOfDeck = mEditText.getEditableText().toString();
        if (nameOfDeck.length() > 0) {
            if (mCardCount > 0) {
                DatabaseHelper databaseHelper = new DatabaseHelper(this);
                databaseHelper.saveDeck(mRolesList, nameOfDeck);

                finish();

            } else {
                showErrorDialog(R.string.error_add_some_cards);
            }
        } else {
            showErrorDialog(R.string.error_enter_name_of_deck);
        }
    }

    private void showErrorDialog(int title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

//        TextView textView = new TextView(this);
//        textView.setTypeface(mTypeFace);
//        textView.setText(R.string.error_enter_name_of_deck);
//        textView.setTextColor(android.R.color.white);
//        textView.setTextSize(getResources().getDimension(R.dimen.big_text));
//        builder.setCustomTitle(textView);
        builder.setTitle(R.string.error_enter_name_of_deck);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        builder.show();
    }
}
