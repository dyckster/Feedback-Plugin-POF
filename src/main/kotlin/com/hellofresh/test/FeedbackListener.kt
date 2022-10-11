package com.hellofresh.test

import com.intellij.ide.browsers.BrowserLauncher
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.util.Consumer
import java.util.concurrent.TimeUnit

class FeedbackListener(private val components: List<ComponentInfo>) : DocumentListener {

    var messageLastShown = 0L
    var messageDisabled = false

    override fun documentChanged(event: DocumentEvent) {
        super.documentChanged(event)
        val designSystemComponent = components.find { event.document.charsSequence.contains(it.componentName) }
        val notificationCanBeShown = System.currentTimeMillis() - messageLastShown > TimeUnit.SECONDS.toMillis(30)
        if (designSystemComponent != null && notificationCanBeShown && !messageDisabled) {
            val notification = Notification(
                    "feedback",
                    "Help us improve the Design System",
                    "How do you like \'${designSystemComponent.componentName}\' API? Please, tell us what you think.",
                    NotificationType.INFORMATION
            )
            notification.addAction(NotificationAction.create("Leave Feedback", Consumer {
                BrowserLauncher.instance.open(designSystemComponent.feedbackLink)
                notification.hideBalloon()
            }))
            notification.addAction(NotificationAction.create("Don't Ask Again", Consumer {
                messageDisabled = true
                notification.hideBalloon()
            }))

            Notifications.Bus.notify(notification)
            messageLastShown = System.currentTimeMillis()
        }
    }
}