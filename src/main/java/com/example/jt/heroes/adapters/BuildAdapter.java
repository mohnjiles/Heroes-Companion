package com.example.jt.heroes.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jt.heroes.HeroDatabase;
import com.example.jt.heroes.R;
import com.example.jt.heroes.Utils;
import com.example.jt.heroes.models.Hero;
import com.example.jt.heroes.models.News;
import com.example.jt.heroes.models.Spell;
import com.example.jt.heroes.models.Talent;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JT on 5/31/2015.
 */
public class BuildAdapter extends UltimateViewAdapter {

    private Context context;
    private SharedPreferences prefs;
    private List<String> buildNames = new ArrayList<>();
    private List<String> buildTalentsAsJson = new ArrayList<>();
    private List<Talent> buildTalents = new ArrayList<>();


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends UltimateRecyclerviewViewHolder {

        @InjectView(R.id.tvBuildName)
        TextView tvBuildName;
        @InjectView(R.id.tvHeroName)
        TextView tvHeroName;
        @InjectView(R.id.ivHeroImage)
        ImageView ivHeroImage;
        @InjectView(R.id.ivLvl1)
        RoundedImageView ivLvl1;
        @InjectView(R.id.ivLvl4)
        RoundedImageView ivLvl4;
        @InjectView(R.id.ivLvl7)
        RoundedImageView ivLvl7;
        @InjectView(R.id.ivLvl10)
        RoundedImageView ivLvl10;
        @InjectView(R.id.ivLvl13)
        RoundedImageView ivLvl13;
        @InjectView(R.id.ivLvl16)
        RoundedImageView ivLvl16;
        @InjectView(R.id.ivLvl20)
        RoundedImageView ivLvl20;
//        @InjectView(R.id.ivBigImage)
//        ImageView ivBigImage;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BuildAdapter(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("com.example.jt.heroes", Context.MODE_PRIVATE);


        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry: allEntries.entrySet()) {
            buildNames.add(entry.getKey());
            buildTalentsAsJson.add(entry.getValue().toString());
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BuildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.savedbuild, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ButterKnife.inject(this, v);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Gson gson = new Gson();
        BuildAdapter.ViewHolder holder2 = (BuildAdapter.ViewHolder) holder;
        RoundedImageView[] imageViews = { holder2.ivLvl1, holder2.ivLvl4, holder2.ivLvl7, holder2.ivLvl10,
                holder2.ivLvl13, holder2.ivLvl16, holder2.ivLvl20};

        Type listType = new TypeToken<ArrayList<Talent>>(){}.getType();
        String json = buildTalentsAsJson.get(position);
        buildTalents = gson.fromJson(json, listType);

        HeroDatabase db = new HeroDatabase(context);
        Talent someTalent = buildTalents.get(0);
        Hero currentHero = db.getHeroById(someTalent.getHeroId());

        holder2.tvBuildName.setText(buildNames.get(position));
        holder2.tvHeroName.setText(currentHero.getName());
        holder2.ivHeroImage.setImageResource(Utils.getResourceIdByName(context,
                Utils.formatSpellImageName(currentHero.getName())));


        for (int i = 0; i < buildTalents.size(); i++) {
            Talent currentTalent = buildTalents.get(i);

            imageViews[i].setImageResource(Utils.getResourceIdByName(context,
                    Utils.formatSpellImageName(currentTalent.getName())));
        }
    }

    @Override
    public UltimateRecyclerviewViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return prefs.getAll().size();
    }

    @Override
    public int getAdapterItemCount() {
        return prefs.getAll().size();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }
}
