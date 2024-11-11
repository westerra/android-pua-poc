package com.westerra.release.custom

import android.content.Context
import com.westerra.release.WesterraApplication

class MySharedPreferences {
    companion object {
        private const val PREF_INTERCEPT_AD_SHOWN = "intercept_advertisement_shown"
        private const val PREF_MEMBER_NUMBER = "member_number"

        fun enableInterceptAdvertisement() {
            getActivity() ?. let {
                val prefs = it.getPreferences(Context.MODE_PRIVATE)
                val prefsEdit = prefs.edit()
                prefsEdit.putBoolean(PREF_INTERCEPT_AD_SHOWN, false)
                prefsEdit.commit()
            }
        }

        fun disableInterceptAdvertisement() {
            getActivity() ?. let {
                val prefs = it.getPreferences(Context.MODE_PRIVATE)
                val prefsEdit = prefs.edit()
                prefsEdit.putBoolean(PREF_INTERCEPT_AD_SHOWN, true)
                prefsEdit.commit()
            }
        }

        fun shouldShowInterceptAdvertisement(): Boolean {
            getActivity() ?. let {
                val prefs = it.getPreferences(Context.MODE_PRIVATE)
                return !prefs.getBoolean(PREF_INTERCEPT_AD_SHOWN, false)
            }
            return false
        }

        fun getMemberNumber(): String? {
            getActivity() ?. let {
                val prefs = it.getPreferences(Context.MODE_PRIVATE)
                return prefs.getString(PREF_MEMBER_NUMBER, null)
            }
            return null
        }

        fun storeMemberNumber(memberNumber: String) {
            getActivity() ?. let {
                val prefs = it.getPreferences(Context.MODE_PRIVATE)
                val prefsEdit = prefs.edit()
                prefsEdit.putString(PREF_MEMBER_NUMBER, memberNumber)
                prefsEdit.commit()
            }
        }

        fun resetMemberNumberState() {
            getActivity() ?. let {
                val prefs = it.getPreferences(Context.MODE_PRIVATE)
                val prefsEdit = prefs.edit()
                prefsEdit.putString(PREF_MEMBER_NUMBER, "")
                prefsEdit.commit()
            }
        }

        private fun getActivity() = WesterraApplication.getInstance().getActivity()
    }
}
