pluginManagement {
    repositories {
        google {
            // Include specific groups of artifacts
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()  // Include Maven Central repository
        gradlePluginPortal()  // Include Gradle Plugin Portal repository
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()  // Include Google repository
        mavenCentral()  // Include Maven Central repository
    }
}

rootProject.name = "DoIt"  // Set the name of the root project
include(":app")  // Include the app module

