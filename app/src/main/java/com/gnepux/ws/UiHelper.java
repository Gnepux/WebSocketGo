package com.gnepux.ws;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * @author gnepux
 */
class UiHelper {

    static Button addBtn(Activity activity, int id, String text, View.OnClickListener listener) {
        LinearLayout linearLayout = activity.findViewById(id == 0 ? R.id.view0 : R.id.view1);
        Button button = new Button(activity);
        button.setText(text);
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setOnClickListener(listener);
        linearLayout.addView(button);
        return button;
    }

    static EditText addEditText(Activity activity, int id, String text) {
        LinearLayout linearLayout = activity.findViewById(id == 0 ? R.id.view0 : R.id.view1);
        EditText editText = new EditText(activity);
        editText.setText(text);
        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(editText);
        return editText;
    }

}
