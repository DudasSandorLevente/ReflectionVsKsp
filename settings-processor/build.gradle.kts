plugins {
    id("org.jetbrains.kotlin.jvm")
}
kotlin {
    jvmToolchain(17)
}
dependencies {
    implementation(project(":settings-annotations"))
    implementation("com.google.devtools.ksp:symbol-processing-api:2.3.10")
}