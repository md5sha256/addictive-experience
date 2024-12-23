plugins {
    `java-test-fixtures`
}

dependencies {
    compileOnlyApi(libs.paper)
    compileOnlyApi(libs.jetbrainsAnnotations)
    compileOnlyApi("io.leangen.geantyref:geantyref:1.3.13")
    compileOnlyApi(libs.spigotutils)

    implementation(libs.paperlib)

    testRuntimeOnly("commons-lang:commons-lang:2.6")
    testRuntimeOnly("io.leangen.geantyref:geantyref:1.3.13")

    testImplementation(libs.adventureApi)
    testImplementation(libs.spigotutils)
    testImplementation(platform(libs.junitBom))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.mockbukkit)
    testImplementation(testFixtures(projects.api))
    testImplementation(libs.paper)
}

project.extensions.getByType(JavaPluginExtension::class).withSourcesJar()


tasks {

    jar {
        archiveBaseName.set("addictive-experience")
        archiveClassifier.set("api")
    }

    test {
        useJUnitPlatform()
    }

}

