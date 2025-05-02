plugins {
    `kotlin-conventions`
    `publishing-conventions`
    alias(libs.plugins.spotless)
}

spotless {
    kotlin {
        ktlint()
    }
}
