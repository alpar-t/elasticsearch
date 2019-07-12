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
package org.elasticsearch.gradle.test

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.options.Option
import org.gradle.util.ConfigureUtil

public class RunTask extends DefaultTask {

    ClusterConfiguration clusterConfig

    public RunTask() {
        description = "Runs elasticsearch with '${project.path}'"
        group = 'Verification'
        clusterConfig = new ClusterConfiguration(project)
        clusterConfig.httpPort = 9200
        clusterConfig.transportPort = 9300
        clusterConfig.daemonize = false
        clusterConfig.distribution = 'default'
        project.afterEvaluate {
            ClusterFormationTasks.setup(project, name, this, clusterConfig)
        }
    }

    @Option(
        option = "debug-jvm",
        description = "Enable debugging configuration, to allow attaching a debugger to elasticsearch."
    )
    public void setDebug(boolean enabled) {
        clusterConfig.debug = enabled;
    }

    /** Configure the cluster that will be run. */
    @Override
    public Task configure(Closure closure) {
        ConfigureUtil.configure(closure, clusterConfig)
        return this
    }
}
