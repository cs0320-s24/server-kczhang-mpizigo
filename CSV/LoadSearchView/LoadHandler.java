package CSV.LoadSearchView;

import CSV.Parser.CSVSearch;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class LoadHandler implements Route {

    private DataSource<CSVSearch> dataSource;

    public LoadHandler(DataSource<CSVSearch> dataSource) {
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