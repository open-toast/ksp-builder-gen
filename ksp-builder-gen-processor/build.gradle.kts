plugins {
    `kotlin-conventions`
    `publishing-conventions`
    alias(libs.plugins.ksp2)
    alias(libs.plugins.spotless)
}

spotless {
    kotlin {
        ktlint()
    }
}

dependencies {
    compileOnly(libs.ksp.api)

    implementation(projects.kspBuilderGenAnnotations)
    implementation(libs.autoservice.annotations)
    implementation(libs.kotlinPoet)
    implementation(libs.kotlinPoetKsp)

    ksp(libs.autoservice.ksp)
}