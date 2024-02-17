package edu.brown.cs.student.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.datasources.CensusData;
import edu.brown.cs.student.main.datasources.MockedACSCensusSource;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestBroadbandHandler {
  @BeforeAll
  public static void setupOnce() {
    // Pick an arbitrary free port
    Spark.port(0);
    // Eliminate logger spam in console for test suite
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root
  }

  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);
  private JsonAdapter<Map<String, Object>> adapter;

  @BeforeEach
  public void setup() {
    // Re-initialize parser, state, etc. for every test method
    MockedACSCensusSource mockedDatasource =
        new MockedACSCensusSource(new CensusData("California", "Butte County", "88.5"));
    Spark.get("broadband", new BroadbandHandler(mockedDatasource));
    Spark.awaitInitialization(); // don't continue until the server is listening

    Moshi moshi = new Moshi.Builder().build();
    this.adapter = moshi.adapter(this.mapStringObject);
    Spark.init();
  }

  @AfterEach
  public void tearDown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("broadband");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * <p>The "throws" clause doesn't matter below -- JUnit will fail if an exception is thrown that
   * hasn't been declared as a parameter to @Test.
   *
   * @param apiCall the call string, including endpoint (Note: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send a request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/broadband" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    // The request body contains a Json object
    clientConnection.setRequestProperty("Content-Type", "application/json");
    // We're expecting a Json object in the response body
    clientConnection.setRequestProperty("Accept", "application/json");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testCensusRequestSuccess() throws IOException {
    /////////// LOAD DATASOURCE ///////////
    // Set up the request, make the request
    HttpURLConnection loadConnection = tryRequest("?state=California&county=butteCounty");
    // Get an OK response
    assertEquals(200, loadConnection.getResponseCode());

    // Get the expected response: a success
    Map<String, Object> responseBody =
        adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    showDetailsIfError(responseBody);
    assertEquals("success", responseBody.get("result"));

    CensusData data = new CensusData("California", "Butte County", "88.5");
    assertEquals(data.getData(), responseBody.get("census"));

    loadConnection.disconnect();
  }

  @Test
  public void testCensusRequestNoParams() throws IOException {
    /////////// LOAD DATASOURCE ///////////
    // Set up the request, make the request
    HttpURLConnection loadConnection = tryRequest("");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());

    // Get the expected response: a success
    Map<String, Object> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    showDetailsIfError(responseBody);
    assertEquals("err_bad_input: no state given", responseBody.get("result"));

    assertEquals(null, responseBody.get("census"));

    loadConnection.disconnect();
  }

  @Test
  public void testCensusRequestNoState() throws IOException {
    /////////// LOAD DATASOURCE ///////////
    // Set up the request, make the request
    HttpURLConnection loadConnection = tryRequest("?county=buttecounty");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());

    // Get the expected response: a success
    Map<String, Object> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    showDetailsIfError(responseBody);
    assertEquals("err_bad_input: no state given", responseBody.get("result"));

    assertEquals(null, responseBody.get("census"));

    loadConnection.disconnect();
  }

  @Test
  public void testCensusRequestNoCounty() throws IOException {
    /////////// LOAD DATASOURCE ///////////
    // Set up the request, make the request
    HttpURLConnection loadConnection = tryRequest("?state=california");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());

    // Get the expected response: a success
    Map<String, Object> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    showDetailsIfError(responseBody);
    assertEquals("err_bad_input: no county given", responseBody.get("result"));

    assertEquals(null, responseBody.get("census"));

    loadConnection.disconnect();
  }

  @Test
  public void testCensusRequestCapitalization() throws IOException {
    /////////// LOAD DATASOURCE ///////////
    // Set up the request, make the request
    HttpURLConnection loadConnection = tryRequest("?state=California&county=butteCounty");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());

    // Get the expected response: a success
    Map<String, Object> responseBody =
        this.adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    showDetailsIfError(responseBody);
    assertEquals("success", responseBody.get("result"));

    // Mocked data: correct temp? We know what it is, because we mocked.
    CensusData data = new CensusData("California", "Butte County", "88.5");
    assertEquals(data.getData(), responseBody.get("census"));

    loadConnection.disconnect();
  }

  /**
   * Helper to make working with a large test suite easier: if an error, print more info.
   *
   * @param body
   */
  private void showDetailsIfError(Map<String, Object> body) {
    if (body.containsKey("type") && "error".equals(body.get("type"))) {
      System.out.println(body.toString());
    }
  }
}
