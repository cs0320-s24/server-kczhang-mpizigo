package edu.brown.cs.student.main.csv;

import edu.brown.cs.student.main.csv.FactoryFailureException;
import java.util.List;

public class RowParserNew implements CreatorFromRow<String[]> {

    public RowParserNew() {}

    @Override
    public String[] create(List<String> row) throws FactoryFailureException {
        return row.toArray(new String[0]);
    }
}
