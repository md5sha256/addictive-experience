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
    testRuntimeOnly("io.leangen.geantyref:geantyref:1.3.4")

    testImplementation(libs.adventureApi)
    testImplementation(libs.spigotutils)
    testCompileOnly(libs.junitApi)
    testRuntimeOnly(libs.junitEngine)
    testImplementation(libs.mockbukkit)
    testImplementation(testFixtures(projects.api))
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

