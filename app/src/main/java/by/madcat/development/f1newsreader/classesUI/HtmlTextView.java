package by.madcat.development.f1newsreader.classesUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.DocParseUtils;

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
        linearLayout.setOrientation(VERTICAL);
    }

    public void setHtmlText(String htmlText){
        ArrayList<View> views = DocParseUtils.getViews(htmlText, getContext());

        if(linearLayout.getChildCount() == 0)
            for(View view : views) {
                linearLayout.addView(view);
            }
    }
}
