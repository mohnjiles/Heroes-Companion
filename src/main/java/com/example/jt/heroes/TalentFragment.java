package com.example.jt.heroes;


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

import com.example.jt.heroes.adapters.TalentAdapter;
import com.example.jt.heroes.models.Hero;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class TalentFragment extends Fragment implements Button.OnClickListener {

    private Hero hero;
    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.rvTalents)
    ObservableRecyclerView rvTalents;
    @InjectView(R.id.btnAll)
    Button btnAll;
    @InjectView(R.id.btnLvl1)
    Button btnLvl1;
    @InjectView(R.id.btnLvl4)
    Button btnLvl4;
    @InjectView(R.id.btnLvl7)
    Button btnLvl7;
    @InjectView(R.id.btnLvl10)
    Button btnLvl10;
    @InjectView(R.id.btnLvl13)
    Button btnLvl13;
    @InjectView(R.id.btnLvl16)
    Button btnLvl16;
    @InjectView(R.id.btnLvl20)
    Button btnLvl20;

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
        rvTalents.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(getResources().getColor(R.color.divider_color))
                        .size(1)
                        .build());
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvTalents.setLayoutManager(mLayoutManager);
        rvTalents.setAdapter(new TalentAdapter(getActivity(), hero));

        btnAll.setOnClickListener(this);
        btnLvl1.setOnClickListener(this);
        btnLvl4.setOnClickListener(this);
        btnLvl7.setOnClickListener(this);
        btnLvl10.setOnClickListener(this);
        btnLvl13.setOnClickListener(this);
        btnLvl16.setOnClickListener(this);
        btnLvl20.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if (rvTalents.getAdapter() != null) {

            clearButtonSelection();
            view.setBackgroundColor(getResources().getColor(R.color.deep_purple_300));

            switch (view.getId()) {
                case R.id.btnAll:
                    ((Filterable) rvTalents.getAdapter()).getFilter().filter("All");
                    break;
                case R.id.btnLvl1:
                    ((Filterable) rvTalents.getAdapter()).getFilter().filter("1");
                    break;
                case R.id.btnLvl4:
                    ((Filterable) rvTalents.getAdapter()).getFilter().filter("4");
                    break;
                case R.id.btnLvl7:
                    ((Filterable) rvTalents.getAdapter()).getFilter().filter("7");
                    break;
                case R.id.btnLvl10:
                    ((Filterable) rvTalents.getAdapter()).getFilter().filter("10");
                    break;
                case R.id.btnLvl13:
                    ((Filterable) rvTalents.getAdapter()).getFilter().filter("13");
                    break;
                case R.id.btnLvl16:
                    ((Filterable) rvTalents.getAdapter()).getFilter().filter("16");
                    break;
                case R.id.btnLvl20:
                    ((Filterable) rvTalents.getAdapter()).getFilter().filter("20");
                    break;

            }

            AlphaAnimation anim = new AlphaAnimation(0.1f, 1.0f);
            anim.setDuration(200);
            anim.setFillAfter(true);
            rvTalents.startAnimation(anim);
        }
    }

    private void clearButtonSelection() {
        btnAll.setBackgroundColor(getResources().getColor(R.color.primary));
        btnLvl1.setBackgroundColor(getResources().getColor(R.color.primary));
        btnLvl4.setBackgroundColor(getResources().getColor(R.color.primary));
        btnLvl7.setBackgroundColor(getResources().getColor(R.color.primary));
        btnLvl10.setBackgroundColor(getResources().getColor(R.color.primary));
        btnLvl13.setBackgroundColor(getResources().getColor(R.color.primary));
        btnLvl16.setBackgroundColor(getResources().getColor(R.color.primary));
        btnLvl20.setBackgroundColor(getResources().getColor(R.color.primary));

    }
}
