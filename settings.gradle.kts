buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("gradle.plugin.net.vivin:gradle-semantic-build-versioning:4.0.0")
    }
}

apply(plugin = "net.vivin.gradle-semantic-build-versioning")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "ksp-builder-gen"

include(
    ":ksp-builder-gen-annotations",
    ":ksp-builder-gen-processor",
    ":integration-tests:ksp1",
    ":integration-tests:ksp2",
)
