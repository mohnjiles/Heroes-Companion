package net.cloudapp.callme.hots3;


import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cloudapp.callme.hots3.adapters.MyAdapter;
import net.cloudapp.callme.hots3.models.Hero;
import net.cloudapp.callme.hots3.models.Spell;
import net.cloudapp.callme.hots3.models.Talent;
import net.cloudapp.callme.hots3.models.Version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by JT on 3/16/2015.
 */
public class HeroListFragment extends Fragment implements ObservableScrollViewCallbacks {

    @InjectView(R.id.my_recycler_view)
    ObservableRecyclerView myRecyclerView;
    @InjectView(R.id.rlHeroList)
    RelativeLayout rlHeroList;
    @InjectView(R.id.rlLoading)
    LinearLayout rlLoading;

    private Toolbar toolbar;
    private InputMethodManager inputManager;
    MyAdapter adapter;

    @Icicle
    ArrayList<Hero> freeHeroes = new ArrayList<>();
    @Icicle
    ArrayList<Hero> allHeroes = new ArrayList<>();

    private int selectedRadioButton = 0;

    private HeroDatabase db = null;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_STRAIGHT_TO_TALENTS = "straight_to_talents";

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

    public static HeroListFragment newInstance(boolean goStraightToTalents) {
        HeroListFragment fragment = new HeroListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_STRAIGHT_TO_TALENTS, goStraightToTalents);
        fragment.setArguments(args);
        return fragment;
    }

    public HeroListFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        Icepick.restoreInstanceState(this, savedInstanceState);

        db = HeroDatabase.getInstance(getActivity());

        Log.w("onActivityCreated", "onActivityCreated Called");

        toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setTitle(R.string.app_name);
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

        inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);


        if (savedInstanceState != null) {
            MyAdapter adapter = new MyAdapter(getActivity(), allHeroes, freeHeroes);

            rlLoading.setVisibility(View.INVISIBLE);

            myRecyclerView.setAdapter(adapter);
            myRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            HeroDatabase db = HeroDatabase.getInstance(getActivity());
                            Intent intent = new Intent(getActivity(), HeroActivity.class);
                            intent.putExtra("hero", db.getHeroById((int) view.findViewById(R.id.textView).getTag()));

                            boolean goToTalents = getArguments().getBoolean(ARG_STRAIGHT_TO_TALENTS);
                            intent.putExtra(ARG_STRAIGHT_TO_TALENTS, goToTalents);


                            inputManager.hideSoftInputFromWindow(rlHeroList.getApplicationWindowToken(), 0);

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
            myRecyclerView.setScrollViewCallbacks(HeroListFragment.this);
        } else {
            final HeroDatabase db = HeroDatabase.getInstance(getActivity());
            allHeroes = (ArrayList<Hero>) db.getAllHeroes();
            rlLoading.setVisibility(View.INVISIBLE);
            //new FetchFreeHeroRotation(getActivity(), this).execute();

            adapter = new MyAdapter(getActivity(), allHeroes, freeHeroes);
            myRecyclerView.setAdapter(adapter);
            myRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {


                            Intent intent = new Intent(getActivity(), HeroActivity.class);
                            intent.putExtra("hero", db.getHeroById((int) view.findViewById(R.id.textView).getTag()));

                            boolean goToTalents = getArguments().getBoolean(ARG_STRAIGHT_TO_TALENTS);
                            intent.putExtra(ARG_STRAIGHT_TO_TALENTS, goToTalents);

                            inputManager.hideSoftInputFromWindow(rlHeroList.getApplicationWindowToken(), 0);
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
            myRecyclerView.setScrollViewCallbacks(HeroListFragment.this);
            rlHeroList.setVisibility(View.VISIBLE);
            myRecyclerView.setVisibility(View.VISIBLE);
        }

        new CheckForUpdate(getActivity()).execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

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
            case R.id.action_sort:
                new MaterialDialog.Builder(getActivity())
                        .title("Filter heroes by...")
                        .items(new CharSequence[]{"All", "New", "Free", "Assassin", "Specialist", "Support", "Warrior"})
                        .itemsCallbackSingleChoice(selectedRadioButton, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {

                                if (myRecyclerView.getAdapter() != null) {
                                    if (TextUtils.isEmpty(charSequence)) {
                                        ((Filterable) myRecyclerView.getAdapter()).getFilter().filter("");
                                    } else {
                                        ((Filterable) myRecyclerView.getAdapter()).getFilter().filter(charSequence);
                                    }
                                }
                                selectedRadioButton = i;
                                //materialDialog.setSelectedIndex();
                                return true;
                            }
                        })
                        .alwaysCallSingleChoiceCallback()
                        .backgroundColor(getResources().getColor(R.color.sorta_dark_purple))
