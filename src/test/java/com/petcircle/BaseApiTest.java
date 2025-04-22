package com.petcircle;

import com.petcircle.model.common.RequestArtifact;
import com.petcircle.utils.ApiUtils.ApiUtil;
import com.petcircle.utils.ReportUtils.ApiLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseApiTest {

    @Autowired
    protected ApiUtil apiUtil;

    @BeforeAll
    public void setUpSuite(TestInfo testInfo) {
        String className = testInfo.getTestClass().get().getSimpleName();
        ApiLogger.setTestClassName(className);
    }

    @AfterAll
    public void tearDownSuite() {
        ApiLogger.flushReport();
    }

}
