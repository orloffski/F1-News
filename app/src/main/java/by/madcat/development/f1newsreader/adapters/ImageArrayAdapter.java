package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import by.madcat.development.f1newsreader.R;

public class ImageArrayAdapter extends ArrayAdapter<CharSequence> {

    private int _index = 0;
    private String[] entryImages;
    Context mContext;

    public ImageArrayAdapter(Context context, CharSequence[] objects, String[] mEntryImages, int i) {
        super(context, R.layout.image_chooser_element, objects);
        _index = i;
        entryImages = mEntryImages;
        mContext = context;
    }

    static class ViewHolder{
        ImageView image;
        CheckedTextView check;
        String imagePath;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.image_chooser_element, null);

            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.check = (CheckedTextView) convertView.findViewById(R.id.check);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (entryImages != null) {
            Glide.with(mContext)
                    .load(Uri.parse(entryImages[position]))
                    .into(viewHolder.image);
            viewHolder.imagePath = entryImages[position];
        }

        viewHolder.check.setText(getItem(position));

        if (position == _index) {
            viewHolder.check.setChecked(true);
        }

        return convertView;
    }

}
