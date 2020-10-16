rootProject.name = "nk2-ui-controls"
val modulesDir = file("${rootProject.projectDir}/modules/")
fileTree(modulesDir)
        .matching { include("*/*/*/build.gradle.kts") }
        .forEach { file ->
            val parentDir = file.parentFile
            val projectName = ":" + modulesDir.toPath().relativize(parentDir.toPath()).joinToString("_")
            include(projectName)
            project(projectName).projectDir = parentDir
        }

include(":app")
