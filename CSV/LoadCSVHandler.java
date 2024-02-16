package CSV;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class LoadCSVHandler implements Route {

    private CSVDataSource<CSVSearch> dataSource;

    public LoadCSVHandler(CSVDataSource<CSVSearch> dataSource) {
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