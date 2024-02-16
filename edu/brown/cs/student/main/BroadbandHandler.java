package edu.brown.cs.student.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

    Date date = Calendar.getInstance().getTime();
    DateFormat format = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");

    try {
      //Gets a CensusData record for the state and county inputs
      CensusData censusData = this.dataSource.getCensusData(stateName, countyName);
      responseMap.put("result", "success");
      responseMap.put("date", format.format(date));
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
