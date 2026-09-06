package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import com.example.data.AppLanguage
import com.example.data.MandiStrings
import com.example.data.QualityGrade
import com.example.data.UserRole
import com.example.data.WorkflowStep
import com.example.ui.theme.GradeAmber
import com.example.ui.theme.GradeGreen
import com.example.ui.theme.GradeOrange
import com.example.ui.theme.MandiGoldSecondary
import com.example.ui.theme.MandiGreenPrimary

@Composable
fun LanguageSwitchButton(
    currentLanguage: AppLanguage,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
        modifier = modifier.testTag("language_switch_button")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = "Language",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (currentLanguage == AppLanguage.ENGLISH) "हिंदी (HI)" else "English (EN)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun RoleSelectorRow(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val roles = listOf(
        UserRole.ALL_ACCESS to MandiStrings.roleOverview(language),
        UserRole.FARMER to MandiStrings.roleFarmer(language),
        UserRole.BUYER to MandiStrings.roleBuyer(language),
        UserRole.INSPECTOR to MandiStrings.roleInspector(language)
    )

    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        roles.forEach { (role, title) ->
            val isSelected = currentRole == role
            val bgColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                label = "role_bg"
            )
            val textColor by animateColorAsState(
                if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                label = "role_text"
            )

            Surface(
                onClick = { onRoleSelected(role) },
                shape = RoundedCornerShape(12.dp),
                color = bgColor,
                modifier = Modifier.testTag("role_chip_${role.name.lowercase()}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    val icon = when (role) {
                        UserRole.ALL_ACCESS -> Icons.Default.DoneAll
                        UserRole.FARMER -> Icons.Default.Agriculture
                        UserRole.BUYER -> Icons.Default.Gavel
                        UserRole.INSPECTOR -> Icons.Default.Science
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = textColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun QualityGradeBadge(
    grade: QualityGrade,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (grade) {
        QualityGrade.GRADE_A -> GradeGreen to Color.White
        QualityGrade.GRADE_B -> GradeAmber to Color.Black
        QualityGrade.GRADE_C -> GradeOrange to Color.White
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = bgColor.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, bgColor),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Verified,
                contentDescription = null,
                tint = bgColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = grade.label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = bgColor
            )
        }
    }
}

@Composable
fun AuctionTimerBadge(
    remainingSeconds: Int,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val isUrgent = remainingSeconds in 1..30
    val timerColor = if (isUrgent) Color(0xFFD32F2F) else MandiGreenPrimary

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = timerColor.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, timerColor),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = "Auction Timer",
                tint = timerColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (remainingSeconds > 0) {
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                } else {
                    if (language == AppLanguage.HINDI) "समाप्त" else "CLOSED"
                },
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = timerColor
            )
        }
    }
}

@Composable
fun LaymanVoiceBanner(
    language: AppLanguage,
    stepTitle: String,
    stepExplanation: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MandiGoldSecondary.copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Voice Guide",
                    tint = MandiGoldSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (language == AppLanguage.HINDI) "कार्यप्रवाह निर्देश (आवाज सहायक)" else "Mandi Workflow Assistant",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = stepExplanation,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun InteractiveWorkflowBar(
    currentStep: WorkflowStep,
    farmerAccepted: Boolean?,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    // 10 key milestones for compact horizontal tracking
    val milestoneSteps = listOf(
        WorkflowStep.GATE_ENTRY to if (language == AppLanguage.HINDI) "गेट प्रवेश" else "Gate Entry",
        WorkflowStep.ASSAYING to if (language == AppLanguage.HINDI) "लैब परख" else "Assaying",
        WorkflowStep.ONLINE_AUCTION to if (language == AppLanguage.HINDI) "ई-नीलामी" else "Auction",
        WorkflowStep.FARMER_DECISION to if (language == AppLanguage.HINDI) "किसान निर्णय" else "Farmer OK?",
        WorkflowStep.WEIGHMENT to if (language == AppLanguage.HINDI) "धर्मकांटा" else "Weighment",
        WorkflowStep.SALE_INVOICE to if (language == AppLanguage.HINDI) "चालान" else "Invoice",
        WorkflowStep.PAYMENT to if (language == AppLanguage.HINDI) "भुगतान" else "Payment",
        WorkflowStep.LOGISTICS to if (language == AppLanguage.HINDI) "परिवहन" else "Logistics",
        WorkflowStep.GATE_EXIT to if (language == AppLanguage.HINDI) "गेट निकास" else "Gate Exit",
        WorkflowStep.BUYER_RECEIVED to if (language == AppLanguage.HINDI) "प्राप्ति" else "Received"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            milestoneSteps.forEachIndexed { index, (step, label) ->
                val isCompleted = currentStep.stepIndex > step.stepIndex || currentStep == WorkflowStep.BUYER_RECEIVED
                val isCurrent = currentStep == step
                val isDeclined = currentStep == WorkflowStep.TRADE_DECLINED && step == WorkflowStep.FARMER_DECISION

                val stepColor = when {
                    isDeclined -> Color(0xFFD32F2F)
                    isCurrent -> MandiGoldSecondary
                    isCompleted -> MandiGreenPrimary
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(stepColor)
                    ) {
                        if (isCompleted && !isCurrent) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        } else if (isDeclined) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )

                    if (index < milestoneSteps.size - 1) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .width(12.dp)
                                .height(2.dp)
                                .background(if (isCompleted) MandiGreenPrimary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
            }
        }
    }
}
