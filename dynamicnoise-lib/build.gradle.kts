import com.vanniktech.maven.publish.SonatypeHost
plugins {
    id("java")
    id("java-library")
    `maven-publish`
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "io.github.zapolyarnydev"
version = "1.0.1"

tasks.test {
    useJUnitPlatform()
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:16.0.1")
    annotationProcessor("org.jetbrains:annotations:16.0.1")
    implementation(files("../dynamicnoise-generation/build/libs/dynamicnoise-generation-1.0.1.jar"))
}

tasks.compileJava{
    dependsOn(":dynamicnoise-generation:jar")
}

tasks.jar {
    from(project(":dynamicnoise-generation").tasks.named("jar").map { zipTree(it.outputs.files.singleFile) }) {
        exclude("module-info.class")
    }
    manifest {
        attributes["Automatic-Module-Name"] = "DynamicNoise.dynamicnoise.lib"
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    coordinates("io.github.zapolyarnydev", "dynamicnoise-lib", "1.0.1")

    pom {
        name.set("DynamicNoise")
        description.set("A lightweight and efficient Java library for creating procedural noise.")
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
    signAllPublications()
}

//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            from(components["java"])
//
//            pom {
//                name.set("DynamicNoise")
//                description.set("A lightweight and efficient Java library for creating procedural noise.")
//                url.set("https://github.com/zapolyarnydev/DynamicNoise")
//
//                licenses {
//                    license {
//                        name.set("MIT License")
//                        url.set("https://opensource.org/licenses/MIT")
//                    }
//                }
//
//                developers {
//                    developer {
//                        id.set("zapolyarnydev")
//                        name.set("Pavel Arkhipov")
//                        email.set("zapolyarnynorth@google.com")
//                    }
//                }
//
//                scm {
//                    connection.set("scm:git:git://github.com/zapolyarnydev/DynamicNoise.git")
//                    developerConnection.set("scm:git:ssh://github.com/zapolyarnydev/DynamicNoise.git")
//                    url.set("https://github.com/zapolyarnydev/DynamicNoise")
//                }
//            }
//        }
//    }
//
//    repositories {
//        maven {
//            name = "Sonatype"
//            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
//            credentials {
//                username = project.findProperty("mavenCentralUsername") as String? ?: ""
//                password = project.findProperty("mavenCentralPassword") as String? ?: ""
//            }
//        }
//    }
//}


task("printCredentials") {
    doLast {
        val username = findProperty("mavenCentralUsername") as String? ?: System.getenv("OSSRH_USERNAME")
        val password = findProperty("mavenCentralPassword") as String? ?: System.getenv("OSSRH_PASSWORD")

        println("Username: $username")
        println("Password: $password")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
//    withJavadocJar()
//    withSourcesJar()
}
