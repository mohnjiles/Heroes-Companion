package net.cloudapp.callme.hots3;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import net.cloudapp.callme.hots3.adapters.SimpleSectionedRecyclerViewAdapter;
import net.cloudapp.callme.hots3.adapters.TalentAdapter;
import net.cloudapp.callme.hots3.models.Hero;
import net.cloudapp.callme.hots3.models.Talent;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import icepick.Icepick;
import icepick.Icicle;


public class TalentFragment extends Fragment implements ObservableScrollViewCallbacks {


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
    String ivLvl1Name;
    @Icicle
    String ivLvl4Name;
    @Icicle
    String ivLvl7Name;
    @Icicle
    String ivLvl10Name;
    @Icicle
    String ivLvl13Name;
    @Icicle
    String ivLvl16Name;
    @Icicle
    String ivLvl20Name;
    @Icicle
    boolean isHelpTextVisible = true;
    @Icicle
    boolean isLayoutVisible = false;
    private Hero hero;
    private MultiSelector mMultiSelector = new MultiSelector();
    private RecyclerView.LayoutManager mLayoutManager;

    private Talent mLvl1Talent;
    private Talent mLvl4Talent;
    private Talent mLvl7Talent;
    private Talent mLvl10Talent;
    private Talent mLvl13Talent;
    private Talent mLvl16Talent;
    private Talent mLvl20Talent;

    public TalentFragment() {
        // Required empty public constructor
    }

    public static TalentFragment newInstance(Hero hero) {
        TalentFragment fragment = new TalentFragment();
        Bundle args = new Bundle();
        args.putSerializable("hero", hero);
        fragment.setArguments(args);
        return fragment;
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            linearLayout2.setVisibility(View.INVISIBLE);
        }

        rvTalents.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvTalents.setLayoutManager(mLayoutManager);
        rvTalents.setScrollViewCallbacks(this);

        fabSave.setImageResource(R.drawable.ic_save_white_24dp);

        if (savedInstanceState != null && !StringUtil.isBlank(ivLvl1Name)) {
            Utils.GlideLoadSpellImage(this, ivLvl1Name, hero.getName(), ivLvl1);
            Utils.GlideLoadSpellImage(this, ivLvl4Name, hero.getName(), ivLvl4);
            Utils.GlideLoadSpellImage(this, ivLvl7Name, hero.getName(), ivLvl7);
            Utils.GlideLoadSpellImage(this, ivLvl10Name, hero.getName(), ivLvl10);
            Utils.GlideLoadSpellImage(this, ivLvl13Name, hero.getName(), ivLvl13);
            Utils.GlideLoadSpellImage(this, ivLvl16Name, hero.getName(), ivLvl16);
            Utils.GlideLoadSpellImage(this, ivLvl20Name, hero.getName(), ivLvl20);

            linearLayout.setVisibility(isLayoutVisible ? View.VISIBLE : View.INVISIBLE);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
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
                                    EditText etBuildName = null;
                                    String buildName = "";

                                    if (editTextView != null)
                                        etBuildName = (EditText) editTextView.findViewById(R.id.etName);

                                    if (etBuildName != null)
                                        buildName = etBuildName.getText().toString();

                                    if (!buildName.equals("")) {
                                        SharedPreferences.Editor prefsEditor = prefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(getSelectedTalents());
                                        prefsEditor.putString(buildName, json);
                                        prefsEditor.apply();

                                        final String finalBuildName = buildName;
                                        final String finalJson = json;

                                        // Inform user build is saved and allow user to view the build now
                                        Snackbar.make(rvTalents, "Talent build " + buildName + " has been saved.", Snackbar.LENGTH_LONG)
                                                .setAction("View Now", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(getActivity(), BuildDetailActivity.class);
                                                        intent.putExtra("build_name", finalBuildName);
                                                        intent.putExtra("talent_build", finalJson);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .setCallback(new Snackbar.Callback() {
                                                    @Override
                                                    public void onDismissed(Snackbar snackbar, int event) {
                                                        super.onDismissed(snackbar, event);

                                                        Utils.animateFabSnackbarHeightDown(fabSave);
                                                    }
                                                }).show();

                                        Utils.animateFabSnackbarHeight(getActivity(), fabSave);

                                        InputMethodManager inputManager = (InputMethodManager) getActivity()
                                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                                        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                                    } else {
                                        Snackbar.make(rvTalents, "Build name cannot be empty.",
                                                Snackbar.LENGTH_LONG).show();
                                        fabSave.callOnClick();
                                    }
                                }

                                @Override
                                public void onNegative(MaterialDialog dialog) {
                                    super.onNegative(dialog);
                                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                }
                            })
                            .show();

                    // show keyboard
                    InputMethodManager inputManager = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                } else {
                    Snackbar.make(rvTalents,
                            "Please select a talent from each tier.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }

    private List<Talent> getSelectedTalents() {
        List<Talent> talentList = new ArrayList<>();

        talentList.add(mLvl1Talent);
        talentList.add(mLvl4Talent);
        talentList.add(mLvl7Talent);
        talentList.add(mLvl10Talent);
        talentList.add(mLvl13Talent);
        talentList.add(mLvl16Talent);
        talentList.add(mLvl20Talent);

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

    public void setIvLvl20Name(String ivLvl20Name) {
        this.ivLvl20Name = ivLvl20Name;
    }

    public void setIvLvl4Name(String ivLvl4Name) {
        this.ivLvl4Name = ivLvl4Name;
    }

    public void setIvLvl7Name(String ivLvl7Name) {
        this.ivLvl7Name = ivLvl7Name;
    }

    public void setIvLvl10Name(String ivLvl10Name) {
        this.ivLvl10Name = ivLvl10Name;
    }

    public void setIvLvl13Name(String ivLvl13Name) {
        this.ivLvl13Name = ivLvl13Name;
    }

    public void setIvLvl16Name(String ivLvl16Name) {
        this.ivLvl16Name = ivLvl16Name;
    }

    public void setIvLvl1Name(String ivLvl1Name) {
        this.ivLvl1Name = ivLvl1Name;
    }


    public void setmLvl1Talent(Talent mLvl1Talent) {
        this.mLvl1Talent = mLvl1Talent;
    }

    public void setmLvl4Talent(Talent mLvl4Talent) {
        this.mLvl4Talent = mLvl4Talent;
    }

    public void setmLvl7Talent(Talent mLvl7Talent) {
        this.mLvl7Talent = mLvl7Talent;
    }


    public void setmLvl10Talent(Talent mLvl10Talent) {
        this.mLvl10Talent = mLvl10Talent;
    }

    public void setmLvl13Talent(Talent mLvl13Talent) {
        this.mLvl13Talent = mLvl13Talent;
    }

    public void setmLvl16Talent(Talent mLvl16Talent) {
        this.mLvl16Talent = mLvl16Talent;
    }


    public void setmLvl20Talent(Talent mLvl20Talent) {
        this.mLvl20Talent = mLvl20Talent;
    }


    public void setIsHelpTextVisible(boolean isHelpTextVisible) {
        this.isHelpTextVisible = isHelpTextVisible;
    }

    public void setIsLayoutVisible(boolean isLayoutVisible) {
        this.isLayoutVisible = isLayoutVisible;
    }

    public ObservableRecyclerView getRvTalents() {
        return rvTalents;
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
