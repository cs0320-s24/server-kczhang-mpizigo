package edu.brown.cs.student.main.load_view_search;

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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class TestLoad {
    private JsonAdapter<Map<String, Object>> adapter;

    public TestLoad() {
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
    public void testLoad() throws IOException {
        HttpURLConnection clientConnection =
            tryRequest("loadcsv?filepath=data/census/income_by_race.csv&header=y");
        assertEquals(200, clientConnection.getResponseCode());
        Map<String, Object> response =
            adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        assertEquals("success", response.get("result"));
    }

    @Test
    public void testView() throws IOException {
        HttpURLConnection clientConnection =
            tryRequest("loadcsv?filepath=data/census/income_by_race.csv&header=y");
        assertEquals(200, clientConnection.getResponseCode());
        Map<String, Object> response =
            adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        assertEquals("success", response.get("result"));
    }

    @Test
    public void testInvalid() throws IOException {
        HttpURLConnection clientConnection =
            tryRequest("loadcsv?filepath=data/census/RI_income.csv&header=y");
        assertEquals(200, clientConnection.getResponseCode());
        Map<String, Object> response =
            adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        assertEquals("failure", response.get("result"));
    }

    @Test
    public void testNoHeader() throws IOException {
        HttpURLConnection clientConnection =
            tryRequest("loadcsv?filepath=data/census/income_by_race.csv&header=n");
        assertEquals(200, clientConnection.getResponseCode());
        Map<String, Object> response =
            adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        assertEquals("success", response.get("result"));
    }
}
