
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    `maven-publish`
    signing
    id("com.vanniktech.maven.publish").version("0.28.0")
}

group = "io.github.vivek656"
version = "0.0.1"


java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // https://mvnrepository.com/artifact/com.fasterxml.jackson.module/jackson-module-kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    dependsOn(tasks.withType<AbstractCompile>())
    dependsOn(tasks.named("kotlinSourcesJar"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

mavenPublishing {
    coordinates(
        group as String,
        name,
        version as String
    )

    pom {
        name = "kotlin-json-dsl"
        description = "DSL to write Json in Kotlin with syntax as close to regular Json"
        url = "https://github.com/vivek656/kotlin-json-dsl"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "vivek656"
                name = "Vivek Singh Latwal"
                email = "viveklatwal656@gmail.com"
            }
        }
        scm {
            url = "https://github.com/vivek656/kotlin-json-dsl"
            connection = "scm:git:https://github.com/vivek656/kotlin-json-dsl.git"
            developerConnection = "scm:git:git@github.com:vivek656/kotlin-json-dsl.git"
        }

    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL , true)

    signAllPublications()

}
