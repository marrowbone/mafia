package com.morrowbone.mafiacards.app.activity;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.adapter.RolesArrayAdapter;
import com.morrowbone.mafiacards.app.model.Card;
import com.morrowbone.mafiacards.app.utils.Constants;

import java.util.List;

public class RulesActivity extends FragmentActivity {
    private ArrayAdapter mAdapter;
    private List<Card> mRolesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        init();
    }

    private void init() {
        mRolesList = Constants.getRoles();


        mAdapter = new RolesArrayAdapter(this, mRolesList);

        ListView listView = (ListView) findViewById(R.id.game_type_listview);
        listView.setAdapter(mAdapter);
//        listView.setOnItemClickListener(this);
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Integer selectedGameId = i;
//        Intent intent = new Intent(this, ShowUserCartActivity.class);
//        intent.putExtra(Constants.EXTRA_CART_COUNT, selectedGameId);
//        startActivity(intent);
//    }
}
