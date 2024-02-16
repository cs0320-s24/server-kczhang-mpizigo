package CSV;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class BroadbandHandler implements Route {

  private CensusDatasource dataSource;
  public BroadbandHandler(CensusDatasource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * API key for ACS: dc5555afd3b59add8f7f8022727e88505d7971b0
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();

    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");

    System.out.println("state param " + stateName);
    System.out.println("county param " + countyName);

    try {
      //Gets a CensusData record for the state and county inputs
      CensusData censusData = this.dataSource.getCensusData(stateName, countyName);
      responseMap.put("result", "success");
      responseMap.put("date", "success");
      responseMap.put("givenState", stateName);
      responseMap.put("givenCounty", countyName);
      responseMap.put("census", censusData.getData());
      return responseMap;
    } catch (Exception e) {
      e.printStackTrace();
      // This is a relatively unhelpful exception message. An important part of this sprint will be
      // in learning to debug correctly by creating your own informative error messages where Spark
      // falls short.
      responseMap.put("result", "Exception");
    }
    return responseMap;
  }
}
