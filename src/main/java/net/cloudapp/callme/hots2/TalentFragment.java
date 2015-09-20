package net.cloudapp.callme.hots2;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bignerdranch.android.multiselector.MultiSelector;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import net.cloudapp.callme.hots2.adapters.SimpleSectionedRecyclerViewAdapter;
import net.cloudapp.callme.hots2.adapters.TalentAdapter;
import net.cloudapp.callme.hots2.models.Hero;
import net.cloudapp.callme.hots2.models.Talent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import icepick.Icepick;
import icepick.Icicle;


public class TalentFragment extends Fragment implements ObservableScrollViewCallbacks {

    private Hero hero;
    private MultiSelector mMultiSelector = new MultiSelector();
    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.rvTalents)
    ObservableRecyclerView rvTalents;

    @InjectView(R.id.ivTalentImage)

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

    @InjectView(R.id.linearLayout2)
    @Optional
    LinearLayout linearLayout2;

    @Icicle
    int ivLvl1Res;
    @Icicle
    int ivLvl4Res;
    @Icicle
    int ivLvl7Res;
    @Icicle
    int ivLvl10Res;
    @Icicle
    int ivLvl13Res;
    @Icicle
    int ivLvl16Res;
    @Icicle
    int ivLvl20Res;

    @Icicle
    boolean isHelpTextVisible = true;
    @Icicle
    boolean isLayoutVisible = false;


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
        Icepick.restoreInstanceState(this, savedInstanceState);

        linearLayout.setVisibility(View.INVISIBLE);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            linearLayout2.setVisibility(View.INVISIBLE);
        }

        rvTalents.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvTalents.setLayoutManager(mLayoutManager);
        rvTalents.setScrollViewCallbacks(this);

        fabSave.setImageDrawable(getResources().getDrawable(R.drawable.ic_save_white_24dp));

        if (savedInstanceState != null) {
            ivLvl1.setImageResource(ivLvl1Res);
            ivLvl4.setImageResource(ivLvl4Res);
            ivLvl7.setImageResource(ivLvl7Res);
            ivLvl10.setImageResource(ivLvl10Res);
            ivLvl13.setImageResource(ivLvl13Res);
            ivLvl16.setImageResource(ivLvl16Res);
            ivLvl20.setImageResource(ivLvl20Res);

            linearLayout.setVisibility(isLayoutVisible ? View.VISIBLE : View.INVISIBLE);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                linearLayout2.setVisibility(isLayoutVisible ? View.VISIBLE : View.INVISIBLE);
            }
            tvHelpText.setVisibility(isHelpTextVisible ? View.VISIBLE : View.INVISIBLE);
        }

        int lvl1 = 0, lvl4 = 0, lvl7 = 0, lvl10 = 0, lvl13 = 0, lvl16 = 0, lvl20 = 0;

        for (Talent t : hero.getTalents()) {
            switch (t.getTier()) {
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
                            .backgroundColorRes(R.color.sorta_dark_purple)
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
                                        fabSave.callOnClick();
                                    }
                                }
                            })
                            .show();

                } else {
                    Toast.makeText(getActivity(),
                            "Please select a talent from each tier.", Toast.LENGTH_SHORT).show();
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
    public LinearLayout getLinearLayout2() {
        return linearLayout2;
    }

    public void setIvLvl1Res(int ivLvl1Res) {
        this.ivLvl1Res = ivLvl1Res;
    }

    public void setIvLvl4Res(int ivLvl4Res) {
        this.ivLvl4Res = ivLvl4Res;
    }

    public void setIvLvl7Res(int ivLvl7Res) {
        this.ivLvl7Res = ivLvl7Res;
    }

    public void setIvLvl10Res(int ivLvl10Res) {
        this.ivLvl10Res = ivLvl10Res;
    }

    public void setIvLvl13Res(int ivLvl13Res) {
        this.ivLvl13Res = ivLvl13Res;
    }

    public void setIvLvl16Res(int ivLvl16Res) {
        this.ivLvl16Res = ivLvl16Res;
    }

    public void setIvLvl20Res(int ivLvl20Res) {
        this.ivLvl20Res = ivLvl20Res;
    }


    public void setIsHelpTextVisible(boolean isHelpTextVisible) {
        this.isHelpTextVisible = isHelpTextVisible;
    }

    public void setIsLayoutVisible(boolean isLayoutVisible) {
        this.isLayoutVisible = isLayoutVisible;
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}