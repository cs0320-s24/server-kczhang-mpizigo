package CSV;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
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
            responseMap.put("data", dataSource.getData().getParsedFile());
            return new CSVUtilities.SuccessResponse(responseMap).serialize();
        } else {
            return new CSVUtilities.FailureResponse("data not found");
        }

    }
}