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
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ArchTemplate"
include(":app")
include(":core:data")
include(":core:designsystem")
include(":feature:home")
include(":core:common")
include(":core:domain")
include(":core:model")
include(":core:datastore")
include(":core:ui")
include(":core:network")
include(":core:database")
include(":feature:login")
include(":feature:work")
include(":feature:profile")
include(":feature:setting")
include(":feature:support")
include(":feature:tododetail")
include(":feature:search")
