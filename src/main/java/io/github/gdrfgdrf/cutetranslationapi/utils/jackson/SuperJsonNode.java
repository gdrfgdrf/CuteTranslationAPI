/*
 *  Copyright 2024 CuteTranslationAPI's contributors
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */

package io.github.gdrfgdrf.cutetranslationapi.utils.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.Iterator;

@Getter
@SuppressWarnings("unused")
public class SuperJsonNode {
    private final JsonNode jsonNode;

    public SuperJsonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public String getString(int i) {
        return jsonNode.get(i).asText();
    }

    public String getString(String key) {
        return jsonNode.get(key).asText();
    }

    public String getStringOrNull(int i) {
        if (jsonNode.has(i)) {
            return jsonNode.get(i).asText();
        }
        return null;
    }

    public String getStringOrNull(String key) {
        if (jsonNode.has(key)) {
            return jsonNode.get(key).asText();
        }
        return null;
    }

    public boolean contains(String key) {
        return jsonNode.has(key);
    }

    public int size() {
        return jsonNode.size();
    }

    public Iterator<String> keySet() {
        return jsonNode.fieldNames();
    }
}
