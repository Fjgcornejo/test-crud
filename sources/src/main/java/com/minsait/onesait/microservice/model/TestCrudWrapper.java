
package com.minsait.onesait.microservice.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * TestCrud
 * <p>
 * TestCrud
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "TestCrud"
})
public class TestCrudWrapper {

    /**
     * Properties for DataModel TestCrud
     * (Required)
     * 
     */
    @JsonProperty("TestCrud")
    @JsonPropertyDescription("Properties for DataModel TestCrud")
    private TestCrud testCrud;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * Properties for DataModel TestCrud
     * (Required)
     * 
     */
    @JsonProperty("TestCrud")
    public TestCrud getTestCrud() {
        return testCrud;
    }

    /**
     * Properties for DataModel TestCrud
     * (Required)
     * 
     */
    @JsonProperty("TestCrud")
    public void setTestCrud(TestCrud testCrud) {
        this.testCrud = testCrud;
    }

    public TestCrudWrapper withTestCrud(TestCrud testCrud) {
        this.testCrud = testCrud;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public TestCrudWrapper withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
