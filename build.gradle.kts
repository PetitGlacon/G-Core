plugins {
    id("java")
    id("maven-publish")
    id ("io.github.goooler.shadow") version "8.1.8"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
}

group = "fr.The__Glacier"
version = "1.0.0-SNAPSHOT-raw"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/" )
    maven("https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
    // implementation("lien") -> inclu le lien dans le projet
    // compileOnly("lien") -> ajoute un projet en dépendance (pas inclu dans le jar final)
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.3")
    implementation("de.tr7zw:item-nbt-api:2.14.1")


    compileOnly("org.projectlombok:lombok:1.18.30")
    // compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    annotationProcessor("org.projectlombok:lombok:1.18.30")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
tasks {
    jar {
        enabled = false
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
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            // from(components["java"])

            groupId = "fr.The__Glacier"
            artifactId = "G-Core"
            version = "1.0.0-SNAPSHOT"  // Version sans le suffixe '-raw'

            // Associe la publication à l'artefact généré par shadowJar
            artifact(tasks["shadowJar"]) {
                classifier = ""  // Pas de classifier pour éviter des artefacts multiples
            }
        }
    }

    repositories {
        mavenLocal()  // Publie l'artefact dans ton dépôt Maven local (~/.m2/repository)
    }
}