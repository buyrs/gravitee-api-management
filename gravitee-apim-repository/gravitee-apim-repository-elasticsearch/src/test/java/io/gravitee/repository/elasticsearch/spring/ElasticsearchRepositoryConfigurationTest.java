/**
 * Copyright (C) 2015 The Gravitee team (http://gravitee.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gravitee.repository.elasticsearch.spring;

import io.gravitee.elasticsearch.config.Endpoint;
import io.gravitee.repository.elasticsearch.configuration.RepositoryConfiguration;
import io.gravitee.repository.elasticsearch.embedded.ElasticsearchNode;
import io.vertx.core.Vertx;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring configuration for the test.
 *
 * @author Guillaume Waignier
 * @author Sebastien Devaux
 */
@Configuration
@Import(ElasticsearchRepositoryConfiguration.class)
public class ElasticsearchRepositoryConfigurationTest {

    @Bean
    public Vertx vertx() {
        return Vertx.vertx();
    }

    @Bean
    public ElasticsearchNode elasticsearchNode() {
        return new ElasticsearchNode();
    }

    @Bean
    public RepositoryConfiguration repositoryConfiguration() {
        RepositoryConfiguration elasticConfiguration = new RepositoryConfiguration();
        elasticConfiguration.setEndpoints(Collections.singletonList(new Endpoint("http://localhost:" + elasticsearchNode().getHttpPort())));
        return elasticConfiguration;
    }
}
