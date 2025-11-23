package com.github.setial.intellijjavadocs.generator.impl;

import com.github.setial.intellijjavadocs.configuration.JavaDocConfiguration;
import com.github.setial.intellijjavadocs.model.JavaDoc;
import com.github.setial.intellijjavadocs.model.settings.GeneralSettings;
import com.github.setial.intellijjavadocs.model.settings.Level;
import com.github.setial.intellijjavadocs.model.settings.Mode;
import com.github.setial.intellijjavadocs.model.settings.Visibility;
import com.google.common.collect.Sets;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase4;
import org.junit.Before;
import org.junit.Test;

import static com.intellij.openapi.application.ActionsKt.runReadAction;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MethodJavaDocGeneratorTest extends LightJavaCodeInsightFixtureTestCase4 {

    private ClassJavaDocGenerator classJavaDocGenerator;
    private MethodJavaDocGenerator methodJavaDocGenerator;
    private PsiElementFactory elementFactory;
    private GeneralSettings generalSettings;
    private PsiFile psiFile;
    private PsiMethod publicGetMethod;
    private PsiMethod protectedGetMethod;
    private PsiClass publicGetClass;

    @Before
    public void setUp() {
        classJavaDocGenerator = new ClassJavaDocGenerator(getProject());
        methodJavaDocGenerator = new MethodJavaDocGenerator(getProject());
        elementFactory = PsiElementFactory.getInstance(getProject());
        JavaDocConfiguration settings = getProject().getService(JavaDocConfiguration.class);
        generalSettings = settings.getConfiguration().getGeneralSettings();
        generalSettings.setMode(Mode.REPLACE);
        PsiFileFactory fileFactory = PsiFileFactory.getInstance(getProject());
        psiFile = runReadAction(() -> fileFactory.createFileFromText(
                "Test.java",
                JavaFileType.INSTANCE, "public class Test {}")
        );
        publicGetMethod = runReadAction(() -> elementFactory.createMethodFromText("public String getParam(String param) {}", psiFile));
        protectedGetMethod = runReadAction(() -> elementFactory.createMethodFromText("protected String getParam(String param) {}", psiFile));
        publicGetClass = runReadAction(() -> elementFactory.createClassFromText("public static class Test3 {}", psiFile));
    }

    @Test
    public void testGenerateClassJavaDoc() {
        /*
        generalSettings.setLevels(): 设置需要生成文档的代码层级
Level.FIELD: 为类的字段生成文档
Level.METHOD: 为类的方法生成文档
Level.TYPE: 为类和接口等类型生成文档
generalSettings.setVisibilities(): 设置需要处理的代码可见性级别
Visibility.PUBLIC: 处理public级别的代码元素
Visibility.PROTECTED: 处理protected级别的代码元素
Visibility.DEFAULT: 处理包私有(默认)级别的代码元素
Visibility.PRIVATE: 处理private级别的代码元素
        * */
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.METHOD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED, Visibility.PUBLIC));

        JavaDoc javaDoc = runReadAction(() -> classJavaDocGenerator.generateJavaDoc(publicGetClass));
        assertThat(javaDoc, notNullValue());
    }

    @Test
    public void testGenerateJavaDoc() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.METHOD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED, Visibility.PUBLIC));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(publicGetMethod));
        assertThat(javaDoc, notNullValue());
    }

    @Test
    public void testGenerateJavaDoc_NoPublicVisibility() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.METHOD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(publicGetMethod));
        assertThat(javaDoc, nullValue());
    }

    @Test
    public void testGenerateJavaDoc_NoMethodLevel() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED, Visibility.PUBLIC));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(publicGetMethod));
        assertThat(javaDoc, nullValue());
    }

    @Test
    public void testGenerateJavaDoc_NoMethodLevelAndPublicVisibility() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(publicGetMethod));
        assertThat(javaDoc, nullValue());
    }

    @Test
    public void testGenerateJavaDocProtected() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE, Level.METHOD));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE, Visibility.PROTECTED));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(protectedGetMethod));
        assertThat(javaDoc, notNullValue());
    }

    @Test
    public void testGenerateJavaDocProtected_NoProtectedVisibility() {
        generalSettings.setLevels(
                Sets.immutableEnumSet(Level.FIELD, Level.TYPE, Level.METHOD));
        generalSettings.setVisibilities(
                Sets.immutableEnumSet(Visibility.DEFAULT, Visibility.PRIVATE));

        JavaDoc javaDoc = runReadAction(() -> methodJavaDocGenerator.generateJavaDoc(protectedGetMethod));
        assertThat(javaDoc, nullValue());
    }

    private Project getProject() {
        return getFixture().getProject();
    }
}