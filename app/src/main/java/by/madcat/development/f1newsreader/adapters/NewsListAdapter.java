package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.madcat.development.f1newsreader.R;

public class NewsListAdapter extends NewsListAbstractAdapter{

    private final NewsListAbstractAdapter.ClickListener clickListener;

    public NewsListAdapter(ClickListener clickListener, Context context){
        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_card, parent, false);
        return new NewsViewHolder(view, clickListener);
    }
}
