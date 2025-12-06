import org.jetbrains.changelog.ChangelogPluginExtension

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.10.5"
    id("org.jetbrains.changelog") version "2.2.0"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

group = "ru.qa.megagenerator"

// Настраиваем changelog-плагин
val changelogExtension = extensions.getByType(ChangelogPluginExtension::class)
changelogExtension.apply {
    path.set(file("CHANGELOG.md").path)
    itemPrefix.set("-")
    groups.set(emptyList())
}

// Чекаем последнюю версию из CHANGELOG.md
val pluginVersion = providers.provider {
    val regex = Regex("""^## \[(\d+\.\d+\.\d+)] - \d{4}-\d{2}-\d{2}""")
    file("CHANGELOG.md")
        .readLines()
        .firstNotNullOfOrNull { regex.matchEntire(it)?.groupValues?.get(1) }
        ?: error("No valid changelog version found")
}

version = pluginVersion.get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2025.2")
    }
}

intellijPlatform {
    pluginConfiguration {
        id = "MG AI Assistent"
        version = pluginVersion.get()

//        ideaVersion {
//            sinceBuild = "252.*"
//            untilBuild = "252.*"
//        }
    }
}

configurations {
    create("externalLibs")
}

dependencies {
    // externalLibs("...") — если потребуется
}

tasks {

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.release.set(17)
    }

    patchPluginXml {
        sinceBuild.set("252")    // IC-252.23892.409 → compatible
        untilBuild.set("252.*")
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
        jvmArgs = listOf("-Dfile.encoding=UTF-8")
    }
}