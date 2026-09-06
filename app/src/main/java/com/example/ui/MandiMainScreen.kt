package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppLanguage
import com.example.data.MandiStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MandiMainScreen(
    viewModel: MandiViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val userRole by viewModel.userRole.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val lots by viewModel.lots.collectAsStateWithLifecycle()
    val selectedLot by viewModel.selectedLot.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val showNewLotDialog by viewModel.showNewLotDialog.collectAsStateWithLifecycle()
    val showAssayingDialog by viewModel.showAssayingDialog.collectAsStateWithLifecycle()
    val showBidDialog by viewModel.showBidDialog.collectAsStateWithLifecycle()

    // Profile, Review, Notification States
    val showProfileDialog by viewModel.showProfileDialog.collectAsStateWithLifecycle()
    val showNotificationDialog by viewModel.showNotificationDialog.collectAsStateWithLifecycle()
    val showReviewDialog by viewModel.showReviewDialog.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val activeToastNotification by viewModel.activeToast.collectAsStateWithLifecycle()
    val farmerProfile by viewModel.farmerProfile.collectAsStateWithLifecycle()
    val buyerProfile by viewModel.buyerProfile.collectAsStateWithLifecycle()
    val unreadNotificationsCount = notifications.count { !it.isRead }

    Scaffold(
        topBar = {
            if (selectedLot == null) {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Agriculture,
                                        contentDescription = "Mandi Logo",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = MandiStrings.appName(language),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = MandiStrings.appSubtitle(language),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        actions = {
                            // Real-time Notification Bell with Badge (Req 3)
                            IconButton(
                                onClick = { viewModel.openNotificationDialog() },
                                modifier = Modifier.testTag("action_open_notifications")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (unreadNotificationsCount > 0) {
                                            Badge(
                                                containerColor = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.testTag("notification_badge")
                                            ) {
                                                Text("$unreadNotificationsCount")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifications",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            // User Profile Icon (Req 2)
                            IconButton(
                                onClick = { viewModel.openProfileDialog() },
                                modifier = Modifier.testTag("action_open_profile")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "User Profile",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Language Switcher
                            LanguageSwitchButton(
                                currentLanguage = language,
                                onToggle = { viewModel.toggleLanguage() },
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    // Role Selector Row (Farmer / Buyer / Inspector / All Access)
                    RoleSelectorRow(
                        currentRole = userRole,
                        onRoleSelected = { viewModel.setUserRole(it) },
                        language = language
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Real-time Notification Toast Banner (Req 3)
                NotificationToastBanner(
                    notification = activeToastNotification,
                    language = language,
                    onOpenLot = { lotId ->
                        lots.find { it.id == lotId }?.let { viewModel.selectLot(it) }
                    },
                    onDismiss = { viewModel.dismissToastNotification() }
                )

                AnimatedContent(
                    targetState = selectedLot,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "screen_transition",
                    modifier = Modifier.weight(1f)
                ) { targetLot ->
                    if (targetLot == null) {
                        LotsOverviewScreen(
                            lots = lots,
                            selectedTab = selectedTab,
                            onTabSelected = { viewModel.selectTab(it) },
                            searchQuery = searchQuery,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                            userRole = userRole,
                            language = language,
                            onSelectLot = { viewModel.selectLot(it) },
                            onOpenNewLot = { viewModel.openNewLotDialog() }
                        )
                    } else {
                        LotDetailScreen(
                            lot = targetLot,
                            language = language,
                            userRole = userRole,
                            onBack = { viewModel.selectLot(null) },
                            onOpenAssaying = { viewModel.openAssayingDialog(targetLot) },
                            onOpenBidDialog = { viewModel.openBidDialog(targetLot) },
                            onConcludeAuction = { viewModel.declareAuctionWinner(targetLot.id) },
                            onFarmerDecision = { accept -> viewModel.setFarmerDecision(targetLot.id, accept) },
                            onCompleteWeighment = {
                                val gross = targetLot.estimatedWeightQuintals * 100.0 + 4120.0
                                val tare = 4120.0
                                viewModel.completeWeighment(targetLot.id, gross, tare)
                            },
                            onProceedPayment = { viewModel.proceedToPayment(targetLot.id) },
                            onProcessPaymentSettlement = { viewModel.processPaymentSettlement(targetLot.id) },
                            onAssignLogistics = {
                                viewModel.assignLogistics(
                                    lotId = targetLot.id,
                                    vehicleNumber = "DL 01 AA 7721 (Multi-Axle Truck)",
                                    driverName = "Mukesh Kumar",
                                    driverPhone = "+91 98101 22910",
                                    destination = "Central Agrawal Mill Warehouse #4, Indore",
                                    destinationHi = "सेंट्रल अग्रवाल मिल गोदाम #४, इंदौर"
                                )
                            },
                            onConfirmGateExit = { viewModel.confirmGateExit(targetLot.id) },
                            onReAuction = { viewModel.reAuctionLot(targetLot.id) },
                            onOpenReviewDialog = { lot, role ->
                                viewModel.openReviewDialog(lot, role)
                            }
                        )
                    }
                }
            }
        }

        // Dialogs
        if (showNewLotDialog) {
            NewLotDialog(
                language = language,
                onDismiss = { viewModel.closeNewLotDialog() },
                onSubmit = { name, phone, village, cEn, cHi, variety, bags, weight, msp, veh ->
                    viewModel.createNewLot(name, phone, village, cEn, cHi, variety, bags, weight, msp, veh)
                }
            )
        }

        showAssayingDialog?.let { lot ->
            AssayingDialog(
                lot = lot,
                language = language,
                onDismiss = { viewModel.closeAssayingDialog() },
                onSubmit = { m, fm, d, tw, grade, tech ->
                    viewModel.submitAssayingReport(lot.id, m, fm, d, tw, grade, tech)
                }
            )
        }

        showBidDialog?.let { lot ->
            PlaceBidDialog(
                lot = lot,
                language = language,
                onDismiss = { viewModel.closeBidDialog() },
                onSubmitBid = { amount ->
                    viewModel.placeBid(lot.id, amount)
                }
            )
        }

        // Detailed User Profile Dialog (Req 2)
        if (showProfileDialog) {
            UserProfileDialog(
                farmerProfile = farmerProfile,
                buyerProfile = buyerProfile,
                allLots = lots,
                currentRole = userRole,
                language = language,
                onLanguageSelected = { viewModel.setLanguage(it) },
                onUpdateFarmer = { viewModel.updateFarmerProfile(it) },
                onUpdateBuyer = { viewModel.updateBuyerProfile(it) },
                onSelectLot = { viewModel.selectLot(it) },
                onDismiss = { viewModel.closeProfileDialog() }
            )
        }

        // Real-Time Notification Center Dialog (Req 3)
        if (showNotificationDialog) {
            NotificationCenterDialog(
                notifications = notifications,
                language = language,
                currentRole = userRole,
                onMarkAllAsRead = { viewModel.markAllNotificationsAsRead() },
                onClearAll = { viewModel.clearAllNotifications() },
                onNotificationClick = { notif ->
                    viewModel.markNotificationAsRead(notif.id)
                    notif.lotId?.let { lotId ->
                        lots.find { it.id == lotId }?.let { viewModel.selectLot(it) }
                        viewModel.closeNotificationDialog()
                    }
                },
                onDismiss = { viewModel.closeNotificationDialog() }
            )
        }

        // Bilateral Rating & Review Dialog (Req 1)
        showReviewDialog?.let { (lot, role) ->
            RatingReviewDialog(
                lot = lot,
                reviewerRole = role,
                language = language,
                onDismiss = { viewModel.closeReviewDialog() },
                onSubmit = { rating, reviewText, tags ->
                    viewModel.submitRatingAndReview(
                        lotId = lot.id,
                        reviewerRole = role,
                        rating = rating,
                        reviewText = reviewText,
                        tags = tags
                    )
                }
            )
        }
    }
}
