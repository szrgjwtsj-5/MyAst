package com.whx.changecode

import com.android.build.gradle.api.BaseVariant
import com.whx.changecode.utils.GradleUtils
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState

class OperationPlugin implements Plugin<Project> {

    private static final String EXT_NAME = "operation"

    @Override
    void apply(Project project) {

        createExtension(project)

        project.afterEvaluate {
            println("================= start operation ====================")
            execTask(project, GradleUtils.getAndroidVariants(project))
        }
    }

    static void createExtension(Project project) {
        project.extensions.create(EXT_NAME, OperationExtension)
    }

    static void execTask(Project project, DomainObjectCollection<BaseVariant> variants) {

        variants.all { variant ->
            def variantName = variant.name.capitalize()

            Task javaCompileTask = project.tasks.findByName(GradleUtils.getJavaCompileTaskName(project, variant))
            OperationTask opTask = project.tasks.create("operation${variantName}File", OperationTask)

            opTask.dependsOn javaCompileTask.taskDependencies.getDependencies(javaCompileTask)
            javaCompileTask.dependsOn opTask
        }

        project.gradle.taskGraph.afterTask { Task task, TaskState state ->
            if (state.failure) {
                if (task instanceof OperationTask) {
                    println("operation task is failed, what the fuck")
                }
            } else {
                if (task instanceof OperationTask) {
                    println("================= end operation ====================")
                }
            }
        }
    }
}