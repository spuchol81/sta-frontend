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

package com.vmware.tanzu.demos.sta.frontend.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
class StockService {
    private final Logger logger = LoggerFactory.getLogger(StockService.class);
    private final RestTemplate client;

    StockService(RestTemplate client) {
        this.client = client;
    }

    Map<String, List<StockValue>> getStockValues() {
        logger.debug("Loading stock values from marketplace");

        // Get the list of stocks first.
        final var stocks = client.getForObject("/api/v1/stocks", Stock[].class);
        if (stocks == null || stocks.length == 0) {
            return Collections.emptyMap();
        }

        // Now load stock values.
        final var stockData = new TreeMap<String, List<StockValue>>();
        for (final Stock stock : stocks) {
            final StockValue[] stockValues = client.getForObject("/api/v1/stocks/" + stock.symbol() + "/values", StockValue[].class);
            if (stockValues != null) {
                stockData.put(stock.symbol(), List.of(stockValues));
            }
        }

        return stockData;
    }
}
