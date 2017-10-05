package by.madcat.development.f1newsreader.Interfaces;

import by.madcat.development.f1newsreader.DataSQLite.DatabaseDescription;

public interface NewsOpenListener {
    void sectionItemOpen(DatabaseDescription.NewsTypes type, int positionID);
}