//                        .positiveText("SELECT")
//                        .negativeText("CANCEL")
                        .show();
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
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
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

    private class FetchFreeHeroRotation extends AsyncTask<Void, Void, List<Hero>> {

        private HeroDatabase db;
        private HeroListFragment frag;

        public FetchFreeHeroRotation(Context context, HeroListFragment frag) {
            db = HeroDatabase.getInstance(context);
            this.frag = frag;
        }

        @Override
        protected List<Hero> doInBackground(Void... voids) {
            List<Hero> freeHeroes = new ArrayList<>();

            URL url;
            BufferedReader in;
            String inputLine;
            String heroNamesCsv = "";
            try {
                url = new URL("http://callme.cloudapp.net/hots/getfreeheroes.php");
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                while ((inputLine = in.readLine()) != null) {
                    heroNamesCsv = inputLine;
                }
            }  catch (IOException e) {
                e.printStackTrace();
            }


            if (!heroNamesCsv.equals("")) {
                for (String heroName : heroNamesCsv.split(",")) {
                    if (!heroName.equals("")) {
                        Hero hero = db.getHeroById(db.getHeroIdFromName(heroName));
                        freeHeroes.add(hero);
                    }
                }
            }


            frag.freeHeroes = (ArrayList<Hero>) freeHeroes;

            return freeHeroes;
        }

        @Override
        protected void onPostExecute(List<Hero> freeHeroes) {
            super.onPostExecute(freeHeroes);
            frag.adapter.setFreeHeroes((ArrayList<Hero>) freeHeroes);
            frag.adapter.notifyDataSetChanged();
            Log.w("FREE HEROES", "onPostExecute set free heroes.");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //db.close();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private class CheckForUpdate extends AsyncTask<Void, Void, Void> {

        private HeroDatabase heroDatabase = HeroDatabase.getInstance(getActivity());
        private SQLiteDatabase db = heroDatabase.getWritableDatabase();
        private Context mContext = null;
        private boolean mDidUpdate = false;

        public CheckForUpdate(Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String json = null;
            try {
                json = Utils.readUrl("http://www.jtmiles.xyz/hotsapi/getversion.php");
            }
            catch (Exception ex){
                Log.w("CheckForUpdate", "Error getting server version: " + ex.getMessage());
            }

            if (json != null) {
                Gson gson = new Gson();
                Version serverVersion = gson.fromJson(json, Version.class);

                if (serverVersion != null && serverVersion.getVersion() > heroDatabase.getDatabaseVersion()) {
                    publishProgress();
                    heroDatabase.clearTables();
                    heroDatabase.insertVersionUpdate(serverVersion);

                    try {
                        json = Utils.readUrl("http://www.jtmiles.xyz/hotsapi/getallheroes.php");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Type listType = new TypeToken<ArrayList<Hero>>() { }.getType();
                    ArrayList<Hero> serverHeroes = gson.fromJson(json, listType);


                    for (Hero hero : serverHeroes) {
                        heroDatabase.addHero(hero, db);
                    }

                    // get spells
                    try {
                        json = Utils.readUrl("http://www.jtmiles.xyz/hotsapi/getallspells.php");

                        Type spellListType = new TypeToken<ArrayList<Spell>>() { }.getType();
                        ArrayList<Spell> serverSpells = gson.fromJson(json, spellListType);
                        for (Spell spell : serverSpells) {
                            heroDatabase.addSpell(spell, db);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // get talents
                    try {
                        json = Utils.readUrl("http://www.jtmiles.xyz/hotsapi/getalltalents.php");

                        Type talentListType = new TypeToken<ArrayList<Talent>>() { }.getType();
                        ArrayList<Talent> serverTalents = gson.fromJson(json, talentListType);
                        for (Talent talent : serverTalents) {
                            heroDatabase.addTalent(talent, db);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mDidUpdate = true;

                }
            }

            Log.d("clearTables()", "tables cleared");

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            Snackbar.make(myRecyclerView, "Updating heroes database...", Snackbar.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            final HeroDatabase db = HeroDatabase.getInstance(getActivity());
            allHeroes = (ArrayList<Hero>) db.getAllHeroes();
            rlLoading.setVisibility(View.INVISIBLE);
            new FetchFreeHeroRotation(mContext, HeroListFragment.this).execute();

            int currentPos = ((GridLayoutManager)myRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

            adapter = new MyAdapter(getActivity(), allHeroes, freeHeroes);
            myRecyclerView.setAdapter(adapter);
            myRecyclerView.scrollVerticallyToPosition(currentPos);

            rlHeroList.setVisibility(View.VISIBLE);
            myRecyclerView.setVisibility(View.VISIBLE);

            if (mDidUpdate)
                Snackbar.make(myRecyclerView, "Update complete.", Snackbar.LENGTH_SHORT).show();

        }
    }
}

