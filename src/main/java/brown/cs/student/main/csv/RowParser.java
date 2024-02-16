package brown.cs.student.main.csv;



import java.util.List;

public class RowParser implements CreatorFromRow<List<String>> {

    public RowParser() {}

    @Override
    public List<String> create(List<String> row) throws FactoryFailureException {
        return row;
    }
}
