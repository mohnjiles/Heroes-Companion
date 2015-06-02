package com.example.jt.heroes;


import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filterable;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.jt.heroes.adapters.MyAdapter;
import com.example.jt.heroes.models.Hero;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.yqritc.recyclerviewflexibledivider.VerticalDividerItemDecoration;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by JT on 3/16/2015.
 */
public class HeroListFragment extends Fragment implements ObservableScrollViewCallbacks {

    @InjectView(R.id.my_recycler_view)
    ObservableRecyclerView myRecyclerView;
    @InjectView(R.id.rlHeroList)
    RelativeLayout rlHeroList;

    private Toolbar toolbar;
    private InputMethodManager inputManager;

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
        setHasOptionsMenu(true);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setTitle("Heroes of the Storm");
        toolbar.setSubtitle("Heroes");

        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.addItemDecoration(new SpacesItemDecoration(getActivity(), 64, 4));

        RecyclerView.LayoutManager mLayoutManager = null;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 1);
        }
        myRecyclerView.setLayoutManager(mLayoutManager);

        inputManager = (InputMethodManager)getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);



        new GetHeroes(getActivity().getApplicationContext()).execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView fsv = null;

        if (item != null) {
            fsv = (SearchView) MenuItemCompat.getActionView(item);
            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem menuItem) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                    inputManager.hideSoftInputFromWindow(rlHeroList.getApplicationWindowToken(), 0);
                    return true;
                }
            });
        }

        if (fsv != null) {

            fsv.setIconifiedByDefault(false);
            fsv.requestFocus();
            fsv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if (myRecyclerView.getAdapter() != null) {
                        if (TextUtils.isEmpty(s)) {
                            ((Filterable) myRecyclerView.getAdapter()).getFilter().filter("");
                        } else {
                            ((Filterable) myRecyclerView.getAdapter()).getFilter().filter(s);
                        }
                    }
                    return false;
                }
            });
            fsv.setLayoutTransition(new LayoutTransition());
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                SearchView fsv = (SearchView) MenuItemCompat.getActionView(item);
                TextView searchText = (TextView) fsv.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                fsv.requestFocus();
                inputManager.toggleSoftInputFromWindow(rlHeroList.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                break;
        }

        return super.onOptionsItemSelected(item);
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
            //dialog.cancel();

            MyAdapter adapter = new MyAdapter(getActivity(), heroList);
            myRecyclerView.setAdapter(adapter);
            myRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            Intent intent = new Intent(getActivity(), HeroActivity.class);
                            intent.putExtra("hero", db.getHeroById((int) view.findViewById(R.id.textView).getTag()));

                            startActivity(intent);
//                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    getActivity(),
//                                    new Pair<>(view.findViewById(R.id.textView),
//                                            getString(R.string.transition_name_name)),
//                                    new Pair<>(view.findViewById(R.id.tvTitle),
//                                            getString(R.string.transition_name_title)),
//                                    new Pair<>(view.findViewById(R.id.ivBigHeroImage),
//                                            getString(R.string.transition_name_image))
//
//                            );
//                            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());

                        }
                    })
            );
            db.close();
            myRecyclerView.setScrollViewCallbacks(HeroListFragment.this);

        }
    }
}

