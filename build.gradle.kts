plugins {
    java
    `maven-publish`
    id ("io.github.goooler.shadow") version "8.1.8"
}

group = "fr.The__Glacier"
version = "1.0.0-SNAPSHOT-raw"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/" )
    maven("https://https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    // implementation("lien") -> inclu le lien dans le projet
    // compileOnly("lien") -> ajoute un projet en dépendance (pas inclu dans le jar final)
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.3")
    implementation("de.tr7zw:item-nbt-api:2.14.1")


    compileOnly("org.projectlombok:lombok:1.18.30")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    annotationProcessor("org.projectlombok:lombok:1.18.30")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
tasks {
    jar {
        dependsOn(shadowJar)
        enabled = true
    }

    shadowJar {
        archiveBaseName.set("G-Core")
        archiveClassifier.set("")
        archiveVersion.set(version.toString().replace("-raw", ""))

        relocate("com.fasterxml", "fr.the__glacier.dependencies.fasterxml")
        relocate("de.tr7zw", "fr.the__glacier.dependencies.tr7zw")
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}