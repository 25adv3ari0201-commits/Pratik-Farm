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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotDetailScreen(
    lot: ProduceLot,
    language: AppLanguage,
    userRole: UserRole,
    onBack: () -> Unit,
    onOpenAssaying: () -> Unit,
    onOpenBidDialog: () -> Unit,
    onConcludeAuction: () -> Unit,
    onFarmerDecision: (Boolean) -> Unit,
    onCompleteWeighment: () -> Unit,
    onProceedPayment: () -> Unit,
    onProcessPaymentSettlement: () -> Unit,
    onAssignLogistics: () -> Unit,
    onConfirmGateExit: () -> Unit,
    onReAuction: () -> Unit,
    onOpenReviewDialog: ((ProduceLot, UserRole) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = lot.id,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (language == AppLanguage.HINDI) lot.commodityHi else lot.commodityEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Interactive Stepper Bar showing progress
            InteractiveWorkflowBar(
                currentStep = lot.currentStep,
                farmerAccepted = lot.farmerAccepted,
                language = language,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Layman Voice & Text Guidance Banner
            val (stepTitle, stepExplanation) = getStepGuidance(lot.currentStep, language)
            LaymanVoiceBanner(
                language = language,
                stepTitle = stepTitle,
                stepExplanation = stepExplanation,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Primary Current Step Action Card (Dynamic according to exact workflow!)
            CurrentStepActionCard(
                lot = lot,
                language = language,
                userRole = userRole,
                onOpenAssaying = onOpenAssaying,
                onOpenBidDialog = onOpenBidDialog,
                onConcludeAuction = onConcludeAuction,
                onFarmerDecision = onFarmerDecision,
                onCompleteWeighment = onCompleteWeighment,
                onProceedPayment = onProceedPayment,
                onProcessPaymentSettlement = onProcessPaymentSettlement,
                onAssignLogistics = onAssignLogistics,
                onConfirmGateExit = onConfirmGateExit,
                onReAuction = onReAuction,
                onOpenReviewDialog = onOpenReviewDialog
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Farmer & Lot Registration Details Card
            FarmerAndLotDetailsCard(lot = lot, language = language)

            Spacer(modifier = Modifier.height(16.dp))

            // Quality & Assaying Card (if certified)
            lot.assayingReport?.let { report ->
                QualityCertificateCard(report = report, language = language)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Weighment Card (if completed)
            lot.weighment?.let { slip ->
                WeighmentSlipCard(slip = slip, language = language)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sale Agreement Invoice (if generated)
            lot.invoice?.let { inv ->
                InvoiceAgreementCard(invoice = inv, lot = lot, language = language)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Payment Receipt (if processed)
            lot.payment?.let { pay ->
                PaymentReceiptCard(payment = pay, language = language)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Logistics & Gate Exit Pass (if assigned)
            if (lot.logistics != null || lot.gateExit != null) {
                LogisticsAndGateExitCard(
                    logistics = lot.logistics,
                    gateExit = lot.gateExit,
                    language = language
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Buyer Received Feedback & Bilateral Reviews (if completed)
            if (lot.buyerDelivered || lot.currentStep == WorkflowStep.BUYER_RECEIVED) {
                BuyerReceivedCard(
                    lot = lot,
                    userRole = userRole,
                    language = language,
                    onOpenReviewDialog = onOpenReviewDialog
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CurrentStepActionCard(
    lot: ProduceLot,
    language: AppLanguage,
    userRole: UserRole,
    onOpenAssaying: () -> Unit,
    onOpenBidDialog: () -> Unit,
    onConcludeAuction: () -> Unit,
    onFarmerDecision: (Boolean) -> Unit,
    onCompleteWeighment: () -> Unit,
    onProceedPayment: () -> Unit,
    onProcessPaymentSettlement: () -> Unit,
    onAssignLogistics: () -> Unit,
    onConfirmGateExit: () -> Unit,
    onReAuction: () -> Unit,
    onOpenReviewDialog: ((ProduceLot, UserRole) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI) "वर्तमान सक्रिय चरण (कार्यवाही)" else "Active Action Station",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (lot.isAuctionLive) {
                    AuctionTimerBadge(
                        remainingSeconds = lot.auctionTimeRemainingSeconds,
                        language = language
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (lot.currentStep) {
                WorkflowStep.GATE_ENTRY -> {
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "गेट प्रवेश पर्ची जारी हो चुकी है। उपज को नमूना जांच हेतु लैब में भेजा गया है।"
                        else
                            "Gate Entry verified. Lot token generated. Ready for chemical assaying & quality test.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onOpenAssaying,
                        colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("action_enter_assaying")
                    ) {
                        Icon(Icons.Default.Science, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(MandiStrings.performAssaying(language))
                    }
                }

                WorkflowStep.ASSAYING -> {
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "मंडी लैब केमिस्ट द्वारा गुणवत्ता परीक्षण (नमी, विदेशी पदार्थ, ग्रेड) दर्ज किया जाना है।"
                        else
                            "Assaying in progress. Enter certified moisture, foreign matter and quality grade.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onOpenAssaying,
                        colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("action_enter_assaying")
                    ) {
                        Icon(Icons.Default.Science, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(MandiStrings.performAssaying(language))
                    }
                }

                WorkflowStep.ONLINE_AUCTION -> {
                    Column {
                        // Live Bidding Arena
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = MandiStrings.mspPrice(language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "₹${lot.mspBasePrice.toInt()}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = MandiStrings.currentHighestBid(language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MandiGoldSecondary
                                )
                                Text(
                                    text = "₹${lot.winningBid?.amountPerQuintal?.toInt() ?: lot.reservePrice.toInt()}${MandiStrings.perQuintal(language)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MandiGoldSecondary
                                )
                            }
                        }

                        lot.winningBid?.let { highBid ->
                            Text(
                                text = if (language == AppLanguage.HINDI)
                                    "वर्तमान सर्वोच्च बोलीदाता: ${highBid.buyerName} (${highBid.firmName})"
                                else
                                    "Highest Bidder: ${highBid.buyerName} (${highBid.firmName})",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Live Bids History
                        if (lot.bids.isNotEmpty()) {
                            Text(
                                text = if (language == AppLanguage.HINDI) "ताजा बोलियां (लाइव फीड):" else "Live Bids Stream:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            lot.bids.takeLast(3).reversed().forEach { bid ->
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${bid.firmName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "₹${bid.amountPerQuintal.toInt()}/Qtl",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (bid.isWinning) MandiGoldSecondary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = onOpenBidDialog,
                                colors = ButtonDefaults.buttonColors(containerColor = MandiGoldSecondary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("action_place_bid")
                            ) {
                                Icon(Icons.Default.Gavel, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(MandiStrings.placeBid(language))
                            }

                            FilledTonalButton(
                                onClick = onConcludeAuction,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("action_conclude_auction")
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (language == AppLanguage.HINDI) "नीलामी समाप्त" else "End Auction")
                            }
                        }
                    }
                }

                WorkflowStep.FARMER_DECISION -> {
                    Column {
                        val winningBid = lot.winningBid
                        val price = winningBid?.amountPerQuintal ?: lot.reservePrice
                        val estTotal = price * lot.estimatedWeightQuintals
                        val profitAboveMsp = (price - lot.mspBasePrice) * lot.estimatedWeightQuintals

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (language == AppLanguage.HINDI) "🏆 विजेता बोली का विवरण" else "🏆 Winning Bid Evaluation",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.HINDI) "खरीदार" else "Buyer",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "${winningBid?.firmName ?: "Top Trader"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.HINDI) "अंतिम दर" else "Final Rate",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "₹${price.toInt()}/Qtl",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MandiGoldSecondary
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.HINDI) "एमएसपी से अधिक लाभ" else "Gain over MSP",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = "+₹${profitAboveMsp.toInt()}",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = GradeGreen
                                    )
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = MandiStrings.estimatedTotal(language),
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "₹${estTotal.toInt()}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MandiGreenPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (language == AppLanguage.HINDI)
                                "👨🌾 क्या किसान यह सर्वोच्च बोली स्वीकार करते हैं?"
                            else
                                "👨🌾 Does the Farmer accept this winning price?",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { onFarmerDecision(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("action_farmer_accept")
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(MandiStrings.acceptBid(language))
                            }

                            OutlinedButton(
                                onClick = { onFarmerDecision(false) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                                border = BorderStroke(1.dp, Color(0xFFD32F2F)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("action_farmer_decline")
                            ) {
                                Icon(Icons.Default.Cancel, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(MandiStrings.declineBid(language))
                            }
                        }
                    }
                }

                WorkflowStep.WEIGHMENT -> {
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "किसान ने भाव स्वीकार कर लिया है! अब ट्रैक्टर/ट्रक को इलेक्ट्रॉनिक धर्मकांटा पर तौलें।"
                        else
                            "Farmer accepted the deal! Proceed to Electronic Weighbridge (Dharam Kanta) for exact weight.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onCompleteWeighment,
                        colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("action_weighment")
                    ) {
                        Icon(Icons.Default.Scale, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(MandiStrings.conductWeighment(language))
                    }
                }

                WorkflowStep.SALE_INVOICE -> {
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "तौल पर्ची तैयार है। बिक्री अनुबंध पत्र (Sale Agreement) व चालान जारी किया जा चुका है।"
                        else
                            "Weighment verified. Official e-NAM Sale Agreement generated. Ready for payment settlement.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onProceedPayment,
                        colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("action_proceed_payment")
                    ) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (language == AppLanguage.HINDI) "भुगतान हेतु आगे बढ़ें" else "Proceed to Payment")
                    }
                }

                WorkflowStep.PAYMENT -> {
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "खरीदार का एस्क्रो फंड तैयार है। किसान के बैंक खाते (DBT) में राशि ट्रांसफर करें।"
                        else
                            "Escrow funds secured. Release payment directly into Farmer's bank account via DBT.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onProcessPaymentSettlement,
                        colors = ButtonDefaults.buttonColors(containerColor = GradeGreen),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("action_process_payment")
                    ) {
                        Icon(Icons.Default.Payments, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(MandiStrings.processPayment(language))
                    }
                }

                WorkflowStep.LOGISTICS -> {
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "किसान को भुगतान सफलतापूर्वक पूरा हुआ! अब खरीदार के लिए वाहन व ई-वे बिल जारी करें।"
                        else
                            "Payment settled! Assign transport logistics vehicle and generate e-Way Bill for dispatch.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onAssignLogistics,
                        colors = ButtonDefaults.buttonColors(containerColor = MandiGoldSecondary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("action_dispatch_logistics")
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(MandiStrings.dispatchLogistics(language))
                    }
                }

                WorkflowStep.GATE_EXIT -> {
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "वाहन में माल लोड हो चुका है। गेट सुरक्षा अधिकारी द्वारा गेट निकासी पर्ची जारी करें।"
                        else
                            "Produce loaded on transport truck. Security gate clearance required for exit.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onConfirmGateExit,
                        colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("action_gate_exit")
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(MandiStrings.issueGateExit(language))
                    }
                }

                WorkflowStep.BUYER_RECEIVED -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = GradeGreen,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI)
                                    "सौदा संपूर्ण! खरीदार के कारखाने में माल सुरक्षित प्राप्त हो चुका है।"
                                else
                                    "Trade Completed! Produce successfully received and verified at buyer warehouse.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = GradeGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Review triggers
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (lot.farmerReview == null && (userRole == UserRole.FARMER || userRole == UserRole.ALL_ACCESS)) {
                                Button(
                                    onClick = { onOpenReviewDialog?.invoke(lot, UserRole.FARMER) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("action_farmer_rate_buyer")
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (language == AppLanguage.HINDI) "व्यापारी को रेटिंग दें" else "Rate Buyer")
                                }
                            }
                            if (lot.buyerReview == null && (userRole == UserRole.BUYER || userRole == UserRole.ALL_ACCESS)) {
                                Button(
                                    onClick = { onOpenReviewDialog?.invoke(lot, UserRole.BUYER) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MandiGoldSecondary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("action_buyer_rate_farmer")
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (language == AppLanguage.HINDI) "किसान को रेटिंग दें" else "Rate Farmer")
                                }
                            }
                        }
                    }
                }

                WorkflowStep.TRADE_DECLINED -> {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.HINDI)
                                    "किसान ने इस दर पर सौदा अस्वीकार कर दिया।"
                                else
                                    "Farmer declined the bid. Trade did not complete.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD32F2F)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onReAuction,
                            colors = ButtonDefaults.buttonColors(containerColor = MandiGreenPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("action_re_auction")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(MandiStrings.reAuctionLot(language))
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Composable
fun FarmerAndLotDetailsCard(lot: ProduceLot, language: AppLanguage) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.HINDI) "👨🌾 किसान व लॉट पंजीकरण विवरण" else "👨🌾 Farmer & Produce Intake Details",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))

            DetailRow(
                label = if (language == AppLanguage.HINDI) "किसान का नाम" else "Farmer Name",
                value = if (language == AppLanguage.HINDI) lot.farmer.nameHi else lot.farmer.name
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "गांव / जिला" else "Village / District",
                value = if (language == AppLanguage.HINDI) "${lot.farmer.villageHi}, ${lot.farmer.district}" else "${lot.farmer.village}, ${lot.farmer.district}"
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "मोबाइल नंबर" else "Mobile Phone",
                value = lot.farmer.phone
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "आधार संख्या (अंतिम ४ अंक)" else "Aadhaar (Last 4)",
                value = "•••• •••• ${lot.farmer.aadhaarLast4}"
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "बैंक खाता (DBT सक्षम)" else "Bank Account (DBT)",
                value = "•••• •••• ${lot.farmer.bankAccountLast4} (${lot.farmer.ifscCode})"
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            DetailRow(
                label = if (language == AppLanguage.HINDI) "फसल व किस्म" else "Commodity & Variety",
                value = if (language == AppLanguage.HINDI) "${lot.commodityHi} - ${lot.varietyHi}" else "${lot.commodityEn} - ${lot.varietyEn}"
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "आवक वाहन संख्या" else "Vehicle Number",
                value = lot.vehicleNumber
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "अनुमानित बोरियां / वजन" else "Est. Bags / Weight",
                value = "${lot.estimatedBags} bags (~${lot.estimatedWeightQuintals} Qtl)"
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "सरकारी एमएसपी दर" else "Govt. MSP Base Rate",
                value = "₹${lot.mspBasePrice.toInt()}/Qtl"
            )
        }
    }
}

@Composable
fun QualityCertificateCard(report: com.example.data.AssayingReport, language: AppLanguage) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (language == AppLanguage.HINDI) "🧪 लैब गुणवत्ता प्रमाण-पत्र" else "🧪 Certified Assaying Report",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                QualityGradeBadge(grade = report.grade, language = language)
            }

            Spacer(modifier = Modifier.height(10.dp))

            DetailRow(
                label = MandiStrings.moistureContent(language),
                value = "${report.moisturePercent}% (Standard <12%)"
            )
            DetailRow(
                label = MandiStrings.foreignMatter(language),
                value = "${report.foreignMatterPercent}% (Standard <1.5%)"
            )
            DetailRow(
                label = MandiStrings.damagedGrain(language),
                value = "${report.damagedGrainPercent}% (Standard <2.0%)"
            )
            DetailRow(
                label = MandiStrings.testWeight(language),
                value = "${report.testWeightHl} kg/hL"
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "प्रमाणित लैब केमिस्ट" else "Certified Chemist",
                value = report.labTechnician
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "प्रमाणन समय" else "Certification Time",
                value = report.certifiedAt
            )
        }
    }
}

@Composable
fun WeighmentSlipCard(slip: com.example.data.WeighmentSlip, language: AppLanguage) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.HINDI) "⚖️ धर्मकांटा इलेक्ट्रॉनिक तौल पर्ची" else "⚖️ Certified Weighment Slip",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))

            DetailRow(
                label = if (language == AppLanguage.HINDI) "तौल पर्ची संख्या" else "Weighbridge Slip No",
                value = slip.slipNo
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "सकल वजन (Gross Weight)" else "Gross Weight (Vehicle + Grain)",
                value = "${slip.grossWeightKg.toInt()} kg"
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "खाली वाहन वजन (Tare Weight)" else "Tare Weight (Empty Vehicle)",
                value = "${slip.tareWeightKg.toInt()} kg"
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            DetailRow(
                label = if (language == AppLanguage.HINDI) "वास्तविक शुद्ध वजन (Net Produce)" else "Net Certified Weight",
                value = "${slip.netWeightKg.toInt()} kg (${slip.netQuintals} Quintals)",
                isHighlighted = true
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "तौल ऑपरेटर" else "Weighmaster Operator",
                value = slip.weighbridgeOperator
            )
        }
    }
}

