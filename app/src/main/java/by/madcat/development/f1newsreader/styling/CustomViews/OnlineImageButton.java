package by.madcat.development.f1newsreader.styling.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import by.madcat.development.f1newsreader.R;

public class OnlineImageButton extends LinearLayout{

    private View rootView;
    private TextView linkText;
    private ImageView linkImage;

    private String linkTextString;
    private Drawable linkImageActive;
    private Drawable linkImageInactive;

    public OnlineImageButton(Context context) {
        super(context);
        init(context);
        handleAttributes(context, null);
    }

    public OnlineImageButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        handleAttributes(context, attrs);
    }

    private void handleAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.OnlineImageButton, 0, 0);

        linkTextString = typedArray.getString(R.styleable.OnlineImageButton_link_text);
        linkImageActive = typedArray.getDrawable(R.styleable.OnlineImageButton_active_image);
        linkImageInactive = typedArray.getDrawable(R.styleable.OnlineImageButton_inactive_image);

        linkText.setText(linkTextString);
        linkImage.setImageDrawable(linkImageActive);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.online_image_button, this);

        linkText = (TextView) rootView.findViewById(R.id.link_text);
        linkImage = (ImageView) rootView.findViewById(R.id.link_image);
    }


}
