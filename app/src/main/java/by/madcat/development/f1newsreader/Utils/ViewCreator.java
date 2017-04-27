package by.madcat.development.f1newsreader.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TableRow;
import android.widget.TextView;

public class ViewCreator {
    public static TableRow createTableRowView(Context context, TableRow.LayoutParams layoutParams){
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(layoutParams);

        return tableRow;
    }

    public static TextView createTextView(Context context, String text, int textSize, Typeface tf, int style, int color){
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTypeface(tf, style);
        textView.setTextColor(color);

        return textView;
    }
}
