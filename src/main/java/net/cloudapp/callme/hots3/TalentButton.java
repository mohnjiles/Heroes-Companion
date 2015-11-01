package net.cloudapp.callme.hots3;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by JT on 5/19/2015.
 */
public class TalentButton extends Button {

    private boolean isSelected;
    private boolean hasBackgroundResource;

    public TalentButton(Context context) {
        super(context);
    }

    public TalentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TalentButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TalentButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
        this.setHasBackgroundResource(true);
    }


    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        this.setHasBackgroundResource(false);
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean hasBackgroundResource() {
        return hasBackgroundResource;
    }

    public void setHasBackgroundResource(boolean hasBackgroundResource) {
        this.hasBackgroundResource = hasBackgroundResource;
    }
}
