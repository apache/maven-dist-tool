/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

pipeline {
    agent {
        label 'ubuntu'
    }
    triggers {
        cron('H H * * *')
    }
    stages {
        stage('Checkout') {
            steps {
                // Clean before build
                cleanWs()
                // We need to explicitly checkout from SCM here
                checkout scm
            }
        }

        stage('Build') {
            steps {
                withMaven(jdk:'jdk_21_latest', maven:'maven_3_latest', mavenLocalRepo:'.repository', options: [
                  artifactsPublisher(disabled: true),
                  findbugsPublisher(disabled: true),
                ]) {
                    sh "mvn -ntp -V -e -Preporting -Dscreenshot=false clean install site"
                }
                archiveArtifacts artifacts: "**/target/site/**/*",allowEmptyArchive: true
            }
        }

        stage('Publish') {
            when {
                branch 'master'
            }
            steps {
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: "${env.WORKSPACE}/target/site", reportFiles: 'index.html', reportName: 'site', reportTitles: ''])
            }
        }

        stage('Check errors') {
            when {
                branch 'master'
            }
            steps {
                withMaven(jdk:'jdk_21_latest', maven:'maven_3_latest', mavenLocalRepo:'.repository', options: [
                  artifactsPublisher(disabled: true),
                  findbugsPublisher(disabled: true),
                ]) {
                    sh "mvn -ntp -V -e failsafe:integration-test"
                }
            }
        }
    }
    options {
        buildDiscarder(logRotator(numToKeepStr:'15'))
        timeout(time: 10, unit: 'MINUTES')
        skipStagesAfterUnstable()
        timestamps()
        disableConcurrentBuilds()
        ansiColor('xterm')
        // This is required if you want to clean before build
        skipDefaultCheckout(true)
    }
}
