plugins {
    alias(libs.plugins.ksp1)
    `kotlin-conventions`
    `java-test-fixtures`
}

tasks {
    test {
        useJUnitPlatform()
    }
}

dependencies {
    ksp(projects.kspBuilderGenProcessor)
    implementation(projects.kspBuilderGenAnnotations)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.strikt.core)

    testFixturesCompileOnly(libs.strikt.core)
    testFixturesCompileOnly(libs.junit.jupiter)

    testRuntimeOnly(libs.junit.launcher)
    testRuntimeOnly(kotlin("reflect"))
}