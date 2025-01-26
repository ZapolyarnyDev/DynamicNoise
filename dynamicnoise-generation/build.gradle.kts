plugins {
    id("java")
}

group = "me.zapolyarny.dynamicnoise"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.jar {
    manifest {
        attributes["Automatic-Module-Name"] = "DynamicNoise.dynamicnoise.generation"
    }
}

tasks.test {
    useJUnitPlatform()
}