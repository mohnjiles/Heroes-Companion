package com.example.jt.heroes;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jt.heroes.models.Hero;
import com.example.jt.heroes.models.Talent;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JT on 2/25/2015.
 */
public class TalentAdapter extends ObservableRecyclerView.Adapter<TalentAdapter.ViewHolder> {

    private Hero hero;
    private int lastPosition = -1;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends ObservableRecyclerView.ViewHolder {
        // each data item is just a string in this case
        @InjectView(R.id.tvTalentName)
        public TextView tvTalentName;
        @InjectView(R.id.tvTalentDescription)
        public TextView tvTalentDescription;

        @InjectView(R.id.ivTalentIcon)
        public ImageView ivTalentIcon;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TalentAdapter(Context context, Hero hero) {
        this.hero = hero;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TalentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.talent, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //holder.mTextView.setText(heroList.get(position).getName());
        Talent talent = hero.getTalents().get(position);

        holder.tvTalentName.setText(talent.getName().toUpperCase(Locale.getDefault()));
        holder.tvTalentDescription.setText(talent.getDescription());
        holder.ivTalentIcon.setImageResource(Utils.getResourceIdByName(context, Utils.formatSpellImageName(talent.getName())));
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return hero.getTalents().size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
