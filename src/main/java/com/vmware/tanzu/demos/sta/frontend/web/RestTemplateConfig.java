/*
 * Copyright (c) 2023 VMware, Inc. or its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vmware.tanzu.demos.sta.frontend.web;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration(proxyBeanMethods = false)
class RestTemplateConfig {
    private final Logger logger = LoggerFactory.getLogger(RestTemplateConfig.class);

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    RestTemplateBuilder restTemplateBuilder(@Value("${spring.application.name}") String appName,
                                            @Value("${app.marketplace.url}") String marketplaceUrl,
                                            @Autowired(required = false) RestTemplateRequestCustomizer<ClientHttpRequest> oauth2Customizer) {
        final var okhttpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(30))
                .readTimeout(Duration.ofSeconds(30))
                .build();
        final var reqFactory = new OkHttp3ClientHttpRequestFactory(okhttpClient);

        logger.info("Using marketplace URL: {}", marketplaceUrl);
        var builder = new RestTemplateBuilder()
                .rootUri(marketplaceUrl)
                .requestFactory(() -> reqFactory)
                .defaultHeader(HttpHeaders.USER_AGENT, appName);
        if (oauth2Customizer != null) {
            builder = builder.additionalRequestCustomizers(oauth2Customizer);
        }
        return builder;
    }
}
