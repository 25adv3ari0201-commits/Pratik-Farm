package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.MandiStrings
import com.example.data.ProduceLot
import com.example.data.TradeReview
import com.example.data.UserRole
import com.example.ui.theme.MandiGoldSecondary
import com.example.ui.theme.MandiGreenPrimary

@Composable
fun StarRatingBar(
    rating: Float,
    maxStars: Int = 5,
    isInteractive: Boolean = false,
    onRatingChanged: ((Float) -> Unit)? = null,
    modifier: Modifier = Modifier,
    starSize: Int = 24
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 1..maxStars) {
            val isFilled = i <= rating
            val icon = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder
            val tint = if (isFilled) Color(0xFFFFB300) else Color.Gray.copy(alpha = 0.5f)

            Icon(
                imageVector = icon,
                contentDescription = "$i Stars",
                tint = tint,
                modifier = Modifier
                    .size(starSize.dp)
                    .testTag("star_$i")
                    .then(
                        if (isInteractive && onRatingChanged != null) {
                            Modifier.clickable { onRatingChanged(i.toFloat()) }
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatingReviewDialog(
    lot: ProduceLot,
    reviewerRole: UserRole,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (rating: Float, reviewText: String, tags: List<String>) -> Unit
) {
    var rating by remember { mutableFloatStateOf(5.0f) }
    var reviewText by remember { mutableStateOf("") }
    val selectedTags = remember { mutableStateListOf<String>() }

    val suggestedTags = if (reviewerRole == UserRole.FARMER) {
        if (language == AppLanguage.HINDI) {
            listOf("समय पर पूरा भुगतान", "सही इलेक्ट्रॉनिक तौल", "सम्मानजनक व्यवहार", "शीघ्र ई-एनएएम चालान", "विश्वसनीय व्यापारी")
        } else {
            listOf("Prompt Payment", "Accurate Weighment", "Respectful Trader", "Instant e-NAM Settlement", "Transparent Deal")
        }
    } else {
        if (language == AppLanguage.HINDI) {
            listOf("उच्चतम गुणवत्ता", "कम नमी (<10%)", "स्वच्छ व ग्रेडिड उपज", "सटीक बोरी गिनती", "उत्कृष्ट किसान")
        } else {
            listOf("Top Produce Quality", "Low Moisture (<10%)", "Clean Grading", "Accurate Bag Count", "Reliable Farmer")
        }
    }

    val targetName = if (reviewerRole == UserRole.FARMER) {
        lot.winningBid?.firmName ?: lot.winningBid?.buyerName ?: "Buyer"
    } else {
        lot.farmer.name
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.RateReview, contentDescription = null, tint = MandiGoldSecondary)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (language == AppLanguage.HINDI) "रेटिंग एवं समीक्षा दर्ज करें" else "Leave Rating & Review",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${lot.id} • $targetName",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Star Selector
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "स्टार रेटिंग चुनें (1 से 5)" else "Select Star Rating (1 to 5)",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        StarRatingBar(
                            rating = rating,
                            isInteractive = true,
                            onRatingChanged = { rating = it },
                            starSize = 36
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${rating.toInt()} / 5.0 ${if (rating >= 4.0f) "★★★★★ Excellent" else "★★★ Fair"}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFF8F00)
                        )
                    }
                }

                // Tag Chips
                Text(
                    text = if (language == AppLanguage.HINDI) "त्वरित अनुभव टैग:" else "Quick Experience Tags:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    suggestedTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                            },
                            label = { Text(tag, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MandiGreenPrimary.copy(alpha = 0.15f),
                                selectedLabelColor = MandiGreenPrimary
                            ),
                            modifier = Modifier.testTag("tag_chip_$tag")
                        )
                    }
                }

                // Written feedback textfield
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text(if (language == AppLanguage.HINDI) "समीक्षा और टिप्पणी" else "Written Review & Feedback") },
                    placeholder = { Text(MandiStrings.writeReviewPlaceholder(language), fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("input_review_text"),
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalReview = if (reviewText.isBlank()) {
                        if (reviewerRole == UserRole.FARMER) {
                            if (language == AppLanguage.HINDI) "समय पर भुगतान व निष्पक्ष तौल के साथ उत्कृष्ट व्यापार।" else "Excellent trading experience with prompt DBT payment."
                        } else {
                            if (language == AppLanguage.HINDI) "सर्वोत्तम गुणवत्ता वाली उपज एवं सही ग्रेडिंग।" else "Top quality grain matching assaying specifications."
                        }
                    } else reviewText

                    onSubmit(rating, finalReview, selectedTags.toList())
                },
                colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                modifier = Modifier.testTag("submit_review_dialog_button")
            ) {
                Text(MandiStrings.submitReview(language))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(MandiStrings.close(language))
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BilateralReviewCard(
    title: String,
    roleBadge: String,
    reviewerName: String,
    review: TradeReview?,
    userRole: UserRole,
    canLeaveReview: Boolean,
    language: AppLanguage,
    onLeaveReviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (review != null) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
            else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (review != null) MandiGreenPrimary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = if (roleBadge.contains("Farmer", ignoreCase = true) || roleBadge.contains("किसान"))
                            MandiGreenPrimary.copy(alpha = 0.15f)
                        else MandiGoldSecondary.copy(alpha = 0.2f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (roleBadge.contains("Farmer", ignoreCase = true) || roleBadge.contains("किसान"))
                                Icons.Default.Agriculture else Icons.Default.Business,
                            contentDescription = null,
                            tint = if (roleBadge.contains("Farmer", ignoreCase = true) || roleBadge.contains("किसान"))
                                MandiGreenPrimary else MandiGoldSecondary,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = reviewerName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (review != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFF8E1),
                        border = BorderStroke(1.dp, Color(0xFFFFD54F))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${review.rating}★",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFFE65100)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            if (review != null) {
                Text(
                    text = "\"${review.reviewText}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic
                )

                if (review.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        review.tags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MandiGreenPrimary.copy(alpha = 0.08f),
                                border = BorderStroke(0.5.dp, MandiGreenPrimary.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "✓ $tag",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MandiGreenPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = MandiGreenPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.HINDI) "सत्यापित मंडी सौदा" else "Verified Mandi Transaction",
                            style = MaterialTheme.typography.labelSmall,
                            color = MandiGreenPrimary
                        )
                    }
                    Text(
                        text = review.timestamp,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Pending review state
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "अभी तक कोई समीक्षा दर्ज नहीं की गई है।" else "No review submitted yet for this completed trade.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                    if (canLeaveReview) {
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedButton(
                            onClick = onLeaveReviewClick,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MandiGreenPrimary),
                            modifier = Modifier.testTag("leave_review_button")
                        ) {
                            Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (language == AppLanguage.HINDI) "समीक्षा दें" else "Write Review")
                        }
                    }
                }
            }
        }
    }
}
