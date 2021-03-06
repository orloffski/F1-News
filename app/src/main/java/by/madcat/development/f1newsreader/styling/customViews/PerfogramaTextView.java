package by.madcat.development.f1newsreader.styling.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.styling.Typefaces;

public class PerfogramaTextView extends android.support.v7.widget.AppCompatTextView {

    public PerfogramaTextView(Context context) {
        this(context, null, 0);
    }

    public PerfogramaTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public PerfogramaTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }

    private void setFont(Context context) {
        Typeface face = Typefaces.get(context, context.getText(R.string.font_Perfograma).toString());
        setTypeface(face);
    }
}
