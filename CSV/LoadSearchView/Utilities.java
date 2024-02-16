package CSV.LoadSearchView;

import com.squareup.moshi.Moshi;

import java.util.Map;

public class Utilities {
    public record SuccessResponse(String response_type, Map<String, Object> responseMap) {
        public SuccessResponse(Map<String, Object> responseMap) {
            this("success", responseMap);
        }
        String serialize() {
            try {
                Moshi moshi = new Moshi.Builder().build();
                return moshi.adapter(SuccessResponse.class).toJson(this);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        public Map<String, Object> getResponseMap() {
            return this.responseMap;
        }
    }

    public record FailureResponse(String response_type, String error_msg) {
        public FailureResponse(String error_msg) {
            this("Exception", error_msg);
        }
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