@Composable
fun InvoiceAgreementCard(
    invoice: com.example.data.InvoiceAgreement,
    lot: ProduceLot,
    language: AppLanguage
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.HINDI) "🧾 बिक्री अनुबंध पत्र एवं बिल (Invoice)" else "🧾 Official e-NAM Sale Agreement",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))

            DetailRow(label = if (language == AppLanguage.HINDI) "अनुबंध संख्या" else "Invoice No", value = invoice.invoiceNo)
            DetailRow(label = if (language == AppLanguage.HINDI) "तय दर (प्रति क्विंटल)" else "Agreed Rate", value = "₹${invoice.agreedRatePerQuintal.toInt()}/Qtl")
            DetailRow(label = if (language == AppLanguage.HINDI) "शुद्ध प्रमाणित तौल" else "Net Weight", value = "${invoice.netWeightQuintals} Quintals")
            DetailRow(label = if (language == AppLanguage.HINDI) "सकल राशि (Gross Amount)" else "Gross Amount", value = "₹${invoice.grossAmount.toInt()}")
            DetailRow(label = if (language == AppLanguage.HINDI) "मंडी उपकर (1% APMC Cess)" else "Mandi Cess (1%)", value = "-₹${invoice.mandiCessAmount.toInt()}")
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            DetailRow(
                label = if (language == AppLanguage.HINDI) "किसान को देय शुद्ध राशि" else "Net Payable to Farmer",
                value = "₹${invoice.netFarmerReceivable.toInt()}",
                isHighlighted = true
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "किसान डिजिटल हस्ताक्षर" else "Farmer Digital Sign",
                value = invoice.farmerSignature
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "खरीदार हस्ताक्षर" else "Buyer Sign",
                value = invoice.buyerSignature
            )
        }
    }
}

