package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.AppLanguage
import com.example.data.MandiNotification
import com.example.data.MandiStrings
import com.example.data.NotificationType
import com.example.data.UserRole
import com.example.ui.theme.MandiGoldSecondary
import com.example.ui.theme.MandiGreenPrimary

@Composable
fun NotificationCenterDialog(
    notifications: List<MandiNotification>,
    language: AppLanguage,
    currentRole: UserRole,
    onMarkAllAsRead: () -> Unit,
    onClearAll: () -> Unit,
    onNotificationClick: (MandiNotification) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredList = when (selectedFilter) {
        "FARMER" -> notifications.filter { it.targetRole == UserRole.FARMER || it.targetRole == UserRole.ALL_ACCESS }
        "BUYER" -> notifications.filter { it.targetRole == UserRole.BUYER || it.targetRole == UserRole.ALL_ACCESS }
        else -> notifications
    }

    val unreadCount = notifications.count { !it.isRead }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .height(680.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MandiGoldSecondary.copy(alpha = 0.2f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = Color(0xFFE65100),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = MandandiNotificationTitle(language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (unreadCount > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFFD32F2F)
                                    ) {
                                        Text(
                                            text = "$unreadCount",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = if (language == AppLanguage.HINDI) "रियल-टाइम नीलामी व बोली अलर्ट" else "Real-time Auction & Bid Alerts",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_notifications_dialog")) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Actions & Filter Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = selectedFilter == "ALL",
                            onClick = { selectedFilter = "ALL" },
                            label = { Text("All", fontSize = 12.sp) }
                        )
                        FilterChip(
                            selected = selectedFilter == "FARMER",
                            onClick = { selectedFilter = "FARMER" },
                            label = { Text(if (language == AppLanguage.HINDI) "किसान" else "Farmer", fontSize = 12.sp) }
                        )
                        FilterChip(
                            selected = selectedFilter == "BUYER",
                            onClick = { selectedFilter = "BUYER" },
                            label = { Text(if (language == AppLanguage.HINDI) "खरीदार" else "Buyer", fontSize = 12.sp) }
                        )
                    }

                    Row {
                        if (unreadCount > 0) {
                            TextButton(onClick = onMarkAllAsRead, modifier = Modifier.testTag("mark_all_read_button")) {
                                Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(MandiStrings.markAllAsRead(language), fontSize = 11.sp)
                            }
                        }
                        if (notifications.isNotEmpty()) {
                            TextButton(onClick = onClearAll, modifier = Modifier.testTag("clear_all_notifications_button")) {
                                Icon(Icons.Default.ClearAll, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(MandiStrings.clearAllNotifications(language), fontSize = 11.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))

                // Notifications List
                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = MandiStrings.noNotifications(language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredList, key = { it.id }) { notif ->
                            NotificationItemCard(
                                notification = notif,
                                language = language,
                                onClick = { onNotificationClick(notif) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItemCard(
    notification: MandiNotification,
    language: AppLanguage,
    onClick: () -> Unit
) {
    val (icon, iconColor, bgBadge) = getNotificationVisuals(notification.type)
    val title = if (language == AppLanguage.HINDI) notification.titleHi else notification.titleEn
    val message = if (language == AppLanguage.HINDI) notification.messageHi else notification.messageEn

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!notification.isRead)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        border = BorderStroke(
            1.dp,
            if (!notification.isRead) MandiGreenPrimary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("notification_item_${notification.id}")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = CircleShape,
                color = bgBadge,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (!notification.isRead) {
                        Surface(
                            shape = CircleShape,
                            color = MandiGreenPrimary,
                            modifier = Modifier.size(8.dp)
                        ) {}
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    notification.lotId?.let { lotId ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MandiGreenPrimary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = lotId,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MandiGreenPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = formatTimeAgo(notification.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationToastBanner(
    notification: MandiNotification?,
    language: AppLanguage,
    onOpenLot: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = notification != null,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it })
    ) {
        notification?.let { notif ->
            val (icon, iconColor, bgBadge) = getNotificationVisuals(notif.type)
            val title = if (language == AppLanguage.HINDI) notif.titleHi else notif.titleEn
            val message = if (language == AppLanguage.HINDI) notif.messageHi else notif.messageEn

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .clickable {
                        notif.lotId?.let { onOpenLot(it) }
                        onDismiss()
                    }
                    .testTag("notification_toast_banner"),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                border = BorderStroke(1.5.dp, MandiGoldSecondary)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = bgBadge,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Dismiss", modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

private fun getNotificationVisuals(type: NotificationType): Triple<ImageVector, Color, Color> {
    return when (type) {
        NotificationType.NEW_BID -> Triple(Icons.Default.Gavel, Color(0xFFE65100), Color(0xFFFFF3E0))
        NotificationType.OUTBID -> Triple(Icons.Default.Warning, Color(0xFFD32F2F), Color(0xFFFFEBEE))
        NotificationType.AUCTION_STARTED -> Triple(Icons.Default.Timer, MandiGreenPrimary, Color(0xFFE8F5E9))
        NotificationType.AUCTION_ENDED -> Triple(Icons.Default.CheckCircle, Color(0xFF1976D2), Color(0xFFE3F2FD))
        NotificationType.BUYER_WON -> Triple(Icons.Default.EmojiEvents, Color(0xFFF57F17), Color(0xFFFFFDE7))
        NotificationType.PURCHASE_SUCCESS -> Triple(Icons.Default.ShoppingCartCheckout, MandiGreenPrimary, Color(0xFFE8F5E9))
        NotificationType.RATING_RECEIVED -> Triple(Icons.Default.Star, Color(0xFFFFB300), Color(0xFFFFF8E1))
        NotificationType.PAYMENT_CONFIRMED -> Triple(Icons.Default.Payments, MandiGreenPrimary, Color(0xFFE8F5E9))
    }
}

private fun MandandiNotificationTitle(lang: AppLanguage) = when (lang) {
    AppLanguage.ENGLISH -> "Notifications"
    AppLanguage.HINDI -> "सूचनाएं एवं अलर्ट"
}

private fun formatTimeAgo(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val minutes = diff / (1000 * 60)
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        else -> "${days}d ago"
    }
}
