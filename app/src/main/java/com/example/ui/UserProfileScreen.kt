package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.AppLanguage
import com.example.data.BuyerProfile
import com.example.data.FarmerProfile
import com.example.data.MandiStrings
import com.example.data.ProduceLot
import com.example.data.UserRole
import com.example.data.WorkflowStep
import com.example.ui.theme.MandiGoldSecondary
import com.example.ui.theme.MandiGreenPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserProfileDialog(
    farmerProfile: FarmerProfile,
    buyerProfile: BuyerProfile,
    allLots: List<ProduceLot>,
    currentRole: UserRole,
    language: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onUpdateFarmer: (FarmerProfile) -> Unit,
    onUpdateBuyer: (BuyerProfile) -> Unit,
    onSelectLot: (ProduceLot) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedProfileTab by remember {
        mutableStateOf(if (currentRole == UserRole.BUYER) 1 else 0)
    }
    var showEditDialog by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .height(720.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Top Header with Close
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MandiGreenPrimary.copy(alpha = 0.12f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = MandiGreenPrimary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = MandiStrings.profileTitle(language),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (language == AppLanguage.HINDI) "केवाईसी एवं व्यापार साख प्रोफ़ाइल" else "KYC Verified Trading Profile",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_profile_dialog")) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Profile Tab Selector (Farmer vs Buyer)
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = selectedProfileTab == 0,
                        onClick = { selectedProfileTab = 0 },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        icon = {
                            Icon(Icons.Default.Agriculture, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    ) {
                        Text(
                            text = MandiStrings.farmerProfile(language),
                            fontWeight = if (selectedProfileTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                    SegmentedButton(
                        selected = selectedProfileTab == 1,
                        onClick = { selectedProfileTab = 1 },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        icon = {
                            Icon(Icons.Default.Business, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    ) {
                        Text(
                            text = MandiStrings.buyerProfile(language),
                            fontWeight = if (selectedProfileTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable Body
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (selectedProfileTab == 0) {
                        // Farmer Profile Content
                        FarmerProfileSection(
                            farmer = farmerProfile,
                            language = language,
                            allLots = allLots,
                            onLanguageSelected = onLanguageSelected,
                            onEditClick = { showEditDialog = true },
                            onSelectLot = {
                                onSelectLot(it)
                                onDismiss()
                            }
                        )
                    } else {
                        // Buyer Profile Content
                        BuyerProfileSection(
                            buyer = buyerProfile,
                            language = language,
                            allLots = allLots,
                            onLanguageSelected = onLanguageSelected,
                            onEditClick = { showEditDialog = true },
                            onSelectLot = {
                                onSelectLot(it)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }

    // Edit Profile Modal
    if (showEditDialog) {
        if (selectedProfileTab == 0) {
            EditFarmerProfileDialog(
                farmer = farmerProfile,
                language = language,
                onDismiss = { showEditDialog = false },
                onSave = { updated ->
                    onUpdateFarmer(updated)
                    showEditDialog = false
                }
            )
        } else {
            EditBuyerProfileDialog(
                buyer = buyerProfile,
                language = language,
                onDismiss = { showEditDialog = false },
                onSave = { updated ->
                    onUpdateBuyer(updated)
                    showEditDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FarmerProfileSection(
    farmer: FarmerProfile,
    language: AppLanguage,
    allLots: List<ProduceLot>,
    onLanguageSelected: (AppLanguage) -> Unit,
    onEditClick: () -> Unit,
    onSelectLot: (ProduceLot) -> Unit
) {
    // Top Hero Identity Card
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MandiGreenPrimary.copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, MandiGreenPrimary.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = MandiGreenPrimary,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = farmer.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.HINDI) farmer.nameHi else farmer.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = "Verified Farmer",
                                tint = MandiGreenPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "Aadhaar: •••• •••• ${farmer.aadhaarLast4} | ID: ${farmer.id}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                OutlinedButton(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MandiGreenPrimary),
                    modifier = Modifier.testTag("edit_farmer_profile_button")
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (language == AppLanguage.HINDI) "संशोधन" else "Edit")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MandiGreenPrimary.copy(alpha = 0.15f))
            Spacer(modifier = Modifier.height(12.dp))

            // Average Rating Banner
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${farmer.rating} / 5.0",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${farmer.ratingCount} ${if (language == AppLanguage.HINDI) "समीक्षाएं" else "reviews"})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MandiGreenPrimary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "शीर्ष श्रेणी किसान" else "Top Grade Producer",
                        style = MaterialTheme.typography.labelSmall,
                        color = MandiGreenPrimary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }

    // Language Preference Section (Req 2)
    ProfileCardSection(
        title = MandiStrings.languagePreference(language),
        icon = Icons.Default.Language
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = if (language == AppLanguage.HINDI)
                    "मंडी इंटरफ़ेस एवं सभी सूचनाओं के लिए पसंदीदा भाषा चुनें:"
                else
                    "Select language for mandi screens and real-time alerts:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LanguageOptionChip(
                    label = "English",
                    subLabel = "Default",
                    isSelected = language == AppLanguage.ENGLISH,
                    onSelect = { onLanguageSelected(AppLanguage.ENGLISH) },
                    modifier = Modifier.weight(1f).testTag("select_lang_english")
                )
                LanguageOptionChip(
                    label = "हिंदी (Hindi)",
                    subLabel = "राजभाषा",
                    isSelected = language == AppLanguage.HINDI,
                    onSelect = { onLanguageSelected(AppLanguage.HINDI) },
                    modifier = Modifier.weight(1f).testTag("select_lang_hindi")
                )
            }
        }
    }

    // Contact Information Section
    ProfileCardSection(
        title = MandiStrings.contactInformation(language),
        icon = Icons.Default.Call
    ) {
        InfoRow("Phone", farmer.phone, Icons.Default.Call)
        InfoRow("Email", farmer.email, Icons.Default.Email)
        InfoRow(
            "Location",
            "${if (language == AppLanguage.HINDI) farmer.villageHi else farmer.village}, ${farmer.district}, ${farmer.state}",
            Icons.Default.LocationOn
        )
        InfoRow("Bank A/C", "•••• •••• ${farmer.bankAccountLast4} (${farmer.ifscCode})", Icons.Default.Verified)
        InfoRow("UPI ID", farmer.upiId, Icons.Default.Verified)
    }

    // Farm & Land Details Section
    ProfileCardSection(
        title = MandiStrings.farmDetails(language),
        icon = Icons.Default.Agriculture
    ) {
        InfoRow(
            label = if (language == AppLanguage.HINDI) "कुल कृषि भूमि" else "Cultivable Land",
            value = "${farmer.landSizeAcres} Acres / एकड़"
        )
        InfoRow(
            label = if (language == AppLanguage.HINDI) "मिट्टी का प्रकार" else "Soil Classification",
            value = if (language == AppLanguage.HINDI) farmer.soilTypeHi else farmer.soilType
        )
        InfoRow(
            label = if (language == AppLanguage.HINDI) "सिंचाई स्रोत" else "Irrigation System",
            value = if (language == AppLanguage.HINDI) farmer.irrigationSourceHi else farmer.irrigationSource
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (language == AppLanguage.HINDI) "स्वामित्व वाले कृषि यंत्र:" else "Farm Machinery Owned:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            farmer.equipmentOwned.forEach { eq ->
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "🚜 $eq",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }

    // Preferred Crops Section
    ProfileCardSection(
        title = MandiStrings.preferredCropsGoods(language),
        icon = Icons.Default.Agriculture
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            farmer.preferredCrops.forEach { crop ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MandiGreenPrimary.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, MandiGreenPrimary.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = "🌾 $crop",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MandiGreenPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }

    // Transaction History Section (Req 2)
    TransactionHistorySection(
        lots = allLots.filter { it.currentStep == WorkflowStep.BUYER_RECEIVED || it.invoice != null },
        userRole = UserRole.FARMER,
        language = language,
        onSelectLot = onSelectLot
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuyerProfileSection(
    buyer: BuyerProfile,
    language: AppLanguage,
    allLots: List<ProduceLot>,
    onLanguageSelected: (AppLanguage) -> Unit,
    onEditClick: () -> Unit,
    onSelectLot: (ProduceLot) -> Unit
) {
    // Top Hero Identity Card
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MandiGoldSecondary.copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, MandiGoldSecondary.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = MandiGoldSecondary,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = buyer.firmName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = buyer.firmName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Verified,
                                contentDescription = "Licensed Trader",
                                tint = MandiGoldSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "${buyer.name} | License: ${buyer.tradeLicenseNo}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                OutlinedButton(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MandiGoldSecondary),
                    modifier = Modifier.testTag("edit_buyer_profile_button")
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (language == AppLanguage.HINDI) "संशोधन" else "Edit")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MandiGoldSecondary.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Average Rating Banner
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${buyer.rating} / 5.0",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${buyer.ratingCount} ${if (language == AppLanguage.HINDI) "समीक्षाएं" else "reviews"})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MandiGoldSecondary.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "विश्वसनीय मंडी खरीदार" else "Verified Mandi Buyer",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF6D4C00),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }

    // Language Preference Section (Req 2)
    ProfileCardSection(
        title = MandiStrings.languagePreference(language),
        icon = Icons.Default.Language
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = if (language == AppLanguage.HINDI)
                    "मंडी इंटरफ़ेस एवं सभी सूचनाओं के लिए पसंदीदा भाषा चुनें:"
                else
                    "Select language for mandi screens and real-time alerts:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LanguageOptionChip(
                    label = "English",
                    subLabel = "Default",
                    isSelected = language == AppLanguage.ENGLISH,
                    onSelect = { onLanguageSelected(AppLanguage.ENGLISH) },
                    modifier = Modifier.weight(1f).testTag("select_lang_english_buyer")
                )
                LanguageOptionChip(
                    label = "हिंदी (Hindi)",
                    subLabel = "राजभाषा",
                    isSelected = language == AppLanguage.HINDI,
                    onSelect = { onLanguageSelected(AppLanguage.HINDI) },
                    modifier = Modifier.weight(1f).testTag("select_lang_hindi_buyer")
                )
            }
        }
    }

    // Contact Information Section
    ProfileCardSection(
        title = MandiStrings.contactInformation(language),
        icon = Icons.Default.Call
    ) {
        InfoRow("Managing Trader", buyer.name, Icons.Default.Person)
        InfoRow("Phone", buyer.phone, Icons.Default.Call)
        InfoRow("Email", buyer.email, Icons.Default.Email)
        InfoRow("City & State", "${buyer.city}, ${buyer.state}", Icons.Default.LocationOn)
        InfoRow("GSTIN", buyer.gstin, Icons.Default.Verified)
        InfoRow("APMC Mandi License", buyer.tradeLicenseNo, Icons.Default.Verified)
    }

    // Business & Facility Details Section
    ProfileCardSection(
        title = MandiStrings.businessDetails(language),
        icon = Icons.Default.Business
    ) {
        InfoRow(
            label = if (language == AppLanguage.HINDI) "व्यापार का प्रकार" else "Business Category",
            value = buyer.businessType
        )
        InfoRow(
            label = if (language == AppLanguage.HINDI) "गोदाम भंडारण क्षमता" else "Warehouse Storage Capacity",
            value = "${buyer.warehouseCapacityQuintals.toInt()} Quintals (क्विंटल)"
        )
    }

    // Preferred Goods Section
    ProfileCardSection(
        title = MandiStrings.preferredCropsGoods(language),
        icon = Icons.Default.Business
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            buyer.preferredGoods.forEach { good ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MandiGoldSecondary.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, MandiGoldSecondary.copy(alpha = 0.35f))
                ) {
                    Text(
                        text = "📦 $good",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6D4C00),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }

    // Transaction History Section (Req 2)
    TransactionHistorySection(
        lots = allLots.filter { it.currentStep == WorkflowStep.BUYER_RECEIVED || it.invoice != null },
        userRole = UserRole.BUYER,
        language = language,
        onSelectLot = onSelectLot
    )
}

@Composable
fun ProfileCardSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MandiGreenPrimary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, icon: ImageVector? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun LanguageOptionChip(
    label: String,
    subLabel: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MandiGreenPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) MandiGreenPrimary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = modifier.clickable { onSelect() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Column {
                Text(
                    text = label,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MandiGreenPrimary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = MandiGreenPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun TransactionHistorySection(
    lots: List<ProduceLot>,
    userRole: UserRole,
    language: AppLanguage,
    onSelectLot: (ProduceLot) -> Unit
) {
    ProfileCardSection(
        title = MandiStrings.transactionHistory(language),
        icon = Icons.Default.History
    ) {
        if (lots.isEmpty()) {
            Text(
                text = if (language == AppLanguage.HINDI) "कोई पूर्ण लेन-देन नहीं मिला।" else "No finalized transactions recorded yet.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                lots.forEach { lot ->
                    val counterparty = if (userRole == UserRole.FARMER) {
                        lot.winningBid?.firmName ?: "Licensed Buyer"
                    } else {
                        lot.farmer.name
                    }
                    val amount = lot.invoice?.netFarmerReceivable ?: (lot.estimatedWeightQuintals * (lot.winningBid?.amountPerQuintal ?: lot.reservePrice))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectLot(lot) }
                            .testTag("history_item_${lot.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (language == AppLanguage.HINDI) lot.commodityHi else lot.commodityEn,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = MandiGreenPrimary.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = lot.id,
                                            fontSize = 11.sp,
                                            color = MandiGreenPrimary,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${if (userRole == UserRole.FARMER) "Buyer: " else "Farmer: "}$counterparty",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${lot.weighment?.netQuintals ?: lot.estimatedWeightQuintals} Qtl • ${lot.payment?.timestamp ?: lot.arrivalTime}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "₹${amount.toInt()}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MandiGreenPrimary
                                )
                                if (lot.buyerReview != null || lot.farmerReview != null) {
                                    val rating = lot.buyerReview?.rating ?: lot.farmerReview?.rating ?: 5.0f
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(12.dp))
                                        Text(text = " $rating★", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditFarmerProfileDialog(
    farmer: FarmerProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (FarmerProfile) -> Unit
) {
    var name by remember { mutableStateOf(farmer.name) }
    var phone by remember { mutableStateOf(farmer.phone) }
    var email by remember { mutableStateOf(farmer.email) }
    var village by remember { mutableStateOf(farmer.village) }
    var district by remember { mutableStateOf(farmer.district) }
    var landSizeText by remember { mutableStateOf("${farmer.landSizeAcres}") }
    var soilType by remember { mutableStateOf(farmer.soilType) }
    var irrigation by remember { mutableStateOf(farmer.irrigationSource) }
    var cropsText by remember { mutableStateOf(farmer.preferredCrops.joinToString(", ")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (language == AppLanguage.HINDI) "किसान प्रोफ़ाइल संशोधन" else "Edit Farmer Profile")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (language == AppLanguage.HINDI) "किसान का नाम" else "Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Phone") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = village,
                    onValueChange = { village = it },
                    label = { Text("Village / Gram") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text("District & State") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = landSizeText,
                    onValueChange = { landSizeText = it },
                    label = { Text("Land Size (Acres)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = soilType,
                    onValueChange = { soilType = it },
                    label = { Text("Soil Type") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cropsText,
                    onValueChange = { cropsText = it },
                    label = { Text("Preferred Crops (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = farmer.copy(
                        name = name,
                        nameHi = name,
                        phone = phone,
                        email = email,
                        village = village,
                        villageHi = village,
                        district = district,
                        landSizeAcres = landSizeText.toDoubleOrNull() ?: farmer.landSizeAcres,
                        soilType = soilType,
                        preferredCrops = cropsText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary)
            ) {
                Text(if (language == AppLanguage.HINDI) "सहेजें" else "Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(MandiStrings.close(language))
            }
        }
    )
}

@Composable
fun EditBuyerProfileDialog(
    buyer: BuyerProfile,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (BuyerProfile) -> Unit
) {
    var name by remember { mutableStateOf(buyer.name) }
    var firmName by remember { mutableStateOf(buyer.firmName) }
    var phone by remember { mutableStateOf(buyer.phone) }
    var email by remember { mutableStateOf(buyer.email) }
    var city by remember { mutableStateOf(buyer.city) }
    var tradeLicense by remember { mutableStateOf(buyer.tradeLicenseNo) }
    var gstin by remember { mutableStateOf(buyer.gstin) }
    var capacityText by remember { mutableStateOf("${buyer.warehouseCapacityQuintals.toInt()}") }
    var goodsText by remember { mutableStateOf(buyer.preferredGoods.joinToString(", ")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (language == AppLanguage.HINDI) "खरीदार प्रोफ़ाइल संशोधन" else "Edit Buyer Profile")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = firmName,
                    onValueChange = { firmName = it },
                    label = { Text(if (language == AppLanguage.HINDI) "फर्म / प्रतिष्ठान का नाम" else "Firm Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Authorized Signatory Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Official Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Operating Mandi / City") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tradeLicense,
                    onValueChange = { tradeLicense = it },
                    label = { Text("APMC Trade License No") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = gstin,
                    onValueChange = { gstin = it },
                    label = { Text("GSTIN") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = capacityText,
                    onValueChange = { capacityText = it },
                    label = { Text("Warehouse Capacity (Quintals)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = goodsText,
                    onValueChange = { goodsText = it },
                    label = { Text("Preferred Goods (comma separated)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = buyer.copy(
                        name = name,
                        firmName = firmName,
                        phone = phone,
                        email = email,
                        city = city,
                        tradeLicenseNo = tradeLicense,
                        gstin = gstin,
                        warehouseCapacityQuintals = capacityText.toDoubleOrNull() ?: buyer.warehouseCapacityQuintals,
                        preferredGoods = goodsText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    )
                    onSave(updated)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MandiGoldSecondary)
            ) {
                Text(if (language == AppLanguage.HINDI) "सहेजें" else "Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(MandiStrings.close(language))
            }
        }
    )
}
