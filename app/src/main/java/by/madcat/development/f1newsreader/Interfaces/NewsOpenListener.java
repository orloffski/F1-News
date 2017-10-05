package by.madcat.development.f1newsreader.interfaces;

import by.madcat.development.f1newsreader.dataSQLite.DatabaseDescription;

public interface NewsOpenListener {
    void sectionItemOpen(DatabaseDescription.NewsTypes type, int positionID);
}
