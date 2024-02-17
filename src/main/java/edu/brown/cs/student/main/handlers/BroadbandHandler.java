package edu.brown.cs.student.main.handlers;

import com.google.common.util.concurrent.UncheckedExecutionException;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.datasources.CensusData;
import edu.brown.cs.student.main.datasources.CensusDatasource;
import edu.brown.cs.student.main.datasources.DatasourceException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The BroadbandHandler class is responsible for handling data from API requests sent to the Census
 * API. It implements the Spark Route interface to handle incoming requests.
 */
public class BroadbandHandler implements Route {

  private CensusDatasource dataSource;

  /**
   * Constructs an instance of BroadbandHandler with the specified datasource.
   *
   * @param dataSource ACS datasource that sends API requests
   */
  public BroadbandHandler(CensusDatasource dataSource) {
    this.dataSource = dataSource;
  }

  /**
   * Handles HTTP requests to the ACS API datasource
   *
   * @param request the HTTP request received by the server
   * @param response the Json response sent by the server
   * @return A Json map containing the requested data
   */
  @Override
  public Object handle(Request request, Response response) {
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    Map<String, Object> responseMap = new HashMap<>();

    String stateName = request.queryParams("state");
    String countyName = request.queryParams("county");

    Date date = Calendar.getInstance().getTime();
    DateFormat format = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");

    try {
      // Checks for null inputs (bad inputs are taken care of in ACSCensusSource
      if (stateName == null) {
        throw new DatasourceException("err_bad_input: no state given");
      }

      if (countyName == null) {
        throw new DatasourceException("err_bad_input: no county given");
      }

      // Gets a CensusData record for the state and county inputs
      CensusData censusData = this.dataSource.getCensusData(stateName, countyName);

      responseMap.put("result", "success");
      responseMap.put("date", format.format(date));
      responseMap.put("givenState", censusData.state());
      responseMap.put("givenCounty", censusData.county());
      responseMap.put("census", censusData.getData());

    } // UncheckedExecutionException's message is a DatasourceException error message
    catch (DatasourceException | UncheckedExecutionException e) {
      e.printStackTrace();
      responseMap.put("result", e.getMessage());
      responseMap.put("date", format.format(date));
      responseMap.put("givenState", stateName);
      responseMap.put("givenCounty", countyName);

    } // Left here to not crash the server in case of edge cases we haven't dealt with
    catch (Exception e) {
      e.printStackTrace();
      responseMap.put("result", "Exception");
    }
    return adapter.toJson(responseMap);
  }
}
