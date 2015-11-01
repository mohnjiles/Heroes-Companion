package net.cloudapp.callme.hots3;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
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

    public static int getRawResourceByName(Context context, String name) {
        Resources resources = context.getResources();

        String rawFileName = name.replaceAll("[^A-Za-z]", "").toLowerCase();

        return resources.getIdentifier(rawFileName, "raw", context.getPackageName());
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
        HeroDatabase db = HeroDatabase.getInstance(context);

        return db.getHeroIdFromName(heroName);
    }

    public static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    public static void GlideLoadImage(final Fragment context, int resId, final String imageUrl, final ImageView imageView) {

        if (resId != 0) {
            Glide.with(context)
                    .load(resId)
                    .into(imageView);
        } else {
            Glide
                    .with(context)
                    .load(imageUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }
    public static void GlideLoadImage(final Context context, int resId, final String imageUrl, final ImageView imageView) {

        if (resId != 0) {
            Glide.with(context)
                    .load(resId)
                    .into(imageView);
        } else {
            Glide
                    .with(context)
                    .load(imageUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        }
    }

    public static void GlideLoadSpellImage(final Context context, String spellName, String heroName, ImageView imageView) {
        GlideLoadImage(context,
                Utils.getResourceIdByName(context, spellName),
                Constants.IMAGE_BASE_URL + heroName + "/"
                        + Utils.formatSpellImageName(spellName) + ".png",
                imageView);
    }

    public static void GlideLoadSpellImage(final Fragment context, String spellName, String heroName, ImageView imageView) {
        GlideLoadImage(context,
                Utils.getResourceIdByName(context.getActivity(), spellName),
                Constants.IMAGE_BASE_URL + heroName + "/"
                        + Utils.formatSpellImageName(spellName) + ".png",
                imageView);
    }

    public static void GlideLoadBigHeroImage(final Context context, String heroName, ImageView imageView) {
        GlideLoadImage(context,
                Utils.getResourceIdByName(context, heroName + "big"),
                Constants.BIG_HERO_IMAGES_URL + Utils.formatSpellImageName(heroName)
                        + "big.compressed.png",
                imageView);
    }

    public static int dpToPixels(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static void animateFabSnackbarHeight(Context context, FloatingActionButton fabToAnimate) {
        fabToAnimate.animate().translationY(dpToPixels(context, -48)).start();
    }

    public static void animateFabSnackbarHeightDown(FloatingActionButton fabToAnimate) {
        fabToAnimate.animate().translationY(0).start();
    }
}

