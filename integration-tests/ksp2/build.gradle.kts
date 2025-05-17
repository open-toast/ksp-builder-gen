plugins {
    alias(libs.plugins.ksp2)
    `kotlin-conventions`
}

tasks {
    test {
        useJUnitPlatform()
    }
}

sourceSets {
    main {
        kotlin.srcDir("../ksp1/src/main/kotlin")
    }
}

dependencies {
    ksp(projects.kspBuilderGenProcessor)
    implementation(projects.kspBuilderGenAnnotations)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.strikt.core)

    testImplementation(testFixtures(projects.integrationTests.ksp1))

    testRuntimeOnly(libs.junit.launcher)
    testRuntimeOnly(kotlin("reflect"))
}