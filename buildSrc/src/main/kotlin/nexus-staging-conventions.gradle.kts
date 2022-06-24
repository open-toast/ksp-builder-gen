import org.gradle.kotlin.dsl.configure

plugins {
    id("io.codearte.nexus-staging")
}

if (isRelease()) {
    nexusStaging {
        username = Remote.username
        password = Remote.password
        packageGroup = "com.toasttab"
        numberOfRetries = 50
    }
}
