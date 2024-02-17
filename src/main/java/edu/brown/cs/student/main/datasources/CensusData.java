package edu.brown.cs.student.main.datasources;

/**
 * The CensusData record is responsible for recording data from the Census API call in an accessible
 * and more secure format.
 *
 * @param state the state name the data relates to
 * @param county the county name the data relates to
 * @param percentage the percentage of homes in the county, state that have braodband internet
 *     access
 */
public record CensusData(String state, String county, String percentage) {

  /**
   * Gets the state name.
   *
   * @return the name of the state
   */
  public String state() {
    return this.state;
  }

  /**
   * Gets the county name.
   *
   * @return the name of the county
   */
  public String county() {
    return this.county;
  }

  /**
   * Getter method for the broadband percentage. Named getPercentage instead of percentage to avoid
   * confusion with java percentage() math method.
   *
   * @return
   */
  public String getPercentage() {
    return this.percentage();
  }

  /**
   * Concatenates and presents the data of the record into a readable sentence.
   *
   * @return a String concatenating the data
   */
  public String getData() {
    return ("The county "
        + this.county.replaceAll("\\s", "").toLowerCase()
        + " in the state "
        + this.state.replaceAll("\\s", "").toLowerCase()
        + " has a broadband internet subscription average of "
        + this.percentage);
  }
}
