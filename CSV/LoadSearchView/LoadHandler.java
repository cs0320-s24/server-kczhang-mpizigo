package CSV.LoadSearchView;

import CSV.Parser.CSVSearch;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class LoadHandler implements Route {

    private DataSource<CSVSearch> dataSource;
    private Map<String, Object> recentMap;

    public LoadHandler(DataSource<CSVSearch> dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public Object handle(Request request, Response response) {

        // Set<String> params = request.queryParams();
        String filePath = request.queryParams("filepath");
        String header = request.queryParams("header");

        // defaults to true unless header set to n
        boolean hasHeader = (header == null || !header.equals("n"));

        System.out.println(hasHeader);

        CSVSearch searcher = new CSVSearch(filePath, hasHeader);
        this.dataSource.setData(searcher);

        Map<String, Object> responseMap = new HashMap<>();
        if (searcher.parsed()) {
            responseMap.put("result", "success");
        } else {
            responseMap.put("result", "failure");
        }

        this.recentMap = responseMap;
        return responseMap;
    }

    public Map<String, Object> getResponseMap() {
        return this.recentMap;
    }
}