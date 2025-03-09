plugins {
    java
    eclipse
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
}

group = "fr.the__glacier"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/")
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.3")


    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.30")

    annotationProcessor("org.projectlombok:lombok:1.18.30")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

val targetJavaVersion = 17
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(targetJavaVersion)
}

val templateSource = file("src/main/templates")
val templateDest = layout.buildDirectory.dir("generated/sources/templates")

val generateTemplates by tasks.registering(Copy::class) {
    val props = mapOf("version" to project.version)
    inputs.properties(props)
    from(templateSource)
    into(templateDest)
    expand(props)
}

sourceSets.main {
    java.srcDir(generateTemplates.map { it.outputs })
}

eclipse.synchronizationTasks(generateTemplates)