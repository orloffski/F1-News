package by.madcat.development.f1newsreader.classesUI;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.madcat.development.f1newsreader.R;

public class HtmlTextView extends LinearLayout{

    private LinearLayout linearLayout;

    public HtmlTextView(Context context) {
        super(context);
        initView();
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        linearLayout = (LinearLayout)findViewById(R.id.html_text_view);
    }

    public void setHtmlText(String htmlText){
        TextView text = new TextView(getContext());
        text.setText(htmlText);
        linearLayout.addView(text);
    }
}
