package edu.brown.cs.student.main.datasources;

public record CensusData(String state, String county, String percentage) {

  public String state() {
    return this.state;
  }

  public String county() {
    return this.county;
  }

  /**
   * Getter method for the broadband percentage. Named getPercentage instead of percentage to avoid
   * confusion with percentage() math method.
   *
   * @return
   */
  public String getPercentage() {
    return this.percentage();
  }

  public String getData() {
    return ("The county "
        + this.county.replaceAll("\\s", "").toLowerCase()
        + " in the state "
        + this.state.replaceAll("\\s", "").toLowerCase()
        + " has a broadband internet subscription average of "
        + this.percentage);
  }
}
