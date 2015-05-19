package com.example.jt.heroes;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.jt.heroes.adapters.NewsAdapter;
import com.example.jt.heroes.models.News;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements ObservableScrollViewCallbacks {

    @InjectView(R.id.rvNews)
    ObservableRecyclerView rvNews;

    private List<News> newsList;
    private Toolbar toolbar;

    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsList = new ArrayList<>();
        toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setTitle("Heroes of the Storm");
        toolbar.setSubtitle("News");

        rvNews.setHasFixedSize(true);
        rvNews.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(getResources().getColor(R.color.super_dark_purple))
                        .size(64)
                        .build());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvNews.setLayoutManager(mLayoutManager);
        new GetNews().execute();
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }

    private class GetNews extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {

            Document doc = null;

            try {
                doc = Jsoup.connect("http://us.battle.net/heroes/en/blog/").get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {

                doc.select(".featured-news-section").remove();

                Elements titles = doc.select(".news-list__item__title > a");
                Elements descriptions = doc.select(".news-list__item__description");
                Elements publishDates = doc.select(".publish-date");
                Elements image = doc.select(".news-list__item__thumbnail > img");

                for (int i = 0; i < titles.size(); i++) {
                    String title = titles.get(i).text();
                    String description = descriptions.get(i).text();
                    String publishDate = publishDates.get(i).text();
                    String imageUrl = image.get(i).attr("src");
                    String articleUrl;
                    if (!titles.get(i).attr("href").startsWith("http")) {
                        articleUrl = "http://us.battle.net" + titles.get(i).attr("href");
                    } else {
                        articleUrl = titles.get(i).attr("href");
                    }

                    News news = new News();
                    news.setTitle(title);
                    news.setDescription(description);
                    news.setPublishDate(publishDate);
                    news.setImage(imageUrl);
                    news.setArticleUrl(articleUrl);

                    newsList.add(news);
                }

            } else {
                return 0;
            }

            return 1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 0) {
                Toast.makeText(getActivity(),
                        "Sorry, could not fetch the news. Please try again later.",
                        Toast.LENGTH_SHORT).show();
            } else {
                rvNews.setAdapter(new NewsAdapter(getActivity(), newsList));
                rvNews.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(newsList.get(position).getArticleUrl()));
                        startActivity(i);
                    }
                }));
                rvNews.setScrollViewCallbacks(NewsFragment.this);
            }
        }
    }

}
