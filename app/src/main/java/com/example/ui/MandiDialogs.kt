package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.AppLanguage
import com.example.data.MandiRepository
import com.example.data.MandiStrings
import com.example.data.ProduceLot
import com.example.data.QualityGrade
import com.example.ui.theme.MandiGoldSecondary
import com.example.ui.theme.MandiGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLotDialog(
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (
        farmerName: String,
        farmerPhone: String,
        village: String,
        commodityEn: String,
        commodityHi: String,
        variety: String,
        bags: Int,
        weight: Double,
        msp: Double,
        vehicle: String
    ) -> Unit
) {
    var farmerName by remember { mutableStateOf("") }
    var farmerPhone by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }

    val commodities = listOf(
        Triple("Wheat (Gehun)", "गेहूं", 2425.0),
        Triple("Paddy / Rice (Dhan)", "धान (बासमती)", 2300.0),
        Triple("Mustard (Sarson)", "सरसों", 5950.0),
        Triple("Soybean (Soyabean)", "सोयाबीन", 4892.0),
        Triple("Gram / Chickpea (Chana)", "चना", 5440.0),
        Triple("Cotton (Kapas)", "कपास", 7121.0)
    )

    var selectedCommodityIndex by remember { mutableStateOf(0) }
    var variety by remember { mutableStateOf("Hybrid Gold / FAQ") }
    var bagsText by remember { mutableStateOf("100") }
    var weightText by remember { mutableStateOf("50.0") }
    var vehicleNumber by remember { mutableStateOf("MP 09 AB 1234 (Tractor)") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Agriculture, contentDescription = null, tint = MandiGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "नया गेट प्रवेश व लॉट दर्ज करें" else "New Gate Entry & Lot Intake",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI) "१. किसान विवरण (Farmer Details):" else "1. Farmer Details:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = farmerName,
                    onValueChange = { farmerName = it },
                    label = { Text(if (language == AppLanguage.HINDI) "किसान का नाम *" else "Farmer Name *") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_farmer_name")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = farmerPhone,
                        onValueChange = { farmerPhone = it },
                        label = { Text(if (language == AppLanguage.HINDI) "मोबाइल नंबर" else "Mobile") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_farmer_phone")
                    )
                    OutlinedTextField(
                        value = village,
                        onValueChange = { village = it },
                        label = { Text(if (language == AppLanguage.HINDI) "गांव / कस्बा" else "Village") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_farmer_village")
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                Text(
                    text = if (language == AppLanguage.HINDI) "२. फसल एवं वाहन (Commodity & Vehicle):" else "2. Commodity & Vehicle:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Commodity Selector
                Text(
                    text = if (language == AppLanguage.HINDI) "फसल चुनें:" else "Select Commodity:",
                    style = MaterialTheme.typography.labelSmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    commodities.take(3).forEachIndexed { index, item ->
                        FilterChip(
                            selected = selectedCommodityIndex == index,
                            onClick = { selectedCommodityIndex = index },
                            label = { Text(if (language == AppLanguage.HINDI) item.second else item.first.take(8)) }
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    commodities.drop(3).forEachIndexed { index, item ->
                        val actualIndex = index + 3
                        FilterChip(
                            selected = selectedCommodityIndex == actualIndex,
                            onClick = { selectedCommodityIndex = actualIndex },
                            label = { Text(if (language == AppLanguage.HINDI) item.second else item.first.take(8)) }
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bagsText,
                        onValueChange = { bagsText = it },
                        label = { Text(if (language == AppLanguage.HINDI) "बोरी संख्या" else "Bags") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_bags")
                    )
                    OutlinedTextField(
                        value = weightText,
                        onValueChange = { weightText = it },
                        label = { Text(if (language == AppLanguage.HINDI) "अनुमानित वजन (Qtl)" else "Weight (Qtl)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_weight")
                    )
                }

                OutlinedTextField(
                    value = vehicleNumber,
                    onValueChange = { vehicleNumber = it },
                    label = { Text(if (language == AppLanguage.HINDI) "वाहन संख्या (ट्रैक्टर/ट्रक)" else "Vehicle Registration No") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_vehicle")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val actualName = farmerName.ifBlank { if (language == AppLanguage.HINDI) "रामलाल शर्मा" else "Ramlal Sharma" }
                    val actualPhone = farmerPhone.ifBlank { "+91 98930 11223" }
                    val actualVillage = village.ifBlank { if (language == AppLanguage.HINDI) "सुवासरा" else "Suwasra" }
                    val chosen = commodities[selectedCommodityIndex]
                    val bags = bagsText.toIntOrNull() ?: 100
                    val weight = weightText.toDoubleOrNull() ?: 50.0

                    onSubmit(
                        actualName, actualPhone, actualVillage,
                        chosen.first, chosen.second, variety,
                        bags, weight, chosen.third, vehicleNumber
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                modifier = Modifier.testTag("submit_new_lot_button")
            ) {
                Text(if (language == AppLanguage.HINDI) "दर्ज करें व लॉट बनाएं" else "Create Gate Entry & LOT")
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
fun AssayingDialog(
    lot: ProduceLot,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (
        moisture: Double,
        foreignMatter: Double,
        damaged: Double,
        testWeight: Double,
        grade: QualityGrade,
        labTech: String
    ) -> Unit
) {
    var moistureText by remember { mutableStateOf("11.2") }
    var foreignMatterText by remember { mutableStateOf("0.8") }
    var damagedText by remember { mutableStateOf("1.0") }
    var testWeightText by remember { mutableStateOf("79.0") }
    var selectedGrade by remember { mutableStateOf(QualityGrade.GRADE_A) }
    var labChemistName by remember { mutableStateOf("Dr. K. S. Rathore (Govt. Certified Assayer)") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Science, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "🧪 लैब गुणवत्ता रिपोर्ट दर्ज करें" else "🧪 Enter Lab Assaying Report",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "${lot.id} • ${if (language == AppLanguage.HINDI) lot.commodityHi else lot.commodityEn}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MandiGreenPrimary
                )

                OutlinedTextField(
                    value = moistureText,
                    onValueChange = { moistureText = it },
                    label = { Text(MandiStrings.moistureContent(language) + " (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = foreignMatterText,
                    onValueChange = { foreignMatterText = it },
                    label = { Text(MandiStrings.foreignMatter(language) + " (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = damagedText,
                    onValueChange = { damagedText = it },
                    label = { Text(MandiStrings.damagedGrain(language) + " (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = testWeightText,
                    onValueChange = { testWeightText = it },
                    label = { Text(MandiStrings.testWeight(language) + " (kg/hL)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = MandiStrings.gradeLabel(language) + ":",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QualityGrade.values().forEach { grade ->
                        FilterChip(
                            selected = selectedGrade == grade,
                            onClick = { selectedGrade = grade },
                            label = { Text(grade.label.take(7)) }
                        )
                    }
                }

                OutlinedTextField(
                    value = labChemistName,
                    onValueChange = { labChemistName = it },
                    label = { Text(if (language == AppLanguage.HINDI) "प्रमाणित लैब केमिस्ट का नाम" else "Chemist Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val m = moistureText.toDoubleOrNull() ?: 11.0
                    val fm = foreignMatterText.toDoubleOrNull() ?: 0.8
                    val d = damagedText.toDoubleOrNull() ?: 1.0
                    val tw = testWeightText.toDoubleOrNull() ?: 78.5
                    onSubmit(m, fm, d, tw, selectedGrade, labChemistName)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                modifier = Modifier.testTag("submit_assaying_button")
            ) {
                Text(if (language == AppLanguage.HINDI) "प्रमाणित करें व नीलामी शुरू करें" else "Certify & Start Auction")
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
fun PlaceBidDialog(
    lot: ProduceLot,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSubmitBid: (amount: Double) -> Unit
) {
    val currentHighest = lot.winningBid?.amountPerQuintal ?: lot.reservePrice
    var bidAmountText by remember { mutableStateOf("${(currentHighest + 50).toInt()}") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = MandiGoldSecondary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "ई-नीलामी में बोली लगाएं" else "Place Live Auction Bid",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "${lot.id} • ${if (language == AppLanguage.HINDI) lot.commodityHi else lot.commodityEn}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = MandiStrings.currentHighestBid(language),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "₹${currentHighest.toInt()}/Qtl",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MandiGoldSecondary
                        )
                    }
                }

                Text(
                    text = if (language == AppLanguage.HINDI) "त्वरित बोली जोड़ें (Quick Increment):" else "Quick Increment:",
                    style = MaterialTheme.typography.labelSmall
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(20, 50, 100).forEach { inc ->
                        OutlinedButton(
                            onClick = {
                                bidAmountText = "${(currentHighest + inc).toInt()}"
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("+₹$inc")
                        }
                    }
                }

                OutlinedTextField(
                    value = bidAmountText,
                    onValueChange = { bidAmountText = it },
                    label = { Text(if (language == AppLanguage.HINDI) "आपकी बोली दर (₹ प्रति क्विंटल)" else "Your Bid (₹ / Quintal)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_bid_amount")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = bidAmountText.toDoubleOrNull() ?: (currentHighest + 50.0)
                    onSubmitBid(amount)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MandiGoldSecondary),
                modifier = Modifier.testTag("submit_bid_button")
            ) {
                Text(MandiStrings.placeBid(language))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(MandiStrings.close(language))
            }
        }
    )
}
