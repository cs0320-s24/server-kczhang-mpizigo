package brown.cs.student.main.handlers;

import brown.cs.student.main.datasources.CensusData;
import brown.cs.student.main.datasources.CensusDatasource;
import brown.cs.student.main.datasources.DatasourceException;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.io.UncheckedIOException;
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
      //Checks for null inputs (bad inputs are taken care of in ACSCensusSource
      if (stateName == null) {
        throw new DatasourceException("err_bad_input: no state given");
      }

      if (countyName == null) {
        throw new DatasourceException("err_bad_input: no county given");
      }

      //Gets a CensusData record for the state and county inputs
      CensusData censusData = this.dataSource.getCensusData(stateName, countyName);
      responseMap.put("result", "success");
      responseMap.put("date", format.format(date));
      responseMap.put("givenState", stateName);
      responseMap.put("givenCounty", countyName);
      responseMap.put("census", censusData.getData());

    } // UncheckedExecutionException's message is a DatasourceException error message
    catch (DatasourceException | UncheckedExecutionException e) {
      e.printStackTrace();
      responseMap.put("result", e.getMessage());

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
