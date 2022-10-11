package com.hellofresh.test

import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.editor.EditorFactory

class FeedbackComponent : ApplicationComponent {

    override fun initComponent() {
        super.initComponent()
        EditorFactory.getInstance().eventMulticaster.addDocumentListener(FeedbackListener(
                listOf(ComponentInfo(componentName = "DesignSystemComponent", feedbackLink = "https://google.com"))
        ))
    }
}