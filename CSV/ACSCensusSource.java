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

public class ACSCensusSource implements CensusDatasource{

  private HashMap<String, String> stateToCode;
  private HashMap<String, String> countyToCode;

  public ACSCensusSource() throws URISyntaxException, IOException, InterruptedException {
    this.stateToCode = new HashMap<>();
    this.countyToCode = new HashMap<>();
    this.setUpCodes();
  }
  @Override
  public CensusData getCensusData(String stateName, String countyName)
      throws DatasourceException, IllegalArgumentException {
    return null;
  }

  private void setUpCodes()
      throws URISyntaxException, IOException, InterruptedException {
    Moshi moshi = new Moshi.Builder().build();
    Type listOfListOfStringType = Types.newParameterizedType(List.class, String.class);
    JsonAdapter<List<List<String>>> adapter = moshi.adapter(listOfListOfStringType);

    // handle state codes
    HttpRequest buildStateCodeRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*")
            )
            .GET()
            .build();
    HttpResponse<String> sentStateCodeResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildStateCodeRequest, HttpResponse.BodyHandlers.ofString());

    List<List<String>> stateToCodeList = adapter.fromJson(sentStateCodeResponse.body());

    if (!stateToCodeList.isEmpty()) {
      for (int i = 1; i < stateToCodeList.size(); i++) { // not a for-each loop to skip the headers
        this.stateToCode.put(stateToCodeList.get(i).get(0).trim(),
            stateToCodeList.get(i).get(1).trim());
      }
    } else {
      System.out.println("problem getting the state codes");
    }

    // handle county codes
    HttpRequest buildCountyCodeRequest =
        HttpRequest.newBuilder()
            .uri(new URI("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*")
            )
            .GET()
            .build();
    HttpResponse<String> sentCountyCodeResponse =
        HttpClient.newBuilder()
            .build()
            .send(buildCountyCodeRequest, HttpResponse.BodyHandlers.ofString());

    List<List<String>> countyToCodeList = adapter.fromJson(sentCountyCodeResponse.body());

    if (!countyToCodeList.isEmpty()) {
      for (int i = 1; i < countyToCodeList.size(); i++) {
        String[] stringArray = countyToCodeList.get(i).get(0).split(",");
        this.countyToCode.put(stringArray[0].trim(),
            countyToCodeList.get(i).get(3).trim());
      }
    } else {
      System.out.println("problem getting the county codes");
    }
  }
}
