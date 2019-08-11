package com.morrowbone.mafiacards.app.adapter;

/**
 * Created by morrow on 31.07.2014.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.morrowbone.mafiacards.app.R;
import com.morrowbone.mafiacards.app.data.AbstractCard;

import java.util.List;

public class RolesArrayAdapter extends ArrayAdapter<AbstractCard> {

    private static final Integer layout_id = R.layout.view_role;
    private LayoutInflater mInflater;

    public RolesArrayAdapter(Context context, List<AbstractCard> values) {
        super(context, layout_id, values);
        mInflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AbstractCard item = getItem(position);
        final Holder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(layout_id, parent, false);

            holder = new Holder();
            holder.title = (TextView) convertView
                    .findViewById(R.id.game_title);

            holder.description = (TextView) convertView
                    .findViewById(R.id.game_description);

            holder.image = (ImageView) convertView.findViewById(R.id.game_type_image);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.image.setImageResource(item.getImageResId());

        return convertView;
    }

    /**
     * View holder for the views we need access to
     */
    private static class Holder {
        private TextView title;
        private TextView description;
        private ImageView image;
    }

}
