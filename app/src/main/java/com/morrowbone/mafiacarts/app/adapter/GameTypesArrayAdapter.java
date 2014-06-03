package com.morrowbone.mafiacarts.app.adapter;

/**
 * Created by morrow on 03.06.2014.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.morrowbone.mafiacarts.app.R;
import com.morrowbone.mafiacarts.app.model.GameType;

import java.util.List;

/**
 * An array adapter that knows how to render views when given Book classes
 */
public class GameTypesArrayAdapter extends ArrayAdapter<GameType> {
    private final static String frontPathInAssets = "fonts/CorleoneDue.ttf";

    private final Typeface mTypeFace;
    private LayoutInflater mInflater;
    private Context mContext;

    private static final Integer layout_id = R.layout.view_game_type;

    public GameTypesArrayAdapter(Context context, List<GameType> values) {
        super(context, layout_id, values);
        mInflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mTypeFace = Typeface.createFromAsset(mContext.getAssets(), frontPathInAssets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GameType item = getItem(position);
        final Holder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(layout_id, parent, false);

            holder = new Holder();
            holder.title = (TextView) convertView
                    .findViewById(R.id.game_title);
            holder.title.setTypeface(mTypeFace);

            holder.description = (TextView) convertView
                    .findViewById(R.id.game_description);

            holder.image = (ImageView) convertView.findViewById(R.id.game_type_image);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

//        holder.title.setText(item.getName());
//        holder.description.setText(item.getDescription());


        return convertView;
    }

    /**
     * View holder for the views we need access to
     */
    private static class Holder {
        private TextView title;
        private TextView description;
        private View infoBtn;
        private ImageView image;
    }

}
