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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
class UserAvatarController {
    private final Logger logger = LoggerFactory.getLogger(UserAvatarController.class);
    private final Map<String, URI> avatarCache = new ConcurrentHashMap<>(4);
    private final RestTemplate client;

    UserAvatarController(RestTemplate client) {
        this.client = client;
    }

    @GetMapping("/users/{user}/avatar")
    ResponseEntity<?> userAvatar(@PathVariable("user") String user) {
        var avatar = avatarCache.get(user);
        if (avatar == null) {
            if (user.contains("@")) {
                // Build a Gravatar URL to get a user profile picture.
                final var gravatarUrl = URI.create("https://gravatar.com/avatar/"
                        + DigestUtils.md5DigestAsHex(user.getBytes(StandardCharsets.UTF_8))
                        + "?d=404");
                if (isAvatarAvailable(gravatarUrl)) {
                    logger.info("Found Gravatar profile URL for user {}: {}", user, gravatarUrl);
                    avatar = gravatarUrl;
                    avatarCache.put(user, gravatarUrl);
                }
            } else {
                // Build a GitHub avatar URL.
                final var githubUrl = URI.create("https://github.com/" + user + ".png");
                if (isAvatarAvailable(githubUrl)) {
                    logger.info("Found GitHub profile URL for user {}: {}", user, githubUrl);
                    avatar = githubUrl;
                    avatarCache.put(user, githubUrl);
                }
            }
        }
        if (avatar == null) {
            logger.info("Found no profile URL for user {}: using default profile avatar", user);
            avatar = ServletUriComponentsBuilder.fromCurrentContextPath().path("/images/profile-icon.png").build().toUri();
            avatarCache.put(user, avatar);
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(avatar).build();
    }

    private boolean isAvatarAvailable(URI avatarUrl) {
        try {
            client.headForHeaders(avatarUrl);
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }
}