package io.github.gdrfgdrf.cutetranslationapi.external;

/**
 * This class will be implemented by the CuteTranslationAPI,
 * This class provides only the default language.
 * @author gdrfgdrf
 */
public abstract class ExternalTranslationProvider {
    public abstract String get(String key);
    public abstract String getOrElse(String key, String otherwise);
    public abstract boolean has(String key);
}
