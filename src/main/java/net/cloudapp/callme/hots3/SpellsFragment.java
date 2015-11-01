package net.cloudapp.callme.hots3;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;

import net.cloudapp.callme.hots3.adapters.SpellsAdapter;
import net.cloudapp.callme.hots3.models.Hero;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpellsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpellsFragment extends Fragment {

    @InjectView(R.id.rvSpells)
    ObservableRecyclerView rvSpells;

    private Hero hero;
    private Toolbar toolbar;

    // TODO: Rename and change types and number of parameters
    public static SpellsFragment newInstance(Hero hero) {
        SpellsFragment fragment = new SpellsFragment();
        Bundle args = new Bundle();
        args.putSerializable("hero", hero);
        fragment.setArguments(args);
        return fragment;
    }

    public SpellsFragment() {
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
        View v = inflater.inflate(R.layout.fragment_spells, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvSpells.setHasFixedSize(true);
        rvSpells.addItemDecoration(new SpacesItemDecoration(getActivity(), 8, 1));

        RecyclerView.LayoutManager mLayoutManager = null;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 1);
        }
        rvSpells.setLayoutManager(mLayoutManager);
        rvSpells.setAdapter(new SpellsAdapter(getActivity(), hero.getSpells()));
    }
}
