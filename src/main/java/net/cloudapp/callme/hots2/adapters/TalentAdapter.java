package net.cloudapp.callme.hots2.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import net.cloudapp.callme.hots2.Constants;
import net.cloudapp.callme.hots2.R;
import net.cloudapp.callme.hots2.TalentFragment;
import net.cloudapp.callme.hots2.Utils;
import net.cloudapp.callme.hots2.models.Hero;
import net.cloudapp.callme.hots2.models.Talent;

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

    private static final int TYPE_LVL1 = 0;
    private static final int TYPE_LVL4 = 1;
    private static final int TYPE_LVL7 = 2;
    private static final int TYPE_LVL10 = 3;
    private static final int TYPE_LVL13 = 4;
    private static final int TYPE_LVL16 = 5;
    private static final int TYPE_LVL20 = 6;

    private final ArrayList<Integer> selectedLvl1 = new ArrayList<>();
    private final ArrayList<Integer> selectedLvl4 = new ArrayList<>();
    private final ArrayList<Integer> selectedLvl7 = new ArrayList<>();
    private final ArrayList<Integer> selectedLvl10 = new ArrayList<>();
    private final ArrayList<Integer> selectedLvl13 = new ArrayList<>();
    private final ArrayList<Integer> selectedLvl16 = new ArrayList<>();
    private final ArrayList<Integer> selectedLvl20 = new ArrayList<>();

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

        @InjectView(R.id.rlTalent)
        public RelativeLayout rlTalent;

        private Talent talent;
        private ImageView ivLvl1;
        private ImageView ivLvl4;
        private ImageView ivLvl7;
        private ImageView ivLvl10;
        private ImageView ivLvl13;
        private ImageView ivLvl16;
        private ImageView ivLvl20;
        private TextView tvHelpText;
        private FloatingActionButton fabSave;
        private LinearLayout linearLayout;
        private LinearLayout linearLayout2;
        private Context context;
        private TalentAdapter talentAdapter;
        private TalentFragment fragment;

        public ViewHolder(View v, TalentFragment fragment, Context context, TalentAdapter talentAdapter) {
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
            tvHelpText = fragment.getTvHelpText();
            fabSave = fragment.getFabSave();
            linearLayout = fragment.getLinearLayout();
            linearLayout2 = fragment.getLinearLayout2();
            this.context = context;
            this.talentAdapter = talentAdapter;
            this.fragment = fragment;

            ButterKnife.inject(this, v);
        }

        public void bindTalent(Talent talent) {
            this.talent = talent;
        }

        @Override
        public void onClick(View view) {
            if (talent != null) {

                int imageResourceId = Utils.getResourceIdByName(
                        context, Utils.formatSpellImageName(talent.getName()));

                tvHelpText.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    linearLayout2.setVisibility(View.VISIBLE);
                }

                fragment.setIsHelpTextVisible(false);
                fragment.setIsLayoutVisible(true);

                int talentTier = talent.getTier();

                view.findViewById(R.id.rlTalent).setBackgroundColor(Color.parseColor("#33FFFFFF"));

                switch (talentTier) {
                    case 1:
                        if (talentAdapter.selectedLvl1.isEmpty()) {
                            talentAdapter.selectedLvl1.add(getAdapterPosition() - 1);
                        } else {
                            int oldSelected = talentAdapter.selectedLvl1.get(0);
                            talentAdapter.selectedLvl1.clear();
                            talentAdapter.selectedLvl1.add(getAdapterPosition() - 1);
                            talentAdapter.notifyItemChanged(oldSelected);
                            talentAdapter.notifyDataSetChanged();
                        }

                        fragment.setIvLvl1Res(imageResourceId);
                        ivLvl1.setImageResource(imageResourceId);
                        ivLvl1.setTag(talent);
                        break;
                    case 4:
                        if (talentAdapter.selectedLvl4.isEmpty()) {
                            talentAdapter.selectedLvl4.add(getAdapterPosition() - 2);
                        } else {
                            int oldSelected = talentAdapter.selectedLvl4.get(0);
                            talentAdapter.selectedLvl4.clear();
                            talentAdapter.selectedLvl4.add(getAdapterPosition() - 2);
                            talentAdapter.notifyItemChanged(oldSelected);
                            talentAdapter.notifyDataSetChanged();
                        }

                        fragment.setIvLvl4Res(imageResourceId);
                        ivLvl4.setImageResource(imageResourceId);
                        ivLvl4.setTag(talent);

                        break;
                    case 7:
                        if (talentAdapter.selectedLvl7.isEmpty()) {
                            talentAdapter.selectedLvl7.add(getAdapterPosition() - 3);
                        } else {
                            int oldSelected = talentAdapter.selectedLvl7.get(0);
                            talentAdapter.selectedLvl7.clear();
                            talentAdapter.selectedLvl7.add(getAdapterPosition() - 3);
                            talentAdapter.notifyItemChanged(oldSelected);
                            talentAdapter.notifyDataSetChanged();
                        }

                        fragment.setIvLvl7Res(imageResourceId);
                        ivLvl7.setImageResource(imageResourceId);
                        ivLvl7.setTag(talent);

                        break;
                    case 10:
                        if (talentAdapter.selectedLvl10.isEmpty()) {
                            talentAdapter.selectedLvl10.add(getAdapterPosition() - 4);
                        } else {
                            int oldSelected = talentAdapter.selectedLvl10.get(0);
                            talentAdapter.selectedLvl10.clear();
                            talentAdapter.selectedLvl10.add(getAdapterPosition() - 4);
                            talentAdapter.notifyItemChanged(oldSelected);
                            talentAdapter.notifyDataSetChanged();
                        }

                        fragment.setIvLvl10Res(imageResourceId);
                        ivLvl10.setImageResource(imageResourceId);
                        ivLvl10.setTag(talent);
                        break;
                    case 13:
                        if (talentAdapter.selectedLvl13.isEmpty()) {
                            talentAdapter.selectedLvl13.add(getAdapterPosition() - 5);
                        } else {
                            int oldSelected = talentAdapter.selectedLvl13.get(0);
                            talentAdapter.selectedLvl13.clear();
                            talentAdapter.selectedLvl13.add(getAdapterPosition() - 5);
                            talentAdapter.notifyItemChanged(oldSelected);
                            talentAdapter.notifyDataSetChanged();
                        }

                        fragment.setIvLvl13Res(imageResourceId);
                        ivLvl13.setImageResource(imageResourceId);
                        ivLvl13.setTag(talent);
                        break;
                    case 16:
                        if (talentAdapter.selectedLvl16.isEmpty()) {
                            talentAdapter.selectedLvl16.add(getAdapterPosition() - 6);
                        } else {
                            int oldSelected = talentAdapter.selectedLvl16.get(0);
                            talentAdapter.selectedLvl16.clear();
                            talentAdapter.selectedLvl16.add(getAdapterPosition() - 6);
                            talentAdapter.notifyItemChanged(oldSelected);
                            talentAdapter.notifyDataSetChanged();
                        }

                        fragment.setIvLvl16Res(imageResourceId);
                        ivLvl16.setImageResource(imageResourceId);
                        ivLvl16.setTag(talent);
                        break;
                    case 20:
                        if (talentAdapter.selectedLvl20.isEmpty()) {
                            talentAdapter.selectedLvl20.add(getAdapterPosition() - 7);
                        } else {
                            int oldSelected = talentAdapter.selectedLvl20.get(0);
                            talentAdapter.selectedLvl20.clear();
                            talentAdapter.selectedLvl20.add(getAdapterPosition() - 7);
                            talentAdapter.notifyItemChanged(oldSelected);
                            talentAdapter.notifyDataSetChanged();
                        }

                        fragment.setIvLvl20Res(imageResourceId);
                        ivLvl20.setImageResource(imageResourceId);
                        ivLvl20.setTag(talent);
                        fabSave.animate().translationY(0).setDuration(300).start();
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
        return new ViewHolder(v, talentFragment, context, this);
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

        int resId = Utils.getResourceIdByName(context, Utils.formatSpellImageName(talent.getName()));
        String imageName = Utils.formatSpellImageName(talent.getName()) + ".png";
        String imageUrl = Constants.IMAGE_BASE_URL + hero.getName() + "/" + imageName;
        Utils.GlideLoadImage(context, resId, imageUrl, holder.ivTalentIcon);

        handleSelectedItemDisplay(holder, position);
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
    public int getItemViewType(int position) {
        switch (talentList.get(position).getTier()) {
            case 1:
                return TYPE_LVL1;
            case 4:
                return TYPE_LVL4;
            case 7:
                return TYPE_LVL7;
            case 10:
                return TYPE_LVL10;
            case 13:
                return TYPE_LVL13;
            case 16:
                return TYPE_LVL16;
            case 20:
                return TYPE_LVL20;
        }


        return super.getItemViewType(position);
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
                            } else if (talent.getTier() == Integer.parseInt(charSequence.toString())) {
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

    private void handleSelectedItemDisplay(TalentAdapter.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_LVL1) {
            if (!selectedLvl1.contains(position)) {
                holder.rlTalent.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            } else if (selectedLvl1.contains(position)) {
                holder.rlTalent.setBackgroundColor(Color.parseColor("#33FFFFFF"));
            }
        }

        if (getItemViewType(position) == TYPE_LVL4) {
            if (!selectedLvl4.contains(position)) {
                holder.rlTalent.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            } else if (selectedLvl4.contains(position)) {
                holder.rlTalent.setBackgroundColor(Color.parseColor("#33FFFFFF"));
            }
        }

        if (getItemViewType(position) == TYPE_LVL7) {
            if (!selectedLvl7.contains(position)) {
                holder.rlTalent.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            } else if (selectedLvl7.contains(position)) {
                holder.rlTalent.setBackgroundColor(Color.parseColor("#33FFFFFF"));
            }
        }

        if (getItemViewType(position) == TYPE_LVL10) {
            if (!selectedLvl10.contains(position)) {
                holder.rlTalent.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            } else if (selectedLvl10.contains(position)) {
                holder.rlTalent.setBackgroundColor(Color.parseColor("#33FFFFFF"));
            }
        }

        if (getItemViewType(position) == TYPE_LVL13) {
            if (!selectedLvl13.contains(position)) {
                holder.rlTalent.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            } else if (selectedLvl13.contains(position)) {
                holder.rlTalent.setBackgroundColor(Color.parseColor("#33FFFFFF"));
            }
        }

        if (getItemViewType(position) == TYPE_LVL16) {
            if (!selectedLvl16.contains(position)) {
                holder.rlTalent.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            } else if (selectedLvl16.contains(position)) {
                holder.rlTalent.setBackgroundColor(Color.parseColor("#33FFFFFF"));
            }
        }

        if (getItemViewType(position) == TYPE_LVL20) {
            if (!selectedLvl20.contains(position)) {
                holder.rlTalent.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            } else if (selectedLvl20.contains(position)) {
                holder.rlTalent.setBackgroundColor(Color.parseColor("#33FFFFFF"));
            }
        }
    }

}
