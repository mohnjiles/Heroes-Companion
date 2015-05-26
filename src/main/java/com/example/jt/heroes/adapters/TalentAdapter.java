package com.example.jt.heroes.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.example.jt.heroes.R;
import com.example.jt.heroes.TalentFragment;
import com.example.jt.heroes.Utils;
import com.example.jt.heroes.models.Hero;
import com.example.jt.heroes.models.Talent;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JT on 2/25/2015.
 */
public class TalentAdapter extends ObservableRecyclerView.Adapter<TalentAdapter.ViewHolder> implements Filterable {

    private Hero hero;
    private int lastPosition = -1;
    private Context context;
    private List<Talent> talentList;
    private List<Talent> originalList;
    private TalentFragment talentFragment;
    private MultiSelector multiSelector;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends ObservableRecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        @InjectView(R.id.tvTalentName)
        public TextView tvTalentName;
        @InjectView(R.id.tvTalentDescription)
        public TextView tvTalentDescription;

        @InjectView(R.id.ivTalentIcon)
        public ImageView ivTalentIcon;

        private Talent talent;
        private ImageView ivLvl1;
        private ImageView ivLvl4;
        private ImageView ivLvl7;
        private ImageView ivLvl10;
        private ImageView ivLvl13;
        private ImageView ivLvl16;
        private ImageView ivLvl20;
        private Context context;

        public ViewHolder(View v, TalentFragment fragment, Context context) {
            super(v);
            v.setClickable(true);
            v.setOnClickListener(this);
            ivLvl1 = fragment.getIvLvl1();
            ivLvl4 = fragment.getIvLvl4();
            ivLvl7 = fragment.getIvLvl7();
            ivLvl10 = fragment.getIvLvl10();
            ivLvl13 = fragment.getIvLvl13();
            ivLvl16 = fragment.getIvLvl16();
            ivLvl20 = fragment.getIvLvl20();
            this.context = context;

            ButterKnife.inject(this, v);
        }

        public void bindTalent(Talent talent) {
            this.talent = talent;
        }

        @Override
        public void onClick(View view) {
            if (talent != null) {
                int talentTier = talent.getTalentTier();
                switch (talentTier) {
                    case 1:
                        ivLvl1.setImageResource(Utils.getResourceIdByName(
                                context,
                                Utils.formatSpellImageName(talent.getName())));
                        break;
                    case 4:
                        ivLvl4.setImageResource(Utils.getResourceIdByName(
                                context,
                                Utils.formatSpellImageName(talent.getName())));
                        break;
                    case 7:
                        ivLvl7.setImageResource(Utils.getResourceIdByName(
                                context,
                                Utils.formatSpellImageName(talent.getName())));
                        break;
                    case 10:
                        ivLvl10.setImageResource(Utils.getResourceIdByName(
                                context,
                                Utils.formatSpellImageName(talent.getName())));
                        break;
                    case 13:
                        ivLvl13.setImageResource(Utils.getResourceIdByName(
                                context,
                                Utils.formatSpellImageName(talent.getName())));
                        break;
                    case 16:
                        ivLvl16.setImageResource(Utils.getResourceIdByName(
                                context,
                                Utils.formatSpellImageName(talent.getName())));
                        break;
                    case 20:
                        ivLvl20.setImageResource(Utils.getResourceIdByName(
                                context,
                                Utils.formatSpellImageName(talent.getName())));
                        break;
                    default:
                        break;
                }
            }
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public TalentAdapter(Context context, Hero hero, TalentFragment fragment, MultiSelector selector) {
        this.hero = hero;
        this.context = context;
        talentFragment = fragment;
        multiSelector = selector;
        talentList = hero.getTalents();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TalentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.talent, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v, talentFragment, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //holder.mTextView.setText(heroList.get(position).getName());
        Talent talent = talentList.get(position);
        holder.bindTalent(talent);
        holder.tvTalentName.setText(talent.getName().toUpperCase(Locale.getDefault()));
        holder.tvTalentName.setTag(talent);
        holder.tvTalentDescription.setText(talent.getDescription());
        holder.ivTalentIcon.setImageResource(Utils.getResourceIdByName(context, Utils.formatSpellImageName(talent.getName())));
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return talentList.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Talent> results = new ArrayList<>();

                if (originalList == null)
                    originalList = new ArrayList<>(talentList);

                if (charSequence != null) {
                    if (originalList.size() > 0) {
                        for (final Talent talent : originalList) {
                            if (charSequence.toString().equals("All")) {
                                results.add(talent);
                            } else if (talent.getTalentTier() == Integer.parseInt(charSequence.toString())) {
                                results.add(talent);
                            }
                        }
                    }

                    oReturn.values = results;
                }

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                talentList = (ArrayList<Talent>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
