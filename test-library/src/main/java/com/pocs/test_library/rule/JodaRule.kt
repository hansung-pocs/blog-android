package com.pocs.test_library.rule

import org.joda.time.DateTimeZone
import org.joda.time.tz.Provider
import org.joda.time.tz.UTCProvider
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class JodaRule @JvmOverloads constructor(
    private val provider: Provider = UTCProvider()
) : TestRule {

    override fun apply(base: Statement, description: Description): Statement {

        return object : Statement() {
            override fun evaluate() {
                DateTimeZone.setProvider(provider)
                base.evaluate()
            }
        }
    }
}
