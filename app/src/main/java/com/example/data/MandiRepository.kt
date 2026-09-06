package com.example.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object MandiRepository {
    private val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())

    val sampleFarmer1 = FarmerProfile(
        id = "FARM-8821",
        name = "Rameshwar Patel",
        nameHi = "रामेश्वर पटेल",
        phone = "+91 98261 44521",
        aadhaarLast4 = "7721",
        village = "Pipliya Mandi",
        villageHi = "पिपलिया मंडी",
        district = "Mandsaur",
        state = "Madhya Pradesh",
        bankAccountLast4 = "4512",
        ifscCode = "SBIN0001245",
        upiId = "rameshwar@sbi",
        email = "rameshwar.patel@kisanmail.in",
        landSizeAcres = 18.5,
        soilType = "Black Cotton (Deep Regur)",
        soilTypeHi = "काली गहरी कपासी मिट्टी",
        irrigationSource = "Chambal Canal & Tubewell",
        irrigationSourceHi = "चंबल नहर एवं नलकूप",
        equipmentOwned = listOf("Mahindra 575 DI (45HP)", "Seed Cum Fertilizer Drill", "Automatic Thresher"),
        preferredCrops = listOf("Wheat (Gehun)", "Soybean (Soyabean)", "Mustard (Sarson)", "Garlic"),
        preferredLanguage = AppLanguage.ENGLISH,
        rating = 4.9f,
        ratingCount = 16
    )

    val sampleFarmer2 = FarmerProfile(
        id = "FARM-9014",
        name = "Baldev Singh Dhillon",
        nameHi = "बलदेव सिंह ढिल्लों",
        phone = "+91 94172 88310",
        aadhaarLast4 = "4392",
        village = "Kotkapura",
        villageHi = "कोटकपूरा",
        district = "Faridkot",
        state = "Punjab",
        bankAccountLast4 = "9823",
        ifscCode = "PUNB0182700",
        upiId = "baldev.farm@paytm",
        email = "baldev.dhillon@punjabagri.org",
        landSizeAcres = 28.0,
        soilType = "Alluvial Loam",
        soilTypeHi = "जलोढ़ दोमट मिट्टी",
        irrigationSource = "Sirhind Canal Link & Solar Pump",
        irrigationSourceHi = "सरहिंद नहर लिंक एवं सोलर पंप",
        equipmentOwned = listOf("John Deere 5050D (50HP)", "Happy Seeder", "Rotary Tiller", "Combine Harvester"),
        preferredCrops = listOf("Paddy / Rice (Basmati)", "Wheat", "Mustard"),
        preferredLanguage = AppLanguage.HINDI,
        rating = 4.8f,
        ratingCount = 21
    )

    val sampleFarmer3 = FarmerProfile(
        id = "FARM-6205",
        name = "Suresh Chandra Meena",
        nameHi = "सुरेश चन्द्र मीणा",
        phone = "+91 97843 12904",
        aadhaarLast4 = "1180",
        village = "Niwai",
        villageHi = "निवाई",
        district = "Tonk",
        state = "Rajasthan",
        bankAccountLast4 = "3310",
        ifscCode = "BARB0NIWAIX",
        upiId = "suresh.meena@okhdfcbank",
        email = "suresh.meena@rajkisan.gov.in",
        landSizeAcres = 12.0,
        soilType = "Sandy Loam & Red Soil",
        soilTypeHi = "बलुई दोमट व लाल मिट्टी",
        irrigationSource = "Banas River Lift & Borewell",
        irrigationSourceHi = "बनास नदी लिफ्ट एवं नलकूप",
        equipmentOwned = listOf("Sonalika DI 35 (39HP)", "Cultivator", "Trolley (Double Axle)"),
        preferredCrops = listOf("Gram / Chickpea (Chana)", "Mustard (Sarson)", "Bajra", "Guar"),
        preferredLanguage = AppLanguage.HINDI,
        rating = 5.0f,
        ratingCount = 9,
        reviewsReceived = listOf(
            TradeReview(
                id = "REV-BUY-101",
                lotId = "LOT-2026-CHN-1182",
                reviewerRole = UserRole.BUYER,
                reviewerName = "Anand Vardhan (Kisan Shakti Agro)",
                targetName = "Suresh Chandra Meena",
                rating = 5.0f,
                reviewText = "Top-tier quality Desi Chana with certified moisture under 10%. Bags were well-stitched and weighbridge counts matched perfectly.",
                timestamp = "05 Sep 2026, 04:30 PM",
                tags = listOf("Pure Quality", "Low Moisture", "Accurate Count")
            )
        )
    )

    val sampleBuyers = listOf(
        BuyerProfile(
            id = "BUY-101",
            name = "Vikas Agrawal",
            firmName = "AgroTrade Commodities Ltd",
            tradeLicenseNo = "APMC-MP-2024-884",
            rating = 4.9f,
            ratingCount = 28,
            phone = "+91 98270 33100",
            email = "vikas@agrotradecommodities.com",
            city = "Indore",
            state = "Madhya Pradesh",
            gstin = "23AAACB9912E1ZU",
            businessType = "Commercial Solvent Extraction & Oilseed Mill",
            warehouseCapacityQuintals = 45000.0,
            preferredGoods = listOf("Soybean", "Wheat", "Mustard", "Maize"),
            preferredLanguage = AppLanguage.ENGLISH
        ),
        BuyerProfile(
            id = "BUY-102",
            name = "Sunil Singhania",
            firmName = "Singhania Food Mills Pvt Ltd",
            tradeLicenseNo = "APMC-DL-2023-112",
            rating = 4.8f,
            ratingCount = 34,
            phone = "+91 98110 54229",
            email = "procurement@singhaniafoods.in",
            city = "New Delhi",
            state = "Delhi NCR",
            gstin = "07AAACS1190K1ZW",
            businessType = "Modern Flour Mill & Packaged Atta Plant",
            warehouseCapacityQuintals = 60000.0,
            preferredGoods = listOf("Wheat (Sharbati & Lokwan)", "Basmati Paddy", "Gram"),
            preferredLanguage = AppLanguage.HINDI
        ),
        BuyerProfile(
            id = "BUY-103",
            name = "Harpreet Batra",
            firmName = "Bharat Organics & Exports",
            tradeLicenseNo = "APMC-PB-2022-901",
            rating = 4.7f,
            ratingCount = 19,
            phone = "+91 98765 44321",
            email = "exports@bharatorganics.org",
            city = "Amritsar",
            state = "Punjab",
            gstin = "03AAACB7781D1ZX",
            businessType = "Certified Organic Agri Exporter",
            warehouseCapacityQuintals = 30000.0,
            preferredGoods = listOf("Pusa Basmati 1121", "Durum Wheat", "Mustard"),
            preferredLanguage = AppLanguage.ENGLISH
        ),
        BuyerProfile(
            id = "BUY-104",
            name = "Anand Vardhan",
            firmName = "Kisan Shakti Agro Procure",
            tradeLicenseNo = "APMC-RJ-2024-441",
            rating = 4.9f,
            ratingCount = 22,
            phone = "+91 94140 77123",
            email = "anand@kisanshakti.com",
            city = "Jaipur",
            state = "Rajasthan",
            gstin = "08AAACK4412H1ZQ",
            businessType = "Dal Mill, Pulse Processing & Retail Supplier",
            warehouseCapacityQuintals = 25000.0,
            preferredGoods = listOf("Gram / Chana", "Mustard", "Soybean", "Moong"),
            preferredLanguage = AppLanguage.HINDI,
            reviewsReceived = listOf(
                TradeReview(
                    id = "REV-FARM-101",
                    lotId = "LOT-2026-CHN-1182",
                    reviewerRole = UserRole.FARMER,
                    reviewerName = "Suresh Chandra Meena",
                    targetName = "Anand Vardhan (Kisan Shakti Agro)",
                    rating = 5.0f,
                    reviewText = "Instant DBT settlement into my Bank of Baroda account within 2 hours. Very respectful communication and exact weighing at the mandi.",
                    timestamp = "05 Sep 2026, 03:15 PM",
                    tags = listOf("Fast DBT Settlement", "Honest Weighment", "Respectful Trader")
                )
            )
        )
    )

    fun getInitialLots(): List<ProduceLot> {
        val now = System.currentTimeMillis()

        // 1. LIVE AUCTION: Premium Sharbati Wheat
        val wheatBids = listOf(
            Bid(UUID.randomUUID().toString(), "BUY-101", "Vikas Agrawal", "AgroTrade Commodities", 2620.0, now - 180000),
            Bid(UUID.randomUUID().toString(), "BUY-102", "Sunil Singhania", "Singhania Food Mills", 2660.0, now - 120000),
            Bid(UUID.randomUUID().toString(), "BUY-103", "Harpreet Batra", "Bharat Organics", 2710.0, now - 60000),
            Bid(UUID.randomUUID().toString(), "BUY-104", "Anand Vardhan", "Kisan Shakti Agro", 2740.0, now - 20000, isWinning = true)
        )
        val lot1 = ProduceLot(
            id = "LOT-2026-WHT-7821",
            commodityEn = "Wheat (Gehun)",
            commodityHi = "गेहूं (शरबती)",
            varietyEn = "Sharbati Gold MP",
            varietyHi = "शरबती गोल्ड एमपी",
            farmer = sampleFarmer1,
            vehicleNumber = "MP 14 GA 8812 (Tractor)",
            arrivalTime = "06 Sep, 08:30 AM",
            estimatedBags = 160,
            estimatedWeightQuintals = 80.0,
            mspBasePrice = 2425.0,
            reservePrice = 2500.0,
            currentStep = WorkflowStep.ONLINE_AUCTION,
            assayingReport = AssayingReport(
                reportId = "LAB-WHT-9921",
                moisturePercent = 10.4,
                foreignMatterPercent = 0.6,
                damagedGrainPercent = 0.8,
                testWeightHl = 80.2,
                grade = QualityGrade.GRADE_A,
                labTechnician = "Dr. S. K. Sharma (Mandi Chemist)",
                certifiedAt = "06 Sep, 09:45 AM"
            ),
            bids = wheatBids,
            winningBid = wheatBids.last(),
            auctionTimeRemainingSeconds = 145,
            isAuctionLive = true
        )

        // 2. FARMER DECISION PENDING: Basmati 1121 Paddy
        val basmatiBids = listOf(
            Bid(UUID.randomUUID().toString(), "BUY-102", "Sunil Singhania", "Singhania Food Mills", 3650.0, now - 600000),
            Bid(UUID.randomUUID().toString(), "BUY-103", "Harpreet Batra", "Bharat Organics", 3820.0, now - 300000, isWinning = true)
        )
        val lot2 = ProduceLot(
            id = "LOT-2026-PDY-4402",
            commodityEn = "Paddy / Rice (Dhan)",
            commodityHi = "धान (बासमती)",
            varietyEn = "Pusa Basmati 1121",
            varietyHi = "पूसा बासमती 1121",
            farmer = sampleFarmer2,
            vehicleNumber = "PB 04 T 9120 (Truck)",
            arrivalTime = "06 Sep, 07:15 AM",
            estimatedBags = 240,
            estimatedWeightQuintals = 120.0,
            mspBasePrice = 2300.0,
            reservePrice = 3400.0,
            currentStep = WorkflowStep.FARMER_DECISION,
            assayingReport = AssayingReport(
                reportId = "LAB-PDY-1082",
                moisturePercent = 11.8,
                foreignMatterPercent = 0.9,
                damagedGrainPercent = 1.1,
                testWeightHl = 76.5,
                grade = QualityGrade.GRADE_A,
                labTechnician = "Er. Gurmeet Singh",
                certifiedAt = "06 Sep, 08:30 AM"
            ),
            bids = basmatiBids,
            winningBid = basmatiBids.last(),
            auctionTimeRemainingSeconds = 0,
            isAuctionLive = false,
            farmerAccepted = null // Waiting for farmer action!
        )

        // 3. ASSAYING IN PROGRESS: Mustard / Sarson
        val lot3 = ProduceLot(
            id = "LOT-2026-MST-3391",
            commodityEn = "Mustard (Sarson)",
            commodityHi = "सरसों (काली)",
            varietyEn = "Pusa Bold 42% Oil",
            varietyHi = "पूसा बोल्ड (४२% तेल)",
            farmer = sampleFarmer3,
            vehicleNumber = "RJ 26 EA 4401 (Eicher)",
            arrivalTime = "06 Sep, 09:10 AM",
            estimatedBags = 110,
            estimatedWeightQuintals = 55.0,
            mspBasePrice = 5950.0,
            reservePrice = 6100.0,
            currentStep = WorkflowStep.ASSAYING,
            assayingReport = null
        )

        // 4. WEIGHMENT & INVOICING: Soybean
        val soyBid = Bid(UUID.randomUUID().toString(), "BUY-101", "Vikas Agrawal", "AgroTrade Commodities", 4920.0, now - 3600000, isWinning = true)
        val soyWeighment = WeighmentSlip(
            slipNo = "WB-IND-2026-883",
            grossWeightKg = 13850.0,
            tareWeightKg = 4120.0,
            netWeightKg = 9730.0,
            netQuintals = 97.3,
            bagCount = 195,
            weighbridgeOperator = "Kailash Verma (Bridge #2)",
            weighmentTime = "06 Sep, 10:15 AM"
        )
        val soyGross = 97.3 * 4920.0 // 4,78,716
        val soyCess = soyGross * 0.01 // 4,787.16
        val soyNet = soyGross - soyCess // 4,73,928.84
        val soyInvoice = InvoiceAgreement(
            invoiceNo = "INV-MND-2026-4412",
            date = "06 Sep 2026",
            agreedRatePerQuintal = 4920.0,
            netWeightQuintals = 97.3,
            grossAmount = soyGross,
            mandiCessPercent = 1.0,
            mandiCessAmount = soyCess,
            netFarmerReceivable = soyNet,
            farmerSignature = "Rameshwar Patel (Digital OTP Authenticated)",
            buyerSignature = "Vikas Agrawal for AgroTrade Commodities"
        )
        val lot4 = ProduceLot(
            id = "LOT-2026-SOY-9910",
            commodityEn = "Soybean (Soyabean)",
            commodityHi = "सोयाबीन (पीला)",
            varietyEn = "JS 9560 Yellow",
            varietyHi = "जे.एस. 9560",
            farmer = sampleFarmer1,
            vehicleNumber = "MP 14 GA 8812 (Tractor)",
            arrivalTime = "06 Sep, 06:45 AM",
            estimatedBags = 195,
            estimatedWeightQuintals = 97.5,
            mspBasePrice = 4892.0,
            reservePrice = 4850.0,
            currentStep = WorkflowStep.PAYMENT,
            assayingReport = AssayingReport(
                reportId = "LAB-SOY-7731",
                moisturePercent = 10.9,
                foreignMatterPercent = 1.2,
                damagedGrainPercent = 1.4,
                testWeightHl = 72.0,
                grade = QualityGrade.GRADE_B,
                labTechnician = "Dr. S. K. Sharma",
                certifiedAt = "06 Sep, 07:30 AM"
            ),
            bids = listOf(soyBid),
            winningBid = soyBid,
            farmerAccepted = true,
            weighment = soyWeighment,
            invoice = soyInvoice,
            payment = null // Ready for Payment action!
        )

        // 5. COMPLETED LOT: Desi Gram / Chana with full journey
        val chanaBid = Bid(UUID.randomUUID().toString(), "BUY-104", "Anand Vardhan", "Kisan Shakti Agro", 5640.0, now - 86400000, isWinning = true)
        val chanaWeighment = WeighmentSlip(
            slipNo = "WB-TNK-2026-102",
            grossWeightKg = 10200.0,
            tareWeightKg = 3700.0,
            netWeightKg = 6500.0,
            netQuintals = 65.0,
            bagCount = 130,
            weighbridgeOperator = "Mahesh Meena",
            weighmentTime = "05 Sep, 11:30 AM"
        )
        val chanaGross = 65.0 * 5640.0
        val chanaCess = chanaGross * 0.01
        val chanaNet = chanaGross - chanaCess
        val chanaInvoice = InvoiceAgreement(
            invoiceNo = "INV-MND-2026-3910",
            date = "05 Sep 2026",
            agreedRatePerQuintal = 5640.0,
            netWeightQuintals = 65.0,
            grossAmount = chanaGross,
            mandiCessPercent = 1.0,
            mandiCessAmount = chanaCess,
            netFarmerReceivable = chanaNet,
            farmerSignature = "Suresh Chandra Meena (Biometric Aadhaar)",
            buyerSignature = "Anand Vardhan for Kisan Shakti"
        )
        val chanaPayment = PaymentRecord(
            transactionRef = "UTR-SBIN-20260905-998811",
            method = "e-NAM Escrow Direct Bank Transfer (DBT)",
            amount = chanaNet,
            status = "Settled in Bank A/C ...3310",
            timestamp = "05 Sep 2026, 02:40 PM",
            beneficiaryAccount = "A/C ...3310 (BARB0NIWAIX)"
        )
        val chanaLogistics = LogisticsRecord(
            vehicleNumber = "RJ 26 G 5520 (Eicher Canter)",
            driverName = "Ramkesh Gurjar",
            driverPhone = "+91 94145 22001",
            destination = "Kisan Shakti Processing Plant, Jaipur",
            destinationHi = "किसान शक्ति प्रोसेसिंग प्लांट, जयपुर",
            eWayBillNo = "EWB-9921-8834-1120",
            status = "Delivered & Verified"
        )
        val chanaGateExit = GateExitPass(
            passNo = "GXP-MND-8891",
            gateNumber = "Gate #3 (Commercial Dispatch Out)",
            securityOfficer = "Inspector R. P. Yadav",
            exitTimestamp = "05 Sep 2026, 04:10 PM",
            verifiedWeightKg = 10200.0
        )
        val lot5 = ProduceLot(
            id = "LOT-2026-CHN-1182",
            commodityEn = "Gram / Chickpea (Chana)",
            commodityHi = "चना (देसी)",
            varietyEn = "Desi Chana Super Bold",
            varietyHi = "देसी चना सुपर बोल्ड",
            farmer = sampleFarmer3,
            vehicleNumber = "RJ 26 EA 4401 (Eicher)",
            arrivalTime = "05 Sep, 08:00 AM",
            estimatedBags = 130,
            estimatedWeightQuintals = 65.0,
            mspBasePrice = 5440.0,
            reservePrice = 5500.0,
            currentStep = WorkflowStep.BUYER_RECEIVED,
            assayingReport = AssayingReport(
                reportId = "LAB-CHN-5510",
                moisturePercent = 9.8,
                foreignMatterPercent = 0.5,
                damagedGrainPercent = 0.7,
                testWeightHl = 81.4,
                grade = QualityGrade.GRADE_A,
                labTechnician = "Dr. K. L. Meena",
                certifiedAt = "05 Sep, 09:30 AM"
            ),
            bids = listOf(chanaBid),
            winningBid = chanaBid,
            farmerAccepted = true,
            weighment = chanaWeighment,
            invoice = chanaInvoice,
            payment = chanaPayment,
            logistics = chanaLogistics,
            gateExit = chanaGateExit,
            buyerDelivered = true,
            buyerFeedback = "Excellent high-density chana, minimal moisture. Instant settlement completed.",
            buyerRating = 5.0f,
            farmerReview = TradeReview(
                id = "REV-FARM-101",
                lotId = "LOT-2026-CHN-1182",
                reviewerRole = UserRole.FARMER,
                reviewerName = "Suresh Chandra Meena",
                targetName = "Anand Vardhan (Kisan Shakti Agro)",
                rating = 5.0f,
                reviewText = "Instant DBT settlement into my Bank of Baroda account within 2 hours. Very respectful communication and exact weighing at the mandi.",
                timestamp = "05 Sep 2026, 03:15 PM",
                tags = listOf("Fast DBT Settlement", "Honest Weighment", "Respectful Trader")
            ),
            buyerReview = TradeReview(
                id = "REV-BUY-101",
                lotId = "LOT-2026-CHN-1182",
                reviewerRole = UserRole.BUYER,
                reviewerName = "Anand Vardhan (Kisan Shakti Agro)",
                targetName = "Suresh Chandra Meena",
                rating = 5.0f,
                reviewText = "Top-tier quality Desi Chana with certified moisture under 10%. Bags were well-stitched and weighbridge counts matched perfectly.",
                timestamp = "05 Sep 2026, 04:30 PM",
                tags = listOf("Pure Quality", "Low Moisture", "Accurate Count")
            )
        )

        return listOf(lot1, lot2, lot3, lot4, lot5)
    }

    fun getInitialNotifications(): List<MandiNotification> {
        val now = System.currentTimeMillis()
        return listOf(
            MandiNotification(
                id = "NOTIF-1",
                type = NotificationType.NEW_BID,
                targetRole = UserRole.FARMER,
                titleEn = "New High Bid Placed",
                titleHi = "नई उच्चतम बोली लगी",
                messageEn = "Kisan Shakti Agro bid ₹2,740/qtl on your Wheat (LOT-2026-WHT-7821). Base was ₹2,425.",
                messageHi = "किसान शक्ति एग्रो ने आपके गेहूं (LOT-2026-WHT-7821) पर ₹२,७४०/क्विंटल की नई बोली लगाई।",
                lotId = "LOT-2026-WHT-7821",
                timestamp = now - 20000,
                isRead = false
            ),
            MandiNotification(
                id = "NOTIF-2",
                type = NotificationType.OUTBID,
                targetRole = UserRole.BUYER,
                titleEn = "Outbid Alert: Wheat",
                titleHi = "बोली पिछड़ने की सूचना: गेहूं",
                messageEn = "You have been outbid on Wheat (LOT-2026-WHT-7821)! Current highest is ₹2,740/qtl.",
                messageHi = "गेहूं (LOT-2026-WHT-7821) पर आपकी बोली पिछड़ गई है! वर्तमान उच्चतम बोली ₹२,७४०/क्विंटल है।",
                lotId = "LOT-2026-WHT-7821",
                timestamp = now - 25000,
                isRead = false
            ),
            MandiNotification(
                id = "NOTIF-3",
                type = NotificationType.AUCTION_ENDED,
                targetRole = UserRole.FARMER,
                titleEn = "Auction Concluded: Basmati Paddy",
                titleHi = "नीलामी समाप्त: बासमती धान",
                messageEn = "Bidding finished on LOT-2026-PDY-4402 at ₹3,820/qtl (+₹1,520 above MSP). Please review and accept.",
                messageHi = "बासमती धान (LOT-2026-PDY-4402) की नीलामी ₹३,८२०/क्विंटल पर पूरी हुई। कृपया अपनी स्वीकृति दें।",
                lotId = "LOT-2026-PDY-4402",
                timestamp = now - 300000,
                isRead = false
            ),
            MandiNotification(
                id = "NOTIF-4",
                type = NotificationType.BUYER_WON,
                targetRole = UserRole.BUYER,
                titleEn = "Winning Bid: Basmati Paddy",
                titleHi = "विजेता बोली: बासमती धान",
                messageEn = "Your bid of ₹3,820/qtl on LOT-2026-PDY-4402 won the auction. Awaiting farmer approval.",
                messageHi = "LOT-2026-PDY-4402 पर ₹३,८२०/क्विंटल की आपकी बोली विजेता रही। किसान की पुष्टि प्रतीक्षित है।",
                lotId = "LOT-2026-PDY-4402",
                timestamp = now - 300000,
                isRead = false
            ),
            MandiNotification(
                id = "NOTIF-5",
                type = NotificationType.PURCHASE_SUCCESS,
                targetRole = UserRole.BUYER,
                titleEn = "Purchase Confirmed: Desi Chana",
                titleHi = "खरीद की पुष्टि: देसी चना",
                messageEn = "Produce delivered & verified at Jaipur plant for LOT-2026-CHN-1182. Leave a review!",
                messageHi = "देसी चना (LOT-2026-CHN-1182) की सुपुर्दगी गोदाम पर पूरी हुई। कृपया किसान को रिव्यू दें!",
                lotId = "LOT-2026-CHN-1182",
                timestamp = now - 86400000,
                isRead = true
            ),
            MandiNotification(
                id = "NOTIF-6",
                type = NotificationType.RATING_RECEIVED,
                targetRole = UserRole.FARMER,
                titleEn = "New 5.0★ Review Received",
                titleHi = "नया ५.०★ रिव्यू प्राप्त हुआ",
                messageEn = "Anand Vardhan rated your produce 5★: 'Top-tier quality Desi Chana with certified moisture...'",
                messageHi = "आनंद वर्धन ने आपकी उपज को ५★ रेटिंग दी: 'न्यूनतम नमी के साथ सर्वोच्च गुणवत्ता का देसी चना...'",
                lotId = "LOT-2026-CHN-1182",
                timestamp = now - 80000000,
                isRead = true
            )
        )
    }

    fun generateNewLot(
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
    ): ProduceLot {
        val randomNum = (1000..9999).random()
        val lotId = "LOT-2026-${commodityEn.take(3).uppercase()}-$randomNum"
        val farmer = FarmerProfile(
            id = "FARM-${(1000..9999).random()}",
            name = farmerName,
            nameHi = farmerName,
            phone = farmerPhone,
            aadhaarLast4 = "${(1000..9999).random()}",
            village = village,
            villageHi = village,
            district = "Local Mandi Hub",
            bankAccountLast4 = "${(1000..9999).random()}",
            ifscCode = "SBIN0002491",
            upiId = "$farmerPhone@upi"
        )
        return ProduceLot(
            id = lotId,
            commodityEn = commodityEn,
            commodityHi = commodityHi,
            varietyEn = varietyEn,
            varietyHi = varietyEn,
            farmer = farmer,
            vehicleNumber = vehicleNumber,
            arrivalTime = dateFormat.format(Date()),
            estimatedBags = estimatedBags,
            estimatedWeightQuintals = estimatedWeight,
            mspBasePrice = mspPrice,
            reservePrice = mspPrice,
            currentStep = WorkflowStep.GATE_ENTRY
        )
    }
}
