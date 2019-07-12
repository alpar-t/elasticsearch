/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.gradle.testclusters;

import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Keep an inventory of all running Clusters and stop them when interrupted
 *
 * This takes advantage of the fact that Gradle interrupts all the threads in the daemon when the build completes.
 */
public class TestClusterCleanupOnShutdown implements Runnable {

    private final Logger logger =  Logging.getLogger(TestClusterCleanupOnShutdown.class);

    private Set<ElasticsearchCluster> clustersToWatch = new HashSet<>();

    public void watch(Collection<ElasticsearchCluster> cluster) {
        synchronized (clustersToWatch) {
            clustersToWatch.addAll(clustersToWatch);
        }
    }

    public void unWatch(Collection<ElasticsearchCluster> cluster) {
        synchronized (clustersToWatch) {
            clustersToWatch.removeAll(clustersToWatch);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(Long.MAX_VALUE);
            }
        } catch (InterruptedException interrupted) {
            synchronized (clustersToWatch) {
                if (clustersToWatch.isEmpty()) {
                    return;
                }
                logger.info("Cleanup thread was interrupted, shutting down all clusters");
                Iterator<ElasticsearchCluster> iterator = clustersToWatch.iterator();
                while (iterator.hasNext()) {
                    ElasticsearchCluster cluster = iterator.next();
                    iterator.remove();
                    try {
                        cluster.stop(false);
                    } catch (Exception e) {
                        logger.warn("Could not shut down {}", cluster, e);
                    }
                }
            }
        }
    }
}
