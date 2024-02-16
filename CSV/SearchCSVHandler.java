package CSV;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class SearchCSVHandler implements Route {
    private CSVDataSource<CSVSearch> dataSource;

    public SearchCSVHandler(CSVDataSource<CSVSearch> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        String col = request.queryParams("col");
        //make sure col is actually empty if not specified
        String val = request.queryParams("val");

        if (col.isEmpty()) {
            this.dataSource.getData().search(val);
        } else if (col.matches("(0|[1-9]\\d*)")) {
            this.dataSource.getData().search(val, Integer.parseInt(col));
        } else {
            this.dataSource.getData().search(val, col);
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", "success");

        return responseMap;
    }
}