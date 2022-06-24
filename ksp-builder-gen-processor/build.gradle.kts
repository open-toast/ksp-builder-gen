plugins {
    `kotlin-conventions`
    `publishing-conventions`
    alias(libs.plugins.ksp)
}

dependencies {
    ksp(libs.autoservice.ksp)
    implementation(projects.kspBuilderGenAnnotations)
    implementation(kotlin("stdlib"))
    implementation(libs.ksp.api)
    implementation(libs.autoservice.annotations)
    implementation(libs.kotlinPoet)
    implementation(libs.kotlinPoetKsp)
}