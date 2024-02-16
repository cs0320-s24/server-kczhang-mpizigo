package brown.cs.student.main.datasources;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import okio.Buffer;

public class ACSCensusSource implements CensusDatasource{

  private HashMap<String, String> stateToCode;
  private HashMap<String, String> countyToCode;

  public ACSCensusSource()
      throws URISyntaxException, IOException, InterruptedException, DatasourceException {
    this.stateToCode = new HashMap<>();
    this.countyToCode = new HashMap<>();
    this.setUpStateCode();
  }
  @Override
  public CensusData getCensusData(String stateName, String countyName) throws DatasourceException {
    try {
      // build and send request to Census API
      String stateCode = this.stateToCode.get(stateName.replaceAll("\\s", "").toLowerCase());

      if (stateCode == null) { //if stateName cannot be found inside
        throw new DatasourceException("error_bad_request: the state " + stateName + " cannot be found");
      }

      this.setUpCountyCode(stateCode);
      String countyCode = this.countyToCode.get(countyName.replaceAll("\\s", "").toLowerCase());

      if (countyCode == null) { //if stateName cannot be found inside
        throw new DatasourceException("error_bad_request: the county " + countyName + " cannot be found");
      }

      // request the census data for the specific state and county
      URL requestURL = new URL("https", "api.census.gov",
          "/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&for=county:"
              + countyCode + "&in=state:" + stateCode + "&key=dc5555afd3b59add8f7f8022727e88505d7971b0");
      HttpURLConnection clientConnection = connect(requestURL);

      Moshi moshi = new Moshi.Builder().build();
      Type listOfListOfStringType = Types.newParameterizedType(List.class, List.class, String.class);
      JsonAdapter<List<List<String>>> adapter = moshi.adapter(listOfListOfStringType);

      List<List<String>> listBody = adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      clientConnection.disconnect();

      if (!listBody.isEmpty() && !listBody.get(1).isEmpty()) {
        return new CensusData(stateName, countyName, Double.parseDouble(listBody.get(1).get(1)));
      } else {
        throw new DatasourceException("error_datasource: no data could be found");
      }
    } catch (MalformedURLException e) {
      throw new DatasourceException("error_bad_json: url could not be formed");
    } catch (IOException e) {
      throw new DatasourceException(e.getMessage(), e);
    }
  }

  private void setUpStateCode() throws IOException, DatasourceException {
    Moshi moshi = new Moshi.Builder().build();
    Type listOfListOfStringType = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listOfListOfStringType);

    try {
      // request ACS for state codes
      URL stateRequestURL = new URL("https", "api.census.gov",
          "/data/2010/dec/sf1?get=NAME&for=state:*&key=dc5555afd3b59add8f7f8022727e88505d7971b0");
      HttpURLConnection clientConnection = connect(stateRequestURL);

      List<List<String>> stateToCodeList = adapter.fromJson(
          new Buffer().readFrom(clientConnection.getInputStream()));
      clientConnection.disconnect();

      // populate map from state name to state code
      if (!stateToCodeList.isEmpty()) {
        for (int i = 1; i < stateToCodeList.size();
            i++) { // not a for-each loop to skip the headers
          this.stateToCode.put(stateToCodeList.get(i).get(0).replaceAll("\\s", "").toLowerCase(),
              stateToCodeList.get(i).get(1).trim());
        }
      }

    } catch (MalformedURLException | DatasourceException e) {
      throw new DatasourceException(e.getMessage(), e);
    }
  }

  private void setUpCountyCode(String stateCode) throws MalformedURLException {
    Moshi moshi = new Moshi.Builder().build();
    Type listOfListOfStringType = Types.newParameterizedType(List.class, List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listOfListOfStringType);

    try {
      // request ACS for county codes
      URL countyRequestURL = new URL("https", "api.census.gov",
          "/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode
              + "&key=dc5555afd3b59add8f7f8022727e88505d7971b0");
      HttpURLConnection newClientConnection = connect(countyRequestURL);

      List<List<String>> countyToCodeList = adapter.fromJson(
          new Buffer().readFrom(newClientConnection.getInputStream()));
      newClientConnection.disconnect();

      // populate map from county name to county code
      if (!countyToCodeList.isEmpty()) {
        for (int i = 1; i < countyToCodeList.size(); i++) {
          String[] stringArray = countyToCodeList.get(i).get(0).split(",");
          this.countyToCode.put(stringArray[0].replaceAll("\\s", "").toLowerCase(),
              countyToCodeList.get(i).get(2).trim());
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (DatasourceException e) {
      throw new RuntimeException(e);
    }
  }

    private static HttpURLConnection connect(URL requestURL) throws DatasourceException, IOException {
    URLConnection urlConnection = requestURL.openConnection();
    if(! (urlConnection instanceof HttpURLConnection))
      throw new DatasourceException("unexpected: result of connection wasn't HTTP");
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect(); // GET
    if(clientConnection.getResponseCode() != 200)
      throw new DatasourceException("unexpected: API connection not success status "+clientConnection.getResponseMessage());
    return clientConnection;
  }
}
