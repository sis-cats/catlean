package fr.ca.cat.catlean.tomcat.api.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author lefebvreme
 * @version 0.0.1
 * @since 29-02-2016
 */
public class DbJobletTest {

    @Test
    public void testDescribe() throws Exception {
        UUID uuid = UUID.randomUUID();
        String jobName = "jobName";
        String serverName = "serverName";
        int jobId = 10;
        int slotId = 20;
        long tsStart = 100L;
        long tsEnd = 1000L;
        DbJoblet job = new DbJoblet(uuid, jobName, serverName, jobId, slotId, tsStart, tsEnd);
        String description = job.describe();
        assertNotNull(description);
        assertTrue(description.contains(uuid.toString()));
        assertTrue(description.contains(jobName));
        assertTrue(description.contains(serverName));
        assertTrue(description.contains(Integer.toString(jobId)));
        assertTrue(description.contains(Integer.toString(slotId)));
        assertTrue(description.contains(Long.toString(tsStart)));
        assertTrue(description.contains(Long.toString(tsEnd)));
    }
}