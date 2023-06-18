plugins {
    java
}

group = "nz.ac.aucklanduni.softeng700.d2-lang"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Guava - Google collection of random utilities - mainly used for graph DSA's (https://github.com/google/guava)
    implementation("com.google.guava:guava:32.0.0-jre")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}