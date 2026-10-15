plugins {
    id("maven-publish")
}

publishing {
    publications {
        register<MavenPublication>("default") {
            artifactId = project.name
            groupId = project.group.toString()
            version = project.version.toString()

            from(components["java"])
        }
    }
}
