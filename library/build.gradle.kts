import com.android.build.api.dsl.LibraryExtension

plugins {
    alias(libs.plugins.pluginkit.android.library)
    alias(libs.plugins.pluginkit.quality)
    alias(libs.plugins.pluginkit.android.testing)
    alias(libs.plugins.pluginkit.android.publishing)
}

group = providers.gradleProperty("groupId").get()
version = providers.gradleProperty("libraryVersion").get()

configure<LibraryExtension> {
    namespace = "es.joshluq.analyticskit"
}

dependencies {
    implementation("es.joshluq.kit:foundationkit:1.1.0-SNAPSHOT")
}

pluginkitQuality {
    sonarHost = "https://sonarcloud.io"
    sonarProjectKey = "joshluq_monitorkit-android"
    koverExclusions = listOf(
        "**.showcase.*",
        "**.BuildConfig",
        "**.R",
        "**.R$*",
        "**.*_MembersInjector"
    )
}

androidPublishing {
    repoName = "GitHubPackages"
    repoUrl = "${providers.gradleProperty("repositoryUrl").get()}/${providers.gradleProperty("artifactId").get()}-android"
    repoUser = System.getenv("GITHUB_ACTOR")
    repoPassword = System.getenv("GITHUB_TOKEN")
    version = "${project.version}${project.findProperty("versionType")}"
    groupId = project.group.toString()
    artifactId = providers.gradleProperty("artifactId").get()
}
