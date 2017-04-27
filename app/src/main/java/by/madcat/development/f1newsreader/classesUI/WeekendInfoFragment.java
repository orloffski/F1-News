package by.madcat.development.f1newsreader.classesUI;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Map;

import by.madcat.development.f1newsreader.R;
import by.madcat.development.f1newsreader.Utils.SystemUtils;
import by.madcat.development.f1newsreader.Utils.ViewCreator;

import static android.graphics.Color.GRAY;

public class WeekendInfoFragment extends Fragment {

    private TableLayout weekendTable;

    public WeekendInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_weekend_info, container, false);

        weekendTable = (TableLayout) view.findViewById(R.id.weekendTable);
        loadWeekendTableData(weekendTable, getContext());

        return view;
    }

    private static void loadWeekendTableData(TableLayout weekendTable, Context context){
        String weekendTitle = SystemUtils.getWeekendTitle(context);
        Map<String, String> weekendData = SystemUtils.getWeekendData(context);

        TableRow titleRow = ViewCreator.createTableRowView(context, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        titleRow.addView(ViewCreator.createTextView(context, weekendTitle.toUpperCase(), 24, null, Typeface.BOLD, GRAY));

        weekendTable.addView(titleRow);

        for(Map.Entry entry : weekendData.entrySet()){
            TableRow date = ViewCreator.createTableRowView(context, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            date.addView(ViewCreator.createTextView(context, entry.getKey().toString(), 20, null, Typeface.BOLD, GRAY));

            TableRow event = ViewCreator.createTableRowView(context, new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            event.addView(ViewCreator.createTextView(context, entry.getValue().toString(), 20, null, Typeface.NORMAL, GRAY));

            weekendTable.addView(date);
            weekendTable.addView(event);
        }
    }
}
