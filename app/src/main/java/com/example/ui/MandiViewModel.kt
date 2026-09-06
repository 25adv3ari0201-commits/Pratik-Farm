package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppLanguage
import com.example.data.AssayingReport
import com.example.data.Bid
import com.example.data.BuyerProfile
import com.example.data.FarmerProfile
import com.example.data.GateExitPass
import com.example.data.InvoiceAgreement
import com.example.data.LogisticsRecord
import com.example.data.MandiNotification
import com.example.data.MandiRepository
import com.example.data.NotificationType
import com.example.data.PaymentRecord
import com.example.data.ProduceLot
import com.example.data.QualityGrade
import com.example.data.TradeReview
import com.example.data.UserRole
import com.example.data.WeighmentSlip
import com.example.data.WorkflowStep
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class MandiViewModel : ViewModel() {
    private val timeFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _userRole = MutableStateFlow(UserRole.ALL_ACCESS)
    val userRole: StateFlow<UserRole> = _userRole.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _lots = MutableStateFlow<List<ProduceLot>>(emptyList())
    val lots: StateFlow<List<ProduceLot>> = _lots.asStateFlow()

    private val _selectedLot = MutableStateFlow<ProduceLot?>(null)
    val selectedLot: StateFlow<ProduceLot?> = _selectedLot.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Dialog control states
    private val _showNewLotDialog = MutableStateFlow(false)
    val showNewLotDialog: StateFlow<Boolean> = _showNewLotDialog.asStateFlow()

    private val _showAssayingDialog = MutableStateFlow<ProduceLot?>(null)
    val showAssayingDialog: StateFlow<ProduceLot?> = _showAssayingDialog.asStateFlow()

    private val _showBidDialog = MutableStateFlow<ProduceLot?>(null)
    val showBidDialog: StateFlow<ProduceLot?> = _showBidDialog.asStateFlow()

    private val _showProfileDialog = MutableStateFlow(false)
    val showProfileDialog: StateFlow<Boolean> = _showProfileDialog.asStateFlow()

    private val _showNotificationDialog = MutableStateFlow(false)
    val showNotificationDialog: StateFlow<Boolean> = _showNotificationDialog.asStateFlow()

    private val _showReviewDialog = MutableStateFlow<Pair<ProduceLot, UserRole>?>(null)
    val showReviewDialog: StateFlow<Pair<ProduceLot, UserRole>?> = _showReviewDialog.asStateFlow()

    // Profiles
    private val _farmerProfile = MutableStateFlow(MandiRepository.sampleFarmer1)
    val farmerProfile: StateFlow<com.example.data.FarmerProfile> = _farmerProfile.asStateFlow()

    private val _buyerProfile = MutableStateFlow(MandiRepository.sampleBuyers.first())
    val buyerProfile: StateFlow<com.example.data.BuyerProfile> = _buyerProfile.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<MandiNotification>>(emptyList())
    val notifications: StateFlow<List<MandiNotification>> = _notifications.asStateFlow()

    private val _activeToast = MutableStateFlow<MandiNotification?>(null)
    val activeToast: StateFlow<MandiNotification?> = _activeToast.asStateFlow()

    init {
        _lots.value = MandiRepository.getInitialLots()
        _notifications.value = MandiRepository.getInitialNotifications()
        startLiveAuctionTicker()
    }

    private fun startLiveAuctionTicker() {
        viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _lots.update { currentList ->
                    currentList.map { lot ->
                        if (lot.isAuctionLive && lot.auctionTimeRemainingSeconds > 0) {
                            val newRemaining = lot.auctionTimeRemainingSeconds - 1
                            if (newRemaining <= 0) {
                                // Time expired -> Winner Declared -> Moves to Farmer Decision!
                                val winner = lot.winningBid ?: lot.bids.maxByOrNull { it.amountPerQuintal }
                                val updatedLot = lot.copy(
                                    auctionTimeRemainingSeconds = 0,
                                    isAuctionLive = false,
                                    winningBid = winner,
                                    currentStep = WorkflowStep.FARMER_DECISION
                                )
                                // Dispatch end-of-auction notifications
                                val winAmount = winner?.amountPerQuintal ?: lot.reservePrice
                                dispatchNotification(
                                    MandiNotification(
                                        id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                                        type = NotificationType.AUCTION_ENDED,
                                        targetRole = UserRole.FARMER,
                                        titleEn = "Auction Ended: ${lot.commodityEn}",
                                        titleHi = "नीलामी समाप्त: ${lot.commodityHi}",
                                        messageEn = "Bidding concluded at ₹$winAmount/qtl on ${lot.id}. Review and provide your decision.",
                                        messageHi = "${lot.id} पर ₹$winAmount/क्विंटल पर बोली समाप्त हुई। कृपया अपनी सहमति दें।",
                                        lotId = lot.id,
                                        timestamp = System.currentTimeMillis()
                                    )
                                )
                                winner?.let { w ->
                                    dispatchNotification(
                                        MandiNotification(
                                            id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                                            type = NotificationType.BUYER_WON,
                                            targetRole = UserRole.BUYER,
                                            titleEn = "Auction Won: ${lot.commodityEn}",
                                            titleHi = "नीलामी में जीत: ${lot.commodityHi}",
                                            messageEn = "Your bid of ₹$winAmount/qtl won ${lot.id}. Awaiting farmer acceptance.",
                                            messageHi = "${lot.id} पर ₹$winAmount/क्विंटल की आपकी बोली विजेता घोषित हुई।",
                                            lotId = lot.id,
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                }
                                updatedLot
                            } else {
                                // Random simulated competing bids every 14 seconds
                                if (newRemaining % 14 == 0 && newRemaining > 10) {
                                    val currentHigh = lot.winningBid?.amountPerQuintal ?: lot.reservePrice
                                    val buyer = MandiRepository.sampleBuyers.random()
                                    val newAmount = currentHigh + listOf(20.0, 30.0, 50.0).random()
                                    val newBid = Bid(
                                        id = UUID.randomUUID().toString(),
                                        buyerId = buyer.id,
                                        buyerName = buyer.name,
                                        firmName = buyer.firmName,
                                        amountPerQuintal = newAmount,
                                        timestamp = System.currentTimeMillis(),
                                        isWinning = true
                                    )
                                    val updatedBids = lot.bids.map { it.copy(isWinning = false) } + newBid
                                    
                                    // Alert Farmer of new bid
                                    dispatchNotification(
                                        MandiNotification(
                                            id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                                            type = NotificationType.NEW_BID,
                                            targetRole = UserRole.FARMER,
                                            titleEn = "New Bid Placed: ${lot.commodityEn}",
                                            titleHi = "नई बोली लगी: ${lot.commodityHi}",
                                            messageEn = "${buyer.firmName} bid ₹$newAmount/qtl on ${lot.id}",
                                            messageHi = "${buyer.firmName} ने ${lot.id} पर ₹$newAmount/क्विंटल की नई बोली लगाई",
                                            lotId = lot.id,
                                            timestamp = System.currentTimeMillis()
                                        )
                                    )
                                    // Alert previous bidders that they got outbid
                                    lot.winningBid?.let { prev ->
                                        if (prev.buyerId != buyer.id) {
                                            dispatchNotification(
                                                MandiNotification(
                                                    id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                                                    type = NotificationType.OUTBID,
                                                    targetRole = UserRole.BUYER,
                                                    titleEn = "Outbid Alert: ${lot.commodityEn}",
                                                    titleHi = "बोली पिछड़ने की सूचना: ${lot.commodityHi}",
                                                    messageEn = "Higher bid of ₹$newAmount/qtl placed on ${lot.id}. Tap to counter-bid!",
                                                    messageHi = "${lot.id} पर ₹$newAmount/क्विंटल की ऊंची बोली लगाई गई है। पुनः बोली लगाएं!",
                                                    lotId = lot.id,
                                                    timestamp = System.currentTimeMillis()
                                                )
                                            )
                                        }
                                    }

                                    lot.copy(
                                        auctionTimeRemainingSeconds = newRemaining,
                                        bids = updatedBids,
                                        winningBid = newBid
                                    )
                                } else {
                                    lot.copy(auctionTimeRemainingSeconds = newRemaining)
                                }
                            }
                        } else {
                            lot
                        }
                    }
                }
                // Also update selectedLot reference if active
                _selectedLot.value?.let { currentSelected ->
                    _lots.value.find { it.id == currentSelected.id }?.let { updated ->
                        _selectedLot.value = updated
                    }
                }
            }
        }
    }

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == AppLanguage.ENGLISH) AppLanguage.HINDI else AppLanguage.ENGLISH
    }

    fun setUserRole(role: UserRole) {
        _userRole.value = role
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun selectLot(lot: ProduceLot?) {
        _selectedLot.value = lot
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openNewLotDialog() {
        _showNewLotDialog.value = true
    }

    fun closeNewLotDialog() {
        _showNewLotDialog.value = false
    }

    fun openAssayingDialog(lot: ProduceLot) {
        _showAssayingDialog.value = lot
    }

    fun closeAssayingDialog() {
        _showAssayingDialog.value = null
    }

    fun openBidDialog(lot: ProduceLot) {
        _showBidDialog.value = lot
    }

    fun closeBidDialog() {
        _showBidDialog.value = null
    }

    fun openProfileDialog() {
        _showProfileDialog.value = true
    }

    fun closeProfileDialog() {
        _showProfileDialog.value = false
    }

    fun openNotificationDialog() {
        _showNotificationDialog.value = true
    }

    fun closeNotificationDialog() {
        _showNotificationDialog.value = false
    }

    fun openReviewDialog(lot: ProduceLot, reviewerRole: UserRole) {
        _showReviewDialog.value = Pair(lot, reviewerRole)
    }

    fun closeReviewDialog() {
        _showReviewDialog.value = null
    }

    fun dispatchNotification(notification: MandiNotification) {
        _notifications.update { listOf(notification) + it }
        _activeToast.value = notification
    }

    fun dismissActiveToast() {
        _activeToast.value = null
    }

    fun dismissToastNotification() {
        _activeToast.value = null
    }

    fun markNotificationAsRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    fun clearAllNotifications() {
        _notifications.value = emptyList()
    }

    fun submitRatingAndReview(
        lotId: String,
        reviewerRole: UserRole,
        rating: Float,
        reviewText: String,
        tags: List<String> = emptyList()
    ) {
        val targetLot = _lots.value.find { it.id == lotId } ?: return
        val reviewerName = if (reviewerRole == UserRole.FARMER) {
            targetLot.farmer.name
        } else {
            targetLot.winningBid?.firmName ?: targetLot.winningBid?.buyerName ?: _buyerProfile.value.firmName
        }
        val targetName = if (reviewerRole == UserRole.FARMER) {
            targetLot.winningBid?.firmName ?: targetLot.winningBid?.buyerName ?: "Buyer"
        } else {
            targetLot.farmer.name
        }

        val review = TradeReview(
            id = "REV-${UUID.randomUUID().toString().take(6).uppercase()}",
            lotId = lotId,
            reviewerRole = reviewerRole,
            reviewerName = reviewerName,
            targetName = targetName,
            rating = rating,
            reviewText = reviewText,
            timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date()),
            tags = tags
        )

        updateLot(lotId) { lot ->
            if (reviewerRole == UserRole.FARMER) {
                lot.copy(farmerReview = review)
            } else {
                lot.copy(
                    buyerReview = review,
                    buyerRating = rating,
                    buyerFeedback = reviewText
                )
            }
        }

        // Update target profile average rating & reviewsReceived
        if (reviewerRole == UserRole.FARMER) {
            _buyerProfile.update { curr ->
                val newReviews = curr.reviewsReceived + review
                val newCount = curr.ratingCount + 1
                val newRating = ((curr.rating * curr.ratingCount) + rating) / newCount
                curr.copy(
                    rating = (newRating * 10).toInt() / 10f,
                    ratingCount = newCount,
                    reviewsReceived = newReviews
                )
            }
            dispatchNotification(
                MandiNotification(
                    id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                    type = NotificationType.RATING_RECEIVED,
                    targetRole = UserRole.BUYER,
                    titleEn = "New Review from Farmer",
                    titleHi = "किसान से नई रेटिंग व समीक्षा",
                    messageEn = "${targetLot.farmer.name} rated you ${rating}★: \"$reviewText\"",
                    messageHi = "${targetLot.farmer.name} ने आपको ${rating}★ रेटिंग दी: \"$reviewText\"",
                    lotId = lotId,
                    timestamp = System.currentTimeMillis()
                )
            )
        } else {
            _farmerProfile.update { curr ->
                val newReviews = curr.reviewsReceived + review
                val newCount = curr.ratingCount + 1
                val newRating = ((curr.rating * curr.ratingCount) + rating) / newCount
                curr.copy(
                    rating = (newRating * 10).toInt() / 10f,
                    ratingCount = newCount,
                    reviewsReceived = newReviews
                )
            }
            dispatchNotification(
                MandiNotification(
                    id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                    type = NotificationType.RATING_RECEIVED,
                    targetRole = UserRole.FARMER,
                    titleEn = "New Review from Buyer",
                    titleHi = "खरीदार से नई रेटिंग व समीक्षा",
                    messageEn = "$reviewerName rated your produce ${rating}★: \"$reviewText\"",
                    messageHi = "$reviewerName ने आपकी उपज को ${rating}★ रेटिंग दी: \"$reviewText\"",
                    lotId = lotId,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        _showReviewDialog.value = null
    }

    fun updateFarmerProfile(profile: FarmerProfile) {
        _farmerProfile.value = profile
        _language.value = profile.preferredLanguage
    }

    fun updateFarmerProfile(
        name: String,
        nameHi: String,
        phone: String,
        email: String,
        village: String,
        villageHi: String,
        district: String,
        state: String,
        landSizeAcres: Double,
        soilType: String,
        soilTypeHi: String,
        irrigationSource: String,
        irrigationSourceHi: String,
        preferredCrops: List<String>,
        lang: AppLanguage
    ) {
        _farmerProfile.update { curr ->
            curr.copy(
                name = name,
                nameHi = nameHi,
                phone = phone,
                email = email,
                village = village,
                villageHi = villageHi,
                district = district,
                state = state,
                landSizeAcres = landSizeAcres,
                soilType = soilType,
                soilTypeHi = soilTypeHi,
                irrigationSource = irrigationSource,
                irrigationSourceHi = irrigationSourceHi,
                preferredCrops = preferredCrops,
                preferredLanguage = lang
            )
        }
        _language.value = lang
    }

    fun updateBuyerProfile(profile: BuyerProfile) {
        _buyerProfile.value = profile
        _language.value = profile.preferredLanguage
    }

    fun updateBuyerProfile(
        name: String,
        firmName: String,
        phone: String,
        email: String,
        city: String,
        state: String,
        tradeLicenseNo: String,
        gstin: String,
        businessType: String,
        warehouseCapacity: Double,
        preferredGoods: List<String>,
        lang: AppLanguage
    ) {
        _buyerProfile.update { curr ->
            curr.copy(
                name = name,
                firmName = firmName,
                phone = phone,
                email = email,
                city = city,
                state = state,
                tradeLicenseNo = tradeLicenseNo,
                gstin = gstin,
                businessType = businessType,
                warehouseCapacityQuintals = warehouseCapacity,
                preferredGoods = preferredGoods,
                preferredLanguage = lang
            )
        }
        _language.value = lang
    }

    // Workflow actions
    fun createNewLot(
        farmerName: String,
        farmerPhone: String,
        village: String,
        commodityEn: String,
        commodityHi: String,
        varietyEn: String,
        estimatedBags: Int,
        estimatedWeight: Double,
        mspPrice: Double,
        vehicleNumber: String
    ) {
        val newLot = MandiRepository.generateNewLot(
            farmerName, farmerPhone, village, commodityEn, commodityHi, varietyEn,
            estimatedBags, estimatedWeight, mspPrice, vehicleNumber
        )
        // Automatically attach Lot ID and move to Assaying preparation
        val updatedLot = newLot.copy(currentStep = WorkflowStep.ASSAYING)
        _lots.update { listOf(updatedLot) + it }
        _selectedLot.value = updatedLot
        _showNewLotDialog.value = false
    }

    fun submitAssayingReport(
        lotId: String,
        moisture: Double,
        foreignMatter: Double,
        damaged: Double,
        testWeight: Double,
        grade: QualityGrade,
        labTechnician: String
    ) {
        val report = AssayingReport(
            reportId = "LAB-${UUID.randomUUID().toString().take(6).uppercase()}",
            moisturePercent = moisture,
            foreignMatterPercent = foreignMatter,
            damagedGrainPercent = damaged,
            testWeightHl = testWeight,
            grade = grade,
            labTechnician = labTechnician,
            certifiedAt = timeFormat.format(Date())
        )
        updateLot(lotId) { lot ->
            lot.copy(
                assayingReport = report,
                currentStep = WorkflowStep.ONLINE_AUCTION,
                isAuctionLive = true,
                auctionTimeRemainingSeconds = 90
            )
        }
        _showAssayingDialog.value = null
    }

    fun startLiveAuction(lotId: String, durationSeconds: Int = 120) {
        val targetLot = _lots.value.find { it.id == lotId }
        targetLot?.let { lot ->
            dispatchNotification(
                MandiNotification(
                    id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                    type = NotificationType.AUCTION_STARTED,
                    targetRole = UserRole.BUYER,
                    titleEn = "Live Auction Started: ${lot.commodityEn}",
                    titleHi = "लाइव नीलामी शुरू: ${lot.commodityHi}",
                    messageEn = "Bidding open for ${lot.commodityEn} (${lot.id}). Base price: ₹${lot.reservePrice}/qtl.",
                    messageHi = "${lot.commodityHi} (${lot.id}) पर बोली शुरू। आधार मूल्य: ₹${lot.reservePrice}/क्विंटल।",
                    lotId = lot.id,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        updateLot(lotId) { lot ->
            lot.copy(
                currentStep = WorkflowStep.ONLINE_AUCTION,
                isAuctionLive = true,
                auctionTimeRemainingSeconds = durationSeconds
            )
        }
    }

    fun placeBid(lotId: String, amountPerQuintal: Double, buyer: BuyerProfile = MandiRepository.sampleBuyers.first()) {
        val targetLot = _lots.value.find { it.id == lotId }
        targetLot?.let { lot ->
            // Notify farmer
            dispatchNotification(
                MandiNotification(
                    id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                    type = NotificationType.NEW_BID,
                    targetRole = UserRole.FARMER,
                    titleEn = "New Bid Placed: ${lot.commodityEn}",
                    titleHi = "नई बोली प्राप्त: ${lot.commodityHi}",
                    messageEn = "${buyer.firmName} bid ₹$amountPerQuintal/qtl on your ${lot.id}",
                    messageHi = "${buyer.firmName} ने आपके ${lot.id} पर ₹$amountPerQuintal/क्विंटल की नई बोली लगाई",
                    lotId = lot.id,
                    timestamp = System.currentTimeMillis()
                )
            )
            // Outbid notification to previous top bidder
            lot.winningBid?.let { prev ->
                if (prev.buyerId != buyer.id) {
                    dispatchNotification(
                        MandiNotification(
                            id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                            type = NotificationType.OUTBID,
                            targetRole = UserRole.BUYER,
                            titleEn = "Outbid Alert: ${lot.commodityEn}",
                            titleHi = "बोली पिछड़ने की सूचना: ${lot.commodityHi}",
                            messageEn = "You were outbid on ${lot.id}. New top bid is ₹$amountPerQuintal/qtl by ${buyer.firmName}",
                            messageHi = "${lot.id} पर आपकी बोली पिछड़ गई है। नई उच्चतम बोली ₹$amountPerQuintal/क्विंटल है",
                            lotId = lot.id,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
        updateLot(lotId) { lot ->
            val newBid = Bid(
                id = UUID.randomUUID().toString(),
                buyerId = buyer.id,
                buyerName = buyer.name,
                firmName = buyer.firmName,
                amountPerQuintal = amountPerQuintal,
                timestamp = System.currentTimeMillis(),
                isWinning = true
            )
            val updatedBids = lot.bids.map { it.copy(isWinning = false) } + newBid
            lot.copy(
                bids = updatedBids,
                winningBid = newBid
            )
        }
        _showBidDialog.value = null
    }

    fun declareAuctionWinner(lotId: String) {
        val targetLot = _lots.value.find { it.id == lotId }
        updateLot(lotId) { lot ->
            val winner = lot.winningBid ?: lot.bids.maxByOrNull { it.amountPerQuintal }
            val winAmount = winner?.amountPerQuintal ?: lot.reservePrice
            dispatchNotification(
                MandiNotification(
                    id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                    type = NotificationType.AUCTION_ENDED,
                    targetRole = UserRole.FARMER,
                    titleEn = "Auction Finished: ${lot.commodityEn}",
                    titleHi = "नीलामी पूर्ण: ${lot.commodityHi}",
                    messageEn = "Bidding on ${lot.id} concluded at ₹$winAmount/qtl. Please accept or decline.",
                    messageHi = "${lot.id} पर बोली ₹$winAmount/क्विंटल पर समाप्त हुई। कृपया स्वीकृति दें।",
                    lotId = lot.id,
                    timestamp = System.currentTimeMillis()
                )
            )
            winner?.let { w ->
                dispatchNotification(
                    MandiNotification(
                        id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                        type = NotificationType.BUYER_WON,
                        targetRole = UserRole.BUYER,
                        titleEn = "Winning Bid: ${lot.commodityEn}",
                        titleHi = "विजेता बोली: ${lot.commodityHi}",
                        messageEn = "Your bid of ₹$winAmount/qtl on ${lot.id} won! Awaiting farmer confirmation.",
                        messageHi = "${lot.id} पर आपकी ₹$winAmount/क्विंटल की बोली जीत गई! किसान की सहमति प्रतीक्षित है।",
                        lotId = lot.id,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
            lot.copy(
                isAuctionLive = false,
                auctionTimeRemainingSeconds = 0,
                winningBid = winner,
                currentStep = WorkflowStep.FARMER_DECISION
            )
        }
    }

    fun setFarmerDecision(lotId: String, accepted: Boolean) {
        val targetLot = _lots.value.find { it.id == lotId }
        updateLot(lotId) { lot ->
            if (accepted) {
                targetLot?.winningBid?.let { w ->
                    dispatchNotification(
                        MandiNotification(
                            id = "NOTIF-${UUID.randomUUID().toString().take(6)}",
                            type = NotificationType.PURCHASE_SUCCESS,
                            targetRole = UserRole.BUYER,
                            titleEn = "Farmer Accepted Your Bid",
                            titleHi = "किसान ने आपकी बोली स्वीकार की",
                            messageEn = "${lot.farmer.name} accepted your offer of ₹${w.amountPerQuintal}/qtl for ${lot.id}. Proceeding to weighment.",
                            messageHi = "${lot.farmer.name} ने ${lot.id} के लिए आपकी ₹${w.amountPerQuintal}/क्विंटल की बोली स्वीकार कर ली है।",
                            lotId = lot.id,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                }
                lot.copy(
                    farmerAccepted = true,
                    currentStep = WorkflowStep.WEIGHMENT
                )
            } else {
                lot.copy(
                    farmerAccepted = false,
                    currentStep = WorkflowStep.TRADE_DECLINED
                )
            }
        }
    }

    fun reAuctionLot(lotId: String) {
        updateLot(lotId) { lot ->
            lot.copy(
                farmerAccepted = null,
                currentStep = WorkflowStep.ONLINE_AUCTION,
                isAuctionLive = true,
                auctionTimeRemainingSeconds = 120
            )
        }
    }

    fun completeWeighment(lotId: String, grossWeight: Double, tareWeight: Double, scaleOperator: String = "Mahesh Verma (Bridge #1)") {
        updateLot(lotId) { lot ->
            val netWeight = grossWeight - tareWeight
            val netQuintals = netWeight / 100.0
            val slip = WeighmentSlip(
                slipNo = "WB-MND-${(1000..9999).random()}",
                grossWeightKg = grossWeight,
                tareWeightKg = tareWeight,
                netWeightKg = netWeight,
                netQuintals = netQuintals,
                bagCount = lot.estimatedBags,
                weighbridgeOperator = scaleOperator,
                weighmentTime = timeFormat.format(Date())
            )
            // Also prepare Invoice Agreement
            val rate = lot.winningBid?.amountPerQuintal ?: lot.reservePrice
            val grossAmount = netQuintals * rate
            val cess = grossAmount * 0.01
            val netFarmerPayable = grossAmount - cess
            val invoice = InvoiceAgreement(
                invoiceNo = "INV-2026-${(10000..99999).random()}",
                date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
                agreedRatePerQuintal = rate,
                netWeightQuintals = netQuintals,
                grossAmount = grossAmount,
                mandiCessPercent = 1.0,
                mandiCessAmount = cess,
                netFarmerReceivable = netFarmerPayable,
                farmerSignature = "${lot.farmer.name} (OTP Verified)",
                buyerSignature = "${lot.winningBid?.firmName ?: "Licensed Buyer"} Auth Signatory"
            )
            lot.copy(
                weighment = slip,
                invoice = invoice,
                currentStep = WorkflowStep.SALE_INVOICE
            )
        }
    }

    fun proceedToPayment(lotId: String) {
        updateLot(lotId) { lot ->
            lot.copy(currentStep = WorkflowStep.PAYMENT)
        }
    }

    fun processPaymentSettlement(lotId: String) {
        updateLot(lotId) { lot ->
            val amount = lot.invoice?.netFarmerReceivable ?: 100000.0
            val payment = PaymentRecord(
                transactionRef = "DBT-eNAM-${UUID.randomUUID().toString().take(8).uppercase()}",
                method = "Direct Bank Transfer (DBT) e-NAM Escrow",
                amount = amount,
                status = "Settled in Farmer A/C ...${lot.farmer.bankAccountLast4}",
                timestamp = timeFormat.format(Date()),
                beneficiaryAccount = "A/C ...${lot.farmer.bankAccountLast4} (${lot.farmer.ifscCode})"
            )
            lot.copy(
                payment = payment,
                currentStep = WorkflowStep.LOGISTICS
            )
        }
    }

    fun assignLogistics(
        lotId: String,
        vehicleNumber: String,
        driverName: String,
        driverPhone: String,
        destination: String,
        destinationHi: String
    ) {
        val logistics = LogisticsRecord(
            vehicleNumber = vehicleNumber,
            driverName = driverName,
            driverPhone = driverPhone,
            destination = destination,
            destinationHi = destinationHi,
            eWayBillNo = "EWB-${(1000..9999).random()}-${(1000..9999).random()}",
            status = "Truck Dispatched / Gate Clearance Ready"
        )
        val gateExit = GateExitPass(
            passNo = "GXP-${(1000..9999).random()}",
            gateNumber = "Gate #2 (Main Out)",
            securityOfficer = "Inspector Devendra Singh",
            exitTimestamp = timeFormat.format(Date()),
            verifiedWeightKg = lotId.let { id -> _lots.value.find { it.id == id }?.weighment?.grossWeightKg ?: 12000.0 }
        )
        updateLot(lotId) { lot ->
            lot.copy(
                logistics = logistics,
                gateExit = gateExit,
                currentStep = WorkflowStep.GATE_EXIT
            )
        }
    }

    fun confirmGateExit(lotId: String) {
        updateLot(lotId) { lot ->
            lot.copy(
                currentStep = WorkflowStep.BUYER_RECEIVED,
                buyerDelivered = true,
                buyerFeedback = "Consignment received at factory warehouse. Grains verified against Assaying certificate. Full settlement completed.",
                buyerRating = 5.0f
            )
        }
    }

    private fun updateLot(lotId: String, transform: (ProduceLot) -> ProduceLot) {
        _lots.update { list ->
            list.map { if (it.id == lotId) transform(it) else it }
        }
        if (_selectedLot.value?.id == lotId) {
            _selectedLot.value = _lots.value.find { it.id == lotId }
        }
    }
}
