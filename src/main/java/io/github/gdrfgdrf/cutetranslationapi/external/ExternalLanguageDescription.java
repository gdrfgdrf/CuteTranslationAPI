package io.github.gdrfgdrf.cutetranslationapi.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author gdrfgdrf
 */
@SuppressWarnings("all")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalLanguageDescription {
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
