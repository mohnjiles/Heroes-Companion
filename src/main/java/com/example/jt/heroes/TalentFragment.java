package com.example.jt.heroes;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Filterable;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.example.jt.heroes.adapters.SimpleSectionedRecyclerViewAdapter;
import com.example.jt.heroes.adapters.TalentAdapter;
import com.example.jt.heroes.models.Hero;
import com.example.jt.heroes.models.Talent;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class TalentFragment extends Fragment  {

    private Hero hero;
    private MultiSelector mMultiSelector = new MultiSelector();

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

        rvTalents.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvTalents.setLayoutManager(mLayoutManager);

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
}
