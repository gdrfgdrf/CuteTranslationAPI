package io.github.gdrfgdrf.cutetranslationapi.external;

/**
 * This class requires a player name to get the specified language
 * @author gdrfgdrf
 */
public abstract class ExternalPlayerTranslationProvider {
    public abstract String get(String playerName, String key);
    public abstract String getOrElse(String playerName, String key, String otherwise);
    public abstract boolean has(String playerName, String key);
}
