package com.github.max.api.navigator.reference

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.*
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.PlatformIcons
import org.apache.commons.lang.StringUtils
import java.util.*

class XmlApiReference(element: XmlAttributeValue, private val psiClass: PsiClass)
    : PsiReferenceBase<XmlAttributeValue?>(element) {

    override fun resolve(): PsiElement? {
        if (!StringUtils.isEmpty(myElement!!.value)) {
            val methods = psiClass.findMethodsByName(myElement.value, false)
            return if (methods.isEmpty()) null else methods[0]
        }
        return null
    }

    override fun getVariants(): Array<Any> {
        return Arrays.stream(psiClass.methods)
                .map { method: PsiMethod ->
                    val signs = Arrays.stream(method.parameterList.parameters)
                            .map { obj: PsiParameter -> obj.type }
                            .map { obj: PsiType -> obj.presentableText }
                            .toArray()
                    val sign = StringUtils.join(signs, ", ")
                    LookupElementBuilder.create(method)
                            .withIcon(PlatformIcons.METHOD_ICON)
                            .withTailText("($sign)")
                }.toArray()
    }
}