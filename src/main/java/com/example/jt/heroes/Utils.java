package com.example.jt.heroes;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

import com.example.jt.heroes.models.Hero;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by JT on 3/13/2015.
 */
public class Utils {

    public static int getResourceIdByName(Context context, String name) {
        Resources resources = context.getResources();

        String drawableName = name.replaceAll("[^A-Za-z]", "").toLowerCase();

        return resources.getIdentifier(drawableName, "drawable", context.getPackageName());
    }

    static float getPixelsFromDp(int dp, Context cxt) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, cxt.getResources().getDisplayMetrics());
    }

    public static void expand(final View v) {
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void animateHeight(final View view, int from, int to, int duration) {
        ValueAnimator anim = ValueAnimator.ofInt(from, to);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(duration);
        anim.start();
    }

    public static String formatSpellImageName(String spellName) {
        return spellName.toLowerCase(Locale.getDefault()).replaceAll("[^a-z]", "").replace("trait", "");
    }

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static int getHeroIdFromName(String heroName, Context context) {
        HeroDatabase db = new HeroDatabase(context);

        return db.getHeroIdFromName(heroName);
    }

}
