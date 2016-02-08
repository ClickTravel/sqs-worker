/*
 * Copyright 2015 Click Travel Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.clicktravel.services.scheduling;

public class SQSPublishEventTask {

    private String eventText;
    private String accessKey;
    private String secretKey;
    private String region;
    private String targetQueue;
    private String eventFrequency;

    public String getEventText() {
        return eventText;
    }

    public void setEventText(final String eventText) {
        this.eventText = eventText;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(final String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(final String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(final String region) {
        this.region = region;
    }

    public String getTargetQueue() {
        return targetQueue;
    }

    public void setTargetQueue(final String targetQueue) {
        this.targetQueue = targetQueue;
    }

    public String getEventFrequency() {
        return eventFrequency;
    }

    public void setEventFrequency(final String eventFrequency) {
        this.eventFrequency = eventFrequency;
    }
}
