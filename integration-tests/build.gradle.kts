plugins {
    alias(libs.plugins.ksp)
    `kotlin-conventions`
    idea
}

idea {
    module {
        sourceDirs = sourceDirs + file("build/generated/ksp/main/kotlin") // or tasks["kspKotlin"].destination
        testSourceDirs = testSourceDirs + file("build/generated/ksp/test/kotlin")
        generatedSourceDirs = generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

dependencies {
    ksp(projects.kspBuilderGenProcessor)
    implementation(projects.kspBuilderGenAnnotations)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
}