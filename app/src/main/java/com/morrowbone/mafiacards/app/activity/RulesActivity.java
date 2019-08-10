package com.morrowbone.mafiacards.app.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.RolesArrayAdapter;
import com.morrowbone.mafiacards.app.application.MafiaApp;
import com.morrowbone.mafiacards.app.data.AbstractCard;
import com.morrowbone.mafiacards.app.data.CardRepository;
import com.morrowbone.mafiacards.app.utils.InjectorUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class RulesActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        ListView listView = (ListView) findViewById(R.id.game_type_listview);
        LoadRolesAsyncTask rolesAsyncTask = new LoadRolesAsyncTask(listView);
        rolesAsyncTask.execute();
    }

    private static class LoadRolesAsyncTask extends AsyncTask<Void, Void, List<AbstractCard>> {
        private WeakReference<ListView> weakListView;

        private LoadRolesAsyncTask(ListView listView) {
            this.weakListView = new WeakReference<>(listView);
        }

        @Override
        protected List<AbstractCard> doInBackground(Void... voids) {
            CardRepository cardRepository = InjectorUtils.INSTANCE.getCardRepository(MafiaApp.getInstance());
            return cardRepository.getCards();
        }

        @Override
        protected void onPostExecute(List<AbstractCard> abstractCards) {
            ListView listView = weakListView.get();
            if (listView != null) {
                RolesArrayAdapter adapter = new RolesArrayAdapter(MafiaApp.getInstance(), abstractCards);
                listView.setAdapter(adapter);
            }
        }
    }
}
