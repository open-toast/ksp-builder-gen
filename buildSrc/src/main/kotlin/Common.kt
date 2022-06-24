import org.gradle.kotlin.dsl.provideDelegate

fun org.gradle.api.Project.isRelease() = !project.version.toString().endsWith("-SNAPSHOT")

object Pgp {
    val key by lazy {
        System.getenv("PGP_KEY")?.replace('$', '\n')
    }

    val password by lazy {
        System.getenv("PGP_PASSWORD")
    }
}

object Remote {
    val username by lazy {
        System.getenv("OSSRH_USERNAME")
    }

    val password by lazy {
        System.getenv("OSSRH_PASSWORD")
    }

    val url = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
}

object ProjectInfo {
    const val name = "KSP Builder Generator"
    const val url = "https://github.com/open-toast/ksp-builder-gen"
    const val description = "Kotlin builder generator using KSP"
}