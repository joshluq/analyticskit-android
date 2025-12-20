pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
    }
    versionCatalogs {
        val projectConfig = loadProjectConfig(rootProject.projectDir)
        val catalogVersion = projectConfig.getProperty("catalogVersion", "es.joshluq.kit.pluginkit:catalog:0.0.1-SNAPSHOT")
        create("libs") {
            from(catalogVersion)
        }
    }
}

rootProject.name = "analyticskit-android"
include(":library")
include(":showcase")
project(":library").name = "analyticskit"


fun loadProjectConfig(projectDir: File): java.util.Properties {
    val properties = java.util.Properties()
    val propertiesFile = File(projectDir, "project-config.properties")
    if (propertiesFile.exists()) {
        propertiesFile.inputStream().use { properties.load(it) }
    }
    return properties
}