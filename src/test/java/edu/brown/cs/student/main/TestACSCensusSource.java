package edu.brown.cs.student.main;

public class TestACSCensusSource {

  //  @Test
  //  public void testCensusRequestBadParams() throws IOException {
  //    /////////// LOAD DATASOURCE ///////////
  //    // Set up the request, make the request
  //    HttpURLConnection loadConnection = tryRequest("?state=banana&county=butteCounty");
  //    // Get an OK response (the *connection* worked, the *API* provides an error response)
  //    assertEquals(200, loadConnection.getResponseCode());
  //
  //    // Get the expected response: a success
  //    Map<String, Object> responseBody =
  //        this.adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
  //    showDetailsIfError(responseBody);
  //    assertEquals("err_bad_input: The state banana could not be found",
  // responseBody.get("result"));
  //
  //    assertEquals(null, responseBody.get("census"));
  //
  //    loadConnection.disconnect();
  //  }
}
