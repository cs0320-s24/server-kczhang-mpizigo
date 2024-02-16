package brown.cs.student.main.handlers;

import brown.cs.student.main.csv.CSVSearch;
import brown.cs.student.main.datasources.Datasource;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class ViewHandler implements Route {
    private Datasource<CSVSearch> dataSource;

    public ViewHandler(Datasource<CSVSearch> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        Map<String, Object> responseMap = new HashMap<>();
        if (dataSource.isLoaded() && dataSource.getData().parsed()) {
            responseMap.put("data", dataSource.getData().getParsedFile());
            return new Utilities.SuccessResponse(responseMap).serialize();
        } else {
            return new Utilities.FailureResponse("data not found");
        }

    }
}