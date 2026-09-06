package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AppLanguage
import com.example.data.ProduceLot
import com.example.data.WorkflowStep
import com.example.ui.theme.GradeGreen
import com.example.ui.theme.MandiGoldSecondary
import com.example.ui.theme.MandiGreenPrimary

data class WorkflowNodeData(
    val step: WorkflowStep,
    val icon: ImageVector,
    val titleEn: String,
    val titleHi: String,
    val descEn: String,
    val descHi: String,
    val responsibleRoleEn: String,
    val responsibleRoleHi: String,
    val color: Color
)

@Composable
fun WorkflowMapScreen(
    lots: List<ProduceLot>,
    language: AppLanguage,
    onSelectLot: (ProduceLot) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedNodeStep by remember { mutableStateOf<WorkflowStep?>(null) }

    val nodes = remember {
        listOf(
            WorkflowNodeData(
                step = WorkflowStep.FARMER_REGISTRATION,
                icon = Icons.Default.HowToReg,
                titleEn = "👨🌾 Farmer Registration",
                titleHi = "👨🌾 किसान पंजीकरण",
                descEn = "Farmer enters credentials (Aadhaar, Phone, Bank A/C) into e-Mandi database.",
                descHi = "किसान अपना नाम, आधार, मोबाइल व बैंक विवरण मंडी सिस्टम में दर्ज करते हैं।",
                responsibleRoleEn = "Farmer & Mandi Helpdesk",
                responsibleRoleHi = "किसान एवं मंडी सहायता केंद्र",
                color = MandiGreenPrimary
            ),
            WorkflowNodeData(
                step = WorkflowStep.BRING_PRODUCE,
                icon = Icons.Default.Agriculture,
                titleEn = "🚜 Bring Produce to Mandi",
                titleHi = "🚜 मंडी में उपज लाना",
                descEn = "Loaded tractor, truck, or trolley arrives at the physical Mandi premises.",
                descHi = "उपज से लदी ट्रैक्टर-ट्रॉली या ट्रक मंडी प्रांगण में पहुंचती है।",
                responsibleRoleEn = "Farmer / Driver",
                responsibleRoleHi = "किसान / चालक",
                color = MandiGreenPrimary
            ),
            WorkflowNodeData(
                step = WorkflowStep.GATE_ENTRY,
                icon = Icons.Default.MeetingRoom,
                titleEn = "🚪 Gate Entry Pass",
                titleHi = "🚪 गेट प्रवेश पर्ची",
                descEn = "Mandi security inspects vehicle & issues digital Gate Entry token.",
                descHi = "मंडी सुरक्षा द्वारा वाहन का सत्यापन व डिजिटल गेट पर्ची जारी की जाती है।",
                responsibleRoleEn = "Gate Security Officer",
                responsibleRoleHi = "गेट सुरक्षा अधिकारी",
                color = MandiGreenPrimary
            ),
            WorkflowNodeData(
                step = WorkflowStep.LOT_CREATION,
                icon = Icons.Default.QrCode,
                titleEn = "📦 Create LOT ID",
                titleHi = "📦 लॉट संख्या (LOT ID) तैयार करें",
                descEn = "Unique QR/Barcode lot identity generated with estimated bag count.",
                descHi = "उपज के लिए अद्वितीय लॉट संख्या एवं बारकोड जनरेट किया जाता है।",
                responsibleRoleEn = "Mandi APMC Operator",
                responsibleRoleHi = "मंडी समिति ऑपरेटर",
                color = MandiGreenPrimary
            ),
            WorkflowNodeData(
                step = WorkflowStep.ASSAYING,
                icon = Icons.Default.Science,
                titleEn = "🧪 Lab Assaying & Testing",
                titleHi = "🧪 गुणवत्ता जांच / लैब परख",
                descEn = "Certified lab chemist analyzes moisture %, foreign matter, damaged grain.",
                descHi = "प्रमाणित लैब केमिस्ट द्वारा नमी %, विजातीय कचरा व दाने का परीक्षण।",
                responsibleRoleEn = "Mandi Assaying Chemist",
                responsibleRoleHi = "मंडी लैब विश्लेषक",
                color = Color(0xFF0284C7)
            ),
            WorkflowNodeData(
                step = WorkflowStep.QUALITY_ATTACHED,
                icon = Icons.Default.Verified,
                titleEn = "🏷️ Quality Info Attached",
                titleHi = "🏷️ गुणवत्ता प्रमाण-पत्र संलग्न",
                descEn = "Grade (A/B/C) certificate permanently linked to LOT ID for online buyers.",
                descHi = "लॉट के साथ गुणवत्ता ग्रेड (A/B/C) प्रमाण-पत्र ऑनलाइन प्रदर्शित होता है।",
                responsibleRoleEn = "Quality Certification System",
                responsibleRoleHi = "गुणवत्ता प्रमाणन प्रणाली",
                color = Color(0xFF0284C7)
            ),
            WorkflowNodeData(
                step = WorkflowStep.ONLINE_AUCTION,
                icon = Icons.Default.Gavel,
                titleEn = "📱 ONLINE AUCTION (Bidding)",
                titleHi = "📱 लाइव ई-नीलामी (बोली प्रक्रिया)",
                descEn = "Traders & millers across the nation submit competitive digital bids.",
                descHi = "देशभर के पंजीकृत व्यापारी व मिल मालिक पारदर्शी डिजिटल बोलियां लगाते हैं।",
                responsibleRoleEn = "Registered Buyers & Traders",
                responsibleRoleHi = "पंजीकृत व्यापारी एवं खरीदार",
                color = MandiGoldSecondary
            ),
            WorkflowNodeData(
                step = WorkflowStep.WINNER_DECLARED,
                icon = Icons.Default.EmojiEvents,
                titleEn = "🏆 Winner Declared",
                titleHi = "🏆 विजेता बोली घोषित",
                descEn = "Auction closes; highest eligible bidder confirmed as winning buyer.",
                descHi = "नीलामी समय समाप्त; उच्चतम दर वाली बोली को विजेता घोषित किया जाता है।",
                responsibleRoleEn = "e-Auction Engine",
                responsibleRoleHi = "ई-नीलामी स्वचालित इंजन",
                color = MandiGoldSecondary
            ),
            WorkflowNodeData(
                step = WorkflowStep.FARMER_DECISION,
                icon = Icons.Default.ThumbUp,
                titleEn = "👨🌾 Farmer Accepts? (YES / NO)",
                titleHi = "👨🌾 किसान की सहमति (हाँ / ना)",
                descEn = "Crucial decision! Farmer can accept the winning bid or decline to re-auction.",
                descHi = "महत्वपूर्ण चरण! किसान सर्वोच्च भाव स्वीकार कर सकते हैं या मना कर सकते हैं।",
                responsibleRoleEn = "Farmer (Autonomous Decision)",
                responsibleRoleHi = "किसान (स्वतंत्र निर्णय)",
                color = Color(0xFF7C3AED)
            ),
            WorkflowNodeData(
                step = WorkflowStep.WEIGHMENT,
                icon = Icons.Default.Scale,
                titleEn = "⚖️ Electronic Weighment",
                titleHi = "⚖️ धर्मकांटा इलेक्ट्रॉनिक तौल",
                descEn = "Gross & tare weights measured to calculate certified net produce weight.",
                descHi = "सकल व खाली वजन घटाकर वास्तविक शुद्ध वजन (क्विंटल में) दर्ज होता है।",
                responsibleRoleEn = "Weighbridge Weighmaster",
                responsibleRoleHi = "धर्मकांटा तौल ऑपरेटर",
                color = Color(0xFF0D9488)
            ),
            WorkflowNodeData(
                step = WorkflowStep.SALE_INVOICE,
                icon = Icons.Default.ReceiptLong,
                titleEn = "🧾 Invoice & Sale Agreement",
                titleHi = "🧾 बिक्री अनुबंध व चालान",
                descEn = "Official trade agreement with Mandi cess & gross/net amounts calculated.",
                descHi = "मंडी शुल्क कटौती व किसान को देय शुद्ध राशि का कानूनी अनुबंध पत्र।",
                responsibleRoleEn = "e-NAM Billing Settlement",
                responsibleRoleHi = "ई-नाम बिलिंग प्रणाली",
                color = Color(0xFF0D9488)
            ),
            WorkflowNodeData(
                step = WorkflowStep.PAYMENT,
                icon = Icons.Default.Payments,
                titleEn = "💳 Direct Payment (DBT)",
                titleHi = "💳 डायरेक्ट बैंक भुगतान (DBT)",
                descEn = "Buyer funds escrow; payment transferred directly to farmer bank account.",
                descHi = "खरीदार द्वारा राशि जमा; सीधे किसान के बैंक खाते में तुरंत ट्रांसफर।",
                responsibleRoleEn = "Escrow Banking Partner",
                responsibleRoleHi = "एस्क्रो बैंकिंग पार्टनर",
                color = GradeGreen
            ),
            WorkflowNodeData(
                step = WorkflowStep.LOGISTICS,
                icon = Icons.Default.LocalShipping,
                titleEn = "🚚 Logistics & Truck Dispatch",
                titleHi = "🚚 परिवहन व वाहन प्रेषण",
                descEn = "e-Way bill generated; transport vehicle loaded and dispatched.",
                descHi = "ई-वे बिल जारी; ट्रक में माल लोड कर गंतव्य हेतु रवाना किया जाता है।",
                responsibleRoleEn = "Transporter / Buyer Logistics",
                responsibleRoleHi = "ट्रांसपोर्टर / लॉजिस्टिक्स",
                color = Color(0xFFD97706)
            ),
            WorkflowNodeData(
                step = WorkflowStep.GATE_EXIT,
                icon = Icons.Default.ExitToApp,
                titleEn = "🚪 Gate Exit Clearance",
                titleHi = "🚪 गेट निकासी पर्ची",
                descEn = "Security verifies weighment slip & payment receipt before opening exit gate.",
                descHi = "भुगतान व तौल रसीद जांचकर सुरक्षा द्वारा गेट निकास पास दिया जाता है।",
                responsibleRoleEn = "Gate Exit Security",
                responsibleRoleHi = "गेट निकास सुरक्षा",
                color = Color(0xFFD97706)
            ),
            WorkflowNodeData(
                step = WorkflowStep.BUYER_RECEIVED,
                icon = Icons.Default.CheckCircle,
                titleEn = "🏭 Buyer Receives Produce",
                titleHi = "🏭 खरीदार को माल सुपुर्द",
                descEn = "Consignment delivered at factory/warehouse; buyer acknowledges receipt & rates trade.",
                descHi = "कारखाने/गोदाम पर माल पहुंचा; खरीदार द्वारा प्राप्ति रसीद व रेटिंग दी जाती है।",
                responsibleRoleEn = "Buyer Warehouse Officer",
                responsibleRoleHi = "खरीदार गोदाम प्रभारी",
                color = GradeGreen
            )
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("workflow_map_list")
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "मंडी संपूर्ण कार्यप्रवाह गाइड" else "Smart Mandi End-to-End Workflow Guide",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "किसान से लेकर खरीदार तक की पूरी यात्रा। किसी भी चरण पर क्लिक करके सक्रिय लॉट्स व विवरण देखें।"
                        else
                            "From farm gate to buyer warehouse. Tap any milestone to view active lots & live details.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        items(nodes) { node ->
            val isExpanded = selectedNodeStep == node.step
            val matchingLots = lots.filter { it.currentStep == node.step }

            Column(modifier = Modifier.fillMaxWidth()) {
                Card(
                    onClick = {
                        selectedNodeStep = if (isExpanded) null else node.step
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isExpanded) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                    ),
                    border = if (isExpanded) BorderStroke(1.5.dp, node.color) else null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(node.color.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = node.icon,
                                    contentDescription = null,
                                    tint = node.color,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) node.titleHi else node.titleEn,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (language == AppLanguage.HINDI) "प्रभारी: ${node.responsibleRoleHi}" else "Role: ${node.responsibleRoleEn}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (matchingLots.isNotEmpty()) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = node.color.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${matchingLots.size} " + if (language == AppLanguage.HINDI) "लॉट" else "lots",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = node.color,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI) node.descHi else node.descEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                            )

                            if (matchingLots.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (language == AppLanguage.HINDI) "इस चरण पर सक्रिय लॉट्स:" else "Lots currently at this step:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                matchingLots.forEach { lot ->
                                    Surface(
                                        onClick = { onSelectLot(lot) },
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surface,
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.padding(10.dp)
                                        ) {
                                            Column {
                                                Text(
                                                    text = lot.id,
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                Text(
                                                    text = if (language == AppLanguage.HINDI) "${lot.commodityHi} • ${lot.farmer.nameHi}" else "${lot.commodityEn} • ${lot.farmer.name}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            Text(
                                                text = if (language == AppLanguage.HINDI) "देखें ➔" else "View ➔",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MandiGoldSecondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Vertical connector arrow
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
