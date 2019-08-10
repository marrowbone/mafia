package com.morrowbone.mafiacards.app.activity

import android.os.AsyncTask
import android.os.Bundle
import android.widget.ListView
import androidx.fragment.app.FragmentActivity
import com.morrowbone.mafiacards.app.R
import com.morrowbone.mafiacards.app.adapter.RolesArrayAdapter
import com.morrowbone.mafiacards.app.application.MafiaApp
import com.morrowbone.mafiacards.app.data.AbstractCard
import com.morrowbone.mafiacards.app.utils.InjectorUtils
import kotlinx.android.synthetic.main.activity_rules.*
import java.lang.ref.WeakReference

class RulesActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        val rolesAsyncTask = LoadRolesAsyncTask(game_type_listview)
        rolesAsyncTask.execute()
    }

    private class LoadRolesAsyncTask constructor(listView: ListView) : AsyncTask<Void, Void, List<AbstractCard>>() {
        private val weakListView: WeakReference<ListView> = WeakReference(listView)

        override fun doInBackground(vararg voids: Void): List<AbstractCard> {
            val cardRepository = InjectorUtils.getCardRepository(MafiaApp.getInstance())
            return cardRepository.getCards()
        }

        override fun onPostExecute(abstractCards: List<AbstractCard>) {
            val listView = weakListView.get()
            if (listView != null) {
                val adapter = RolesArrayAdapter(MafiaApp.getInstance(), abstractCards)
                listView.adapter = adapter
            }
        }
    }
}
