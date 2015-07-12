package com.ovlstuff.android.spotifystreamer.util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by ovaldez on 7/12/15.
 */
public class Util {

    public static void hideSfotKeyboard(Activity activity, TextView textView) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(textView.getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textView.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
