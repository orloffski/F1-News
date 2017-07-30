package by.madcat.development.f1newsreader.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.adapters.ViewHolders.SessionViewHolder;
import by.madcat.development.f1newsreader.dataInet.Models.HelmetsDataModel;
import by.madcat.development.f1newsreader.dataInet.Models.TimingElement;

public class OnlineSessionAdapter extends RecyclerView.Adapter<SessionViewHolder>{

    private LinkedList<TimingElement> timings;
    private Context context;

    public OnlineSessionAdapter(LinkedList<TimingElement> timings, Context context) {
        this.timings = timings;
        this.context = context;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_timing_element, parent, false);

        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        TimingElement element = timings.get(position);

        holder.name.setText(element.getName());
        holder.position.setText(String.valueOf(element.getPosition()));
        holder.gap.setText(element.getGap().equals("-")?element.getGap():"+" + element.getGap());
        holder.bestLap.setText(element.getBestLap());
        holder.pits.setText(element.getPits());
        holder.lastLap.setText(String.valueOf(element.getLastLap()));

        Glide.with(context).load(getHelmetFromDrawable(element.getName())).placeholder(R.drawable.helmets_default).into(holder.helmet);
    }

    @Override
    public int getItemCount() {
        return timings.size();
    }

    private int getHelmetFromDrawable(String driverName){
        if(driverName.equals("Алонсо"))
            return R.drawable.helmets_alonso;
        if(driverName.equals("Боттас"))
            return R.drawable.helmets_bottas;
        if(driverName.equals("Эриксон"))
            return R.drawable.helmets_ericsson;
        if(driverName.equals("Джовинацци"))
            return R.drawable.helmets_giovinazzi;
        if(driverName.equals("Грожан"))
            return R.drawable.helmets_grojean;
        if(driverName.equals("Хэмилтон"))
            return R.drawable.helmets_hamilton;
        if(driverName.equals("Хюлкенберг"))
            return R.drawable.helmets_hulkenberg;
        if(driverName.equals("Квят"))
            return R.drawable.helmets_kvyat;
        if(driverName.equals("Магнуссен"))
            return R.drawable.helmets_magnussen;
        if(driverName.equals("Масса"))
            return R.drawable.helmets_massa;
        if(driverName.equals("Окон"))
            return R.drawable.helmets_ocon;
        if(driverName.equals("Палмер"))
            return R.drawable.helmets_palmer;
        if(driverName.equals("Перес"))
            return R.drawable.helmets_perez;
        if(driverName.equals("Райкконен"))
            return R.drawable.helmets_raikkonen;
        if(driverName.equals("Риккардо"))
            return R.drawable.helmets_ricciardo;
        if(driverName.equals("Сайнс"))
            return R.drawable.helmets_sainz;
        if(driverName.equals("Сироткин"))
            return R.drawable.helmets_sirotkin;
        if(driverName.equals("Стролл"))
            return R.drawable.helmets_stroll;
        if(driverName.equals("Вандорн"))
            return R.drawable.helmets_vandorne;
        if(driverName.equals("Ферстаппен"))
            return R.drawable.helmets_verstappen;
        if(driverName.equals("Феттель"))
            return R.drawable.helmets_vettel;
        if(driverName.equals("Верляйн"))
            return R.drawable.helmets_wehrlein;

        return R.drawable.helmets_default;
    }
}
