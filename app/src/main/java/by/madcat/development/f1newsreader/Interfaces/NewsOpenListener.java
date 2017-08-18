package by.madcat.development.f1newsreader.Interfaces;

import by.madcat.development.f1newsreader.data.DatabaseDescription;

public interface NewsOpenListener {
    void sectionItemOpen(DatabaseDescription.NewsTypes type, int positionID);
}
