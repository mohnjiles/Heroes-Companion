package com.example.jt.heroes;


import android.app.ActivityOptions;
import android.support.v7.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jt.heroes.models.Hero;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JT on 3/16/2015.
 */
public class HeroListFragment extends Fragment implements ObservableScrollViewCallbacks {

    @InjectView(R.id.my_recycler_view)
    ObservableRecyclerView myRecyclerView;

    private ObservableRecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static HeroListFragment newInstance(int sectionNumber) {
        HeroListFragment fragment = new HeroListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HeroListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.argb(115, 255, 255, 255))
                        .size(1)
                        .build());
        mLayoutManager = new LinearLayoutManager(getActivity());
        myRecyclerView.setLayoutManager(mLayoutManager);

        new GetHeroes(getActivity().getApplicationContext()).execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
//        ActionBar ab = ((ActionBarActivity) getActivity()).getSupportActionBar();
//        if (scrollState == ScrollState.UP) {
//            if (ab.isShowing()) {
//                ab.hide();
//            }
//        } else if (scrollState == ScrollState.DOWN) {
//            if (!ab.isShowing()) {
//                ab.show();
//            }
//        }
    }

    private class GetHeroes extends AsyncTask<Void, Void, ArrayList<Hero>> {

        ProgressDialog dialog;
        Context context;
        HeroDatabase db;

        private GetHeroes(Context context) {
            this.context = context;
            dialog = new ProgressDialog(context);
            db = new HeroDatabase(context);
        }

        @Override
        protected ArrayList<Hero> doInBackground(Void... params) {
            return new ArrayList<>(db.getAllHeroes());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //dialog.setMessage("Loading heroes...");
            //dialog.show();
        }

        @Override
        protected void onPostExecute(final ArrayList<Hero> heroList) {
            super.onPostExecute(heroList);
            db.close();
            //dialog.cancel();

            MyAdapter adapter = new MyAdapter(getActivity(), heroList);
            myRecyclerView.setAdapter(adapter);
            myRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Intent intent = new Intent(getActivity(), HeroActivity.class);
                            intent.putExtra("hero", heroList.get(position));
                            startActivity(intent);

                        }
                    })
            );

            myRecyclerView.setScrollViewCallbacks(HeroListFragment.this);
            myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                int mLastFirstVisibleItem = 0;

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    final int currentFirstVisibleItem = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();

                    if (currentFirstVisibleItem > this.mLastFirstVisibleItem) {
                        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
                    } else if (currentFirstVisibleItem < this.mLastFirstVisibleItem) {
                        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
                    }

                    this.mLastFirstVisibleItem = currentFirstVisibleItem;

                }
            });
        }
    }
}

