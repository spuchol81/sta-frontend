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

package com.vmware.tanzu.demos.sta.frontend.leaderboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class LeaderboardService {
    private final Logger logger = LoggerFactory.getLogger(LeaderboardService.class);
    private final RestTemplate client;

    LeaderboardService(RestTemplate client) {
        this.client = client;
    }

    Leaderboard getLeaderboard() {
        final var leaderboard = client.getForObject("/api/v1/leaderboard", Leaderboard.class);
        logger.debug("Loaded leaderboard: {}", leaderboard);
        return leaderboard;
    }
}
