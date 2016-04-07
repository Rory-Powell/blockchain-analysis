package org.rpowell.blockchain.network.responses;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class QueryResponse {

    @JsonProperty("results")
    private Object results;
    @JsonProperty("errors")
    private Object errors;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The results
     */
    @JsonProperty("results")
    public Object getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    @JsonProperty("results")
    public void setResults(Object results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The errors
     */
    @JsonProperty("errors")
    public Object getErrors() {
        return errors;
    }

    /**
     *
     * @param errors
     * The errors
     */
    @JsonProperty("errors")
    public void setErrors(Object errors) {
        this.errors = errors;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}