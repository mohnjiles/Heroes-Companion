package com.example.jt.heroes;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jt.heroes.models.Hero;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeroDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeroDetailFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    @InjectView(R.id.tv_hero_title)
    TextView tvHeroTitle;
    @InjectView(R.id.iv_hero_image)
    RoundedImageView ivHeroImage;
    @InjectView(R.id.ivFranchise)
    ImageView ivFranchise;
    @InjectView(R.id.ivRole)
    ImageView ivRole;
    @InjectView(R.id.tv_lore)
    TextView tvLore;
    @InjectView(R.id.tv_role)
    TextView tvRole;
    @InjectView(R.id.tv_franchise)
    TextView tvFranchise;
    @InjectView(R.id.tv_type)
    TextView tvType;
    @InjectView(R.id.tv_hp)
    TextView tvHp;
    @InjectView(R.id.tv_hp_per_level)
    TextView tvHpPerLevel;
    @InjectView(R.id.tv_hp_regen)
    TextView tvHpRegen;
    @InjectView(R.id.tv_hp_regen_per_level)
    TextView tvHpRegenPerLevel;
    @InjectView(R.id.tv_energy)
    TextView tvEnergy;
    @InjectView(R.id.tv_energy_per_level)
    TextView tvEnergyPerLevel;
    @InjectView(R.id.tv_energy_regen)
    TextView tvEnergyRegen;
    @InjectView(R.id.tv_energy_regen_per_level)
    TextView tvEnergyRegenPerLevel;
    @InjectView(R.id.tv_attack_damage)
    TextView tvAttackDamage;
    @InjectView(R.id.tv_attack_damage_per_level)
    TextView tvAttackDamagePerLevel;
    @InjectView(R.id.tv_attack_speed)
    TextView tvAttackSpeed;
    @InjectView(R.id.mrScrolls)
    ObservableScrollView mrScrolls;

    private Hero hero;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment HeroDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeroDetailFragment newInstance(Hero hero) {
        HeroDetailFragment fragment = new HeroDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("hero", hero);
        fragment.setArguments(args);
        return fragment;
    }

    public HeroDetailFragment() {
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
        View v = inflater.inflate(R.layout.fragment_hero_detail, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String heroTitle = hero.getName() + ", " + hero.getTitle();

        ivHeroImage.setBorderColor(Color.BLACK);
        ivHeroImage.mutateBackground(true);
        ivHeroImage.setImageResource(Utils.getResourceIdByName(getActivity(), hero.getName()));

        ivRole.setImageResource(Utils.getResourceIdByName(getActivity(), hero.getRole()));
        ivFranchise.setImageResource(Utils.getResourceIdByName(getActivity(), hero.getFranchise() + "game"));

        tvHeroTitle.setText(heroTitle.toUpperCase());
        tvRole.setText(hero.getRole());
        tvFranchise.setText(hero.getFranchise());
        tvType.setText(hero.getType());
        tvHp.setText(String.valueOf(hero.getHp()));
        tvHpRegen.setText(String.valueOf(hero.getHpRegen()));
        tvEnergy.setText(String.valueOf(hero.getMana()));
        tvEnergyRegen.setText(String.valueOf(hero.getManaRegen()));
        tvAttackSpeed.setText(String.valueOf(hero.getAttackSpeed()));
        tvAttackDamage.setText(String.valueOf(hero.getAttackDamage()));
        tvHpPerLevel.setText(String.valueOf(hero.getHpPerLevel()));
        tvHpRegenPerLevel.setText(String.valueOf(hero.getHpRegenPerLevel()));
        tvAttackDamagePerLevel.setText(String.valueOf(hero.getAttackDamagePerLevel()));
        tvEnergyPerLevel.setText(String.valueOf(hero.getManaPerLevel()));
        tvEnergyRegenPerLevel.setText(String.valueOf(hero.getManaRegenPerLevel()));
        tvLore.setText(hero.getLore());

    }

}
