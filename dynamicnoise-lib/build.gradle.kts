plugins {
    id("java")
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

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


