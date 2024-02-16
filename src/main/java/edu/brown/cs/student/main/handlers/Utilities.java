package edu.brown.cs.student.main.handlers;

import com.squareup.moshi.Moshi;
import java.util.Map;

/**
 * The Utilities class provides utility methods for handling responses in the CSV loading and search view.
 */
public class Utilities {

  /**
   * Represents a successful response with a response type and a map of response data.
   */
  public record SuccessResponse(String response_type, Map<String, Object> responseMap) {

    /**
     * Constructs a SuccessResponse with the provided response map and sets the response type to "success".
     *
     * @param responseMap The map containing response data.
     */
    public SuccessResponse(Map<String, Object> responseMap) {
      this("success", responseMap);
    }

    /**
     * Serializes the SuccessResponse object to JSON format.
     *
     * @return A JSON string representing the serialized SuccessResponse object.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(SuccessResponse.class).toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }

    /**
     * Retrieves the response map containing response data.
     *
     * @return The map containing response data.
     */
    public Map<String, Object> getResponseMap() {
      return this.responseMap;
    }
  }

  /**
   * Represents a failure response with a response type and an error message.
   */
  public record FailureResponse(String response_type, String error_msg) {

    /**
     * Constructs a FailureResponse with the provided error message and sets the response type to "Exception".
     *
     * @param error_msg The error message describing the failure.
     */
    public FailureResponse(String error_msg) {
      this("Exception", error_msg);
    }

    /**
     * Serializes the FailureResponse object to JSON format.
     *
     * @return A JSON string representing the serialized FailureResponse object.
     */
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(FailureResponse.class).toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }
}