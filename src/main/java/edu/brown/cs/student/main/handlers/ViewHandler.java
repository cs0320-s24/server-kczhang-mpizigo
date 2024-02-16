package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.csv.CSVSearch;
import edu.brown.cs.student.main.datasources.Datasource;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The ViewHandler class is responsible for handling requests to load and search CSV data.
 * It implements the Spark Route interface to handle HTTP requests.
 */
public class ViewHandler implements Route {
  private Datasource<CSVSearch> dataSource; // Data source for CSVSearch objects

  /**
   * Constructs a ViewHandler with the specified data source.
   *
   * @param dataSource The data source for CSVSearch objects.
   */
  public ViewHandler(Datasource<CSVSearch> dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Handles HTTP requests to load and search CSV data.
   *
   * @param request  The HTTP request object.
   * @param response The HTTP response object.
   * @return A JSON string representing the response.
   * @throws Exception If an error occurs during request handling.
   */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    Map<String, Object> responseMap = new HashMap<>(); // Map to hold response data

    // Check if data source is loaded and CSV is parsed successfully
    if (dataSource.isLoaded() && dataSource.getData().parsed()) {
      responseMap.put("data", dataSource.getData().getParsedFile()); // Add parsed CSV data to response map
      return new Utilities.SuccessResponse(responseMap).serialize(); // Serialize success response
    } else {
      return new Utilities.FailureResponse("error_datasource").serialize(); // Serialize failure response
    }
  }
}