/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.ca.cat.catlean.tomcat.protocol;

import com.google.inject.Inject;
import fr.ca.cat.catlean.tomcat.api.conf.ContextConfiguration;
import fr.ca.cat.catlean.tomcat.api.workers.zookeeper.ZooKeeperOperation;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the higher level implementation of the protocol which provides high level helpers
 * to deal with zookeeper along with retrying synchronous
 *  operations if the connection to ZooKeeper closes such as
 *  {@link #retryOperation(ZooKeeperOperation)}
 *
 * @author lefebvreme
 * @since 28-02-2016
 * @version 0.0.1
 */
public abstract class ProtocolSupport implements Watcher {

    private static final Logger log = Logger.getLogger(ProtocolSupport.class.getName());

    protected final ZooKeeper zooKeeper;
    private AtomicBoolean closed = new AtomicBoolean(false);
    private long retryDelay = 500L;
    private int retryCount = 10;
    private List<ACL> acl = ZooDefs.Ids.OPEN_ACL_UNSAFE;

    @Inject
    public ProtocolSupport(ContextConfiguration contextConfiguration) throws IOException {
        log.info("Initialize the protocol support");
        try {
            this.zooKeeper = new ZooKeeper(contextConfiguration.getZookeeperHosts(), contextConfiguration.getZookeeperPort(), this);
        } catch (IOException e) {
            log.log(Level.SEVERE, String.format("Error occurred during zookeeper initialization with message [%s]",
                    e.getMessage()));
            throw e;
        }
    }

    /**
     * Closes this strategy and releases any ZooKeeper resources; but keeps the
     *  ZooKeeper instance open
     */
    public void close() {
        if (closed.compareAndSet(false, true)) {
            doClose();
        }
    }

    /**
     * return zookeeper client instance
     * @return zookeeper client instance
     */
    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    /**
     * return the acl its using
     * @return the acl.
     */
    public List<ACL> getAcl() {
        return acl;
    }

    /**
     * Allow derived classes to perform
     * some custom closing operations to release resources
     */
    protected void doClose() {
    }

    /**
     * get the retry delay in milliseconds
     * @return the retry delay
     */
    public long getRetryDelay() {
        return retryDelay;
    }

    /**
     * Sets the time waited between retry delays
     * @param retryDelay the retry delay
     */
    public void setRetryDelay(long retryDelay) {
        this.retryDelay = retryDelay;
    }/**
     * Perform the given operation, retrying if the connection fails
     * @return object. it needs to be cast to the callee's expected
     * return type.
     */
    protected Object retryOperation(ZooKeeperOperation operation)
            throws KeeperException, InterruptedException {
        KeeperException exception = null;
        for (int i = 0; i < retryCount; i++) {
            try {
                return operation.execute();
            } catch (KeeperException.SessionExpiredException e) {
                log.severe("Session expired for: " + zooKeeper + " so reconnecting due to: " + e);
                throw e;
            } catch (KeeperException.ConnectionLossException e) {
                if (exception == null) {
                    exception = e;
                }
                log.severe("Attempt " + i + " failed with connection loss so " +
                        "attempting to reconnect: " + e);
                retryDelay(i);
            }
        }
        throw exception;
    }

    /**
     * Ensures that the given path exists with no data, the current
     * ACL and no flags
     * @param path
     */
    protected void ensurePathExists(String path) {
        ensureExists(path, null, acl, CreateMode.PERSISTENT);
    }

    /**
     * Ensures that the given path exists with the given data, ACL and flags
     *
     * @param path
     * @param acl
     * @param flags
     */
    protected void ensureExists(final String path, final byte[] data,
                                final List<ACL> acl, final CreateMode flags) {
        try {
            retryOperation(new ZooKeeperOperation() {
                public boolean execute() throws KeeperException, InterruptedException {
                    Stat stat = zooKeeper.exists(path, false);
                    if (stat != null) {
                        return true;
                    }
                    zooKeeper.create(path, data, acl, flags);
                    return true;
                }
            });
        } catch (KeeperException e) {
            log.severe("Caught: " + e);
        } catch (InterruptedException e) {
            log.severe("Caught: " + e);
        }
    }

    /**
     * Returns true if this protocol has been closed
     * @return true if this protocol is closed
     */
    protected boolean isClosed() {
        return closed.get();
    }

    /**
     * Performs a retry delay if this is not the first attempt
     * @param attemptCount the number of the attempts performed so far
     */
    protected void retryDelay(int attemptCount) {
        if (attemptCount > 0) {
            try {
                Thread.sleep(attemptCount * retryDelay);
            } catch (InterruptedException e) {
                log.fine("Failed to sleep: " + e);
            }
        }
    }

}
