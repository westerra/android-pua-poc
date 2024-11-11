package com.westerra.release.constants

import org.junit.Assert
import org.junit.Test

class BuildFlavorsTest {

    private val buildFlavor1 = BuildFlavors.fromString("prod")
    private val buildFlavor2 = BuildFlavors.fromString("uat")
    private val buildFlavor4 = BuildFlavors.DEV

    @Test
    fun buildFlavors_test() {
        Assert.assertNotNull(buildFlavor1)
        Assert.assertEquals(Constants.AWS_WAF_PROD_INTEGRATION_URL, buildFlavor1?.integrationUrl())
        Assert.assertEquals(Constants.AWS_WAF_PROD_DOMAIN_NAME, buildFlavor1?.domainName())
        Assert.assertNotNull(buildFlavor2)
        Assert.assertEquals(Constants.AWS_WAF_UAT_INTEGRATION_URL, buildFlavor2?.integrationUrl())
        Assert.assertEquals(Constants.AWS_WAF_UAT_DOMAIN_NAME, buildFlavor2?.domainName())
        Assert.assertEquals(Constants.AWS_WAF_DEV_INTEGRATION_URL, buildFlavor4.integrationUrl())
        Assert.assertEquals(Constants.AWS_WAF_DEV_DOMAIN_NAME, buildFlavor4.domainName())
    }
}
