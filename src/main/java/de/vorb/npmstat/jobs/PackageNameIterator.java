/*
 * Copyright 2012-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.vorb.npmstat.jobs;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

public class PackageNameIterator implements Iterator<String>, AutoCloseable {

    private final JsonParser parser;

    public PackageNameIterator(URL allDocsUrl, ObjectMapper objectMapper) {
        try {
            final JsonFactory factory = objectMapper.getFactory();
            parser = factory.createParser(allDocsUrl);
            forwardToRowsArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("could not parse url: " + allDocsUrl);
        }
    }

    private void forwardToRowsArray() throws IOException {
        JsonToken token;
        while (!parser.isClosed() && (token = parser.nextToken()) != null) {
            if (JsonToken.FIELD_NAME.equals(token) && "rows".equals(parser.getCurrentName())) {
                token = parser.nextToken();
                if (!JsonToken.START_ARRAY.equals(token)) {
                    throw new IllegalStateException("rows must be an array");
                }
                token = parser.nextToken();
                if (!JsonToken.START_OBJECT.equals(token)) {
                    throw new IllegalArgumentException("rows must contain objects");
                }
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        try {
            JsonToken token;
            while (!parser.isClosed() && (token = parser.nextToken()) != null) {
                if (JsonToken.FIELD_NAME.equals(token) && "key".equals(parser.getCurrentName())) {
                    token = parser.nextToken();
                    if (JsonToken.VALUE_STRING.equals(token)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String next() {
        try {
            return parser.getText();
        } catch (IOException e) {
            throw new IllegalStateException("parser is not at text value node");
        }
    }

    @Override
    public void close() throws Exception {
        parser.close();
    }
}
