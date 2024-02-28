val gradleContainerVersion = "8.5.0-jdk17"

job("Build and tests") {

    startOn {
        gitPush {
            anyBranch()
        }
    }

    container(displayName = "Build and test", image = "gradle:$gradleContainerVersion") {
        kotlinScript { api ->
            api.gradlew("check")
            api.gradlew("test")
            api.gradlew("build")
        }
    }
}
