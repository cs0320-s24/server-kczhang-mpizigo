package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.CSVSearch;
import edu.brown.cs.student.main.datasources.Datasource;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The LoadHandler class is responsible for handling HTTP requests related to loading CSV data. It
 * implements the Spark Route interface to handle incoming requests.
 */
public class LoadHandler implements Route {

  private Datasource<CSVSearch> dataSource; // Data source for CSVSearch objects
  private Map<String, Object> recentMap; // Map to store recent response data

  /**
   * Constructs a LoadHandler object with the specified data source.
   *
   * @param dataSource The data source containing CSVSearch objects.
   */
  public LoadHandler(Datasource<CSVSearch> dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Handles HTTP requests to load CSV data.
   *
   * @param request The HTTP request received by the server.
   * @param response The HTTP response to be sent back to the client.
   * @return A map containing the response data.
   */
  @Override
  public Object handle(Request request, Response response) {
    // Get the file path and header information from the request
    String filePath = request.queryParams("filepath");
    String header = request.queryParams("header");

    // Defaults to true unless header is set to 'n'
    boolean hasHeader = (header == null || !header.equals("n"));

    // Create a CSVSearch object with the specified file path and header information
    CSVSearch searcher = new CSVSearch(filePath, hasHeader);
    // Set the CSVSearch object as data in the data source
    this.dataSource.setData(searcher);

    // Create a map to hold the response data
    Map<String, Object> responseMap = new HashMap<>();
    // Check if the CSV data was parsed successfully
    if (searcher.parsed()) {
      responseMap.put("result", "success");
    } else {
      responseMap.put("result", "failure");
    }

    // Store the response map as the recent response
    this.recentMap = responseMap;
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    String jsonString = jsonAdapter.toJson(responseMap);
    return jsonString;
  }

  /**
   * Gets the recent response map.
   *
   * @return The map containing the recent response data.
   */
  public Map<String, Object> getResponseMap() {
    return this.recentMap;
  }
}
