package com.mmednet.library.view.edit;

import android.content.Context;

import com.mmednet.library.view.EditLayout;

public class ViewFactory {

    public static EditView create(Context context, int type) {
        if (type == EditLayout.TYPE_EDITBOX)
            return new IEditText(context);
        if (type == EditLayout.TYPE_TEXTBOX)
            return new ITextView(context);
        if (type == EditLayout.TYPE_SPINNER)
            return new ISpinner(context);
        if (type == EditLayout.TYPE_CHECKBOX)
            return new ICheckBox(context);
        if (type == EditLayout.TYPE_RADIOBOX)
            return new IRadioBox(context);
        if (type == EditLayout.TYPE_SINGLEBOX)
            return new ISingleBox(context);
        if (type == EditLayout.TYPE_MULTIBOX)
            return new IMultiBox(context);
        return new IEditText(context);
    }

}
