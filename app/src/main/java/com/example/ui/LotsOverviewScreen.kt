package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.MandiStrings
import com.example.data.ProduceLot
import com.example.data.UserRole
import com.example.data.WorkflowStep
import com.example.ui.theme.GradeGreen
import com.example.ui.theme.MandiGoldSecondary
import com.example.ui.theme.MandiGreenPrimary

@Composable
fun LotsOverviewScreen(
    lots: List<ProduceLot>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    userRole: UserRole,
    language: AppLanguage,
    onSelectLot: (ProduceLot) -> Unit,
    onOpenNewLot: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabTitles = listOf(
        MandiStrings.tabLiveAuctions(language) to Icons.Default.Gavel,
        MandiStrings.tabMyLots(language) to Icons.Default.ListAlt,
        MandiStrings.tabWorkflowMap(language) to Icons.Default.Route,
        MandiStrings.tabCompleted(language) to Icons.Default.CheckCircle
    )

    // Filter lots according to tab & search query
    val filteredLots = lots.filter { lot ->
        val queryMatches = searchQuery.isBlank() ||
                lot.commodityEn.contains(searchQuery, ignoreCase = true) ||
                lot.commodityHi.contains(searchQuery, ignoreCase = true) ||
                lot.id.contains(searchQuery, ignoreCase = true) ||
                lot.farmer.name.contains(searchQuery, ignoreCase = true) ||
                lot.farmer.nameHi.contains(searchQuery, ignoreCase = true)

        val tabMatches = when (selectedTab) {
            0 -> lot.currentStep == WorkflowStep.ONLINE_AUCTION || lot.currentStep == WorkflowStep.FARMER_DECISION
            1 -> lot.currentStep != WorkflowStep.BUYER_RECEIVED && lot.currentStep != WorkflowStep.TRADE_DECLINED
            2 -> true // handled by WorkflowMapScreen
            3 -> lot.currentStep == WorkflowStep.BUYER_RECEIVED || lot.currentStep == WorkflowStep.TRADE_DECLINED
            else -> true
        }

        val roleMatches = when (userRole) {
            UserRole.ALL_ACCESS -> true
            UserRole.FARMER -> true
            UserRole.BUYER -> lot.assayingReport != null // Buyers see assayed & auction lots
            UserRole.INSPECTOR -> true
        }

        queryMatches && tabMatches && roleMatches
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Tab Row
            androidx.compose.material3.TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("mandi_tabs")
            ) {
                tabTitles.forEachIndexed { index, (title, icon) ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { onTabSelected(index) },
                        text = {
                            Text(
                                text = title,
                                maxLines = 1,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        modifier = Modifier.testTag("tab_$index")
                    )
                }
            }

            // If Workflow Map Tab (Index 2)
            if (selectedTab == 2) {
                WorkflowMapScreen(
                    lots = lots,
                    language = language,
                    onSelectLot = onSelectLot,
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Search Box
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChange("") }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    placeholder = { Text(MandiStrings.searchPlaceholder(language)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("search_lot_field")
                )

                // Lots list
                if (filteredLots.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Agriculture,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "कोई लॉट नहीं मिला" else "No matching lots found",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) "नया गेट प्रवेश पर्ची दर्ज करें या फ़िल्टर बदलें" else "Create a new gate entry or adjust filters",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredLots, key = { it.id }) { lot ->
                            LotItemCard(
                                lot = lot,
                                language = language,
                                onSelect = { onSelectLot(lot) }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(80.dp)) // space for FAB
                        }
                    }
                }
            }
        }

        // Floating Action Button for Gate Entry & Lot creation
        ExtendedFloatingActionButton(
            onClick = onOpenNewLot,
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = {
                Text(
                    text = MandiStrings.registerNewProduce(language),
                    fontWeight = FontWeight.Bold
                )
            },
            containerColor = MandiGreenPrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("fab_new_lot")
        )
    }
}

