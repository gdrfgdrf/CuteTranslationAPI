package io.github.gdrfgdrf.cutetranslationapi.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gdrfgdrf
 */
@SuppressWarnings("all")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalModDescription {
    @JsonProperty("display-name")
    private String displayName;
    @JsonProperty("default-language")
    private String defaultLanguage = "en_us";
    @JsonProperty("languages")
    private List<ExternalLanguageDescription> languages = new ArrayList<>();

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public List<ExternalLanguageDescription> getLanguages() {
        return languages;
    }

    public void setLanguages(List<ExternalLanguageDescription> languages) {
        this.languages = languages;
    }
}