@Composable
fun PaymentReceiptCard(payment: com.example.data.PaymentRecord, language: AppLanguage) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = GradeGreen.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, GradeGreen),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = GradeGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.HINDI) "💳 बैंक भुगतान रसीद (सफलतापूर्वक जमा)" else "💳 Bank Payment Settlement (DBT)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = GradeGreen
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            DetailRow(
                label = if (language == AppLanguage.HINDI) "ट्रांजेक्शन संदर्भ (UTR No)" else "Transaction UTR Ref",
                value = payment.transactionRef
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "ट्रांसफर राशि" else "Transferred Amount",
                value = "₹${payment.amount.toInt()}",
                isHighlighted = true
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "लाभार्थी खाता" else "Beneficiary Account",
                value = payment.beneficiaryAccount
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "स्थिति" else "Status",
                value = payment.status
            )
            DetailRow(
                label = if (language == AppLanguage.HINDI) "भुगतान समय" else "Settlement Time",
                value = payment.timestamp
            )
        }
    }
}

@Composable
fun LogisticsAndGateExitCard(
    logistics: com.example.data.LogisticsRecord?,
    gateExit: com.example.data.GateExitPass?,
    language: AppLanguage
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (language == AppLanguage.HINDI) "🚚 परिवहन एवं गेट निकासी पास" else "🚚 Logistics & Gate Exit Pass",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(10.dp))

            logistics?.let { log ->
                DetailRow(
                    label = if (language == AppLanguage.HINDI) "प्रेषण ट्रक संख्या" else "Dispatch Vehicle No",
                    value = log.vehicleNumber
                )
                DetailRow(
                    label = if (language == AppLanguage.HINDI) "चालक का नाम व मोबाइल" else "Driver & Phone",
                    value = "${log.driverName} (${log.driverPhone})"
                )
                DetailRow(
                    label = if (language == AppLanguage.HINDI) "गंतव्य गोदाम / मिल" else "Destination",
                    value = if (language == AppLanguage.HINDI) log.destinationHi else log.destination
                )
                DetailRow(
                    label = if (language == AppLanguage.HINDI) "ई-वे बिल संख्या" else "e-Way Bill No",
                    value = log.eWayBillNo
                )
            }

            gateExit?.let { gxp ->
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                DetailRow(
                    label = if (language == AppLanguage.HINDI) "गेट निकास पास संख्या" else "Gate Exit Pass No",
                    value = gxp.passNo
                )
                DetailRow(
                    label = if (language == AppLanguage.HINDI) "प्रस्थान गेट" else "Exit Gate",
                    value = gxp.gateNumber
                )
                DetailRow(
                    label = if (language == AppLanguage.HINDI) "सत्यापन सुरक्षा अधिकारी" else "Security Officer",
                    value = gxp.securityOfficer
                )
            }
        }
    }
}

