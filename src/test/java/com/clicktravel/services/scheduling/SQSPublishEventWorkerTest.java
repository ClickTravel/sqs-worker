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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SQSPublishEventWorker.class, AmazonSQS.class, ObjectMapper.class })
public class SQSPublishEventWorkerTest {

    @Test
    public void shouldRunMain() throws Exception {
        // Given
        final String payloadFileLocation = "payload.json";
        final SQSPublishEventWorker sqsPublishEventWorker = mock(SQSPublishEventWorker.class);
        whenNew(SQSPublishEventWorker.class).withNoArguments().thenReturn(sqsPublishEventWorker);

        final String[] args = new String[] { "-payload", payloadFileLocation };

        // When
        SQSPublishEventWorker.main(args);

        // Then
        verify(sqsPublishEventWorker).processFile(eq(payloadFileLocation));
    }

    @Test
    public void shouldNotRunMain_asNoPayload() throws Exception {
        // Given
        final SQSPublishEventWorker sqsPublishEventWorker = mock(SQSPublishEventWorker.class);
        whenNew(SQSPublishEventWorker.class).withNoArguments().thenReturn(sqsPublishEventWorker);

        final String[] args = new String[] { "-payload" };

        // When
        SQSPublishEventWorker.main(args);

        // Then
        verify(sqsPublishEventWorker, times(0)).processFile(any(String.class));
    }

    @Test
    public void shouldNotRunMain_asNoArguments() throws Exception {
        // Given
        final SQSPublishEventWorker sqsPublishEventWorker = mock(SQSPublishEventWorker.class);
        whenNew(SQSPublishEventWorker.class).withNoArguments().thenReturn(sqsPublishEventWorker);

        final String[] args = new String[] {};

        // When
        SQSPublishEventWorker.main(args);

        // Then
        verify(sqsPublishEventWorker, times(0)).processFile(any(String.class));
    }

    @Test
    public void shouldProcessFile() throws Exception {
        // Given
        final String payloadFileLocation = RandomStringUtils.random(10);

        final SQSPublishEventTasks sqsPublishEventTasks = mock(SQSPublishEventTasks.class);
        final ObjectMapper mapper = mock(ObjectMapper.class);
        whenNew(ObjectMapper.class).withNoArguments().thenReturn(mapper);
        when(mapper.readValue(any(File.class), eq(SQSPublishEventTasks.class))).thenReturn(sqsPublishEventTasks);

        final SQSPublishEventTask sqsPublishEventTask = randomSQSPublishEventTask(false);
        when(sqsPublishEventTasks.getTasks()).thenReturn(Lists.newArrayList(sqsPublishEventTask));

        final AmazonSQSClient sqs = mock(AmazonSQSClient.class);
        whenNew(AmazonSQSClient.class).withAnyArguments().thenReturn(sqs);

        final GetQueueUrlResult getQueueUrlResult = mock(GetQueueUrlResult.class);
        when(sqs.getQueueUrl(any(GetQueueUrlRequest.class))).thenReturn(getQueueUrlResult);

        final SQSPublishEventWorker sqsPublishEventWorker = new SQSPublishEventWorker();

        // When
        sqsPublishEventWorker.processFile(payloadFileLocation);

        // Then
        verify(sqs).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    public void shouldProcessFile_withEmptyPayload() throws Exception {
        // Given
        final String payloadFileLocation = RandomStringUtils.random(10);

        final SQSPublishEventTasks sqsPublishEventTasks = mock(SQSPublishEventTasks.class);
        final ObjectMapper mapper = mock(ObjectMapper.class);
        whenNew(ObjectMapper.class).withNoArguments().thenReturn(mapper);
        when(mapper.readValue(any(File.class), eq(SQSPublishEventTasks.class))).thenReturn(sqsPublishEventTasks);

        final SQSPublishEventWorker sqsPublishEventWorker = new SQSPublishEventWorker();

        // When
        sqsPublishEventWorker.processFile(payloadFileLocation);

        // Then
        verify(sqsPublishEventTasks).getTasks();
    }

    private SQSPublishEventTask randomSQSPublishEventTask(final Boolean withFrequency) {
        final SQSPublishEventTask sqsPublishEventTask = new SQSPublishEventTask();
        sqsPublishEventTask.setAccessKey(RandomStringUtils.random(10));

        sqsPublishEventTask.setEventText(RandomStringUtils.random(10));
        final List<Region> regions = RegionUtils.getRegions();
        sqsPublishEventTask.setRegion(regions.get(new Random().nextInt(regions.size())).getName());
        sqsPublishEventTask.setSecretKey(RandomStringUtils.random(10));
        sqsPublishEventTask.setTargetQueue(RandomStringUtils.random(10));
        if (withFrequency) {
            sqsPublishEventTask.setEventFrequency(EVENT_FREQUENCY.LAST_DAY_OF_MONTH.name());
        }
        return sqsPublishEventTask;
    }
}
