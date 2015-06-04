package com.example.jt.heroes;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jt.heroes.adapters.BuildAdapter;
import com.example.jt.heroes.models.Talent;
import com.example.jt.heroes.models.TalentBuild;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marshalchen.ultimaterecyclerview.SwipeToDismissTouchListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedBuildsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedBuildsFragment extends Fragment {

    @InjectView(R.id.fabClear)
    FloatingActionButton fabClear;
    @InjectView(R.id.rlMain)
    RelativeLayout rlMain;
    @InjectView(R.id.rlNewTalent)
    RelativeLayout rlNewTalent;
    @InjectView(R.id.rvBuilds)
    UltimateRecyclerView rvBuilds;

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
        prefs = getActivity().getSharedPreferences("com.example.jt.heroes",
                Context.MODE_PRIVATE);

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
        toolbar.setTitle("Heroes of the Storm");
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
                    new MaterialDialog.Builder(getActivity())
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    prefs.edit().remove(prefKeys.get(data.position)).commit();
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.container, SavedBuildsFragment.newInstance())
                                            .commit();
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                    rvBuilds.setAdapter(new BuildAdapter(getActivity()));
                                }
                            })
                            .title("Confirm Deletion")
                            .backgroundColorRes(R.color.sorta_dark_purple)
                            .content("Are you sure you wish to delete build \"" + prefKeys.get(data.position) + "\"?")
                            .positiveText("YES")
                            .negativeText("CANCEL")
                            .show();

                }
            }

            @Override
            public void onResetMotion() {

            }

            @Override
            public void onTouchDown() {

            }
        });

        HeroDatabase db = new HeroDatabase(getActivity());

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

        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title("Delete All Talent Builds")
                        .content("Are you sure you wish to delete all saved talent builds?")
                        .positiveText("DELETE ALL")
                        .negativeText("CANCEL")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                prefs.edit().clear().commit();
                                Toast.makeText(getActivity(), "All talent builds cleared.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();

            }
        });
    }
}
