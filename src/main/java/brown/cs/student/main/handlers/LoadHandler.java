package brown.cs.student.main.handlers;

import brown.cs.student.main.csv.CSVSearch;
import brown.cs.student.main.datasources.Datasource;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class LoadHandler implements Route {

    private Datasource<CSVSearch> dataSource;

    public LoadHandler(Datasource<CSVSearch> dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public Object handle(Request request, Response response) {

        // Set<String> params = request.queryParams();
        String filePath = request.queryParams("file");
        // maybe also get if there is header??

        CSVSearch searcher = new CSVSearch(filePath, true);
        this.dataSource.setData(searcher);

        Map<String, Object> responseMap = new HashMap<>();
        if (searcher.parsed()) {
            responseMap.put("result", "success");
        } else {
            responseMap.put("result", "failure");
        }

        return responseMap;
    }
}