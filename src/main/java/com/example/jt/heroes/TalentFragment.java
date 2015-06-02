package com.example.jt.heroes;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.example.jt.heroes.adapters.SimpleSectionedRecyclerViewAdapter;
import com.example.jt.heroes.adapters.TalentAdapter;
import com.example.jt.heroes.models.Hero;
import com.example.jt.heroes.models.Talent;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class TalentFragment extends Fragment implements ObservableScrollViewCallbacks {

    private Hero hero;
    private MultiSelector mMultiSelector = new MultiSelector();
    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.rvTalents)
    ObservableRecyclerView rvTalents;

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

    @InjectView(R.id.fabSave)
    FloatingActionButton fabSave;

    @InjectView(R.id.tvHelpText)
    TextView tvHelpText;

    @InjectView(R.id.linearLayout)
    LinearLayout linearLayout;

    public static TalentFragment newInstance(Hero hero) {
        TalentFragment fragment = new TalentFragment();
        Bundle args = new Bundle();
        args.putSerializable("hero", hero);
        fragment.setArguments(args);
        return fragment;
    }

    public TalentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hero = (Hero) getArguments().getSerializable("hero");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_talent, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        linearLayout.setVisibility(View.INVISIBLE);
        rvTalents.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvTalents.setLayoutManager(mLayoutManager);
        rvTalents.setScrollViewCallbacks(this);

        fabSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_white_24dp));

        int lvl1 = 0, lvl4 = 0, lvl7 = 0, lvl10 = 0, lvl13 = 0, lvl16 = 0, lvl20 = 0;

        for (Talent t : hero.getTalents()) {
            switch (t.getTalentTier()) {
                case 1:
                    lvl1++;
                case 4:
                    lvl4++;
                case 7:
                    lvl7++;
                case 10:
                    lvl10++;
                case 13:
                    lvl13++;
                case 16:
                    lvl16++;
                case 20:
                    lvl20++;
                    break;
            }

        }

        mMultiSelector.setSelectable(true);
        TalentAdapter talentAdapter = new TalentAdapter(getActivity(), hero, this, mMultiSelector);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Level 1"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(lvl1, "Level 4"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(lvl4, "Level 7"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(lvl7, "Level 10"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(lvl10, "Level 13"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(lvl13, "Level 16"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(lvl16, "Level 20"));

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.section, R.id.section_text, talentAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        rvTalents.setAdapter(mSectionedAdapter);

//        rvTalents.addOnItemTouchListener(
//                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                        Talent talent = (Talent) view.findViewById(R.id.tvTalentName).getTag();
//
//                    }
//                })
//        );

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences prefs = getActivity().getSharedPreferences("com.example.jt.heroes", Context.MODE_PRIVATE);

                List<Talent> talents = getSelectedTalents();
                if (talents.get(0) != null && talents.get(1) != null && talents.get(2) != null
                        && talents.get(3) != null && talents.get(4) != null && talents.get(5) != null
                        && talents.get(6) != null) {
                    new MaterialDialog.Builder(getActivity())
                            .title("Save As")
                            .backgroundColorRes(R.color.really_dark_purple)
                            .customView(R.layout.talent_dialog, true)
                            .positiveText("Save")
                            .negativeText("Cancel")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    View editTextView = dialog.getCustomView();
                                    EditText etBuildName = (EditText) editTextView.findViewById(R.id.etName);
                                    String buildName = etBuildName.getText().toString();

                                    if (!buildName.equals("")) {
                                        SharedPreferences.Editor prefsEditor = prefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(getSelectedTalents());
                                        prefsEditor.putString(buildName, json);
                                        prefsEditor.apply();
                                        Toast.makeText(getActivity(),
                                                "Talent build " + buildName + " has been saved.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Build name cannot be empty.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .show();

                } else {
                    Toast.makeText(getActivity(),
                            "Please select a talent from each tier.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private List<Talent> getSelectedTalents() {
        List<Talent> talentList = new ArrayList<>();

        talentList.add((Talent) ivLvl1.getTag());
        talentList.add((Talent) ivLvl4.getTag());
        talentList.add((Talent) ivLvl7.getTag());
        talentList.add((Talent) ivLvl10.getTag());
        talentList.add((Talent) ivLvl13.getTag());
        talentList.add((Talent) ivLvl16.getTag());
        talentList.add((Talent) ivLvl20.getTag());

        return talentList;
    }

    public RoundedImageView getIvLvl1() {
        return ivLvl1;
    }

    public RoundedImageView getIvLvl4() {
        return ivLvl4;
    }

    public RoundedImageView getIvLvl7() {
        return ivLvl7;
    }

    public RoundedImageView getIvLvl10() {
        return ivLvl10;
    }

    public RoundedImageView getIvLvl13() {
        return ivLvl13;
    }

    public RoundedImageView getIvLvl16() {
        return ivLvl16;
    }

    public RoundedImageView getIvLvl20() {
        return ivLvl20;
    }

    public TextView getTvHelpText() {
        return tvHelpText;
    }

    public FloatingActionButton getFabSave() {
        return fabSave;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            // hide
            fabSave.animate().translationY(500).setDuration(300).start();
        } else if (scrollState == ScrollState.DOWN) {
            // show
            fabSave.animate().translationY(0).setDuration(300).start();
        }
    }
}