@Composable
fun LotItemCard(
    lot: ProduceLot,
    language: AppLanguage,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onSelect,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .testTag("lot_card_${lot.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Commodity + Lot ID
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Agriculture,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.HINDI) lot.commodityHi else lot.commodityEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) lot.varietyHi else lot.varietyEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = lot.id,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Farmer & Vehicle Info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI)
                        "👨🌾 ${lot.farmer.nameHi} (${lot.farmer.villageHi})"
                    else
                        "👨🌾 ${lot.farmer.name} (${lot.farmer.village})",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "🚜 ${lot.estimatedWeightQuintals} Qtl (${lot.estimatedBags} bags)",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Quality Badge & Auction Timer (if present)
            if (lot.assayingReport != null || lot.isAuctionLive) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    lot.assayingReport?.let { report ->
                        QualityGradeBadge(grade = report.grade, language = language)
                        Text(
                            text = "${MandiStrings.moistureContent(language)}: ${report.moisturePercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (lot.isAuctionLive) {
                        AuctionTimerBadge(
                            remainingSeconds = lot.auctionTimeRemainingSeconds,
                            language = language
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pricing & Current Step Box
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column {
                        val highBid = lot.winningBid?.amountPerQuintal
                        if (highBid != null) {
                            Text(
                                text = MandiStrings.currentHighestBid(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MandiGoldSecondary
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "₹${highBid.toInt()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MandiGoldSecondary
                                )
                                Text(
                                    text = MandiStrings.perQuintal(language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
                                )
                            }
                        } else {
                            Text(
                                text = MandiStrings.mspPrice(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${lot.mspBasePrice.toInt()}${MandiStrings.perQuintal(language)}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = getStepDisplayLabel(lot.currentStep, language),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = MandiStrings.viewDetails(language),
                                style = MaterialTheme.typography.labelSmall,
                                color = MandiGoldSecondary
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = MandiGoldSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

fun getStepDisplayLabel(step: WorkflowStep, language: AppLanguage): String {
    return when (step) {
        WorkflowStep.FARMER_REGISTRATION -> if (language == AppLanguage.HINDI) "पंजीकरण" else "Registration"
        WorkflowStep.BRING_PRODUCE -> if (language == AppLanguage.HINDI) "उपज आवक" else "Arrival"
        WorkflowStep.GATE_ENTRY -> if (language == AppLanguage.HINDI) "गेट प्रवेश" else "Gate Entry"
        WorkflowStep.LOT_CREATION -> if (language == AppLanguage.HINDI) "लॉट निर्मित" else "Lot Created"
        WorkflowStep.ASSAYING -> if (language == AppLanguage.HINDI) "लैब परख जारी" else "Assaying"
        WorkflowStep.QUALITY_ATTACHED -> if (language == AppLanguage.HINDI) "प्रमाण-पत्र तैयार" else "Quality Certified"
        WorkflowStep.ONLINE_AUCTION -> if (language == AppLanguage.HINDI) "🔴 लाइव नीलामी" else "🔴 Live Auction"
        WorkflowStep.WINNER_DECLARED -> if (language == AppLanguage.HINDI) "विजेता तय" else "Winner Declared"
        WorkflowStep.FARMER_DECISION -> if (language == AppLanguage.HINDI) "⚠️ किसान निर्णय" else "⚠️ Farmer Action"
        WorkflowStep.WEIGHMENT -> if (language == AppLanguage.HINDI) "धर्मकांटा तौल" else "Weighment"
        WorkflowStep.SALE_INVOICE -> if (language == AppLanguage.HINDI) "बिक्री चालान" else "Sale Agreement"
        WorkflowStep.PAYMENT -> if (language == AppLanguage.HINDI) "भुगतान प्रक्रिया" else "Payment Pending"
        WorkflowStep.LOGISTICS -> if (language == AppLanguage.HINDI) "वाहन प्रेषण" else "Logistics"
        WorkflowStep.GATE_EXIT -> if (language == AppLanguage.HINDI) "गेट निकासी" else "Gate Exit"
        WorkflowStep.BUYER_RECEIVED -> if (language == AppLanguage.HINDI) "✅ माल प्राप्त" else "✅ Delivered"
        WorkflowStep.TRADE_DECLINED -> if (language == AppLanguage.HINDI) "❌ रद्द सौदा" else "❌ Declined"
    }
}
