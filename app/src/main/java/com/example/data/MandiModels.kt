package com.example.data

enum class UserRole {
    FARMER,
    BUYER,
    INSPECTOR,
    ALL_ACCESS
}

enum class WorkflowStep(val stepIndex: Int, val iconName: String) {
    FARMER_REGISTRATION(1, "person"),
    BRING_PRODUCE(2, "agriculture"),
    GATE_ENTRY(3, "meeting_room"),
    LOT_CREATION(4, "qr_code"),
    ASSAYING(5, "science"),
    QUALITY_ATTACHED(6, "verified"),
    ONLINE_AUCTION(7, "gavel"),
    WINNER_DECLARED(8, "emoji_events"),
    FARMER_DECISION(9, "thumbs_up_down"),
    WEIGHMENT(10, "scale"),
    SALE_INVOICE(11, "receipt_long"),
    PAYMENT(12, "payments"),
    LOGISTICS(13, "local_shipping"),
    GATE_EXIT(14, "exit_to_app"),
    BUYER_RECEIVED(15, "done_all"),
    TRADE_DECLINED(99, "cancel")
}

enum class QualityGrade(val label: String, val descriptionEn: String, val descriptionHi: String) {
    GRADE_A("Grade A (FAQ+)", "Premium Quality / Lowest Foreign Matter", "सर्वोच्च गुणवत्ता (प्रीमियम) / न्यूनतम कचरा"),
    GRADE_B("Grade B (FAQ)", "Fair Average Quality / Standard Mandi Spec", "औसत गुणवत्ता / सामान्य मंडी मानक"),
    GRADE_C("Grade C (Sub-FAQ)", "Higher Moisture / Suitable for Milling", "अधिक नमी / मिलिंग हेतु उपयुक्त")
}

data class TradeReview(
    val id: String,
    val lotId: String,
    val reviewerRole: UserRole,
    val reviewerName: String,
    val targetName: String,
    val rating: Float,
    val reviewText: String,
    val timestamp: String,
    val tags: List<String> = emptyList()
)

data class FarmerProfile(
    val id: String,
    val name: String,
    val nameHi: String,
    val phone: String,
    val aadhaarLast4: String,
    val village: String,
    val villageHi: String,
    val district: String,
    val bankAccountLast4: String,
    val ifscCode: String,
    val upiId: String,
    val email: String = "farmer@krishi.gov.in",
    val state: String = "Madhya Pradesh",
    val landSizeAcres: Double = 14.5,
    val soilType: String = "Black Cotton (Regur)",
    val soilTypeHi: String = "काली कपासी मिट्टी",
    val irrigationSource: String = "Tubewell & Canal",
    val irrigationSourceHi: String = "नलकूप एवं नहर",
    val equipmentOwned: List<String> = listOf("Tractor 45HP", "Rotavator", "Seed Drill"),
    val preferredCrops: List<String> = listOf("Wheat (Gehun)", "Soybean", "Gram (Chana)", "Mustard (Sarson)"),
    val preferredLanguage: AppLanguage = AppLanguage.ENGLISH,
    val rating: Float = 4.9f,
    val ratingCount: Int = 14,
    val reviewsReceived: List<TradeReview> = emptyList()
)

data class BuyerProfile(
    val id: String,
    val name: String,
    val firmName: String,
    val tradeLicenseNo: String,
    val rating: Float,
    val phone: String,
    val city: String,
    val email: String = "procure@agrotrade.com",
    val state: String = "Madhya Pradesh",
    val gstin: String = "23AAACB1234F1Z8",
    val businessType: String = "Commodity Processor & Oil Mill",
    val warehouseCapacityQuintals: Double = 35000.0,
    val preferredGoods: List<String> = listOf("Soybean", "Wheat", "Mustard", "Gram"),
    val preferredLanguage: AppLanguage = AppLanguage.ENGLISH,
    val ratingCount: Int = 22,
    val reviewsReceived: List<TradeReview> = emptyList()
)

enum class NotificationType {
    NEW_BID,
    AUCTION_STARTED,
    AUCTION_ENDED,
    BUYER_WON,
    OUTBID,
    PURCHASE_SUCCESS,
    RATING_RECEIVED,
    PAYMENT_CONFIRMED
}

data class MandiNotification(
    val id: String,
    val type: NotificationType,
    val targetRole: UserRole,
    val titleEn: String,
    val titleHi: String,
    val messageEn: String,
    val messageHi: String,
    val lotId: String? = null,
    val timestamp: Long,
    val isRead: Boolean = false
)

data class AssayingReport(
    val reportId: String,
    val moisturePercent: Double,
    val foreignMatterPercent: Double,
    val damagedGrainPercent: Double,
    val testWeightHl: Double,
    val grade: QualityGrade,
    val labTechnician: String,
    val certifiedAt: String,
    val labSealVerified: Boolean = true
)

data class Bid(
    val id: String,
    val buyerId: String,
    val buyerName: String,
    val firmName: String,
    val amountPerQuintal: Double,
    val timestamp: Long,
    val isWinning: Boolean = false
)

data class WeighmentSlip(
    val slipNo: String,
    val grossWeightKg: Double,
    val tareWeightKg: Double,
    val netWeightKg: Double,
    val netQuintals: Double,
    val bagCount: Int,
    val weighbridgeOperator: String,
    val weighmentTime: String
)

data class InvoiceAgreement(
    val invoiceNo: String,
    val date: String,
    val agreedRatePerQuintal: Double,
    val netWeightQuintals: Double,
    val grossAmount: Double,
    val mandiCessPercent: Double = 1.0,
    val mandiCessAmount: Double,
    val netFarmerReceivable: Double,
    val farmerSignature: String,
    val buyerSignature: String
)

data class PaymentRecord(
    val transactionRef: String,
    val method: String,
    val amount: Double,
    val status: String,
    val timestamp: String,
    val beneficiaryAccount: String
)

data class LogisticsRecord(
    val vehicleNumber: String,
    val driverName: String,
    val driverPhone: String,
    val destination: String,
    val destinationHi: String,
    val eWayBillNo: String,
    val status: String
)

data class GateExitPass(
    val passNo: String,
    val gateNumber: String,
    val securityOfficer: String,
    val exitTimestamp: String,
    val verifiedWeightKg: Double
)

data class ProduceLot(
    val id: String,
    val commodityEn: String,
    val commodityHi: String,
    val varietyEn: String,
    val varietyHi: String,
    val farmer: FarmerProfile,
    val vehicleNumber: String,
    val arrivalTime: String,
    val estimatedBags: Int,
    val estimatedWeightQuintals: Double,
    val mspBasePrice: Double,
    val reservePrice: Double,
    val currentStep: WorkflowStep,
    val assayingReport: AssayingReport? = null,
    val bids: List<Bid> = emptyList(),
    val winningBid: Bid? = null,
    val auctionTimeRemainingSeconds: Int = 0,
    val isAuctionLive: Boolean = false,
    val farmerAccepted: Boolean? = null,
    val weighment: WeighmentSlip? = null,
    val invoice: InvoiceAgreement? = null,
    val payment: PaymentRecord? = null,
    val logistics: LogisticsRecord? = null,
    val gateExit: GateExitPass? = null,
    val buyerDelivered: Boolean = false,
    val buyerFeedback: String? = null,
    val buyerRating: Float? = null,
    val farmerReview: TradeReview? = null,
    val buyerReview: TradeReview? = null
)
