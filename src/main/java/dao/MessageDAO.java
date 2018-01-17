package dao;

import Main.WarningMessage_1C;

public interface MessageDAO {

    //add
    void add(WarningMessage_1C warningMessage1C);

    //delete first
    void deleteFirst(WarningMessage_1C warningMessage1C);

    //get first
    WarningMessage_1C getFirst(String tableName);

}
