val gradleContainerVersion = "8.5.0-jdk17"

job("Publish") {
    startOn {
        gitPush {
            anyBranchMatching {
                +"*main"
            }
        }
    }

    container(displayName = "Publish on success", image = "gradle:$gradleContainerVersion") {
        env["ADI_MAVEN_USERNAME"] = "{{ project:ADI_MAVEN_USERNAME }}"
        env["ADI_MAVEN_PASSWORD"] = "{{ project:ADI_MAVEN_PASSWORD }}"

        kotlinScript { api ->
            api.gradlew("check")
            api.gradlew("test")
            api.gradlew("build")
            // no publishing for now
            // api.gradlew("publish")
        }
    }
}

job("Build and tests") {

    startOn {
        gitPush {
            anyBranch()
        }
    }

    container(displayName = "Build and test", image = "gradle:$gradleContainerVersion") {
        kotlinScript { api ->
            api.gradlew("check")
            api.gradlew("test", "-V", "-S")
            api.gradlew("build")
        }
    }
}
