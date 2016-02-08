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

import java.io.File;
import java.io.IOException;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SQSPublishEventWorker {

    private final static Logger logger = LoggerFactory.getLogger(SQSPublishEventWorker.class);

    public static void main(final String[] args) {
        String payloadFileLocation = "";
        for (int i = 0; i < args.length; i++) {
            if ("-payload".equals(args[i]) && (i + 1) < args.length) {
                payloadFileLocation = args[i + 1];
            }
        }

        if (!payloadFileLocation.isEmpty()) {
            logger.debug(String.format("Processing with payload: %s", payloadFileLocation));
            final SQSPublishEventWorker sqsPublishEventWorker = new SQSPublishEventWorker();
            sqsPublishEventWorker.processFile(payloadFileLocation);
        } else {
            logger.warn("No payload defined for this task.");
        }
    }

    public void processFile(final String payloadFileLocation) {
        final File payloadFile = new File(payloadFileLocation);

        final SQSPublishEventTasks sQSPublishEventTaskPayload = fileToObject(payloadFile, SQSPublishEventTasks.class);

        for (final SQSPublishEventTask sQSPublishEventTask : sQSPublishEventTaskPayload.getTasks()) {
            if (sQSPublishEventTask.getEventFrequency() != null) {
                if (EVENT_FREQUENCY.LAST_DAY_OF_MONTH.name().equals(sQSPublishEventTask.getEventFrequency())) {
                    if (!new LocalDate().dayOfMonth().withMaximumValue().equals(new LocalDate())) {
                        continue;
                    }
                }
            }

            final AWSCredentials credentials = new BasicAWSCredentials(sQSPublishEventTask.getAccessKey(),
                    sQSPublishEventTask.getSecretKey());

            final AmazonSQS sqs = new AmazonSQSClient(credentials);
            final Region region = Region.getRegion(Regions.fromName(sQSPublishEventTask.getRegion()));
            sqs.setRegion(region);

            final GetQueueUrlResult queue = sqs
                    .getQueueUrl(new GetQueueUrlRequest(sQSPublishEventTask.getTargetQueue()));
            final String myQueueUrl = queue.getQueueUrl();

            final SendMessageResult sendMessageResult = sqs.sendMessage(new SendMessageRequest(myQueueUrl,
                    String.format(sQSPublishEventTask.getEventText(), LocalDate.now().toString())));
            logger.debug(sendMessageResult.toString());
            sqs.shutdown();
        }
    }

    private <T> T fileToObject(final File file, final Class<T> valueType) {
        final ObjectMapper mapper = new ObjectMapper();
        T value = null;
        try {
            value = mapper.readValue(file, valueType);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
