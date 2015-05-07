package com.example.jt.heroes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jt.heroes.models.Hero;
import com.example.jt.heroes.models.Spell;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SpellsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpellsFragment extends Fragment {

    @InjectView(R.id.rlSpells)
    RelativeLayout rlSpells;

    private Hero hero;

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

    static class ViewHolder {
        @InjectView(R.id.tvSpellName)
        TextView tvSpellName;
        @InjectView(R.id.tvSpellText)
        TextView tvSpellText;
        @InjectView(R.id.ivSpellImage)
        ImageView ivSpellImage;
        @InjectView(R.id.tvLetter)
        TextView tvLetter;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int lastView = 0;
        int counter = 1;

        for (Spell spell : hero.getSpells()) {
            ViewHolder holder;
            RelativeLayout spellLayout;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            spellLayout = (RelativeLayout) getLayoutInflater(savedInstanceState)
                    .inflate(R.layout.spell, rlSpells, false);
            spellLayout.setId(counter);
            holder = new ViewHolder(spellLayout);
            view.setTag(holder);

            lp.addRule(RelativeLayout.BELOW, lastView);
            lp.setMargins(0, 16, 0, 0);

            lastView = spellLayout.getId();

            rlSpells.addView(spellLayout, lp);
            holder.tvSpellName.setText(spell.getName().toUpperCase(Locale.getDefault()));
            holder.tvSpellText.setText(spell.getDescription());
            holder.tvLetter.setText("[ " + spell.getLetter().toUpperCase(Locale.getDefault()) + " ]");
            holder.ivSpellImage.setImageResource(Utils.getResourceIdByName(getActivity(), Utils.formatSpellImageName(spell.getName())));
            counter++;
        }

    }
}
