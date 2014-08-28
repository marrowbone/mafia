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
import com.morrowbone.mafiacards.app.model.Card;
import com.morrowbone.mafiacards.app.utils.Constants;

import java.util.List;

public class RolesArrayAdapter extends ArrayAdapter<Card> {

    private static final Integer layout_id = R.layout.view_role;
    private final Typeface mTypeFace;
    private LayoutInflater mInflater;
    private Context mContext;

    public RolesArrayAdapter(Context context, List<Card> values) {
        super(context, layout_id, values);
        mInflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mTypeFace = Typeface.createFromAsset(mContext.getAssets(), Constants.getTypeFacePath());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Card item = getItem(position);
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

        holder.title.setText(item.getRoleNameStringId());
        holder.description.setText(item.getCardDescriptionStringId());
        holder.image.setImageResource(item.getCartFrontSideImageId());

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
