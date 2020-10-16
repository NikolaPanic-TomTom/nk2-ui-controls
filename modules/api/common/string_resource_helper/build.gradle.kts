
dependencies {
implementation(com.tomtom.nk2.buildsrc.environment.Libraries.Android.APPCOMPAT)

    implementation(project(":core_common_math"))
    implementation(project(":core_common_geography"))

    implementation(com.tomtom.nk2.buildsrc.environment.Libraries.Util.JSCIENCE)

    testImplementation(project(":tools_testing_unit"))
}
