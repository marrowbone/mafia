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
import com.morrowbone.mafiacards.app.activity.CreatorActivity;
import com.morrowbone.mafiacards.app.model.Card;
import com.morrowbone.mafiacards.app.utils.Constants;

import java.util.List;

public class CreateDeckArrayAdapter extends ArrayAdapter<Card> {

    private static final Integer layout_id = R.layout.view_creator_cart;
    private final Typeface mTypeFace;
    private LayoutInflater mInflater;
    private CreatorActivity mContext;
    private static Integer mCardCount = 0;

    public CreateDeckArrayAdapter(Context context, List<Card> values) {
        super(context, layout_id, values);
        mInflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mContext = (CreatorActivity) context;
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

            holder.image = (ImageView) convertView.findViewById(R.id.game_type_image);

            holder.decrement = convertView.findViewById(R.id.decrement);
            holder.increment = convertView.findViewById(R.id.increment);
            holder.cardCount = (TextView) convertView.findViewById(R.id.card_count);
            holder.cardCount.setTypeface(mTypeFace);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.title.setText(item.getRoleNameStringId());
        holder.image.setImageResource(item.getCartFrontSideImageId());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final Integer[] curCount = {item.getCountInDeck()};

        holder.cardCount.setText(String.valueOf(curCount[0]));
        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curCount[0] > 0) {
                    curCount[0]--;
                    holder.cardCount.setText(String.valueOf(curCount[0]));
                    item.setCountInDeck(curCount[0]);

                    mCardCount--;
                    mContext.setCardCount(mCardCount);
                }
            }
        });

        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curCount[0] < 100) {
                    curCount[0]++;
                    item.setCountInDeck(curCount[0]);
                    holder.cardCount.setText(String.valueOf(curCount[0]));

                    mCardCount++;
                    mContext.setCardCount(mCardCount);
                }
            }
        });

        return convertView;
    }

    /**
     * View holder for the views we need access to
     */
    private static class Holder {
        private TextView title;
        private ImageView image;
        private View increment;
        private View decrement;
        private TextView cardCount;
    }

}
