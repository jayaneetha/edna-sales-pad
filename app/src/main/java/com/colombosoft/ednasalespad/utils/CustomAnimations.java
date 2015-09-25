package com.colombosoft.ednasalespad.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.colombosoft.ednasalespad.R;

/**
 * Created by thahzan on 1/13/15.
 */
public class CustomAnimations {

    private Animation pushUpIn, pushUpOut, pushDownIn, pushDownOut;

    public CustomAnimations(Context context) {
        pushUpIn = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
        pushUpOut = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
        pushDownIn = AnimationUtils.loadAnimation(context, R.anim.push_down_in);
        pushDownOut = AnimationUtils.loadAnimation(context, R.anim.push_down_out);
    }

    public Animation getPushUpIn() {
        return pushUpIn;
    }

    public void setPushUpIn(Animation pushUpIn) {
        this.pushUpIn = pushUpIn;
    }

    public Animation getPushUpOut() {
        return pushUpOut;
    }

    public void setPushUpOut(Animation pushUpOut) {
        this.pushUpOut = pushUpOut;
    }

    public Animation getPushDownIn() {
        return pushDownIn;
    }

    public void setPushDownIn(Animation pushDownIn) {
        this.pushDownIn = pushDownIn;
    }

    public Animation getPushDownOut() {
        return pushDownOut;
    }

    public void setPushDownOut(Animation pushDownOut) {
        this.pushDownOut = pushDownOut;
    }
}
