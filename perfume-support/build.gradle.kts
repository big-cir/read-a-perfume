dependencies {
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:6.32.0")
    implementation("io.sentry:sentry-logback:6.32.0")
    implementation("org.springframework.boot:spring-boot-starter-aop:3.2.2")

    implementation ("org.springframework.boot:spring-boot-starter-actuator")
    implementation ("io.micrometer:micrometer-registry-prometheus")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
