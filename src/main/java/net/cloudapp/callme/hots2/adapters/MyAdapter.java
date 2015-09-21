package net.cloudapp.callme.hots2.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;

import net.cloudapp.callme.hots2.Constants;
import net.cloudapp.callme.hots2.R;
import net.cloudapp.callme.hots2.Utils;
import net.cloudapp.callme.hots2.models.Hero;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JT on 2/25/2015.
 */
public class MyAdapter extends ObservableRecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {

    private ArrayList<Hero> heroList;

    private ArrayList<Hero> freeHeroes;
    private ArrayList<Hero> originalList;
    private int lastPosition = -1;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends ObservableRecyclerView.ViewHolder {
        // each data item is just a string in this case
        @InjectView(R.id.textView)
        public TextView mTextView;

        @InjectView(R.id.ivHeroIcon)
        public RoundedImageView ivHeroIcon;
        @InjectView(R.id.ivFranchiseIcon)
        public ImageView ivFranchiseIcon;
        @InjectView(R.id.ivRoleIcon)
        public ImageView ivRoleIcon;
        @InjectView(R.id.ivFree)
        ImageView ivFree;

        @InjectView(R.id.tvDescription)
        public TextView tvDescription;

        @InjectView(R.id.tvRole)
        TextView tvRole;

        @InjectView(R.id.tvTitle)
        public TextView tvTitle;

        @InjectView(R.id.ivBigHeroImage)
        ImageView ivBigHeroImage;

        @InjectView(R.id.recycler_view_container)
        public RelativeLayout recyclerViewContainer;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, ArrayList<Hero> heroes, ArrayList<Hero> freeHeroes) {
        heroList = heroes;
        this.freeHeroes = freeHeroes;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.heroes_list_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        vh.ivHeroIcon.setBorderColor(Color.BLACK);
        vh.ivHeroIcon.mutateBackground(true);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(heroList.get(position).getName());
        holder.mTextView.setTag(heroList.get(position).getId());
        holder.tvDescription.setText(heroList.get(position).getDescription());
        holder.tvTitle.setText(heroList.get(position).getTitle());

        switch (heroList.get(position).getRole()) {
            case "Warrior":
                holder.tvRole.setTextColor(context.getResources().getColor(R.color.blue_500));
                break;
            case "Support":
                holder.tvRole.setTextColor(context.getResources().getColor(R.color.teal_500));
                break;
            case "Specialist":
                holder.tvRole.setTextColor(context.getResources().getColor(R.color.purple_500));
                break;
            case "Assassin":
                holder.tvRole.setTextColor(context.getResources().getColor(R.color.red_500));
                break;

        }

        holder.tvRole.setText(heroList.get(position).getRole());

        // set icons
        int resId = heroList.get(position).getIconResourceId();
        String imageUrl = Constants.IMAGE_BASE_URL + "HeroImages/"
                + Utils.formatSpellImageName(heroList.get(position).getName()) + ".png";
        Utils.GlideLoadImage(context, resId, imageUrl, holder.ivHeroIcon);

        switch (heroList.get(position).getRole()) {
            case "Assassin":
                holder.ivRoleIcon.setImageResource(R.drawable.assassin);
                break;
            case "Specialist":
                holder.ivRoleIcon.setImageResource(R.drawable.specialist);
                break;
            case "Support":
                holder.ivRoleIcon.setImageResource(R.drawable.support);
                break;
            case "Warrior":
                holder.ivRoleIcon.setImageResource(R.drawable.warrior);
                break;
        }
        switch (heroList.get(position).getFranchise()) {
            case "Warcraft":
                holder.ivFranchiseIcon.setImageResource(R.drawable.warcraftgame);
                break;
            case "Diablo":
                holder.ivFranchiseIcon.setImageResource(R.drawable.diablogame);
                break;
            case "Starcraft":
                holder.ivFranchiseIcon.setImageResource(R.drawable.starcraftgame);
                break;
            case "Blizzard":
                holder.ivFranchiseIcon.setImageResource(R.drawable.blizzardgame);
                break;


        }

        final String bigImageUrl = Constants.IMAGE_BASE_URL + "webm/" +
                Utils.formatSpellImageName(heroList.get(position).getName()) + "big.compressed.png";

        int bigResId = Utils.getResourceIdByName(context,
                Utils.formatSpellImageName(heroList.get(position).getName() + "big"));
        Utils.GlideLoadImage(context, bigResId, bigImageUrl, holder.ivBigHeroImage);

        if ((freeHeroes != null && freeHeroes.size() > 0) && freeHeroes.contains(heroList.get(position))) {
            holder.ivFree.setVisibility(View.VISIBLE);
        } else {
            holder.ivFree.setVisibility(View.INVISIBLE);
        }

//        holder.ivBigHeroImage.setImageResource(Utils.getResourceIdByName(context,
//                Utils.formatSpellImageName(heroList.get(position).getName() + "big")));
        //setAnimation(holder.recyclerViewContainer, position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Hero> results = new ArrayList<>();

                if (originalList == null)
                    originalList = new ArrayList<>(heroList);

                if (charSequence != null && freeHeroes != null
                        && freeHeroes.size() > 0 && charSequence == "Free") {
                    for (final Hero hero : freeHeroes) {
                        results.add(hero);
                    }

                } else if (charSequence == "All") {
                    for (Hero hero: originalList) {
                        results.add(hero);
                    }
                } else if (charSequence == "Assassin") {
                    for (Hero hero : originalList) {
                        if (hero.getRole().toLowerCase().equals("assassin")) {
                            results.add(hero);
                        }
                    }
                } else if (charSequence == "Support") {
                    for (Hero hero : originalList) {
                        if (hero.getRole().toLowerCase().equals("support")) {
                            results.add(hero);
                        }
                    }
                } else if (charSequence == "Specialist") {
                    for (Hero hero : originalList) {
                        if (hero.getRole().toLowerCase().equals("specialist")) {
                            results.add(hero);
                        }
                    }
                } else if (charSequence == "Warrior") {
                    for (Hero hero : originalList) {
                        if (hero.getRole().toLowerCase().equals("warrior")) {
                            results.add(hero);
                        }
                    }
                }  else if (charSequence != null) {
                    if (originalList.size() > 0) {
                        for (final Hero hero : originalList) {
                            if (hero.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                results.add(hero);
                            }
                        }
                    }
                }

                oReturn.values = results;

                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                heroList = (ArrayList<Hero>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return heroList.size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



    public void setFreeHeroes(ArrayList<Hero> freeHeroes) {
        this.freeHeroes = freeHeroes;
    }
}
