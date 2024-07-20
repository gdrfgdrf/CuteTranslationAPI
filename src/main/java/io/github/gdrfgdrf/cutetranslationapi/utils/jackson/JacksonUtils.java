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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public class JacksonUtils {
    private static final ObjectMapper MAPPER_INSTANCE = new ObjectMapper();

    private JacksonUtils() {}

    public static <T> T readString(String jsonString, Class<?> type) throws JsonProcessingException {
        return (T) MAPPER_INSTANCE.readValue(jsonString, type);
    }

    public static <T> T readFile(File file, Class<?> type) throws IOException {
        return (T) MAPPER_INSTANCE.readValue(file, type);
    }

    public static <T> T readInputStream(InputStream inputStream, Class<?> type) throws IOException {
        return (T) MAPPER_INSTANCE.readValue(inputStream, type);
    }

    public static <T> T readBytes(byte[] bytes, Class<?> type) throws IOException {
        return (T) MAPPER_INSTANCE.readValue(bytes, type);
    }

    public static ObjectNode newTree() throws JsonProcessingException {
        return (ObjectNode) MAPPER_INSTANCE.readTree("{}");
    }

    public static SuperJsonNode readStringTree(String jsonString) throws JsonProcessingException {
        return new SuperJsonNode(MAPPER_INSTANCE.readTree(jsonString));
    }

    public static SuperJsonNode readFileTree(File file) throws IOException {
        return new SuperJsonNode(MAPPER_INSTANCE.readTree(file));
    }

    public static SuperJsonNode readInputStreamTree(InputStream inputStream) throws IOException {
        return new SuperJsonNode(MAPPER_INSTANCE.readTree(inputStream));
    }

    public static SuperJsonNode readBytes(byte[] bytes) throws IOException {
        return new SuperJsonNode(MAPPER_INSTANCE.readTree(bytes));
    }

    public static <E> List<E> toList(String jsonString, Class<E> E) throws JsonProcessingException {
        List<E> result = new ArrayList<>();
        SuperJsonNode jsonNode = JacksonUtils.readStringTree(jsonString);
        if (jsonNode.getJsonNode().isArray()) {
            for (int i = 0; i < jsonNode.size(); i++) {
                E e = JacksonUtils.readString(jsonNode.getString(i), E);
                result.add(e);
            }
        }

        return result;
    }

    public static String writeJsonString(Object obj) throws JsonProcessingException {
        return MAPPER_INSTANCE.writeValueAsString(obj);
    }


}
