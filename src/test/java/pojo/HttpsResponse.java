
package pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lastUpdateId",
    "bids",
    "asks"
})
public class HttpsResponse {

    @JsonProperty("lastUpdateId")
    private Integer lastUpdateId;
    @JsonProperty("bids")
    private List<List<String>> bids = null;
    @JsonProperty("asks")
    private List<List<String>> asks = null;

    @JsonProperty("lastUpdateId")
    public Integer getLastUpdateId() {
        return lastUpdateId;
    }

    @JsonProperty("lastUpdateId")
    public void setLastUpdateId(Integer lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    @JsonProperty("bids")
    public List<List<String>> getBids() {
        return bids;
    }

    @JsonProperty("bids")
    public void setBids(List<List<String>> bids) {
        this.bids = bids;
    }

    @JsonProperty("asks")
    public List<List<String>> getAsks() {
        return asks;
    }

    @JsonProperty("asks")
    public void setAsks(List<List<String>> asks) {
        this.asks = asks;
    }

}
