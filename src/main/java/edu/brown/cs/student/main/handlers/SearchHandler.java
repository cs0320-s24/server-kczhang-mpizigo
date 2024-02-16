package edu.brown.cs.student.main.handlers;

import edu.brown.cs.student.main.csv.CSVSearch;
import edu.brown.cs.student.main.datasources.Datasource;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class SearchHandler implements Route {
  private Datasource<CSVSearch> dataSource;

  public SearchHandler(Datasource<CSVSearch> dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Object handle(Request request, Response response) throws Exception {

    // do params instead of query
    String col = request.queryParams("col");
    // make sure col is actually empty if not specified
    String val = request.queryParams("val");

    Map<String, Object> responseMap = new HashMap<>();
    if (dataSource.isLoaded() && dataSource.getData().parsed()) {
      if (col == null || col.isEmpty()) {
        this.dataSource.getData().search(val);
      } else if (col.matches("(0|[1-9]\\d*)")) {
        this.dataSource.getData().search(val, Integer.parseInt(col));
      } else {
        this.dataSource.getData().search(val, col);
      }
      responseMap.put("data", this.dataSource.getData().getLastSearch());
      return new Utilities.SuccessResponse(responseMap);
    } else {
      return new Utilities.FailureResponse("data not found");
    }
  }
}
