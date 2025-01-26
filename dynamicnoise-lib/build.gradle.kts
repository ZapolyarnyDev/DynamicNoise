plugins {
    id("java")
    `java-library`
    `maven-publish`
    signing
}

group = "io.github.zapolyarnydev"
version = "1.0.0"


tasks.test {
    useJUnitPlatform()
}

repositories{
    mavenCentral()
}

dependencies{
    compileOnly("org.jetbrains:annotations:16.0.1")
    annotationProcessor("org.jetbrains:annotations:16.0.1")
    implementation(project(":dynamicnoise-generation"))
}

tasks.jar {
    from(project(":dynamicnoise-generation").tasks.named("jar").map { zipTree(it.outputs.files.singleFile) }) {
        exclude("module-info.class")
    }
    manifest {
        attributes["Automatic-Module-Name"] = "DynamicNoise.dynamicnoise.lib"
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            group = project.group as String
            artifactId = "dynamicnoise"
            version = project.version as String

            pom {
                name.set("DynamicNoise")
                description.set("A lightweight and efficient Java library for creating procedural noise, ideal for game development, simulation, and procedural content creation applications.")
                url.set("https://github.com/zapolyarnydev/DynamicNoise")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("zapolyarnydev")
                        name.set("Pavel Arkhipov")
                        email.set("zapolyarnynorth@google.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/zapolyarnydev/DynamicNoise.git")
                    developerConnection.set("scm:git:ssh://github.com/zapolyarnydev/DynamicNoise.git")
                    url.set("https://github.com/zapolyarnydev/DynamicNoise")
                }
            }
        }
    }

    repositories {
        maven {
            name = "ossrh"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                password = findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withJavadocJar()
    withSourcesJar()
}


