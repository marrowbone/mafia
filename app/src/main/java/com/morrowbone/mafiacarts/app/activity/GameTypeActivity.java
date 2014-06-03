package com.morrowbone.mafiacarts.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.morrowbone.mafiacarts.app.R;
import com.morrowbone.mafiacarts.app.adapter.GameTypesArrayAdapter;
import com.morrowbone.mafiacarts.app.model.GameType;
import com.morrowbone.mafiacarts.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GameTypeActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    private ArrayAdapter mAdapter;
    private List<GameType> mGameTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);

        init();
    }

    private void init() {
        mGameTypes = new ArrayList<GameType>();

        GameType gameType = new GameType(R.drawable.mafia_image_main_ver1, R.string.test_mafia_title, R.string.mafia_short_rules);
        GameType gameType2 = new GameType(R.drawable.mafia_image_main_ver1, R.string.test_mafia_title, R.string.mafia_short_rules);
        GameType gameType3 = new GameType(R.drawable.mafia_image_main_ver1, R.string.test_mafia_title, R.string.mafia_short_rules);

        mGameTypes.add(gameType);
        mGameTypes.add(gameType2);
        mGameTypes.add(gameType3);

        mAdapter = new GameTypesArrayAdapter(this, mGameTypes);

        ListView listView = (ListView) findViewById(R.id.game_type_listview);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Integer selectedGameId = i;
        Intent intent = new Intent(this, ShowUserCartActivity.class);
        intent.putExtra(Constants.EXTRA_GAME_ID, selectedGameId);
        startActivity(intent);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.game_type, menu);
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
