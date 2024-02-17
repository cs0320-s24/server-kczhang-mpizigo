package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.cache.CacheData;
import edu.brown.cs.student.main.cache.CachedACSCensusSource;
import edu.brown.cs.student.main.csv.CSVSearch;
import edu.brown.cs.student.main.datasources.ACSCensusSource;
import edu.brown.cs.student.main.datasources.Datasource;
import edu.brown.cs.student.main.datasources.DatasourceException;
import edu.brown.cs.student.main.handlers.BroadbandHandler;
import edu.brown.cs.student.main.handlers.LoadHandler;
import edu.brown.cs.student.main.handlers.SearchHandler;
import edu.brown.cs.student.main.handlers.ViewHandler;
import java.io.IOException;
import java.net.URISyntaxException;
import spark.Spark;

/**
 * The main class of the program. It initializes the local serve through Spark Java, as well as sets
 * its various endpoints. It prints out a link to the localhost for easy access.
 */
public class Server {

  static final int port = 3232;

  /**
   * The main method of our program. Initializes our server and sets its endpoints.
   *
   * @param args
   * @throws URISyntaxException
   * @throws IOException
   * @throws InterruptedException
   * @throws DatasourceException
   */
  public static void main(String[] args)
      throws URISyntaxException, IOException, InterruptedException, DatasourceException {
    Spark.port(port);

    after(
        (request, response) -> {
          response.header("https://api.census.gov/data", "*");
          response.header("Access-Control-Allow-Origin", "*"); // from all origins, not v good idea
          response.header("Access-Control-Allow-Methods", "GET"); // ask for only GET?
        });

    Datasource<CSVSearch> dataSource = new Datasource<>();
    Spark.get("loadcsv", new LoadHandler(dataSource));
    Spark.get("viewcsv", new ViewHandler(dataSource));
    Spark.get("searchcsv", new SearchHandler(dataSource));
    Spark.get(
        "broadband",
        new BroadbandHandler(
            new CachedACSCensusSource(new ACSCensusSource(), new CacheData(100, 5))));
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }
}
