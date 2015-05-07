package com.example.jt.heroes;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jt.heroes.models.Hero;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.Observable;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class TalentFragment extends Fragment {

    private Hero hero;
    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.rvTalents)
    ObservableRecyclerView rvTalents;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvTalents.setHasFixedSize(true);
        rvTalents.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.argb(115, 255, 255, 255))
                        .size(1)
                        .build());
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvTalents.setLayoutManager(mLayoutManager);
        rvTalents.setAdapter(new TalentAdapter(getActivity(), hero));
    }
}
