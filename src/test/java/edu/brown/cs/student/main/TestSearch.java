package edu.brown.cs.student.main;

import static org.testng.AssertJUnit.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.datasources.Datasource;
import edu.brown.cs.student.main.handlers.LoadHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.handlers.ViewHandler;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestSearch {
  private JsonAdapter<Map<String, Object>> adapter;

  public TestSearch() {
    Moshi moshi = new Moshi.Builder().build();
    Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
    adapter = moshi.adapter(type);
  }

  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  @BeforeEach
  public void setup() {
    Datasource accessCSV = new Datasource();
    Spark.get("loadcsv", new LoadHandler(accessCSV));
    Spark.get("searchcsv", new SearchHandler(accessCSV));
    Spark.get("viewcsv", new ViewHandler(accessCSV));
    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints after each test
    Spark.unmap("loadcsv");
    Spark.unmap("searchcsv");
    Spark.unmap("viewcsv");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  private static HttpURLConnection tryRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

    clientConnection.setRequestMethod("GET");

    clientConnection.connect();
    return clientConnection;
  }

  @Test
  public void testSearchVal() throws IOException {
    HttpURLConnection clientConnection1 =
        tryRequest("loadcsv?filepath=data/census/income_by_race.csv&header=y");
    assertEquals(200, clientConnection1.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?val=10626");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals(Arrays.asList(Arrays.asList("4", "Asian", "2013", "2013", "69161", "10626", "\"Kent County, RI\"", "05000US44003", "kent-county-ri")), response.get("data"));
  }

  @Test
  public void testSearchColIndex() throws IOException {
    HttpURLConnection clientConnection1 =
        tryRequest("loadcsv?filepath=data/census/income_by_race.csv&header=y");
    assertEquals(200, clientConnection1.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?val=10626&col=5");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals(Arrays.asList(Arrays.asList("4", "Asian", "2013", "2013", "69161", "10626", "\"Kent County, RI\"", "05000US44003", "kent-county-ri")), response.get("data"));
  }

  @Test
  public void testSearchColNumber() throws IOException {
    HttpURLConnection clientConnection1 =
        tryRequest("loadcsv?filepath=data/census/income_by_race.csv&header=y");
    assertEquals(200, clientConnection1.getResponseCode());
    HttpURLConnection clientConnection = tryRequest("searchcsv?val=10626&col=Household%20Income%20by%20Race%20Moe");
    assertEquals(200, clientConnection.getResponseCode());
    Map<String, Object> response =
        adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals(Arrays.asList(Arrays.asList("4", "Asian", "2013", "2013", "69161", "10626", "\"Kent County, RI\"", "05000US44003", "kent-county-ri")), response.get("data"));
  }
}
