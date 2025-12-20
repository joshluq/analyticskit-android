plugins {
    alias(libs.plugins.pluginkit.android.library)
    alias(libs.plugins.pluginkit.android.testing)
}

group = "es.joshluq.analyticskit"

val projectConfig = loadProjectConfig(rootProject.projectDir)
version = projectConfig.getProperty("libraryVersion", "1.0.0")

android {
    namespace = "es.joshluq.analyticskit"
}

dependencies {

}
