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
package org.elasticsearch.gradle.test;

import org.elasticsearch.gradle.testclusters.ElasticsearchCluster;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.testing.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.elasticsearch.gradle.Distribution.INTEG_TEST;

/**
 * Customized version of Gradle {@link Test} task which tracks a collection of {@link ElasticsearchCluster} as a task input. We must do this
 * as a custom task type because the current {@link org.gradle.api.tasks.TaskInputs} runtime API does not have a way to register
 * {@link Nested} inputs.
 */
@CacheableTask
public class RestTestRunnerTask extends Test {

    private Collection<ElasticsearchCluster> clusters = new ArrayList<>();

    public RestTestRunnerTask() {
        super();
        this.getOutputs().doNotCacheIf("Build cache is only enabled for tests against clusters using the 'integ-test' distribution",
            task -> clusters.stream().flatMap(c -> c.getNodes().stream()).anyMatch(n -> n.getDistribution() != INTEG_TEST));
    }

    @Nested
    public Collection<ElasticsearchCluster> getClusters() {
        return clusters;
    }

    public void testCluster(ElasticsearchCluster cluster) {
        this.clusters.add(cluster);
    }
}
