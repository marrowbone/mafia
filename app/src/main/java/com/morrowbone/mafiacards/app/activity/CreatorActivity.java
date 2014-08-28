package com.morrowbone.mafiacards.app.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.CreateDeckArrayAdapter;
import com.morrowbone.mafiacards.app.model.Card;
import com.morrowbone.mafiacards.app.utils.Constants;

import java.util.List;

public class CreatorActivity extends Activity {
    private ListView mListView;
    private ArrayAdapter mArrayAdapter;
    private List<Card> mRolesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);

        mRolesList = Constants.getRoles();
        mArrayAdapter = new CreateDeckArrayAdapter(this, mRolesList);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(mArrayAdapter);
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
}
