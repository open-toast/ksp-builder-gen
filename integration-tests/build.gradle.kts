plugins {
    alias(libs.plugins.ksp)
    `kotlin-conventions`
    idea
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    ksp(projects.kspBuilderGenProcessor)
    implementation(projects.kspBuilderGenAnnotations)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)

    testRuntimeOnly(libs.junit.launcher)
}