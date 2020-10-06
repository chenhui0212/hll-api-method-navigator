package com.github.max.api.navigator

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScope
import org.apache.commons.lang.ArrayUtils
import org.apache.commons.lang.StringUtils
import java.util.*

object JavaUtils {

    @JvmStatic
    fun findMethod(project: Project, clazzName: String?, methodName: String?): Optional<PsiMethod> {
        if (StringUtils.isBlank(clazzName) && StringUtils.isBlank(methodName)) {
            return Optional.empty()
        }
        val clazz = findClazz(project, clazzName!!)
        if (clazz.isPresent) {
            val methods = clazz.get().findMethodsByName(methodName, true)
            return if (ArrayUtils.isEmpty(methods)) Optional.empty() else Optional.of(methods[0])
        }
        return Optional.empty()
    }

    @JvmStatic
    fun findClazz(project: Project, clazzName: String): Optional<PsiClass> {
        return Optional.ofNullable(JavaPsiFacade.getInstance(project)
                .findClass(clazzName, GlobalSearchScope.allScope(project)))
    }
}