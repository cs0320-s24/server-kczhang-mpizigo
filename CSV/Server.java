package CSV;

import static spark.Spark.after;

import spark.Spark;

public class Server {

  static final int port = 3232;

  public static void main(String[] args) {
    Spark.port(port);

    after((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*"); // from all origins, not v good idea
      response.header("Access-Control-Allow-Methods", "GET"); // ask for only GET?
    });

    Spark.get("broadband", new BroadbandHandler());
    Spark.init();
    Spark.awaitInitialization();

    System.out.println("Server started at http://localhost:" + port);
  }

}
