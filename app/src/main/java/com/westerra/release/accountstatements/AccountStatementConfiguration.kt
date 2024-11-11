package com.westerra.release.accountstatements

import android.content.Context
import androidx.core.content.ContextCompat
import com.backbase.android.retail.journey.accountstatements.configuration.AccountStatementsCategoryIconMappingConfiguration
import com.backbase.android.retail.journey.accountstatements.configuration.AccountStatementsCategoryMappingConfiguration
import com.backbase.android.retail.journey.accountstatements.configuration.AccountStatementsConfiguration
import com.backbase.android.retail.journey.accountstatements.filter.AccountStatementsFilterScreenConfiguration
import com.backbase.android.retail.journey.accountstatements.list.AccountStatementsListScreenConfiguration
import com.backbase.deferredresources.DeferredBoolean
import com.backbase.deferredresources.DeferredColor
import com.backbase.deferredresources.DeferredDrawable
import com.westerra.release.R
import com.westerra.release.extensions.resourceString
import com.westerra.release.extensions.toDeferredBoolean
import com.westerra.release.extensions.toDeferredText

object AccountStatementConfiguration {

    operator fun invoke(context: Context): AccountStatementsConfiguration {
        return AccountStatementsConfiguration {
            categoryIconMapping = getAccountStatementsCategoryMapping(context)
            filterScreen = getFilterScreenConfiguration()
            formattedDateProvider = CustomAccountStatementsTemporalFormatter()
            loadingFailedTitle = R.string.oops_error_message.toDeferredText()
            loadingFailedSubtitle = R.string.statements_loading_failed_message.toDeferredText()
            retryButtonTitle = R.string.please_try_again_button_text.toDeferredText()
            listScreen = AccountStatementsListScreenConfiguration.Builder()
                .apply {
                    pageSize = 10
                }
                .build()
        }
    }

    private fun getAccountStatementsCategoryMapping(
        context: Context,
    ): AccountStatementsCategoryIconMappingConfiguration {
        return AccountStatementsCategoryMappingConfiguration {
            categoryIconMap = getCategoryIconMap(context)
            defaultIconBackgroundColor = getDefaultBackgroundColour()
            iconBackgroundColorMap = getBackgroundIconColorMap(context)
        }
    }

    private fun getFilterScreenConfiguration(): AccountStatementsFilterScreenConfiguration {
        return AccountStatementsFilterScreenConfiguration {
            statementTypesTitle = R.string.statement_types.toDeferredText()
            showStatementCategoryFilter = getShowStatementCategoryFilter()
        }
    }

    private fun getShowStatementCategoryFilter(): DeferredBoolean {
        return true.toDeferredBoolean()
    }

    private fun getBackgroundIconColorMap(context: Context): Map<String, DeferredColor> {
        return mapOf(
            R.string.monthly_statement.resourceString(context) to getDefaultBackgroundColour(),
            R.string.tax_statement.resourceString(context) to getDefaultBackgroundColour(),
            R.string.other.resourceString(context) to getDefaultBackgroundColour(),
        )
    }

    private fun getCategoryIconMap(context: Context): Map<String, DeferredDrawable> {
        return mapOf(
            R.string.monthly_statement.resourceString(context) to
                DeferredDrawable.Resource(R.drawable.ic_monthly_statement) {
                    setTint(
                        ContextCompat.getColor(
                            context,
                            R.color.accountStatementIconColour,
                        ),
                    )
                },
            R.string.tax_statement.resourceString(context) to
                DeferredDrawable.Resource(R.drawable.ic_tax_statement) {
                    setTint(
                        ContextCompat.getColor(
                            context,
                            R.color.accountStatementIconColour,
                        ),
                    )
                },
            R.string.other.resourceString(context) to
                DeferredDrawable.Resource(R.drawable.ic_other) {
                    setTint(ContextCompat.getColor(context, R.color.accountStatementIconColour))
                },
            "a" to DeferredDrawable.Resource(R.drawable.ic_monthly_statement),
        )
    }

    private fun getDefaultBackgroundColour(): DeferredColor =
        DeferredColor.Resource(R.color.accountStatementCategoryBackgroundColour)
}
