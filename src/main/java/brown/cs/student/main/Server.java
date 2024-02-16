package brown.cs.student.main;

import static spark.Spark.after;

import brown.cs.student.main.cache.CacheData;
import brown.cs.student.main.cache.CachedACSCensusSource;
import brown.cs.student.main.csv.CSVSearch;
import brown.cs.student.main.datasources.ACSCensusSource;
import brown.cs.student.main.datasources.Datasource;
import brown.cs.student.main.datasources.DatasourceException;
import brown.cs.student.main.handlers.BroadbandHandler;
import brown.cs.student.main.handlers.LoadHandler;
import brown.cs.student.main.handlers.SearchHandler;
import brown.cs.student.main.handlers.ViewHandler;
import com.google.common.cache.Cache;
import java.io.IOException;
import java.net.URISyntaxException;

import spark.Spark;

public class Server {

  static final int port = 3232;

  public static void main(String[] args)
      throws URISyntaxException, IOException, InterruptedException, DatasourceException {
    Spark.port(port);

    after((request, response) -> {
      response.header("https://api.census.gov/data", "*");
      response.header("Access-Control-Allow-Origin", "*"); // from all origins, not v good idea
      response.header("Access-Control-Allow-Methods", "GET"); // ask for only GET?
    });

    Datasource<CSVSearch> dataSource = new Datasource<>();
    Spark.get("loadcsv", new LoadHandler(dataSource));
    Spark.get("viewcsv", new ViewHandler(dataSource));
    Spark.get("searchcsv", new SearchHandler(dataSource));
    Spark.get("broadband",
        new BroadbandHandler(new CachedACSCensusSource(new ACSCensusSource(), new CacheData(100, 5))));
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }

}