@Composable
fun BuyerReceivedCard(
    lot: ProduceLot,
    userRole: UserRole,
    language: AppLanguage,
    onOpenReviewDialog: ((ProduceLot, UserRole) -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        tint = GradeGreen,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI) "⭐ द्विपक्षीय रेटिंग एवं समीक्षा" else "⭐ Bilateral Ratings & Reviews",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (language == AppLanguage.HINDI)
                    "सौदा संपन्न होने के बाद किसान एवं खरीदार दोनों एक-दूसरे को स्टार रेटिंग और समीक्षा प्रदान करते हैं।"
                else
                    "After transaction delivery, both farmer and buyer submit verified reviews for each other.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Farmer's Review of Buyer
            BilateralReviewCard(
                title = if (language == AppLanguage.HINDI) "किसान द्वारा व्यापारी की समीक्षा" else "Farmer's Review for Buyer",
                roleBadge = "Farmer",
                reviewerName = lot.farmer.name,
                review = lot.farmerReview,
                userRole = userRole,
                canLeaveReview = lot.farmerReview == null && (userRole == UserRole.FARMER || userRole == UserRole.ALL_ACCESS),
                language = language,
                onLeaveReviewClick = { onOpenReviewDialog?.invoke(lot, UserRole.FARMER) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Buyer's Review of Farmer
            val buyerDisplayName = lot.winningBid?.firmName ?: lot.winningBid?.buyerName ?: "Licensed Buyer"
            BilateralReviewCard(
                title = if (language == AppLanguage.HINDI) "खरीदार द्वारा किसान की समीक्षा" else "Buyer's Review for Farmer",
                roleBadge = "Buyer",
                reviewerName = buyerDisplayName,
                review = lot.buyerReview,
                userRole = userRole,
                canLeaveReview = lot.buyerReview == null && (userRole == UserRole.BUYER || userRole == UserRole.ALL_ACCESS),
                language = language,
                onLeaveReviewClick = { onOpenReviewDialog?.invoke(lot, UserRole.BUYER) }
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, isHighlighted: Boolean = false) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlighted) MandiGreenPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}

fun getStepGuidance(step: WorkflowStep, language: AppLanguage): Pair<String, String> {
    return when (step) {
        WorkflowStep.FARMER_REGISTRATION -> Pair(
            "पंजीकरण",
            if (language == AppLanguage.HINDI)
                "किसान पंजीकरण हो चुका है। अब उपज को मंडी प्रांगण में लाने की तैयारी है।"
            else
                "Farmer is registered in the national e-Mandi registry. Ready to bring produce."
        )
        WorkflowStep.BRING_PRODUCE -> Pair(
            "उपज आवक",
            if (language == AppLanguage.HINDI)
                "वाहन मंडी के मुख्य द्वार पर आ चुका है। सुरक्षा अधिकारी द्वारा गेट पर्ची जारी की जा रही है।"
            else
                "Produce vehicle arrived at Mandi gates. Awaiting digital security pass."
        )
        WorkflowStep.GATE_ENTRY -> Pair(
            "गेट प्रवेश",
            if (language == AppLanguage.HINDI)
                "गेट पर्ची बन चुकी है। अब लॉट आईडी बनाकर उपज का सैंपल लैब परीक्षण के लिए भेजा जाएगा।"
            else
                "Gate Entry generated. LOT ID tagged to produce before sending for chemical assaying."
        )
        WorkflowStep.LOT_CREATION -> Pair(
            "लॉट निर्मित",
            if (language == AppLanguage.HINDI)
                "लॉट संख्या निर्धारित हो गई है। गुणवत्ता विश्लेषक लैब में नमूना जांच कर रहे हैं।"
            else
                "LOT ID created. Produce sample submitted to Mandi Assaying Laboratory."
        )
        WorkflowStep.ASSAYING -> Pair(
            "गुणवत्ता जांच (लैब परख)",
            if (language == AppLanguage.HINDI)
                "लैब में नमी (Moisture) और कचरे की जांच हो रही है। रिपोर्ट आते ही ऑनलाइन नीलामी शुरू होगी।"
            else
                "Lab assaying is measuring moisture %, purity and seed density. Auction starts after certification."
        )
        WorkflowStep.QUALITY_ATTACHED -> Pair(
            "प्रमाण-पत्र संलग्न",
            if (language == AppLanguage.HINDI)
                "गुणवत्ता प्रमाण-पत्र लॉट के साथ जुड़ गया है। अब व्यापारी ऑनलाइन बोलियां लगा सकते हैं।"
            else
                "Quality grade permanently tagged to lot. Online bidding window is opening."
        )
        WorkflowStep.ONLINE_AUCTION -> Pair(
            "लाइव ई-नीलामी",
            if (language == AppLanguage.HINDI)
                "नीलामी चल रही है! व्यापारी लाइव बोलियां लगा रहे हैं। समय समाप्त होने पर उच्चतम बोली विजेता बनेगी।"
            else
                "Auction is LIVE! Traders across India are placing competitive bids in real time."
        )
        WorkflowStep.WINNER_DECLARED -> Pair(
            "विजेता घोषित",
            if (language == AppLanguage.HINDI)
                "नीलामी समाप्त हो गई है। सर्वोच्च बोली तय हो चुकी है। अब किसान की सहमति आवश्यक है।"
            else
                "Highest bid locked as winner. Waiting for farmer's acceptance decision."
        )
        WorkflowStep.FARMER_DECISION -> Pair(
            "किसान सहमति",
            if (language == AppLanguage.HINDI)
                "किसान भाई: कृपया विजेता बोली की जांच करें। यदि भाव पसंद है तो 'स्वीकार' दबाएं अन्यथा 'अस्वीकार' करें।"
            else
                "Farmer: Review the top bid price. If satisfied, click ACCEPT to proceed to weighment, or DECLINE."
        )
        WorkflowStep.WEIGHMENT -> Pair(
            "धर्मकांटा तौल",
            if (language == AppLanguage.HINDI)
                "किसान ने भाव स्वीकार कर लिया है! अब इलेक्ट्रॉनिक धर्मकांटा पर शुद्ध वजन काटा जा रहा है।"
            else
                "Farmer accepted! Electronic weighbridge measures gross and tare to determine certified net weight."
        )
        WorkflowStep.SALE_INVOICE -> Pair(
            "बिक्री अनुबंध",
            if (language == AppLanguage.HINDI)
                "तौल के आधार पर अंतिम बिल बन गया है। अब खरीदार से किसान के खाते में भुगतान होगा।"
            else
                "Sale Agreement signed with Mandi fee deductions. Ready for escrow bank transfer."
        )
        WorkflowStep.PAYMENT -> Pair(
            "बैंक भुगतान (DBT)",
            if (language == AppLanguage.HINDI)
                "भुगतान राशि सीधे किसान के बैंक खाते में DBT द्वारा तुरंत ट्रांसफर की जा रही है।"
            else
                "Direct Bank Transfer (DBT) is being processed directly into the farmer's verified bank account."
        )
        WorkflowStep.LOGISTICS -> Pair(
            "परिवहन प्रेषण",
            if (language == AppLanguage.HINDI)
                "किसान को पैसा मिल चुका है! माल को ट्रक में लोड कर ई-वे बिल के साथ रवाना किया जा रहा है।"
            else
                "Farmer fully paid! Transport logistics assigned with electronic e-Way bill."
        )
        WorkflowStep.GATE_EXIT -> Pair(
            "गेट निकासी",
            if (language == AppLanguage.HINDI)
                "गेट सुरक्षा द्वारा भुगतान व तौल रसीद का अंतिम सत्यापन किया जा रहा है।"
            else
                "Security clearance at exit gate. Gate exit pass issued after vehicle weigh check."
        )
        WorkflowStep.BUYER_RECEIVED -> Pair(
            "माल सुपुर्दगी पूर्ण",
            if (language == AppLanguage.HINDI)
                "खरीदार ने अपने गोदाम पर माल प्राप्त कर लिया है। यह सौदा सफलतापूर्वक संपन्न हुआ!"
            else
                "Buyer received and verified the produce at destination warehouse. Trade cycle complete!"
        )
        WorkflowStep.TRADE_DECLINED -> Pair(
            "सौदा अस्वीकृत",
            if (language == AppLanguage.HINDI)
                "किसान ने भाव कम होने पर बोली अस्वीकार कर दी। लॉट को दोबारा नीलामी में डाला जा सकता है।"
            else
                "Farmer declined the offer. Lot can be scheduled for re-auction or withdrawn."
        )
    }
}
