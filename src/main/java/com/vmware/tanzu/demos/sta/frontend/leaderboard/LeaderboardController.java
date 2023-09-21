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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;

@Controller
class LeaderboardController {
    private final LeaderboardService leaderboardService;

    LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @ModelAttribute("module")
    String getModule() {
        return "leaderboard";
    }

    @ModelAttribute("leaderboard")
    Leaderboard getLeaderboard() {
        return loadLeaderboard();
    }

    @GetMapping("/leaderboard")
    String leaderboard() {
        return "leaderboard";
    }

    @GetMapping("/leaderboard/data")
    String leaderboardData() {
        return "data/leaderboard-table";
    }

    private Leaderboard loadLeaderboard() {
        return leaderboardService.getLeaderboard();
    }
}