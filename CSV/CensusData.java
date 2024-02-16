package CSV;

public record CensusData(String state, String county, double percentage) {

  public String state() {
    return this.state;
  }

  public String county() {
    return this.county;
  }

  public double percentage() {
    return this.percentage();
  }

  public String getData() {
    return ("The county " + this.county + " in the state " + this.state + " has a broadband "
        + "internet subscription average of " + Double.toString(this.percentage));
  }

}
