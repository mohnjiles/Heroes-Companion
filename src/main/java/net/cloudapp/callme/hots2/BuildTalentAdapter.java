package net.cloudapp.callme.hots2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import net.cloudapp.callme.hots2.models.Talent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JT on 5/10/2015.
 */
public class BuildTalentAdapter extends ObservableRecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Talent> talentList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String buildName;
    private String heroName;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ItemViewHolder extends ObservableRecyclerView.ViewHolder {

        @InjectView(R.id.tvTalentDescription)
        TextView tvTalentDescription;
        @InjectView(R.id.tvTalentName)
        TextView tvTalentName;
        @InjectView(R.id.ivTalentImage)
        ImageView ivTalentImage;
        @InjectView(R.id.tvTalentLevel)
        TextView tvTalentLevel;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }

    public static class HeaderViewHolder extends ObservableRecyclerView.ViewHolder {

        @InjectView(R.id.tvBuildName)
        TextView tvBuildName;
        @InjectView(R.id.tvHeroName)
        TextView tvHeroName;
        @InjectView(R.id.ivHeroImage)
        ImageView ivHeroImage;
        @InjectView(R.id.ivBigHeroImage)
        ImageView ivBigHeroImage;

        public HeaderViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BuildTalentAdapter(Context context, List<Talent> talentList, String buildName, String heroName) {
        this.context = context;
        this.talentList = talentList;
        this.buildName = buildName;
        this.heroName = heroName;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.talent_build_talent, parent, false);
            return new ItemViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.talent_build_header, parent, false);
            return new HeaderViewHolder(v);
        }

        throw new RuntimeException("There is no type that matches the type: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder) {
            Talent talent = getCurrentTalent(position);
            ((ItemViewHolder) holder).tvTalentDescription.setText(talent.getDescription());
            ((ItemViewHolder) holder).tvTalentLevel.setText("LEVEL " + talent.getTier());
            ((ItemViewHolder) holder).tvTalentName.setText(talent.getName());
            ((ItemViewHolder) holder).ivTalentImage.setImageResource(Utils.getResourceIdByName(
                    context, Utils.formatSpellImageName(talent.getName())
            ));
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).tvBuildName.setText(buildName);
            ((HeaderViewHolder) holder).tvHeroName.setText(heroName);
            ((HeaderViewHolder) holder).ivHeroImage.setImageResource(Utils.getResourceIdByName(context,
                    Utils.formatSpellImageName(heroName)));
            ((HeaderViewHolder) holder).ivBigHeroImage.setImageResource(Utils.getResourceIdByName(context,
                    Utils.formatSpellImageName(heroName + "big")));
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return talentList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private Talent getCurrentTalent(int position) {
        return talentList.get(position - 1);
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
