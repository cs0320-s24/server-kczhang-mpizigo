package CSV;

import static spark.Spark.after;

import java.io.IOException;
import java.net.URISyntaxException;

import CSV.LoadSearchView.DataSource;
import CSV.LoadSearchView.LoadHandler;
import CSV.LoadSearchView.SearchHandler;
import CSV.LoadSearchView.ViewHandler;
import CSV.Parser.CSVSearch;
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

    DataSource<CSVSearch> dataSource = new DataSource<>();
    Spark.get("loadcsv", new LoadHandler(dataSource));
    Spark.get("viewcsv", new ViewHandler(dataSource));
    Spark.get("searchcsv", new SearchHandler(dataSource));
    Spark.get("broadband", new BroadbandHandler(new ACSCensusSource()));
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }

}