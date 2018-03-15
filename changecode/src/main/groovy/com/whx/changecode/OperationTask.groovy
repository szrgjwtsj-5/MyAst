package com.whx.changecode

import com.whx.changecode.utils.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class OperationTask extends DefaultTask {

    @TaskAction
    void opFile() {

        String srcPath = OperationExtension.getConfig(project).srcDir

        FileUtils.getFileList(srcPath, ".java").forEach { file ->
            println((new ChangeIf(file)).changeIf())
        }
    }
}