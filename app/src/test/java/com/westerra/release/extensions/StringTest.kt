package com.westerra.release.extensions

import org.junit.Assert
import org.junit.Test

class StringTest {

    private val version0 = "0.0.0-dev"
    private val version1 = "0.0.0"
    private val version2 = "1.100.2345"
    private val version3 = "1.1.23-prod"
    private val version4 = "10.1.23-prod"
    private val version5 = "1.10.23-prod"
    private val version6 = "1.1.26-prod"
    private val badVersion0 = "0.0"
    private val badVersion1 = "a.s.f-prod"
    private val badVersion3 = "1.2.asdf"
    private val badVersion4 = "PROD-1.2.4-prod"

    @Test
    fun parseVersionComponents_test() {
        Assert.assertEquals(listOf(0, 0, 0), version0.parseVersionComponents())
        Assert.assertEquals(listOf(0, 0, 0), version1.parseVersionComponents())
        Assert.assertEquals(listOf(1, 100, 2345), version2.parseVersionComponents())
        Assert.assertEquals(listOf(1, 1, 23), version3.parseVersionComponents())
        Assert.assertEquals(listOf(0, 0), badVersion0.parseVersionComponents())
        Assert.assertEquals(listOf<Int>(), badVersion1.parseVersionComponents())
        Assert.assertEquals(listOf<Int>(), badVersion3.parseVersionComponents())
        Assert.assertEquals(listOf<Int>(), badVersion4.parseVersionComponents())
    }

    @Test
    fun compareVersion_test() {
        Assert.assertFalse(version0.compareVersion(otherVersion = version0))
        Assert.assertFalse(version0.compareVersion(otherVersion = version1))
        Assert.assertFalse(version1.compareVersion(otherVersion = version0))
        Assert.assertTrue(version0.compareVersion(otherVersion = version2))
        Assert.assertFalse(version2.compareVersion(otherVersion = version0))
        Assert.assertTrue(version0.compareVersion(otherVersion = version3))
        Assert.assertFalse(version3.compareVersion(otherVersion = version0))
        Assert.assertTrue(version0.compareVersion(otherVersion = version3))
        Assert.assertFalse(version2.compareVersion(otherVersion = version3))
        Assert.assertTrue(version3.compareVersion(otherVersion = version2))
        Assert.assertTrue(version3.compareVersion(otherVersion = version4))
        Assert.assertFalse(version4.compareVersion(otherVersion = version3))
        Assert.assertTrue(version3.compareVersion(otherVersion = version5))
        Assert.assertFalse(version5.compareVersion(otherVersion = version3))
        Assert.assertTrue(version3.compareVersion(otherVersion = version6))
        Assert.assertFalse(version6.compareVersion(otherVersion = version3))
        Assert.assertFalse(badVersion0.compareVersion(otherVersion = version0))
        Assert.assertFalse(version0.compareVersion(otherVersion = badVersion0))
        Assert.assertFalse(badVersion1.compareVersion(otherVersion = badVersion0))
    }
}
