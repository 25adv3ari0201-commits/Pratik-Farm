package com.example

import com.example.data.AppLanguage
import com.example.data.QualityGrade
import com.example.data.UserRole
import com.example.data.WorkflowStep
import com.example.ui.MandiViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class MandiWorkflowTest {

    @Test
    fun testLanguageToggle() {
        val vm = MandiViewModel()
        assertEquals(AppLanguage.ENGLISH, vm.language.value)
        vm.toggleLanguage()
        assertEquals(AppLanguage.HINDI, vm.language.value)
        vm.toggleLanguage()
        assertEquals(AppLanguage.ENGLISH, vm.language.value)
    }

    @Test
    fun testUserRoleSelection() {
        val vm = MandiViewModel()
        assertEquals(UserRole.ALL_ACCESS, vm.userRole.value)
        vm.setUserRole(UserRole.FARMER)
        assertEquals(UserRole.FARMER, vm.userRole.value)
        vm.setUserRole(UserRole.BUYER)
        assertEquals(UserRole.BUYER, vm.userRole.value)
    }

    @Test
    fun testCreateNewProduceLot() {
        val vm = MandiViewModel()
        val initialCount = vm.lots.value.size
        vm.createNewLot(
            farmerName = "Devendra Patel",
            farmerPhone = "+91 99260 55441",
            village = "Khandwa",
            commodityEn = "Soybean",
            commodityHi = "सोयाबीन",
            varietyEn = "JS-9560 (Black)",
            estimatedBags = 120,
            estimatedWeight = 60.0,
            mspPrice = 4892.0,
            vehicleNumber = "MP 12 CA 8899"
        )
        assertEquals(initialCount + 1, vm.lots.value.size)
        val created = vm.lots.value.first()
        assertEquals(WorkflowStep.ASSAYING, created.currentStep)
        assertEquals("Devendra Patel", created.farmer.name)
        assertEquals(120, created.estimatedBags)
    }

    @Test
    fun testAssayingAndAuctionProgression() {
        val vm = MandiViewModel()
        // Find or create a lot at GATE_ENTRY
        val lot = vm.lots.value.firstOrNull { it.currentStep == WorkflowStep.GATE_ENTRY } ?: run {
            vm.createNewLot(
                farmerName = "Test Farmer",
                farmerPhone = "+91 98989 00000",
                village = "Test Village",
                commodityEn = "Wheat",
                commodityHi = "गेहूं",
                varietyEn = "Sharbati",
                estimatedBags = 80,
                estimatedWeight = 40.0,
                mspPrice = 2425.0,
                vehicleNumber = "RJ 14 EA 1111"
            )
            vm.lots.value.first()
        }

        vm.submitAssayingReport(
            lotId = lot.id,
            moisture = 10.5,
            foreignMatter = 0.6,
            damaged = 0.8,
            testWeight = 80.2,
            grade = QualityGrade.GRADE_A,
            labTechnician = "Dr. Test Assayer"
        )

        val updated = vm.lots.value.first { it.id == lot.id }
        assertEquals(WorkflowStep.ONLINE_AUCTION, updated.currentStep)
        assertNotNull(updated.assayingReport)
        assertEquals(QualityGrade.GRADE_A, updated.assayingReport?.grade)
        assertTrue(updated.isAuctionLive)
    }

    @Test
    fun testBiddingAndWinnerDeclaration() {
        val vm = MandiViewModel()
        val auctionLot = vm.lots.value.first { it.currentStep == WorkflowStep.ONLINE_AUCTION }

        val initialBidsCount = auctionLot.bids.size
        vm.placeBid(auctionLot.id, 4100.0)

        val bidUpdated = vm.lots.value.first { it.id == auctionLot.id }
        assertEquals(initialBidsCount + 1, bidUpdated.bids.size)
        assertEquals(4100.0, bidUpdated.winningBid?.amountPerQuintal ?: 0.0, 0.01)

        // Declare Winner
        vm.declareAuctionWinner(auctionLot.id)
        val winnerDeclared = vm.lots.value.first { it.id == auctionLot.id }
        assertEquals(WorkflowStep.FARMER_DECISION, winnerDeclared.currentStep)
    }

    @Test
    fun testFarmerAcceptanceThroughToCompletion() {
        val vm = MandiViewModel()
        val decisionLot = vm.lots.value.firstOrNull { it.currentStep == WorkflowStep.FARMER_DECISION } ?: run {
            val auctionLot = vm.lots.value.first { it.currentStep == WorkflowStep.ONLINE_AUCTION }
            vm.declareAuctionWinner(auctionLot.id)
            vm.lots.value.first { it.id == auctionLot.id }
        }

        // Farmer Accepts
        vm.setFarmerDecision(decisionLot.id, accepted = true)
        val afterAccept = vm.lots.value.first { it.id == decisionLot.id }
        assertEquals(WorkflowStep.WEIGHMENT, afterAccept.currentStep)

        // Electronic Weighment
        val grossWeight = 14500.0
        val tareWeight = 4500.0
        vm.completeWeighment(decisionLot.id, grossWeight, tareWeight)
        val afterWeigh = vm.lots.value.first { it.id == decisionLot.id }
        assertEquals(WorkflowStep.SALE_INVOICE, afterWeigh.currentStep)
        assertNotNull(afterWeigh.weighment)
        assertEquals(100.0, afterWeigh.weighment!!.netQuintals, 0.01)

        // Invoice Check
        assertNotNull(afterWeigh.invoice)
        val inv = afterWeigh.invoice!!
        assertTrue(inv.netFarmerReceivable > 0)
        assertEquals(inv.grossAmount * 0.01, inv.mandiCessAmount, 1.0)

        // Proceed to Payment
        vm.proceedToPayment(decisionLot.id)
        val readyPayment = vm.lots.value.first { it.id == decisionLot.id }
        assertEquals(WorkflowStep.PAYMENT, readyPayment.currentStep)

        // Execute Bank Payment Settlement
        vm.processPaymentSettlement(decisionLot.id)
        val afterPayment = vm.lots.value.first { it.id == decisionLot.id }
        assertEquals(WorkflowStep.LOGISTICS, afterPayment.currentStep)
        assertNotNull(afterPayment.payment)

        // Assign Logistics
        vm.assignLogistics(
            lotId = decisionLot.id,
            vehicleNumber = "HR 55 B 9901",
            driverName = "Suresh Pal",
            driverPhone = "+91 99112 33445",
            destination = "Agro Mill, Rewari",
            destinationHi = "एग्रो मिल, रेवाड़ी"
        )
        val afterLogistics = vm.lots.value.first { it.id == decisionLot.id }
        assertEquals(WorkflowStep.GATE_EXIT, afterLogistics.currentStep)

        // Authorize Gate Exit
        vm.confirmGateExit(decisionLot.id)
        val afterExit = vm.lots.value.first { it.id == decisionLot.id }
        assertEquals(WorkflowStep.BUYER_RECEIVED, afterExit.currentStep)
        assertTrue(afterExit.buyerDelivered)
    }

    @Test
    fun testFarmerDeclineAndReAuction() {
        val vm = MandiViewModel()
        val decisionLot = vm.lots.value.firstOrNull { it.currentStep == WorkflowStep.FARMER_DECISION } ?: run {
            val auctionLot = vm.lots.value.first { it.currentStep == WorkflowStep.ONLINE_AUCTION }
            vm.declareAuctionWinner(auctionLot.id)
            vm.lots.value.first { it.id == auctionLot.id }
        }

        // Farmer Declines
        vm.setFarmerDecision(decisionLot.id, accepted = false)
        val declined = vm.lots.value.first { it.id == decisionLot.id }
        assertEquals(WorkflowStep.TRADE_DECLINED, declined.currentStep)

        // Farmer re-auctions
        vm.reAuctionLot(decisionLot.id)
        val reAuctioned = vm.lots.value.first { it.id == decisionLot.id }
        assertEquals(WorkflowStep.ONLINE_AUCTION, reAuctioned.currentStep)
        assertTrue(reAuctioned.isAuctionLive)
    }

    @Test
    fun testBilateralRatingAndReview() {
        val vm = MandiViewModel()
        val deliveredLot = vm.lots.value.first { it.currentStep == WorkflowStep.BUYER_RECEIVED }

        // Farmer reviews buyer
        val initialBuyerReviews = vm.buyerProfile.value.reviewsReceived.size
        vm.submitRatingAndReview(
            lotId = deliveredLot.id,
            reviewerRole = UserRole.FARMER,
            rating = 5.0f,
            reviewText = "Excellent prompt payment and courteous driver.",
            tags = listOf("Fast Payment", "Fair Weighment")
        )

        val lotAfterFarmerReview = vm.lots.value.first { it.id == deliveredLot.id }
        assertNotNull(lotAfterFarmerReview.farmerReview)
        assertEquals(5.0f, lotAfterFarmerReview.farmerReview?.rating)
        assertEquals(initialBuyerReviews + 1, vm.buyerProfile.value.reviewsReceived.size)

        // Buyer reviews farmer
        val initialFarmerReviews = vm.farmerProfile.value.reviewsReceived.size
        vm.submitRatingAndReview(
            lotId = deliveredLot.id,
            reviewerRole = UserRole.BUYER,
            rating = 4.5f,
            reviewText = "Superb crop quality, low moisture as per assay report.",
            tags = listOf("Top Quality", "Honest Seller")
        )

        val lotAfterBuyerReview = vm.lots.value.first { it.id == deliveredLot.id }
        assertNotNull(lotAfterBuyerReview.buyerReview)
        assertEquals(4.5f, lotAfterBuyerReview.buyerReview?.rating)
        assertEquals(initialFarmerReviews + 1, vm.farmerProfile.value.reviewsReceived.size)
    }

    @Test
    fun testUserProfileManagementAndLanguagePreference() {
        val vm = MandiViewModel()
        val currentFarmer = vm.farmerProfile.value

        // Update farmer profile with Hindi preference
        val updatedFarmer = currentFarmer.copy(
            village = "Narmadapuram",
            preferredLanguage = AppLanguage.HINDI,
            landSizeAcres = 18.5
        )
        vm.updateFarmerProfile(updatedFarmer)

        assertEquals("Narmadapuram", vm.farmerProfile.value.village)
        assertEquals(AppLanguage.HINDI, vm.language.value)
        assertEquals(18.5, vm.farmerProfile.value.landSizeAcres, 0.01)

        // Update buyer profile with English preference
        val currentBuyer = vm.buyerProfile.value
        val updatedBuyer = currentBuyer.copy(
            firmName = "Premier Grain Logistics Corp",
            preferredLanguage = AppLanguage.ENGLISH
        )
        vm.updateBuyerProfile(updatedBuyer)

        assertEquals("Premier Grain Logistics Corp", vm.buyerProfile.value.firmName)
        assertEquals(AppLanguage.ENGLISH, vm.language.value)
    }

    @Test
    fun testRealTimeNotificationsFlow() {
        val vm = MandiViewModel()
        val initialNotificationCount = vm.notifications.value.size

        // Placing a bid triggers notification to farmer and previous outbid buyers
        val auctionLot = vm.lots.value.first { it.currentStep == WorkflowStep.ONLINE_AUCTION }
        val highestCurrent = auctionLot.winningBid?.amountPerQuintal ?: auctionLot.mspBasePrice
        vm.placeBid(auctionLot.id, highestCurrent + 50.0)

        // Verifying notification was dispatched
        assertTrue(vm.notifications.value.size > initialNotificationCount)
        val latestNotif = vm.notifications.value.first()
        assertNotNull(latestNotif)

        // Test marking as read
        vm.markNotificationAsRead(latestNotif.id)
        val readNotif = vm.notifications.value.first { it.id == latestNotif.id }
        assertTrue(readNotif.isRead)

        // Test mark all as read
        vm.markAllNotificationsAsRead()
        assertTrue(vm.notifications.value.all { it.isRead })

        // Test clearing notifications
        vm.clearAllNotifications()
        assertTrue(vm.notifications.value.isEmpty())
    }
}
