package com.morrowbone.mafiacards.app.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.RolesArrayAdapter;
import com.morrowbone.mafiacards.app.data.AbstractCard;
import com.morrowbone.mafiacards.app.data.CardRepository;
import com.morrowbone.mafiacards.app.utils.InjectorUtils;

import java.util.List;

public class RulesActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        init();
    }

    private void init() {
        AsyncTask<Void, Void, List<AbstractCard>> asyncTask = new AsyncTask<Void, Void, List<AbstractCard>>() {
            @Override
            protected List<AbstractCard> doInBackground(Void... voids) {
                CardRepository cardRepository = InjectorUtils.INSTANCE.getCardRepository(RulesActivity.this);
                return cardRepository.getCards();
            }

            @Override
            protected void onPostExecute(List<AbstractCard> abstractCards) {
                RolesArrayAdapter adapter = new RolesArrayAdapter(RulesActivity.this, abstractCards);
                ListView listView = (ListView) findViewById(R.id.game_type_listview);
                listView.setAdapter(adapter);
            }
        };
        asyncTask.execute();
    }
}
