import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

var relocationBase = "com.github.md5sha256.addictiveexperience.shaded"

repositories {
    maven {
        name = "sonatype-snapshots"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
        content {
            includeModule("com.github.MilkBowl", "VaultAPI")
        }
    }
    maven {
        name = "minebench-repo"
        url = uri("https://repo.minebench.de/")
        content {
            includeModule("de.themoep", "inventorygui")
        }
    }
    maven {
        name = "incendo-repo"
        url = uri("https://repo.incendo.org/content/repositories/snapshots")
    }
}

dependencies {

    implementation(projects.api)

    implementation(libs.adventureTextSerializerPlain) {
        exclude("net.kyori", "adventure-api")
    }

    implementation(libs.paperlib)
    implementation(libs.configurateGson)
    implementation(libs.configurateYaml)
    implementation("de.themoep:inventorygui:1.5.1-SNAPSHOT")
    implementation(libs.spigotutils)
    implementation(libs.cloud)
    implementation(libs.cloudMinecraftExtras)
    implementation(libs.cloudAnnotations)

    compileOnly(libs.paper)
    compileOnly(libs.jetbrainsAnnotations)


    implementation(libs.guice)
    implementation(libs.assistedInject) {
        exclude("com.google.inject", "guice")
    }

    compileOnly(libs.vault)

    testCompileOnly(libs.junitApi)
    testRuntimeOnly(libs.junitEngine)
    testImplementation(libs.mockbukkit)
    testImplementation(testFixtures(projects.api))
}

tasks {

    withType(ProcessResources::class.java) {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    withType(Jar::class) {
        archiveBaseName.set("addictive-experience")
        archiveClassifier.set("implementation")
        dependsOn(processResources)
    }

    withType(ShadowJar::class) {
        archiveBaseName.set("addictive-experience")
        archiveClassifier.set("")

        dependencies {
            exclude(dependency(libs.gson.orNull))
            exclude(dependency(libs.guava.orNull))
            exclude(dependency(libs.errorproneAnnotation.orNull))
            exclude(dependency(libs.adventureApi.orNull))
            exclude(dependency(libs.adventureKey.orNull))
            exclude(dependency(libs.adventureBom.orNull))
            exclude(dependency(libs.examinationApi.orNull))
            exclude(dependency(libs.examinationString.orNull))
        }

        relocate("com.github.md5sha256.spigotutils", "${relocationBase}.spigotutils")
        relocate("com.google.inject", "${relocationBase}.inject")
        relocate("cloud.commandframework", "${relocationBase}.cloud")
        relocate("de.themoep.inventorygui", "${relocationBase}.inventorygui")
        relocate("io.leangen.geantyref", "${relocationBase}.geantyref")
        relocate("io.papermc.lib", "${relocationBase}.paperlib")
        relocate("net.kyori.adventure.text.serializer.plain", "${relocationBase}.text.serializer.plain")
        relocate("org.aopalliance", "${relocationBase}.aopalliance")
        relocate("org.spongepowered.configurate", "${relocationBase}.configurate")
        relocate("org.checkerframework", "${relocationBase}.checkerframework")
        relocate("org.yaml.snakeyaml", "${relocationBase}.snakeyaml")
        relocate("javax.inject", "${relocationBase}.javax.inject")
        relocate("javax.annotation", "${relocationBase}.javax.annotation")

        minimize()
    }

    withType(Assemble::class) {
        dependsOn(shadowJar)
    }

    withType(Test::class) {
        useJUnitPlatform()
    }

}