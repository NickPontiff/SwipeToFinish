package com.nickpontiff.swipetofinish;

import android.animation.Animator;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by npontiff on 2/18/15.
 */
public class SwipeToFinishActivity extends ActionBarActivity {

    //Swipe
    private ViewGroup rootView;
    private ViewPager viewPager;
    private float mPrevX;
    private float mPrevY;
    private float mSwipeRatio;

    protected void enableSwipeLeftToFinish(ViewGroup viewGroup){
        this.rootView = viewGroup;
    }

    protected void enableSwipeLeftToFinish(ViewGroup viewGroup, ViewPager viewPager){
        this.rootView = viewGroup;
        this.viewPager = viewPager;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //do nothing if a root view has not been set
        if(rootView!=null) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    //store down touch event
                    mPrevX = event.getRawX();
                    mPrevY = event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    //get the distance the touch has moved.
                    float dx = event.getRawX() - mPrevX;
                    float dy = event.getRawY() - mPrevY;
                    //store new last recorded touch location
                    mPrevX = event.getRawX();
                    mPrevY = event.getRawY();
                    //detect if vertical scroll is greater than horizontal
                    if (Math.abs(dy) > Math.abs(dx)) {
                        //if the activity has been dragged, block vertical scrolling.
                        //otherwise do nothing and dispatch the touch to the child.
                        if(rootView.getTranslationX() != 0){
                            return true;
                        } else {
                            break;
                        }
                    }

                    float translationX = rootView.getTranslationX();

                    //the swipe ratio is the percent of the width of the screen the view has translated
                    mSwipeRatio = translationX / rootView.getMeasuredWidth();

                    //don't translate unless the drag distance is greater than 0
                    if (Math.abs(dx) > 0 && (viewPager == null || viewPager.getCurrentItem() == 0)) {

                        //we don't want to translate to the left of 0
                        float newTranslation = Math.max(translationX + dx, 0);

                        //if the translation is already back to 0, send touch event back to the ViewPager
                        if (translationX == 0 && newTranslation == 0) {
                            break;
                        } else {
                            rootView.setTranslationX(newTranslation);
                            return true;
                        }

                    }
                    break;

                case MotionEvent.ACTION_UP:
                    //do nothing unless the view has been translated
                    if(rootView.getTranslationX() != 0){
                        finishGesture();
                        return true;
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                    //do nothing unless the view has been translated
                    if(rootView.getTranslationX() != 0){
                        animateViewLeft();
                        return true;
                    }
                    break;

            }
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * Animate the root view back to the left.
     */
    private void animateViewLeft() {
        float translation = -rootView.getTranslationX();
        rootView.animate()
                .translationXBy(translation)
                .setDuration(300)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    /**
     * Animate the root view to the right, off of the screen.
     * Finish the activity when the animation completes
     */
    private void animateViewRight() {
        float translation = rootView.getMeasuredWidth() - rootView.getTranslationX();
        rootView.animate()
                .translationXBy(translation)
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    /**
     * Determine whether to animate the view to right or left
     * This is determined based on the value of the swipe ratio.
     * If the view has been translated at least 40% of its width it will be dismissed to the right.
     */
    private void finishGesture() {
        if (rootView.getTranslationX() != 0) {
            if (mSwipeRatio < 0.4f) {
                animateViewLeft();
            } else {
                animateViewRight();
            }
        }
    }
}
