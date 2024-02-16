package edu.brown.cs.student.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.csv.CSVSearch;
import edu.brown.cs.student.main.datasources.Datasource;
import edu.brown.cs.student.main.handlers.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

import okio.Buffer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestLoad {

    private LoadHandler load_handler;
    @BeforeAll
    public static void setup_before_everything() {
        Spark.port(0);
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

    @BeforeEach
    public void setup() {

        Datasource<CSVSearch> dataSource = new Datasource<>();
        this.load_handler = new LoadHandler(dataSource);

        Spark.get("loadcsv", this.load_handler);
        Spark.init();
        Spark.awaitInitialization(); // don't continue until the server is listening
    }

    @AfterEach
    public void tearDown() {
        Spark.unmap("loadcsv");
        Spark.awaitStop();
    }

    private static HttpURLConnection tryRequest(String apiCall) throws IOException {
        // Configure the connection (but don't actually send the request yet)
        URL requestURL;
        if (!apiCall.equals("")) {
            requestURL = new URL("http://localhost:" + Spark.port() + "/loadcsv?" + apiCall);
        } else {
            requestURL = new URL("http://localhost:" + Spark.port() + "/loadcsv?");
        }

        System.out.println("request url is " + requestURL);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

        // The default method is "GET", which is what we're using here.
        // If we were using "POST", we'd need to say so.
        clientConnection.setRequestMethod("GET");

        clientConnection.connect();
        return clientConnection;
    }

    @Test
    public void dummy() {
        assertEquals(1, 1);
    }

    @Test
    public void testBasicLoad() throws IOException {
        HttpURLConnection clientConnection = tryRequest("filepath=data/census/income_by_race.csv");
        // Get an OK response (the *connection* worked, the *API* provides an error response)
        assertEquals(200, clientConnection.getResponseCode());

        Moshi moshi = new Moshi.Builder().build();
        Utilities.SuccessResponse response = moshi.adapter(Utilities.SuccessResponse.class)
                .fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
        assert response != null;
        assertEquals("Success",response.getResponseMap().get("result"));


        System.out.println(response);
        // ^ If that succeeds, we got the expected response. Notice that this is *NOT* an exception, but
        // a real Json reply.

        clientConnection.disconnect();
    }
}
