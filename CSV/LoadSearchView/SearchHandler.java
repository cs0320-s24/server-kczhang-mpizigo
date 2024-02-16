package CSV.LoadSearchView;

import CSV.Parser.CSVSearch;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

/**
 * The SearchHandler class is responsible for handling HTTP requests related to searching data within a CSV file.
 * It implements the Spark Route interface to handle incoming requests.
 */
public class SearchHandler implements Route {

    private DataSource<CSVSearch> dataSource; // Data source for CSVSearch objects

    /**
     * Constructs a SearchHandler object with the specified data source.
     *
     * @param dataSource The data source containing CSVSearch objects.
     */
    public SearchHandler(DataSource<CSVSearch> dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Handles HTTP requests to search data within a CSV file.
     *
     * @param request  The HTTP request received by the server.
     * @param response The HTTP response to be sent back to the client.
     * @return A JSON string representing the response to the client.
     * @throws Exception If an error occurs during request handling.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        // Extract column and value parameters from the request
        String col = request.queryParams("col");
        String val = request.queryParams("val");

        // Create a map to hold the response data
        Map<String, Object> responseMap = new HashMap<>();

        // Check if the data source is loaded and CSV data is parsed successfully
        if (dataSource.isLoaded() && dataSource.getData().parsed()) {
            // Perform search based on the specified column and value
            if (col == null || col.isEmpty()) {
                this.dataSource.getData().search(val);
            } else if (col.matches("(0|[1-9]\\d*)")) {
                this.dataSource.getData().search(val, Integer.parseInt(col));
            } else {
                this.dataSource.getData().search(val, col);
            }
            // Add the search result to the response map
            responseMap.put("data", this.dataSource.getData().getLastSearch());
            // Return a success response with the search result
            return new Utilities.SuccessResponse(responseMap);
        } else {
            // Return a failure response if data source is not loaded or CSV data is not parsed
            return new Utilities.FailureResponse("error_datasource");
        }
    }
}
