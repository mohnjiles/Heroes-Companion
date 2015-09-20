package net.cloudapp.callme.hots2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;


import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.cloudapp.callme.hots2.models.Hero;
import net.cloudapp.callme.hots2.models.Talent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class BuildDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.rvBuildTalents)
    ObservableRecyclerView rvBuildTalents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_detail);
        ButterKnife.inject(this);

        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.dark_primary));
        }

        if (toolbar != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }

        Intent intent = getIntent();
        String json = intent.getStringExtra("talent_build");
        String buildName = intent.getStringExtra("build_name");

        toolbar.setTitle(buildName);

        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Talent>>(){}.getType();
        List<Talent> talentList = gson.fromJson(json, listType);

        HeroDatabase db = HeroDatabase.getInstance(this);
        Hero hero = db.getHeroById(talentList.get(0).getHeroId());

        rvBuildTalents.setHasFixedSize(true);
        rvBuildTalents.setLayoutManager(new LinearLayoutManager(this));
        rvBuildTalents.setAdapter(new BuildTalentAdapter(this, talentList, buildName, hero.getName()));
        rvBuildTalents.setScrollViewCallbacks(this);
        rvBuildTalents.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_build_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
