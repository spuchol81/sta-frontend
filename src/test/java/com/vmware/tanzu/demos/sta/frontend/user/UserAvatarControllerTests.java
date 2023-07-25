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

package com.vmware.tanzu.demos.sta.frontend.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
public class UserAvatarControllerTests {
    @Autowired
    private TestRestTemplate client;

    @Test
    void testGravatar() {
        final var resp = client.getForEntity("/users/alexandre.roman@gmail.com/avatar", String.class);
        assertThat(resp.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(resp.getHeaders().getLocation()).isEqualTo(URI.create("https://gravatar.com/avatar/31ad3a0c560a54d14cb65a59b732057c?d=404"));
    }

    @Test
    void testGitHub() {
        final var resp = client.getForEntity("/users/alexandreroman/avatar", String.class);
        assertThat(resp.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(resp.getHeaders().getLocation()).isEqualTo(URI.create("https://github.com/alexandreroman.png"));
    }
}
