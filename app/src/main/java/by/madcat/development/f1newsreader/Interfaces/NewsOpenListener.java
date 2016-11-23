package by.madcat.development.f1newsreader.Interfaces;

import java.util.ArrayList;

import by.madcat.development.f1newsreader.data.DatabaseDescription;

public interface NewsOpenListener {
    void sectionItemOpen(DatabaseDescription.NewsTypes type, int positionID);
    void setSectionItemsCount(int count);
    void setSectionNewsLinks(ArrayList<String> links);
}
