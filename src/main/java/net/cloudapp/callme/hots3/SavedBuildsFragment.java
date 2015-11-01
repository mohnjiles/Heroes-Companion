package net.cloudapp.callme.hots3;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.marshalchen.ultimaterecyclerview.SwipeToDismissTouchListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedBuildsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedBuildsFragment extends Fragment {

//    @InjectView(R.id.fabClear)
//    FloatingActionButton fabClear;
    @InjectView(R.id.rlMain)
    RelativeLayout rlMain;
    @InjectView(R.id.rlNewTalent)
    RelativeLayout rlNewTalent;
    @InjectView(R.id.rvBuilds)
    UltimateRecyclerView rvBuilds;
    @InjectView(R.id.fabNewBuild)
    FloatingActionButton fabNewBuild;
    @InjectView(R.id.fabNewBuildOther)
    FloatingActionButton fabNewBuildOther;

    @OnClick({R.id.fabNewBuild, R.id.fabNewBuildOther})
    public void onNewBuildClick() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, HeroListFragment.newInstance(true), "hero_list_fragment")
                .commit();
        ((MainActivity)getActivity()).setNavigationPosition(0);
    }

    private SharedPreferences prefs;
    private List<String> prefKeys = new ArrayList<>();
    private Map<String, String> prefsMap = new HashMap<>();

    public static SavedBuildsFragment newInstance() {
        SavedBuildsFragment fragment = new SavedBuildsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public SavedBuildsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_saved_builds, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fabNewBuild.setImageResource(R.drawable.ic_add_white_24dp);
        fabNewBuildOther.setImageResource(R.drawable.ic_add_white_24dp);

        prefs = getActivity().getSharedPreferences("com.example.jt.heroes", Context.MODE_PRIVATE);

        Map<String, ?> allEntries = prefs.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getValue() instanceof String) {
                prefKeys.add(entry.getKey());
                prefsMap.put(entry.getKey(), entry.getValue().toString());
            }
        }
        if (prefKeys.size() > 0) {
            rlNewTalent.setVisibility(View.INVISIBLE);
            rlMain.setVisibility(View.VISIBLE);
        } else {
            rlNewTalent.setVisibility(View.VISIBLE);
            rlMain.setVisibility(View.INVISIBLE);
        }
        Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
        toolbar.setTitle(R.string.app_name);
        toolbar.setSubtitle("Talent Builds");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvBuilds.setLayoutManager(mLayoutManager);
        rvBuilds.setAdapter(new BuildAdapter(getActivity()));
        rvBuilds.setSwipeToDismissCallback(new SwipeToDismissTouchListener.DismissCallbacks() {
            @Override
            public SwipeToDismissTouchListener.SwipeDirection dismissDirection(int i) {
                return SwipeToDismissTouchListener.SwipeDirection.BOTH;
            }

            @Override
            public void onDismiss(RecyclerView recyclerView, List<SwipeToDismissTouchListener.PendingDismissData> list) {
                for (final SwipeToDismissTouchListener.PendingDismissData data : list) {

                    final String key = prefKeys.get(data.position);
                    final String json = prefsMap.get(key);

                    // delete the build
                    prefs.edit().remove(prefKeys.get(data.position)).apply();

                    // show snackbar with undo
                    Snackbar.make(rvBuilds, "Build " + prefKeys.get(data.position) + " deleted.",
                            Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            prefs.edit().putString(key, json).apply();
                            rvBuilds.setAdapter(new BuildAdapter(getActivity()));
                        }
                    }).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);

                            // animate FAB back down to regular position
                            if (rlMain.getVisibility() == View.VISIBLE) {
                                Utils.animateFabSnackbarHeightDown(fabNewBuildOther);
                            } else {
                                Utils.animateFabSnackbarHeightDown(fabNewBuild);
                            }
                        }
                    }).show();

                    // animate FAB up the snackbar height per design guidelines
                    if (rlMain.getVisibility() == View.VISIBLE) {
                        Utils.animateFabSnackbarHeight(getActivity(), fabNewBuildOther);
                    } else {
                        Utils.animateFabSnackbarHeight(getActivity(), fabNewBuild);
                    }

                    prefKeys.remove(data.position);
                    rvBuilds.setAdapter(new BuildAdapter(getActivity()));

                    Map<String, ?> allEntries = prefs.getAll();
                    Map<String, String> prefsRemaining = new HashMap<>();
                    for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                        if (entry.getValue() instanceof String) {
                            prefsRemaining.put(entry.getKey(), entry.getValue().toString());
                        }
                    }

                    if (prefsRemaining.size() <= 0) {
                        rlMain.setVisibility(View.INVISIBLE);
                        rlNewTalent.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onResetMotion() {

            }

            @Override
            public void onTouchDown() {

            }
        });

        rvBuilds.mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), BuildDetailActivity.class);
                        intent.putExtra("talent_build", prefsMap.get(prefKeys.get(position)));
                        intent.putExtra("build_name", prefKeys.get(position));
                        startActivity(intent);
                    }
                }));

//        fabClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new MaterialDialog.Builder(getActivity())
//                        .title("Delete All Talent Builds")
//                        .content("Are you sure you wish to delete all saved talent builds?")
//                        .positiveText("DELETE ALL")
//                        .negativeText("CANCEL")
//                        .callback(new MaterialDialog.ButtonCallback() {
//                            @Override
//                            public void onPositive(MaterialDialog dialog) {
//                                prefs.edit().clear().commit();
//                                Toast.makeText(getActivity(), "All talent builds cleared.", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .show();
//
//            }
//        });
    }

}
