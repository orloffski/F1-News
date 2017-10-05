package by.madcat.development.f1newsreader.Styling.CustomViews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListAdapter;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.adapters.ImageArrayAdapter;

public class ImageListPreference extends ListPreference {

    private String[] mEntryImages;

    public ImageListPreference(Context context) {
        super(context);
    }

    public ImageListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageListPreference);
            String[] imageNames = context.getResources().getStringArray(typedArray.getResourceId(typedArray.getIndexCount() - 1, -1));

            setEntryImages(imageNames);

            typedArray.recycle();
        } catch (Exception e) {
        }
    }

    private void setEntryImages(String[] entryImages) {
        mEntryImages = entryImages;
    }

    protected void onPrepareDialogBuilder(final AlertDialog.Builder builder) {
        int index = findIndexOfValue(getSharedPreferences().getString(getKey(), "0"));
        ListAdapter listAdapter;
        listAdapter = new ImageArrayAdapter(getContext(), getEntries(), mEntryImages, index);
        builder.setAdapter(listAdapter, this);
        builder.setTitle(getDialogTitle());
        builder.setNegativeButton(android.R.string.cancel, null);
        super.onPrepareDialogBuilder(builder);
    }

}
