package edu.brown.cs.student.main.Parser;


import edu.brown.cs.student.main.CreatorFromRow;

import java.util.List;

public class RowParser implements CreatorFromRow<List<String>> {

    public RowParser() {}

    @Override
    public List<String> create(List<String> row) throws FactoryFailureException {
        return row;
    }
}
