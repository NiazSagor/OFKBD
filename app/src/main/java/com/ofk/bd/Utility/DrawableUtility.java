package com.ofk.bd.Utility;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.ofk.bd.R;

public class DrawableUtility {

    private static final int[] avatars = {R.drawable.ic_dog, R.drawable.ic_duck, R.drawable.ic_fox, R.drawable.ic_lion, R.drawable.ic_cat, R.drawable.ic_tiger, R.drawable.ic_squirrel, R.drawable.ic_giraffe, R.drawable.ic_elephant, R.drawable.ic_parrot};

    private static final int[] profileItem = {R.drawable.ic_profile, R.drawable.ic_phone, R.drawable.ic_email, R.drawable.ic_resource_class, R.drawable.ic_school, R.drawable.ic_birthday, R.drawable.ic_gender, R.drawable.ic_logout};

    private static final int[] badge_icons = {R.drawable.apprentice_1, R.drawable.apprentice_2, R.drawable.apprentice_3,
            R.drawable.journeyman_1, R.drawable.journeyman_2, R.drawable.journeyman_3,
            R.drawable.master_1, R.drawable.master_2, R.drawable.master_3,
            R.drawable.grand_master_1, R.drawable.grand_master_2, R.drawable.grand_master_3,
            R.drawable.super_kids_1, R.drawable.super_kids_2, R.drawable.super_kids_3};

    private static final int[] blogThumbs = {R.drawable.mental, R.drawable.story, R.drawable.video, R.drawable.skill, R.drawable.awarness, R.drawable.fiction};

    private static final int[] sectionIcons = {R.drawable.art, R.drawable.calligraphy, R.drawable.case_solve, R.drawable.craft, R.drawable.critical, R.drawable.digital,
            R.drawable.guitar, R.drawable.programming, R.drawable.robotics};

    private static final int[] sectionGradients = {R.drawable.gradient_pink, R.drawable.gradient_cyan, R.drawable.gradient_purple,
            R.drawable.gradient_blue, R.drawable.gradient_purple_pink, R.drawable.gradient_pink_yellow,
            R.drawable.gradient_pink, R.drawable.gradient_cyan, R.drawable.gradient_purple};

    // get badge
    public static Drawable getDrawable(Context context, int index) {
        return ContextCompat.getDrawable(context, badge_icons[index]);
    }

    // get profile items
    public static Drawable getProfileItemDrawable(Context context, int index) {
        return ContextCompat.getDrawable(context, profileItem[index]);
    }

    // get avatar
    public static Drawable getAvatarDrawable(Context context, int index) {
        return ContextCompat.getDrawable(context, avatars[index]);
    }

    // get blog thumb
    public static Drawable getBlogThumb(Context context, int index) {
        return ContextCompat.getDrawable(context, blogThumbs[index]);
    }

    // get section icons
    public static Drawable getSectionIcon(Context context, int index){
        return ContextCompat.getDrawable(context, sectionIcons[index]);
    }

    // get section gradients
    public static Drawable getGradient(Context context, int index){
        return ContextCompat.getDrawable(context, sectionGradients[index]);
    }
}
