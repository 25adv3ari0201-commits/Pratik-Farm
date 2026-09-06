package com.example.data

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिंदी")
}

object MandiStrings {
    fun appName(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Smart Mandi"
        AppLanguage.HINDI -> "स्मार्ट मंडी"
    }

    fun appSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Digital Agri Marketplace & e-Auction"
        AppLanguage.HINDI -> "डिजिटल कृषि विपणन एवं ई-नीलामी"
    }

    // Role switcher
    fun roleFarmer(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Farmer Mode"
        AppLanguage.HINDI -> "किसान मोड"
    }

    fun roleBuyer(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Trader / Buyer"
        AppLanguage.HINDI -> "व्यापारी / खरीदार"
    }

    fun roleInspector(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Mandi Officer"
        AppLanguage.HINDI -> "मंडी अधिकारी"
    }

    fun roleOverview(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Full Workflow"
        AppLanguage.HINDI -> "संपूर्ण कार्यप्रवाह"
    }

    // Tabs
    fun tabLiveAuctions(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Live Auctions"
        AppLanguage.HINDI -> "लाइव नीलामी"
    }

    fun tabMyLots(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Mandi Lots"
        AppLanguage.HINDI -> "मंडी लॉट्स"
    }

    fun tabWorkflowMap(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Workflow Guide"
        AppLanguage.HINDI -> "कार्यप्रवाह गाइड"
    }

    fun tabCompleted(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Completed Trades"
        AppLanguage.HINDI -> "पूर्ण सौदे"
    }

    // Workflow Steps
    fun stepFarmerReg(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "1. Farmer Registration"
        AppLanguage.HINDI -> "1. किसान पंजीकरण"
    }

    fun stepBringProduce(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "2. Bring Produce"
        AppLanguage.HINDI -> "2. उपज मंडी लाना"
    }

    fun stepGateEntry(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "3. Gate Entry Pass"
        AppLanguage.HINDI -> "3. गेट प्रवेश पर्ची"
    }

    fun stepLotCreation(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "4. Create LOT ID"
        AppLanguage.HINDI -> "4. लॉट संख्या बनाएं"
    }

    fun stepAssaying(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "5. Lab Assaying"
        AppLanguage.HINDI -> "5. गुणवत्ता जांच / लैब परख"
    }

    fun stepQualityAttached(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "6. Quality Attached"
        AppLanguage.HINDI -> "6. गुणवत्ता प्रमाण-पत्र संलग्न"
    }

    fun stepOnlineAuction(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "7. Online Auction"
        AppLanguage.HINDI -> "7. लाइव ई-नीलामी"
    }

    fun stepWinner(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "8. Winner Declared"
        AppLanguage.HINDI -> "8. विजेता बोली घोषित"
    }

    fun stepFarmerDecision(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "9. Farmer Decision"
        AppLanguage.HINDI -> "9. किसान सहमति (स्वीकृति)"
    }

    fun stepWeighment(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "10. Weighment (Dharam Kanta)"
        AppLanguage.HINDI -> "10. धर्मकांटा तौल माप"
    }

    fun stepInvoice(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "11. Sale Agreement & Invoice"
        AppLanguage.HINDI -> "11. बिक्री अनुबंध व चालान"
    }

    fun stepPayment(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "12. Payment Settlement"
        AppLanguage.HINDI -> "12. बैंक भुगतान / ट्रांसफर"
    }

    fun stepLogistics(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "13. Logistics & Vehicle"
        AppLanguage.HINDI -> "13. वाहन प्रेषण / परिवहन"
    }

    fun stepGateExit(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "14. Gate Exit Pass"
        AppLanguage.HINDI -> "14. गेट निकासी पर्ची"
    }

    fun stepBuyerReceived(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "15. Buyer Receives Produce"
        AppLanguage.HINDI -> "15. खरीदार को माल सुपुर्द"
    }

    fun stepTradeDeclined(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Trade Declined by Farmer"
        AppLanguage.HINDI -> "किसान द्वारा बोली अस्वीकृत"
    }

    // Actions & Buttons
    fun registerNewProduce(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "New Gate Entry & Lot"
        AppLanguage.HINDI -> "नया गेट प्रवेश व लॉट दर्ज करें"
    }

    fun placeBid(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Place Bid"
        AppLanguage.HINDI -> "बोली लगाएं"
    }

    fun acceptBid(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Accept Bid (Proceed)"
        AppLanguage.HINDI -> "बोली स्वीकार करें (आगे बढ़ें)"
    }

    fun declineBid(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Decline Bid (Reject)"
        AppLanguage.HINDI -> "बोली अस्वीकार करें"
    }

    fun performAssaying(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Enter Lab Test Results"
        AppLanguage.HINDI -> "लैब परीक्षण रिपोर्ट दर्ज करें"
    }

    fun startAuction(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Start Live Bidding"
        AppLanguage.HINDI -> "लाइव नीलामी शुरू करें"
    }

    fun conductWeighment(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Generate Weighment Slip"
        AppLanguage.HINDI -> "तौल पर्ची जारी करें"
    }

    fun issueInvoice(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Generate Sale Agreement"
        AppLanguage.HINDI -> "बिक्री अनुबंध पत्र तैयार करें"
    }

    fun processPayment(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Release Escrow / Bank Transfer"
        AppLanguage.HINDI -> "बैंक खाते में राशि ट्रांसफर करें"
    }

    fun dispatchLogistics(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Assign Logistics & Truck"
        AppLanguage.HINDI -> "ट्रक / वाहन आवंटित करें"
    }

    fun issueGateExit(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Issue Gate Exit Pass"
        AppLanguage.HINDI -> "गेट निकासी पर्ची जारी करें"
    }

    fun confirmDelivery(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Confirm Produce Received"
        AppLanguage.HINDI -> "माल प्राप्ति की पुष्टि करें"
    }

    fun reAuctionLot(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Put Back in Auction"
        AppLanguage.HINDI -> "पुनः नीलामी में डालें"
    }

    // Quality metrics
    fun moistureContent(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Moisture Content"
        AppLanguage.HINDI -> "नमी की मात्रा"
    }

    fun foreignMatter(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Foreign Matter / Dirt"
        AppLanguage.HINDI -> "कचरा / विजातीय तत्व"
    }

    fun damagedGrain(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Damaged / Shriveled"
        AppLanguage.HINDI -> "क्षतिग्रस्त / दागी दाने"
    }

    fun testWeight(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Hectolitre Test Weight"
        AppLanguage.HINDI -> "हेक्टोलीटर वजन"
    }

    fun gradeLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Assaying Grade"
        AppLanguage.HINDI -> "गुणवत्ता श्रेणी"
    }

    // Price & Stats
    fun mspPrice(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Govt. MSP"
        AppLanguage.HINDI -> "सरकारी एमएसपी"
    }

    fun currentHighestBid(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Highest Bid"
        AppLanguage.HINDI -> "सर्वोच्च बोली"
    }

    fun reservePrice(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Base Price"
        AppLanguage.HINDI -> "शुरुआती मूल्य"
    }

    fun perQuintal(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "/ Quintal"
        AppLanguage.HINDI -> "/ क्विंटल"
    }

    fun estimatedTotal(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Est. Total Payout"
        AppLanguage.HINDI -> "अनुमानित कुल भुगतान"
    }

    fun viewDetails(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "View Details & Action"
        AppLanguage.HINDI -> "विवरण देखें व कार्यवाही"
    }

    fun close(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Close"
        AppLanguage.HINDI -> "बंद करें"
    }

    fun searchPlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Search commodity or Lot ID..."
        AppLanguage.HINDI -> "फसल या लॉट आईडी खोजें..."
    }

    // Profile & User Info
    fun profileTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "User Profile & Settings"
        AppLanguage.HINDI -> "उपयोगकर्ता प्रोफ़ाइल व सेटिंग्स"
    }

    fun farmerProfile(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Farmer Profile"
        AppLanguage.HINDI -> "किसान प्रोफ़ाइल"
    }

    fun buyerProfile(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Buyer Profile"
        AppLanguage.HINDI -> "खरीदार प्रोफ़ाइल"
    }

    fun contactInformation(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Contact Information"
        AppLanguage.HINDI -> "संपर्क जानकारी"
    }

    fun farmDetails(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Farm & Land Details"
        AppLanguage.HINDI -> "खेत एवं भूमि विवरण"
    }

    fun businessDetails(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Business & Processing Details"
        AppLanguage.HINDI -> "व्यापार एवं प्रसंस्करण विवरण"
    }

    fun preferredCropsGoods(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Preferred Crops & Goods"
        AppLanguage.HINDI -> "पसंदीदा फसलें व कृषि उत्पाद"
    }

    fun transactionHistory(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Transaction History"
        AppLanguage.HINDI -> "लेन-देन का इतिहास"
    }

    fun languagePreference(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Language Preference"
        AppLanguage.HINDI -> "भाषा प्राथमिकता"
    }

    fun averageRating(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Average Rating"
        AppLanguage.HINDI -> "औसत रेटिंग"
    }

    // Ratings & Reviews
    fun bilateralReviews(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Ratings & Reviews"
        AppLanguage.HINDI -> "रेटिंग एवं समीक्षाएं"
    }

    fun farmerReviewForBuyer(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Farmer's Review for Buyer"
        AppLanguage.HINDI -> "खरीदार के लिए किसान की समीक्षा"
    }

    fun buyerReviewForFarmer(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Buyer's Review for Farmer"
        AppLanguage.HINDI -> "किसान के लिए खरीदार की समीक्षा"
    }

    fun leaveReview(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Leave Rating & Review"
        AppLanguage.HINDI -> "रेटिंग व समीक्षा दर्ज करें"
    }

    fun submitReview(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Submit Review"
        AppLanguage.HINDI -> "समीक्षा सबमिट करें"
    }

    fun writeReviewPlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Write your experience (payment speed, weighment honesty, produce quality, etc.)..."
        AppLanguage.HINDI -> "अपना अनुभव लिखें (भुगतान गति, तौल की ईमानदारी, फसल की गुणवत्ता आदि)..."
    }

    // Notifications
    fun notifications(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Notifications"
        AppLanguage.HINDI -> "सूचनाएं"
    }

    fun markAllAsRead(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Mark all read"
        AppLanguage.HINDI -> "सभी को पढ़ा हुआ चिह्नित करें"
    }

    fun clearAllNotifications(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "Clear all"
        AppLanguage.HINDI -> "सभी हटाएं"
    }

    fun noNotifications(lang: AppLanguage) = when (lang) {
        AppLanguage.ENGLISH -> "No notifications right now"
        AppLanguage.HINDI -> "इस समय कोई सूचना नहीं है"
    }
}
