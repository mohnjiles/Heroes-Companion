package net.cloudapp.callme.hots3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import net.cloudapp.callme.hots3.Constants;
import net.cloudapp.callme.hots3.HeroDatabase;
import net.cloudapp.callme.hots3.R;
import net.cloudapp.callme.hots3.Utils;
import net.cloudapp.callme.hots3.models.Hero;
import net.cloudapp.callme.hots3.models.Spell;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JT on 5/10/2015.
 */
public class SpellsAdapter extends ObservableRecyclerView.Adapter<SpellsAdapter.ViewHolder> {

    private Context context;
    private List<Spell> spellList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends ObservableRecyclerView.ViewHolder {

        @InjectView(R.id.tvSpellName)
        TextView tvSpellName;
        @InjectView(R.id.tvSpellText)
        TextView tvSpellText;
        @InjectView(R.id.ivSpellImage)
        ImageView ivSpellImage;
        @InjectView(R.id.tvLetter)
        TextView tvLetter;
        @InjectView(R.id.tvCooldown)
        TextView tvCooldown;
        @InjectView(R.id.tvCost)
        TextView tvCost;
//        @InjectView(R.id.ivBigImage)
//        ImageView ivBigImage;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SpellsAdapter(Context context, List<Spell> spellList) {
        this.context = context;
        this.spellList = spellList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SpellsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spell, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Spell spell = spellList.get(position);

        holder.tvSpellName.setText(spell.getName().toUpperCase(Locale.getDefault()));
        holder.tvSpellText.setText(spell.getDescription());
        holder.tvLetter.setText("[ " + spell.getHotkey().toUpperCase(Locale.getDefault()) + " ]");

        String spellNameFormatted = Utils.formatSpellImageName(spell.getName());
        Hero hero = HeroDatabase.getInstance(context).getHeroById(spell.getHeroId());

        final String imageUrl = Constants.IMAGE_BASE_URL + hero.getName() + "/"
                + spellNameFormatted + ".png";

        int resId = Utils.getResourceIdByName(context, spellNameFormatted);
        Utils.GlideLoadImage(context, resId, imageUrl, holder.ivSpellImage);

        if (spell.getCooldown() == 0) {
            holder.tvCooldown.setVisibility(View.GONE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.tvCost.getLayoutParams();
            lp.addRule(RelativeLayout.RIGHT_OF, R.id.ivSpellImage);
            holder.tvCost.setLayoutParams(lp);

        } else {
            holder.tvCooldown.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.tvCost.getLayoutParams();
            lp.addRule(RelativeLayout.RIGHT_OF, R.id.tvCooldown);
            holder.tvCost.setLayoutParams(lp);
        }

        if (spell.getCost() == 0) {
            holder.tvCost.setVisibility(View.INVISIBLE);
        } else {
            holder.tvCost.setVisibility(View.VISIBLE);
        }

        if (spell.getCost() == 0 && spell.getCooldown() == 0) {
            holder.tvCooldown.setVisibility(View.GONE);
            holder.tvCost.setVisibility(View.GONE);
        }

        String cooldown = String.valueOf(spell.getCooldown());
        String cost = String.valueOf(spell.getCost());

        holder.tvCooldown.setText("Cooldown: " + cooldown.replace(".0", "") + " seconds");
        holder.tvCost.setText("Cost: " + cost.replace(".0", ""));

//        int bigImageResId = Utils.getResourceIdByName(context, Utils.formatSpellImageName(spell.getName() + "big"));
//        holder.ivBigImage.setImageResource(bigImageResId);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return spellList.size();
    }
}
