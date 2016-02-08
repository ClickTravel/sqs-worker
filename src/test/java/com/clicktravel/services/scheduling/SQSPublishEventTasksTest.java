package com.clicktravel.services.scheduling;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SQSPublishEventTasksTest {

    @Test
    public void shouldSetGetTargetQueue() {
        // Given
        final List<SQSPublishEventTask> tasks = new ArrayList<SQSPublishEventTask>();
        final SQSPublishEventTasks sqsPublishEventTasks = new SQSPublishEventTasks();

        // When
        sqsPublishEventTasks.setTasks(tasks);
        final List<SQSPublishEventTask> testTasks = sqsPublishEventTasks.getTasks();

        // Then
        assertEquals(tasks, testTasks);
    }
}
