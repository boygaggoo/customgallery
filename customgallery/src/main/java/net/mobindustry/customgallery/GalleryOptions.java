package net.mobindustry.customgallery;

import android.content.Context;
import android.graphics.drawable.Drawable;


public class GalleryOptions {
    private int colorToolbar;
    private int colorSelectedToolbar;
    private Drawable buttonBackground;
    private int colorBackground;
    private int colorBackgroundGridView;
    private int textColorInButtons;
    private int textColorInToolbar;
    private Drawable drawableNavigationIconToolbar;
    private Drawable drawablePlaceHolder;

    private Context context;

    public GalleryOptions(Context context) {
        this.context = context;
        setColorToolbar(context.getResources().getColor(R.color.background_action_bar));
        setColorSelectedToolbar(context.getResources().getColor(R.color.background_action_bar_checked));
        setButtonBackground(context.getResources().getDrawable(R.drawable.custom_btn));
        setColorBackground(context.getResources().getColor(R.color.background_activity));
        setColorBackgroundGridView(context.getResources().getColor(R.color.background_grid));
        setTextColorInButtons(context.getResources().getColor(R.color.background_activity));
        setTextColorInToolbar(context.getResources().getColor(R.color.background_activity));
        setDrawableNavigationIconToolbar(context.getResources().getDrawable(R.drawable.ic_back));
        setDrawablePlaceHolder(context.getResources().getDrawable(R.drawable.ic_netelegram_placeholder));
    }

    public Drawable getButtonBackground() {
        return buttonBackground;
    }

    public Drawable getDrawablePlaceHolder() {
        return drawablePlaceHolder;
    }

    public void setDrawablePlaceHolder(Drawable drawablePlaceHolder) {
        this.drawablePlaceHolder = drawablePlaceHolder;
    }

    public int getColorBackgroundGridView() {
        return colorBackgroundGridView;
    }

    public void setColorBackgroundGridView(int colorBackgroundGridView) {
        this.colorBackgroundGridView = colorBackgroundGridView;
    }

    public void setButtonBackground(Drawable buttonBackground) {
        this.buttonBackground = buttonBackground;
    }

    public int getColorToolbar() {
        return colorToolbar;
    }

    public int getColorSelectedToolbar() {
        return colorSelectedToolbar;
    }

    public Drawable getDrawableNavigationIconToolbar() {
        return drawableNavigationIconToolbar;
    }

    public void setDrawableNavigationIconToolbar(Drawable drawableNavigationIconToolbar) {
        this.drawableNavigationIconToolbar = drawableNavigationIconToolbar;
    }

    public int getColorBackground() {
        return colorBackground;
    }

    public int getTextColorInButtons() {
        return textColorInButtons;
    }

    public int getTextColorInToolbar() {
        return textColorInToolbar;
    }



    public void setColorToolbar(int colorToolbar) {
        this.colorToolbar = colorToolbar;
    }

    public void setColorSelectedToolbar(int colorSelectedToolbar) {
        this.colorSelectedToolbar = colorSelectedToolbar;
    }


    public void setColorBackground(int colorBackground) {
        this.colorBackground = colorBackground;
    }

    public void setTextColorInButtons(int textColorInButtons) {
        this.textColorInButtons = textColorInButtons;
    }

    public void setTextColorInToolbar(int textColorInToolbar) {
        this.textColorInToolbar = textColorInToolbar;
    }

}
