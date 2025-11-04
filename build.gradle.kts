import org.jetbrains.changelog.ChangelogPluginExtension

plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.2"
    id("org.jetbrains.changelog") version "2.2.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

group = "ru.qa.megagenerator"
version = "0.0.0"
// Настраиваем changelog-плагин
val changelogExtension = extensions.getByType(ChangelogPluginExtension::class)
changelogExtension.apply {
    path.set(file("CHANGELOG.md").path)
    itemPrefix.set("-")
    groups.set(emptyList())
}

// Ручной парсинг CHANGELOG.md — получаем последнюю версию
val pluginVersion = providers.provider {
    val changelogFile = file("CHANGELOG.md")
    val lines = changelogFile.readLines()
    val regex = Regex("""^## \[(\d+\.\d+\.\d+)] - \d{4}-\d{2}-\d{2}""")

    val version = lines
        .firstNotNullOfOrNull { line -> regex.matchEntire(line)?.groupValues?.get(1) }
        ?: error("No valid changelog version found")

    version
}

version = pluginVersion.get()

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    pluginName.set("ai-assistant")
    version.set("2024.1")
    type.set("IC") // Target IDE Platform
    plugins.set(listOf("com.intellij.java"))
}

configurations {create("externalLibs")}

dependencies {

}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

tasks {

    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        version.set(pluginVersion)
        sinceBuild.set("241")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

    runIde {
        jvmArgs = listOf(
            "-Dfile.encoding=UTF-8"
        )
    }
}