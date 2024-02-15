package CSV;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class ViewCSVHandler implements Route {
    private CSVDataSource<CSVSearch> dataSource;

    public ViewCSVHandler(CSVDataSource<CSVSearch> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        Map<String, Object> responseMap = new HashMap<>();
        if (dataSource.isLoaded() && dataSource.getData().parsed()) {
            responseMap.put("result", "success");
            responseMap.put("data", dataSource.getData().getLastSearch());
        } else {
            responseMap.put("result", "failure");
        }

        return responseMap;
    }
}