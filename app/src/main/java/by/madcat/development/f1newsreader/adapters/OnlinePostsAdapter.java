package by.madcat.development.f1newsreader.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.dataInet.OnlinePost;

public class OnlinePostsAdapter extends RecyclerView.Adapter<OnlineViewHolder>{

    private LinkedList<OnlinePost> onlinePosts;

    public OnlinePostsAdapter(LinkedList<OnlinePost> onlinePosts) {
        this.onlinePosts = onlinePosts;
    }

    @Override
    public OnlineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_post_card, parent, false);

        return new OnlineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OnlineViewHolder holder, int position) {
        OnlinePost post = onlinePosts.get(position);

        holder.time.setText(post.getOnlinePostTime());
        holder.post.setText(Html.fromHtml(post.getOnlinePostText()));
    }

    @Override
    public int getItemCount() {
        return onlinePosts.size();
    }
}
