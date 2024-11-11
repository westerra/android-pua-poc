package com.westerra.release.custom

import java.lang.ref.WeakReference

/*
  https://stackoverflow.com/questions/4878159/whats-the-best-way-to-share-data-between-activities
  Passing data between Activities using Intents is usually fine but if the objects are large it
  can cause crashes. This cache can be used to avoid that issue but there is no long term
  guarantee that the transfer cache object are retained in memory and they will eventually be
  garbage cleaned. Best to write code assuming the cached data can be lost at any time and only
  try to use the cache for real world short periods of time, like between launching an Activity
  and that Activity reading the cache object.
 */
class DataTransferCache {
    companion object {
        private val data = mutableMapOf<String, WeakReference<Any>>()
    }
    fun save(id: String, obj: Any) {
        data[id] = WeakReference(obj)
    }
    fun retrieve(id: String?): Any? {
        return data[id]?.get()
    }

    fun resetBtpDisclosuresState() {
        save(DataTransferCacheKeys.BTP_DISCLOSURES_TERMS_SELECTED, false)
        save(DataTransferCacheKeys.BTP_DISCLOSURES_IRS_WITHOLDING_SELECTED, false)
        save(DataTransferCacheKeys.BTP_DISCLOSURES_WEGOTYA_SELECTED, false)
    }
}
