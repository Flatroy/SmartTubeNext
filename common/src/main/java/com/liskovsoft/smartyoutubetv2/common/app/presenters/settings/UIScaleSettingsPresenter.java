package com.liskovsoft.smartyoutubetv2.common.app.presenters.settings;

import android.content.Context;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.smartyoutubetv2.common.R;
import com.liskovsoft.smartyoutubetv2.common.app.models.playback.ui.OptionItem;
import com.liskovsoft.smartyoutubetv2.common.app.models.playback.ui.UiOptionItem;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.AppSettingsPresenter;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.base.BasePresenter;
import com.liskovsoft.smartyoutubetv2.common.app.views.ViewManager;
import com.liskovsoft.smartyoutubetv2.common.prefs.MainUIData;

import java.util.ArrayList;
import java.util.List;

public class UIScaleSettingsPresenter extends BasePresenter<Void> {
    private final MainUIData mMainUIData;
    private boolean mRestartApp;

    public UIScaleSettingsPresenter(Context context) {
        super(context);
        mMainUIData = MainUIData.instance(context);
    }

    public static UIScaleSettingsPresenter instance(Context context) {
        return new UIScaleSettingsPresenter(context);
    }

    public void show() {
        AppSettingsPresenter settingsPresenter = AppSettingsPresenter.instance(getContext());
        settingsPresenter.clear();

        appendScaleUI(settingsPresenter);
        appendVideoGridScale(settingsPresenter);

        settingsPresenter.showDialog(getContext().getString(R.string.settings_ui_scale), () -> {
            if (mRestartApp) {
                mRestartApp = false;
                ViewManager.instance(getContext()).restartApp();
            }
        });
    }

    private void appendVideoGridScale(AppSettingsPresenter settingsPresenter) {
        List<OptionItem> options = new ArrayList<>();

        for (float scale : new float[] {0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.35f, 1.4f, 1.5f}) {
            options.add(UiOptionItem.from(String.format("%sx", scale),
                    optionItem -> mMainUIData.setVideoGridScale(scale),
                    Helpers.floatEquals(scale, mMainUIData.getVideoGridScale())));
        }

        settingsPresenter.appendRadioCategory(getContext().getString(R.string.video_grid_scale), options);
    }

    private void appendScaleUI(AppSettingsPresenter settingsPresenter) {
        List<OptionItem> options = new ArrayList<>();

        for (float scale : new float[] {0.6f, 0.65f, 0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f, 1.0f, 1.05f, 1.1f, 1.15f, 1.2f}) {
            options.add(UiOptionItem.from(String.format("%sx", scale),
                    optionItem -> {
                        mMainUIData.setUIScale(scale);
                        mRestartApp = true;
                    },
                    Helpers.floatEquals(scale, mMainUIData.getUIScale())));
        }

        settingsPresenter.appendRadioCategory(getContext().getString(R.string.scale_ui), options);
    }
}