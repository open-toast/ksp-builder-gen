import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType

plugins {
    id("maven-publish")
    id("signing")
}

group = rootProject.group
version = rootProject.version

configure<JavaPluginExtension> {
    withSourcesJar()
    withJavadocJar()
}

tasks.named<Jar>("javadocJar") {
    from("$rootDir/README.md")
    archiveClassifier.set("javadoc")
}

publishing {
    publications {
        create<MavenPublication>("main") {
            from(components["java"])

            artifactId = project.name
            version = project.version.toString()
            groupId = project.group.toString()

            pom {
                name.set(ProjectInfo.name)
                description.set(ProjectInfo.description)
                url.set(ProjectInfo.url)
                scm {
                    url.set(ProjectInfo.url)
                }
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("Toast")
                        name.set("Toast Open Source")
                        email.set("opensource@toasttab.com")
                    }
                }
            }
        }
    }
}

@Suppress("IMPLICIT_CAST_TO_ANY")
if (isRelease() && Pgp.key != null) {
    signing {
        useInMemoryPgpKeys(Pgp.key, Pgp.password)

        project.publishing.publications.withType<MavenPublication> {
            sign(this)
        }
    }
}
