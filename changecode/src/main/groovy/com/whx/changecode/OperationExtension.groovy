package com.whx.changecode

import org.gradle.api.Project
import org.gradle.api.tasks.Input

class OperationExtension {

    @Input
    String srcDir

    static OperationExtension getConfig(Project project) {
        OperationExtension config = project.getExtensions().findByType(OperationExtension.class)

        return config == null ? new OperationExtension() : config
    }
}