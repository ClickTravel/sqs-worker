package com.clicktravel.services.scheduling;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class SQSPublishEventTaskTest {

    @Test
    public void shouldSetGetEventText() {
        // Given
        final String eventText = RandomStringUtils.random(10);
        final SQSPublishEventTask sqsPublishEventTask = new SQSPublishEventTask();

        // When
        sqsPublishEventTask.setEventText(eventText);
        final String testEventText = sqsPublishEventTask.getEventText();

        // Then
        assertEquals(eventText, testEventText);
    }

    @Test
    public void shouldSetGetAccessKey() {
        // Given
        final String accessKey = RandomStringUtils.random(10);
        final SQSPublishEventTask sqsPublishEventTask = new SQSPublishEventTask();

        // When
        sqsPublishEventTask.setAccessKey(accessKey);
        final String testAccessKey = sqsPublishEventTask.getAccessKey();

        // Then
        assertEquals(accessKey, testAccessKey);
    }

    @Test
    public void shouldSetGetSecretKey() {
        // Given
        final String secretKey = RandomStringUtils.random(10);
        final SQSPublishEventTask sqsPublishEventTask = new SQSPublishEventTask();

        // When
        sqsPublishEventTask.setSecretKey(secretKey);
        final String testSecretKey = sqsPublishEventTask.getSecretKey();

        // Then
        assertEquals(secretKey, testSecretKey);
    }

    @Test
    public void shouldSetGetRegion() {
        // Given
        final String region = RandomStringUtils.random(10);
        final SQSPublishEventTask sqsPublishEventTask = new SQSPublishEventTask();

        // When
        sqsPublishEventTask.setRegion(region);
        final String testRegion = sqsPublishEventTask.getRegion();

        // Then
        assertEquals(region, testRegion);
    }

    @Test
    public void shouldSetGetTargetQueue() {
        // Given
        final String targetQueue = RandomStringUtils.random(10);
        final SQSPublishEventTask sqsPublishEventTask = new SQSPublishEventTask();

        // When
        sqsPublishEventTask.setTargetQueue(targetQueue);
        final String testTargetQueue = sqsPublishEventTask.getTargetQueue();

        // Then
        assertEquals(targetQueue, testTargetQueue);
    }

    @Test
    public void shouldSetGetEventFrequency() {
        // Given
        final String eventFrequency = RandomStringUtils.random(10);
        final SQSPublishEventTask sqsPublishEventTask = new SQSPublishEventTask();

        // When
        sqsPublishEventTask.setEventFrequency(eventFrequency);
        final String testEventFrequency = sqsPublishEventTask.getEventFrequency();

        // Then
        assertEquals(eventFrequency, testEventFrequency);
    }
}
