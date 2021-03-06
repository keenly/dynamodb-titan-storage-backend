/*
 * Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.amazon.titan.graphdb.dynamodb;

import com.amazon.titan.testutils.CiHeartbeat;
import org.junit.After;
import org.junit.AfterClass;

import com.amazon.titan.TestGraphUtil;
import com.amazon.titan.diskstorage.dynamodb.BackendDataModel;
import com.thinkaurelius.titan.diskstorage.BackendException;
import com.thinkaurelius.titan.diskstorage.configuration.WriteConfiguration;
import com.thinkaurelius.titan.graphdb.TitanOperationCountingTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 *
 * @author Alexander Patrikalakis
 * @author Johan Jacobs
 *
 */
public abstract class AbstractDynamoDBOperationCountingTest extends TitanOperationCountingTest {

    @Rule
    public final TestName testName = new TestName();

    private final CiHeartbeat ciHeartbeat;
    protected final BackendDataModel model;
    protected AbstractDynamoDBOperationCountingTest(BackendDataModel model) {
        this.model = model;
        this.ciHeartbeat = new CiHeartbeat();
    }

    @Override
    public WriteConfiguration getBaseConfiguration()
    {
        return TestGraphUtil.instance().graphConfig(model);
    }

    @AfterClass
    public static void deleteTables() throws BackendException {
        TestGraphUtil.instance().cleanUpTables();
    }

    @Override
    public boolean storeUsesConsistentKeyLocker() {
        return false;//TODO(amcp) remove abstract and make super do return !features.hasLocking()
    }

    @Before
    public void setUpTest() throws Exception {
        this.ciHeartbeat.startHeartbeat(this.testName.getMethodName());
    }

    @After
    public void tearDownTest() throws Exception {
        this.ciHeartbeat.stopHeartbeat();
    }
}
