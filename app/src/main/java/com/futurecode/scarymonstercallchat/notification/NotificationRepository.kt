package com.futurecode.scarymonstercallchat.notification

import com.futurecode.scarymonstercallchat.utils.getNotificationListFromPrefs

object NotificationRepository {
    val notifications = getNotificationListFromPrefs()

}