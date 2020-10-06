package com.github.max.api.navigator.reference

import com.github.max.api.navigator.JavaUtils
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.xml.XmlAttribute
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.util.ProcessingContext
import org.apache.commons.lang.StringUtils

class XmlApiReferenceContributor : PsiReferenceContributor() {

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(XmlAttributeValue::class.java),
                object : PsiReferenceProvider() {

                    override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
                        val attrValue = element as XmlAttributeValue

                        // 获取当前 XmlAttribute
                        if (attrValue.parent !is XmlAttribute) {
                            return PsiReference.EMPTY_ARRAY
                        }
                        val currentAttr = attrValue.parent as XmlAttribute

                        // 判断当前属性名称
                        if (currentAttr.name != "method") {
                            return PsiReference.EMPTY_ARRAY
                        }

                        // 获取定义服务的属性
                        val serviceAttr = currentAttr.parent.getAttribute("service")
                        if (serviceAttr == null || StringUtils.isEmpty(serviceAttr.value)) {
                            return PsiReference.EMPTY_ARRAY
                        }

                        val psiClass = JavaUtils.findClazz(currentAttr.project, serviceAttr.value!!)
                        return if (psiClass.isPresent) {
                            arrayOf(XmlApiReference(attrValue, psiClass.get()))
                        } else PsiReference.EMPTY_ARRAY
                    }
                }
        )
    }
}