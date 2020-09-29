package com.github.chenhui0212.hllapimethodnavigator.services

import com.intellij.openapi.project.Project
import com.github.chenhui0212.hllapimethodnavigator.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
