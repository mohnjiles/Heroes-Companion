package com.example.jt.heroes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jt.heroes.R;
import com.example.jt.heroes.models.News;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JT on 5/10/2015.
 */
public class NewsAdapter extends ObservableRecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private int lastPosition = -1;
    private Context context;
    private List<News> newsList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends ObservableRecyclerView.ViewHolder {
        // each data item is just a string in this case
        @InjectView(R.id.tvNewsTitle)
        public TextView tvNewsTitle;
        @InjectView(R.id.tvDescription)
        public TextView tvNewsDescription;
        @InjectView(R.id.tvPublishDate)
        public TextView tvPublishDate;

        @InjectView(R.id.ivThumbnail)
        public ImageView ivNewsIcon;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = newsList.get(position);

        holder.tvNewsTitle.setText(news.getTitle().toUpperCase(Locale.getDefault()));
        holder.tvNewsDescription.setText(news.getDescription());
        holder.tvPublishDate.setText(news.getPublishDate().toUpperCase(Locale.getDefault()));
        String url = "http:" + news.getImage();
        Picasso.with(context).load(url).into(holder.ivNewsIcon);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return newsList.size();
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