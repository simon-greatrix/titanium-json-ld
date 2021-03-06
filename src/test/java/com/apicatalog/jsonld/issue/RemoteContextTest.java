/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.apicatalog.jsonld.issue;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.api.JsonLdError;
import com.apicatalog.jsonld.api.JsonLdOptions;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.document.RdfDocument;
import com.apicatalog.jsonld.loader.ClasspathLoader;
import com.apicatalog.rdf.RdfComparison;
import com.apicatalog.rdf.RdfDataset;

public class RemoteContextTest {

    /**
     * @see <a href="https://github.com/filip26/titanium-json-ld/issues/61">Issue #61</a>
     * @throws JsonLdError
     * @throws IOException
     */
    @Test
    public void testToRdfMissingTriples1() throws JsonLdError, IOException {

        final Document document = readDocument("issue61-in.json");
        final Document context = readDocument("issue61-context.json");
        
        final RdfDataset result = JsonLd.toRdf(document).context(context).get();
        
        assertNotNull(result);

        try (final InputStream is = getClass().getResourceAsStream("issue61-out.nq")) {
            
            assertNotNull(is);

            boolean match = RdfComparison.equals(result, RdfDocument.of(is).getRdfContent().orElse(null));
            
            assertTrue(match);            
        }
    }

    /**
     * @see <a href="https://github.com/filip26/titanium-json-ld/issues/61">Issue #61</a>
     * @throws JsonLdError
     * @throws IOException
     */
    @Test
    public void testToRdfMissingTriples2() throws JsonLdError, IOException {

        final Document document = readDocument("issue61-in.json");
        final Document context = readDocument("issue61-context.json");
        
        final JsonLdOptions options = new JsonLdOptions();
        options.setExpandContext(context);
        
        final RdfDataset result = JsonLd.toRdf(document).options(options).get();
        
        assertNotNull(result);

        try (final InputStream is = getClass().getResourceAsStream("issue61-out.nq")) {
            
            assertNotNull(is);

            boolean match = RdfComparison.equals(result, RdfDocument.of(is).getRdfContent().orElse(null));
                        
            assertTrue(match);            
        }
    }

    /**
     * @see <a href="https://github.com/filip26/titanium-json-ld/issues/61">Issue #61</a>
     * @throws JsonLdError
     * @throws IOException
     */
    @Test
    public void testToRdfMissingTriples3() throws JsonLdError, IOException {

        final RdfDataset result = 
                    JsonLd
                        .toRdf("classpath:/com/apicatalog/jsonld/issue/issue61-in.json")
                        .context("classpath:/com/apicatalog/jsonld/issue/issue61-context.json")
                        .base("https://api.inaturalist.org/v1/observations/")
                        .loader(new ClasspathLoader())
                        .get();

        assertNotNull(result);

        try (final InputStream is = getClass().getResourceAsStream("issue61-out.nq")) {

            assertNotNull(is);

            boolean match = RdfComparison.equals(result, RdfDocument.of(is).getRdfContent().orElse(null));

            assertTrue(match);
        }
    }

    /**
     * @see <a href="https://github.com/filip26/titanium-json-ld/issues/63">Issue #63</a>
     * @throws JsonLdError
     * @throws IOException
     */
    @Test
    public void testRemoteContext() throws JsonLdError, IOException {

        final Document document = readDocument("issue63-in.json");
        
        final RdfDataset result = JsonLd.toRdf(document).loader(new ClasspathLoader()).get();
        
        assertNotNull(result);

        try (final InputStream is = getClass().getResourceAsStream("issue63-out.nq")) {
            
            assertNotNull(is);

            boolean match = RdfComparison.equals(result, RdfDocument.of(is).getRdfContent().orElse(null));
                        
            assertTrue(match);            
        }
    }
    
    private final Document readDocument(final String name) throws JsonLdError, IOException {
        try (final InputStream is = getClass().getResourceAsStream(name)) {
            return JsonDocument.of(is);
        }
    }    
}
