package com.github.max.api.navigator.reference;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.PlatformIcons;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class XmlApiReference extends PsiReferenceBase<XmlAttributeValue> {

    private final PsiClass psiClass;

    public XmlApiReference(@NotNull XmlAttributeValue element, PsiClass psiClass) {
        super(element);
        this.psiClass = psiClass;
    }


    @Nullable
    @Override
    public PsiElement resolve() {
        if (!StringUtils.isEmpty(myElement.getValue())) {
            PsiMethod[] methods = psiClass.findMethodsByName(myElement.getValue(), false);
            return methods.length == 0 ? null : methods[0];
        }
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return Arrays.stream(psiClass.getMethods())
                .map(method -> {
                            String[] signs = Arrays.stream(method.getParameterList().getParameters())
                                    .map(PsiParameter::getType)
                                    .map(PsiType::getPresentableText)
                                    .toArray(String[]::new);
                            String sign = StringUtils.join(signs, ", ");
                            return LookupElementBuilder.create(method)
                                    .withIcon(PlatformIcons.METHOD_ICON)
                                    .withTailText("(" + sign + ")");
                        }
                ).toArray();
    }
}