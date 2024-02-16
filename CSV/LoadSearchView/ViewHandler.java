package CSV.LoadSearchView;

import CSV.Parser.CSVSearch;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class ViewHandler implements Route {
    private DataSource<CSVSearch> dataSource;

    public ViewHandler(DataSource<CSVSearch> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        Map<String, Object> responseMap = new HashMap<>();
        if (dataSource.isLoaded() && dataSource.getData().parsed()) {
            responseMap.put("data", dataSource.getData().getParsedFile());
            return new Utilities.SuccessResponse(responseMap).serialize();
        } else {
            return new Utilities.FailureResponse("error_datasource");
        }

    }
}