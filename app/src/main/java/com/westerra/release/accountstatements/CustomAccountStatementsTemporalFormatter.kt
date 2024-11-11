package com.westerra.release.accountstatements

import com.backbase.android.retail.journey.accountstatements.configuration.AccountStatementsTemporalFormatter
import com.westerra.release.extensions.toPreferredDatePattern
import java.time.LocalDate
import java.time.temporal.TemporalAccessor

class CustomAccountStatementsTemporalFormatter : AccountStatementsTemporalFormatter {
    override fun format(temporal: TemporalAccessor): String {
        if (temporal is LocalDate) {
            return temporal.toPreferredDatePattern()
        }
        return temporal.toString()
    }
}
