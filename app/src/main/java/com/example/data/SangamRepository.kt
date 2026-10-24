package com.example.data

import androidx.compose.ui.graphics.Color

object SangamRepository {

    fun createInitialOrganizations(): List<Organization> {
        var msgIdCounter = 100

        fun textMsg(
            authorName: String?,
            authorRole: String,
            text: String,
            time: String,
            totalMembers: Int = 1240,
            seenCount: Int = 0,
            requiresConfirm: Boolean = false,
            confirmedCount: Int = 0
        ) = Message(
            id = ++msgIdCounter,
            authorName = authorName,
            authorRole = authorRole,
            kind = MessageKind.TEXT,
            text = text,
            time = time,
            totalMembers = totalMembers,
            seenCount = seenCount,
            requiresConfirm = requiresConfirm,
            confirmedCount = confirmedCount
        )

        fun audioMsg(
            authorName: String?,
            authorRole: String,
            duration: String,
            time: String,
            totalMembers: Int = 1240,
            seenCount: Int = 0
        ) = Message(
            id = ++msgIdCounter,
            authorName = authorName,
            authorRole = authorRole,
            kind = MessageKind.AUDIO,
            text = "Voice message",
            audioDuration = duration,
            time = time,
            totalMembers = totalMembers,
            seenCount = seenCount
        )

        fun pollMsg(
            authorName: String,
            authorRole: String,
            question: String,
            time: String,
            options: List<Pair<String, Int>>,
            totalMembers: Int = 1240,
            seenCount: Int = 0
        ) = Message(
            id = ++msgIdCounter,
            authorName = authorName,
            authorRole = authorRole,
            kind = MessageKind.POLL,
            time = time,
            pollQuestion = question,
            pollOptions = options.map { PollOption(it.first, it.second) },
            totalMembers = totalMembers,
            seenCount = seenCount
        )

        fun imgMsg(
            authorName: String?,
            authorRole: String,
            caption: String,
            time: String,
            hue: Float = 210f,
            totalMembers: Int = 1240,
            seenCount: Int = 0
        ) = Message(
            id = ++msgIdCounter,
            authorName = authorName,
            authorRole = authorRole,
            kind = MessageKind.IMAGE,
            mediaCaption = caption,
            time = time,
            mediaHue = hue,
            totalMembers = totalMembers,
            seenCount = seenCount
        )

        fun vidMsg(
            authorName: String?,
            authorRole: String,
            caption: String,
            time: String,
            duration: String,
            hue: Float = 160f,
            totalMembers: Int = 1240,
            seenCount: Int = 0
        ) = Message(
            id = ++msgIdCounter,
            authorName = authorName,
            authorRole = authorRole,
            kind = MessageKind.VIDEO,
            mediaCaption = caption,
            time = time,
            mediaDuration = duration,
            mediaHue = hue,
            totalMembers = totalMembers,
            seenCount = seenCount
        )

        // 1. Maxinnov (Tech Company)
        val maxinnovGroups = listOf(
            CommunityGroup(
                id = 101, name = "All Company", type = GroupType.ALL, membersCount = 950,
                messages = listOf(
                    textMsg(null, "", "Maxinnov Q4 Product Roadmap & AI feature release is live. All teams please review.", "09:00", 950, 890, true, 620),
                    pollMsg("Anand Kumar", "VP Product", "Which AI assistant feature should we prioritize for next sprint?", "09:30", listOf("Automated Code Review" to 180, "Voice Transcription" to 140, "Smart Search" to 95), 950, 810)
                ), unreadCount = 3, pinnedMessageId = 102
            ),
            CommunityGroup(id = 102, name = "Chennai HQ", type = GroupType.BRANCH, membersCount = 320, messages = listOf(textMsg("Thatchina Moorthi", "Founder & Owner", "Welcome to Chennai HQ team. New AI dev lab is open on 4th floor.", "10:00", 320, 310), imgMsg("Priya Sharma", "Lead Designer", "New UI design system prototype", "10:30", 270f, 320, 290)), unreadCount = 2),
            CommunityGroup(id = 103, name = "Bengaluru Hub", type = GroupType.BRANCH, membersCount = 280, messages = listOf(textMsg("Karthik Raja", "Engineering Director", "All microservices deployed to AWS prod region.", "11:15", 280, 260))),
            CommunityGroup(id = 104, name = "San Francisco Branch", type = GroupType.BRANCH, membersCount = 120, messages = listOf(textMsg("Sarah Jenkins", "US Sales Lead", "Client demo with Silicon Valley partners went excellent.", "12:00", 120, 110))),
            CommunityGroup(id = 105, name = "London Branch", type = GroupType.BRANCH, membersCount = 90, messages = listOf(textMsg("David Miller", "UK Operations", "EU GDPR security audit passed with 100% score.", "14:20", 90, 85))),
            CommunityGroup(id = 106, name = "Singapore Branch", type = GroupType.BRANCH, membersCount = 80, messages = listOf(textMsg("Li Wei", "APAC Lead", "Regional partner onboarding completed.", "15:00", 80, 75))),
            CommunityGroup(id = 107, name = "Hyderabad Tech Hub", type = GroupType.BRANCH, membersCount = 60, messages = listOf(textMsg("Rohan Verma", "Hub Lead", "New engineering cohort joined today.", "16:10", 60, 58))),
            CommunityGroup(id = 108, name = "AI ML Labs", type = GroupType.TEAM, membersCount = 45, messages = listOf(textMsg("Dr. Arun Prakash", "Chief AI Scientist", "Gemini 1.5 model fine-tuning accuracy hit 98.4%.", "11:45", 45, 43), vidMsg("Dr. Arun Prakash", "Chief AI Scientist", "LLM Inference latency benchmark demo", "12:10", "1:45", 280f, 45, 41)), unreadCount = 1),
            CommunityGroup(id = 109, name = "Frontend Team", type = GroupType.TEAM, membersCount = 65, messages = listOf(textMsg("Siddharth N", "Frontend Lead", "Jetpack Compose v1.7 design tokens updated across all screens.", "13:00", 65, 60)), unreadCount = 1),
            CommunityGroup(id = 110, name = "Backend Team", type = GroupType.TEAM, membersCount = 70, messages = listOf(audioMsg("Karthik Raja", "Engineering Director", "0:25", "14:00", 70, 68))),
            CommunityGroup(id = 111, name = "DevOps & Cloud", type = GroupType.TEAM, membersCount = 30, messages = listOf(textMsg("Gaurav Mehta", "DevOps Lead", "Kubernetes cluster auto-scaling enabled.", "15:30", 30, 29))),
            CommunityGroup(id = 112, name = "Product Design", type = GroupType.TEAM, membersCount = 25, messages = listOf(imgMsg("Priya Sharma", "Lead Designer", "Sangam Dark Theme visual assets", "16:00", 300f, 25, 24))),
            CommunityGroup(id = 113, name = "QA & Testing", type = GroupType.TEAM, membersCount = 35, messages = listOf(textMsg("Deepa Lakshmi", "QA Lead", "Automated UI test suite execution 100% green.", "16:45", 35, 34)))
        )

        val maxinnovOrg = Organization(
            id = 1, name = "Maxinnov", shortName = "MX", sector = "Tech & AI Solutions · 12 offices", accentHex = "#8E24AA", accentColor = Color(0xFF8E24AA), totalPeople = 950,
            groups = maxinnovGroups,
            description = "Leading AI research laboratory and enterprise software solutions provider with 12 global offices across Asia, Europe, and North America.",
            headquarters = "Chennai HQ & Bengaluru Hub",
            foundedYear = 2014,
            founderName = "Thatchina Moorthi",
            founderEmail = "thatchinamoorthiopto@gmail.com",
            statuses = listOf(
                StatusUpdate(101, "Thatchina Moorthi", "Founder & Owner", "10m", false, listOf(StatusSlide("Maxinnov AI platform crossed 500,000 active users!", 270f), StatusSlide("Thank you to all engineering and design teams.", 290f))),
                StatusUpdate(102, "Anand Kumar", "VP Product", "25m", false, listOf(StatusSlide("Q4 roadmap walkthrough recording posted.", 210f))),
                StatusUpdate(103, "Priya Sharma", "Lead Designer", "45m", false, listOf(StatusSlide("New M3 design system guidelines released.", 300f))),
                StatusUpdate(104, "Karthik Raja", "Engineering Director", "1h", false, listOf(StatusSlide("AWS multi-region latency reduced by 40%.", 180f))),
                StatusUpdate(105, "Dr. Arun Prakash", "Chief AI Scientist", "2h", false, listOf(StatusSlide("AI model accuracy benchmark results are in!", 280f))),
                StatusUpdate(106, "Sarah Jenkins", "US Sales Lead", "3h", false, listOf(StatusSlide("Signed 3 new enterprise clients in San Francisco.", 160f))),
                StatusUpdate(107, "Gaurav Mehta", "DevOps Lead", "4h", false, listOf(StatusSlide("Zero downtime deployment completed.", 140f))),
                StatusUpdate(108, "Deepa Lakshmi", "QA Lead", "5h", false, listOf(StatusSlide("Robolectric & UI automated test suite 100% pass.", 220f))),
                StatusUpdate(109, "Li Wei", "APAC Lead", "6h", false, listOf(StatusSlide("Singapore tech summit keynote prep done.", 250f))),
                StatusUpdate(110, "David Miller", "UK Operations", "7h", false, listOf(StatusSlide("London office expanded with 50 new workstations.", 320f))),
                StatusUpdate(111, "Siddharth N", "Frontend Lead", "8h", false, listOf(StatusSlide("Jetpack Compose performance optimizations merged.", 200f))),
                StatusUpdate(112, "Rohan Verma", "Hub Lead", "9h", false, listOf(StatusSlide("Hyderabad campus Hackathon starts tomorrow!", 260f)))
            ),
            myStatus = StatusUpdate(0, "Thatchina Moorthi", "Owner", "just now", true, emptyList()),
            meetings = listOf(
                Meeting(101, "Maxinnov All-Hands Tech Sync", "Today", "4:00 PM", "45 min", "Thatchina Moorthi", "All Hands", listOf(AgendaItem("4:00", "Welcome & Q4 AI vision"), AgendaItem("4:20", "Product demo & roadmap"), AgendaItem("4:35", "Open Q&A")), 412, 950, true),
                Meeting(102, "AI ML Model Architecture Sync", "Today", "6:00 PM", "30 min", "Dr. Arun Prakash", "AI ML Labs", listOf(AgendaItem("6:00", "Inference optimization"), AgendaItem("6:20", "Next sprint goals")), 38, 45),
                Meeting(103, "Frontend Design System Walkthrough", "Tomorrow", "11:00 AM", "40 min", "Priya Sharma", "Product Design", listOf(AgendaItem("11:00", "M3 design tokens"), AgendaItem("11:25", "Component review")), 22, 25),
                Meeting(104, "DevOps & Cloud Security Audit", "This week", "Thu 2:00 PM", "40 min", "Gaurav Mehta", "DevOps & Cloud", listOf(AgendaItem("2:00", "AWS security review"), AgendaItem("2:25", "Auto-scaling rules")), 28, 30)
            ),
            directory = listOf(
                Person(101, "Thatchina Moorthi", "Founder & Owner", "Chennai HQ", "Leadership", "+91 98765 43210", "thatchinamoorthiopto@gmail.com", "Available", 99, 2014, "Founder & Owner of Maxinnov. Leading AI products & multi-branch technology innovation."),
                Person(102, "Anand Kumar", "VP Product", "Chennai HQ", "Product", "+91 98111 22334", "anand.k@maxinnov.tech", "Available", 96, 2016, "VP Product managing global tech roadmap."),
                Person(103, "Priya Sharma", "Lead Designer", "Chennai HQ", "Product Design", "+91 98222 33445", "priya.s@maxinnov.tech", "Available", 95, 2017, "Leading UI/UX & Design System across web and Android."),
                Person(104, "Karthik Raja", "Engineering Director", "Bengaluru Hub", "Engineering", "+91 98333 44556", "karthik.r@maxinnov.tech", "Available", 94, 2018, "Director of Cloud Backend & Microservices."),
                Person(105, "Dr. Arun Prakash", "Chief AI Scientist", "Chennai HQ", "AI ML Labs", "+91 98444 55667", "arun.p@maxinnov.tech", "In a meeting", 98, 2019, "Leading Generative AI & Machine Learning Research."),
                Person(106, "Siddharth N", "Frontend Lead", "Chennai HQ", "Frontend Team", "+91 98555 66778", "siddharth.n@maxinnov.tech", "Available", 93, 2020, "Specialist in Jetpack Compose and modern Android UI architecture."),
                Person(107, "Gaurav Mehta", "DevOps Lead", "Bengaluru Hub", "DevOps & Cloud", "+91 98666 77889", "gaurav.m@maxinnov.tech", "Available", 92, 2021, "Cloud security and Kubernetes auto-scaling architect."),
                Person(108, "Deepa Lakshmi", "QA Lead", "Chennai HQ", "QA & Testing", "+91 98777 88990", "deepa.l@maxinnov.tech", "Available", 91, 2021, "Automated test suites & Robolectric quality assurance lead."),
                Person(109, "Li Wei", "APAC Lead", "Singapore Branch", "Regional Operations", "+65 9123 4567", "li.wei@maxinnov.tech", "Available", 90, 2022, "Managing Singapore & APAC expansion."),
                Person(110, "Sarah Jenkins", "US Sales Lead", "San Francisco Branch", "Sales", "+1 415 555 0199", "sarah.j@maxinnov.tech", "Available", 89, 2022, "Enterprise partnerships lead in Silicon Valley.")
            )
        )

        // 2. Midway (Mechanic & Roadside Assistance App Company)
        val midwayGroups = listOf(
            CommunityGroup(
                id = 201, name = "All Centers", type = GroupType.ALL, membersCount = 1820,
                messages = listOf(
                    textMsg(null, "", "Midway Roadside Emergency 24/7 Service Protocol update. Dispatch response time target is under 12 minutes.", "08:30", 1820, 1600, true, 1100),
                    pollMsg("Ramesh V", "Operations Head", "Which towing vehicle addition should we prioritize for monsoon season?", "09:00", listOf("Heavy Duty Flatbed" to 220, "Hydraulic Lift Truck" to 180, "EV Charger Van" to 115), 1820, 1400)
                ), unreadCount = 2, pinnedMessageId = 201
            ),
            CommunityGroup(id = 202, name = "Chennai Central", type = GroupType.BRANCH, membersCount = 380, messages = listOf(textMsg("Thatchina Moorthi", "Founder & Owner", "New emergency dispatch unit active at Mount Road garage.", "09:45", 380, 360), imgMsg("Kannan M", "Garage Manager", "New diagnostic scanner delivered", "10:15", 30f, 380, 340)), unreadCount = 1),
            CommunityGroup(id = 203, name = "Mumbai Garage Hub", type = GroupType.BRANCH, membersCount = 420, messages = listOf(textMsg("Sanjay Patil", "Regional Admin", "Monsoon breakdown emergency teams deployed on Western Express Highway.", "10:50", 420, 400))),
            CommunityGroup(id = 204, name = "Delhi NCR Hub", type = GroupType.BRANCH, membersCount = 390, messages = listOf(textMsg("Rajesh Kumar", "Hub Lead", "Winter battery jumpstart service kits delivered to all 15 stations.", "11:30", 390, 370))),
            CommunityGroup(id = 205, name = "Bengaluru Auto Zone", type = GroupType.BRANCH, membersCount = 350, messages = listOf(textMsg("Venkatesh K", "Zone Manager", "EV roadside charger units fully operational in Indiranagar & Koramangala.", "12:10", 350, 330))),
            CommunityGroup(id = 206, name = "Hyderabad Garage", type = GroupType.BRANCH, membersCount = 280, messages = listOf(textMsg("Syed Imran", "Garage Manager", "Hitech city mobile mechanic units expanded.", "13:00", 280, 260))),
            CommunityGroup(id = 207, name = "Pune Workshop", type = GroupType.BRANCH, membersCount = 210, messages = listOf(textMsg("Nitin Joshi", "Workshop Lead", "Hydraulic ramp maintenance completed.", "14:00", 210, 200))),
            CommunityGroup(id = 208, name = "Mechanic Operations", type = GroupType.TEAM, membersCount = 520, messages = listOf(textMsg("Kannan M", "Head Mechanic", "Standard safety checklist for high-voltage EV battery repairs updated.", "09:15", 520, 490), vidMsg("Kannan M", "Head Mechanic", "OBD2 scanner quick walkthrough", "09:40", "2:10", 20f, 520, 470)), unreadCount = 2),
            CommunityGroup(id = 209, name = "Emergency Roadside", type = GroupType.TEAM, membersCount = 410, messages = listOf(textMsg("Ramesh V", "Dispatch Director", "Average response time today is 9.4 minutes!", "10:00", 410, 395)), unreadCount = 1),
            CommunityGroup(id = 210, name = "Parts & Supply Chain", type = GroupType.TEAM, membersCount = 85, messages = listOf(textMsg("Arun Vijay", "Supply Lead", "Original spare parts inventory restocked across all 24 city hubs.", "11:20", 85, 82))),
            CommunityGroup(id = 211, name = "Mobile App Support", type = GroupType.TEAM, membersCount = 40, messages = listOf(textMsg("Deepika R", "App Support Lead", "Midway Mechanic Booking App v3.2 released on Play Store.", "14:30", 40, 38)))
        )

        val midwayOrg = Organization(
            id = 2, name = "Midway", shortName = "MW", sector = "Mechanic & Auto Care · 24 cities", accentHex = "#D84315", accentColor = Color(0xFFD84315), totalPeople = 1820,
            groups = midwayGroups,
            description = "India's premier 24/7 emergency roadside assistance and mechanic dispatch network serving 24 major cities.",
            headquarters = "Chennai Central & Mumbai Hub",
            foundedYear = 2016,
            founderName = "Thatchina Moorthi",
            founderEmail = "thatchinamoorthiopto@gmail.com",
            statuses = listOf(
                StatusUpdate(201, "Thatchina Moorthi", "Owner", "15m", false, listOf(StatusSlide("Midway completed 100,000 roadside breakdown rescues this year!", 15f))),
                StatusUpdate(202, "Ramesh V", "Operations Head", "30m", false, listOf(StatusSlide("Emergency response time reduced to 9.4 minutes.", 25f))),
                StatusUpdate(203, "Kannan M", "Head Mechanic", "1h", false, listOf(StatusSlide("New EV battery diagnostic tools deployed.", 35f))),
                StatusUpdate(204, "Sanjay Patil", "Regional Admin", "2h", false, listOf(StatusSlide("Mumbai highway rescue squads ready.", 45f))),
                StatusUpdate(205, "Venkatesh K", "Zone Manager", "3h", false, listOf(StatusSlide("Bengaluru EV mobile charging vans active.", 20f))),
                StatusUpdate(206, "Rajesh Kumar", "Hub Lead", "4h", false, listOf(StatusSlide("Delhi NCR battery jumpstart kits supplied.", 30f))),
                StatusUpdate(207, "Deepika R", "App Support Lead", "5h", false, listOf(StatusSlide("Midway Mechanic App v3.2 rating is 4.9 stars!", 40f))),
                StatusUpdate(208, "Arun Vijay", "Supply Lead", "6h", false, listOf(StatusSlide("Original spare parts supply chain 100% stocked.", 10f))),
                StatusUpdate(209, "Syed Imran", "Garage Manager", "7h", false, listOf(StatusSlide("Hyderabad garage night shift team ready.", 50f))),
                StatusUpdate(210, "Nitin Joshi", "Workshop Lead", "8h", false, listOf(StatusSlide("Pune workshop safety audit passed.", 28f)))
            ),
            myStatus = StatusUpdate(0, "Thatchina Moorthi", "Founder & Owner", "just now", true, emptyList()),
            meetings = listOf(
                Meeting(201, "Midway Emergency Dispatch Review", "Today", "5:00 PM", "40 min", "Thatchina Moorthi", "All Hubs", listOf(AgendaItem("5:00", "Response times by city"), AgendaItem("5:25", "Monsoon gear supply")), 88, 120, true),
                Meeting(202, "EV Service & Charging Safety Sync", "Tomorrow", "10:00 AM", "30 min", "Kannan M", "Mechanic Operations", listOf(AgendaItem("10:00", "High voltage safety"), AgendaItem("10:15", "Tooling gaps")), 42, 50)
            ),
            directory = listOf(
                Person(201, "Thatchina Moorthi", "Founder & Owner", "Chennai HQ", "Leadership", "+91 98765 43210", "thatchinamoorthiopto@gmail.com", "Available", 98, 2016, "Founder & Owner of Midway. Revolutionizing 24/7 mechanic dispatch & roadside assistance."),
                Person(202, "Ramesh V", "Operations Head", "Chennai Central", "Operations", "+91 97111 33445", "ramesh.v@midwayapp.in", "Available", 95, 2017, "Leading 24/7 dispatch operations across 24 cities."),
                Person(203, "Kannan M", "Head Mechanic", "Chennai Central", "Mechanic Operations", "+91 97222 44556", "kannan.m@midwayapp.in", "Available", 94, 2017, "Chief mechanic overseeing diagnostic scanners and EV repair protocols."),
                Person(204, "Sanjay Patil", "Regional Admin", "Mumbai Garage Hub", "Regional Ops", "+91 97333 55667", "sanjay.p@midwayapp.in", "Available", 93, 2018, "Managing highway breakdown units across Mumbai-Pune expressways."),
                Person(205, "Venkatesh K", "Zone Manager", "Bengaluru Auto Zone", "Branch Operations", "+91 97444 66778", "venkatesh.k@midwayapp.in", "Available", 92, 2019, "Managing mobile EV charging vans in Indiranagar & Koramangala."),
                Person(206, "Rajesh Kumar", "Hub Lead", "Delhi NCR Hub", "Branch Operations", "+91 97555 77889", "rajesh.k@midwayapp.in", "Available", 91, 2020, "Overseeing 15 roadside assistance hubs in Delhi NCR."),
                Person(207, "Deepika R", "App Support Lead", "Chennai HQ", "Mobile App Support", "+91 97666 88990", "deepika.r@midwayapp.in", "Available", 90, 2021, "Leading customer app booking support and dispatch tracking."),
                Person(208, "Arun Vijay", "Supply Lead", "Chennai HQ", "Parts & Supply Chain", "+91 97777 99001", "arun.v@midwayapp.in", "Available", 89, 2022, "Original spare parts inventory and supplier relationships manager.")
            )
        )

        // 3. EBCT (Electricity Bill Calculator & Utility Analytics)
        val ebctGroups = listOf(
            CommunityGroup(
                id = 301, name = "All Regions", type = GroupType.ALL, membersCount = 1450,
                messages = listOf(
                    textMsg(null, "", "EBCT Electricity Tariff Revision & Automatic Bill Calculation engine update. State grid rules synchronized.", "08:15", 1450, 1310, true, 920),
                    pollMsg("Manoj Swaminathan", "Chief Utility Analyst", "Which power consumption insights feature helps users save most?", "09:00", listOf("Peak Hour Usage Alert" to 310, "Appliance Breakdown Cost" to 240, "Solar Tariff Savings" to 190), 1450, 1200)
                ), unreadCount = 2, pinnedMessageId = 301
            ),
            CommunityGroup(id = 302, name = "State Grid South", type = GroupType.BRANCH, membersCount = 410, messages = listOf(textMsg("Thatchina Moorthi", "Founder & Owner", "TNEB & BESCOM tariff calculator engine updated for new slabs.", "09:30", 410, 390), imgMsg("Subhashini K", "Senior Analyst", "Smart meter consumption graph algorithm", "10:10", 180f, 410, 370)), unreadCount = 1),
            CommunityGroup(id = 303, name = "State Grid North", type = GroupType.BRANCH, membersCount = 380, messages = listOf(textMsg("Vikas Sharma", "Grid Lead", "UPPCL & BSES bill calculation rules integrated.", "10:45", 380, 360))),
            CommunityGroup(id = 304, name = "Central Utility Hub", type = GroupType.BRANCH, membersCount = 320, messages = listOf(textMsg("Anita Roy", "Hub Manager", "Commercial high-tension tariff audit completed.", "11:20", 320, 300))),
            CommunityGroup(id = 305, name = "East Power Zone", type = GroupType.BRANCH, membersCount = 180, messages = listOf(textMsg("Arup Banerjee", "Zone Lead", "West Bengal state grid slab rates published.", "12:30", 180, 170))),
            CommunityGroup(id = 306, name = "West Regional Grid", type = GroupType.BRANCH, membersCount = 160, messages = listOf(textMsg("Pravin Deshmukh", "West Lead", "MSEDCL solar net-metering calculator live.", "14:10", 160, 150))),
            CommunityGroup(id = 307, name = "Billing Operations", type = GroupType.TEAM, membersCount = 210, messages = listOf(textMsg("Subhashini K", "Billing Lead", "Automated meter OCR scanner accuracy improved to 99.1%.", "09:15", 210, 198), vidMsg("Subhashini K", "Billing Lead", "Meter photo reading walkthrough", "09:40", "1:15", 180f, 210, 190)), unreadCount = 1),
            CommunityGroup(id = 308, name = "Meter Audit Team", type = GroupType.TEAM, membersCount = 140, messages = listOf(textMsg("Manoj Swaminathan", "Chief Analyst", "Commercial meter anomaly detection alert engine tested.", "11:00", 140, 132))),
            CommunityGroup(id = 309, name = "Tariff Analytics", type = GroupType.TEAM, membersCount = 65, messages = listOf(textMsg("Kavitha M", "Analytics Lead", "Solar rooftop ROI calculator feature published.", "13:45", 65, 62)))
        )

        val ebctOrg = Organization(
            id = 3, name = "EBCT", shortName = "EB", sector = "Electricity & Utility Analytics · 18 regions", accentHex = "#00838F", accentColor = Color(0xFF00838F), totalPeople = 1450,
            groups = ebctGroups,
            description = "Electricity bill calculation engine and utility tariff analytics platform covering 18 state grids across South, North, East, and West India.",
            headquarters = "Central Utility Hub, Chennai",
            foundedYear = 2017,
            founderName = "Thatchina Moorthi",
            founderEmail = "thatchinamoorthiopto@gmail.com",
            statuses = listOf(
                StatusUpdate(301, "Thatchina Moorthi", "Owner", "20m", false, listOf(StatusSlide("EBCT Calculator helped users save ₹25 Crore in electricity bills this month!", 180f))),
                StatusUpdate(302, "Manoj Swaminathan", "Chief Utility Analyst", "35m", false, listOf(StatusSlide("New solar net metering calculator is live in 18 states.", 190f))),
                StatusUpdate(303, "Subhashini K", "Billing Lead", "50m", false, listOf(StatusSlide("Smart Meter OCR camera scanner accuracy reached 99.1%.", 200f))),
                StatusUpdate(304, "Vikas Sharma", "Grid Lead", "1h", false, listOf(StatusSlide("North grid slab calculation rules verified.", 170f))),
                StatusUpdate(305, "Anita Roy", "Hub Manager", "2h", false, listOf(StatusSlide("Commercial tariff audit finished with zero errors.", 160f))),
                StatusUpdate(306, "Kavitha M", "Analytics Lead", "3h", false, listOf(StatusSlide("Peak hour power consumption alert engine live.", 210f))),
                StatusUpdate(307, "Arup Banerjee", "Zone Lead", "4h", false, listOf(StatusSlide("East power zone utility partners onboarded.", 185f))),
                StatusUpdate(308, "Pravin Deshmukh", "West Lead", "5h", false, listOf(StatusSlide("Solar rooftop savings estimator deployed.", 195f))),
                StatusUpdate(309, "Ranjit Singh", "Grid Analyst", "6h", false, listOf(StatusSlide("State grid electricity tariff sync active.", 175f))),
                StatusUpdate(310, "Swati Verma", "Customer Support", "7h", false, listOf(StatusSlide("EBCT App user satisfaction rating is 4.8/5!", 205f)))
            ),
            myStatus = StatusUpdate(0, "Thatchina Moorthi", "Owner", "just now", true, emptyList()),
            meetings = listOf(
                Meeting(301, "EBCT Utility Tariff & Grid Calculation Sync", "Today", "4:30 PM", "45 min", "Thatchina Moorthi", "All Regions", listOf(AgendaItem("4:30", "State grid slab updates"), AgendaItem("4:55", "Solar net metering rules")), 62, 80, true),
                Meeting(302, "Smart Meter OCR Accuracy Review", "Tomorrow", "11:30 AM", "30 min", "Subhashini K", "Billing Operations", listOf(AgendaItem("11:30", "OCR model performance"), AgendaItem("11:45", "Camera scanner UX")), 35, 40)
            ),
            directory = listOf(
                Person(301, "Thatchina Moorthi", "Founder & Owner", "Central Hub", "Leadership", "+91 98765 43210", "thatchinamoorthiopto@gmail.com", "Available", 98, 2017, "Founder & Owner of EBCT. Empowering millions with accurate electricity bill calculation & power insights."),
                Person(302, "Manoj Swaminathan", "Chief Utility Analyst", "State Grid South", "Analytics", "+91 96111 44556", "manoj.s@ebct.in", "Available", 96, 2018, "Leading utility tariff analytics across 18 state grids."),
                Person(303, "Subhashini K", "Senior Analyst & Billing Lead", "State Grid South", "Billing Operations", "+91 96222 55667", "subhashini.k@ebct.in", "Available", 95, 2019, "Smart meter OCR reading and tariff slab calculation specialist."),
                Person(304, "Vikas Sharma", "Grid Lead", "State Grid North", "Branch Operations", "+91 96333 66778", "vikas.s@ebct.in", "Available", 94, 2020, "Managing North region state grid synchronization."),
                Person(305, "Anita Roy", "Hub Manager", "Central Utility Hub", "Operations", "+91 96444 77889", "anita.r@ebct.in", "Available", 93, 2021, "Commercial high-tension tariff audit lead."),
                Person(306, "Pravin Deshmukh", "West Lead", "West Regional Grid", "Branch Operations", "+91 96555 88990", "pravin.d@ebct.in", "Available", 92, 2021, "MSEDCL solar net-metering calculator architect."),
                Person(307, "Kavitha M", "Analytics Lead", "Tariff Analytics", "Analytics", "+91 96666 99001", "kavitha.m@ebct.in", "Available", 91, 2022, "Solar rooftop ROI calculator and peak hour alert engineer.")
            )
        )

        // 4. Arvind Retail Group
        val arvindGroups = listOf(
            CommunityGroup(
                id = 401, name = "All Company", type = GroupType.ALL, membersCount = 1240,
                messages = listOf(
                    textMsg(null, "", "Diwali sale starts Friday 6 AM at every store. Price tags must be updated by Thursday night.", "09:05", 1240, 1103, true, 684),
                    pollMsg("Meera Iyer", "Regional Manager", "Which shift works best for weekend staff during the sale?", "09:40", listOf("6 AM – 2 PM" to 142, "2 PM – 10 PM" to 96, "Split shift" to 38), 1240, 890)
                ), unreadCount = 3, pinnedMessageId = 401
            ),
            CommunityGroup(id = 402, name = "Chennai Branch", type = GroupType.BRANCH, membersCount = 212, messages = listOf(textMsg("Meera Iyer", "Regional Manager", "Window displays go live tomorrow. Please post photos here by 8 PM.", "10:02", 212, 198), imgMsg("Lakshmi P", "Sales Associate", "Anna Nagar window display is ready", "10:48", 22f, 212, 180)), unreadCount = 2),
            CommunityGroup(id = 403, name = "Coimbatore Branch", type = GroupType.BRANCH, membersCount = 148, messages = listOf(textMsg("Suresh Kumar", "Store Manager", "Festive stock count is at 70%. We will finish by Wednesday.", "08:30", 148, 140))),
            CommunityGroup(id = 404, name = "Bengaluru Branch", type = GroupType.BRANCH, membersCount = 176, messages = listOf(textMsg("Imran Pasha", "Branch Manager", "Two extra billing counters will open at Indiranagar for the sale weekend.", "11:15", 176, 160)), unreadCount = 1),
            CommunityGroup(id = 405, name = "Madurai Branch", type = GroupType.BRANCH, membersCount = 132, messages = listOf(textMsg("Anil Kumar", "Branch Manager", "Monthly targets updated on dashboard.", "11:55", 132, 120))),
            CommunityGroup(id = 406, name = "Trichy Branch", type = GroupType.BRANCH, membersCount = 98, messages = listOf(textMsg("Deepak Joshi", "Branch Manager", "Weekly review on Monday 10 AM.", "10:40", 98, 90))),
            CommunityGroup(id = 407, name = "Digital Team", type = GroupType.TEAM, membersCount = 34, messages = listOf(textMsg("Divya Ramesh", "Head of Digital", "Sale banners ready.", "12:20", 34, 32))),
            CommunityGroup(id = 408, name = "Accounts Team", type = GroupType.TEAM, membersCount = 27, messages = listOf(textMsg("Karthik Rao", "Accounts Lead", "GST filing closes on 20th.", "13:05", 27, 25, true, 18)))
        )

        val arvindOrg = Organization(
            id = 4, name = "Arvind Retail Group", shortName = "AR", sector = "Retail · 38 stores", accentHex = "#C94F0E", accentColor = Color(0xFFC94F0E), totalPeople = 1240,
            groups = arvindGroups,
            description = "Multi-brand retail and fashion store conglomerate operating 38 stores across Tamil Nadu and South India.",
            headquarters = "Head Office, Chennai",
            foundedYear = 2015,
            founderName = "Thatchina Moorthi",
            founderEmail = "thatchinamoorthiopto@gmail.com",
            statuses = listOf(
                StatusUpdate(401, "Meera Iyer", "Regional Manager", "25m", false, listOf(StatusSlide("Chennai reached 120% of target!", 25f))),
                StatusUpdate(402, "Divya Ramesh", "Head of Digital", "1h", false, listOf(StatusSlide("Sale banner live on website.", 210f))),
                StatusUpdate(403, "Karthik Rao", "Accounts Lead", "2h", false, listOf(StatusSlide("31 store invoices uploaded.", 160f))),
                StatusUpdate(404, "Suresh Kumar", "Store Manager", "3h", false, listOf(StatusSlide("Stock count 70% done.", 30f))),
                StatusUpdate(405, "Imran Pasha", "Branch Manager", "4h", false, listOf(StatusSlide("Billing counters tested.", 40f))),
                StatusUpdate(406, "Anil Kumar", "Branch Manager", "5h", false, listOf(StatusSlide("Madurai team target achieved.", 20f))),
                StatusUpdate(407, "Deepak Joshi", "Branch Manager", "6h", false, listOf(StatusSlide("Trichy store window ready.", 50f))),
                StatusUpdate(408, "Lakshmi P", "Sales Associate", "7h", false, listOf(StatusSlide("Customer feedback score 4.9!", 35f))),
                StatusUpdate(409, "Pooja Singh", "Store Operations", "8h", false, listOf(StatusSlide("Festive shift schedule published.", 15f))),
                StatusUpdate(410, "Farhan Ali", "Inventory Lead", "9h", false, listOf(StatusSlide("Warehouse delivery completed.", 45f)))
            ),
            myStatus = StatusUpdate(0, "Thatchina Moorthi", "Founder & Owner", "just now", true, emptyList()),
            meetings = listOf(Meeting(401, "Diwali sale readiness", "Today", "4:00 PM", "45 min", "Thatchina Moorthi", "All Store Managers", listOf(AgendaItem("4:00", "Readiness")), 31, 38, true)),
            directory = listOf(
                Person(401, "Thatchina Moorthi", "Founder & Owner", "Head Office", "Leadership", "+91 98765 43210", "thatchinamoorthiopto@gmail.com", "Available", 98, 2015, "Founder & Owner of Arvind Retail Group. Leading multi-store retail expansion."),
                Person(402, "Meera Iyer", "Regional Manager", "Chennai Branch", "Regional Operations", "+91 95111 22334", "meera.i@arvindretail.com", "Available", 96, 2016, "Managing Chennai & Tamil Nadu store operations."),
                Person(403, "Suresh Kumar", "Store Manager", "Coimbatore Branch", "Store Ops", "+91 95222 33445", "suresh.k@arvindretail.com", "Available", 94, 2017, "Coimbatore flagship store manager."),
                Person(404, "Imran Pasha", "Branch Manager", "Bengaluru Branch", "Store Ops", "+91 95333 44556", "imran.p@arvindretail.com", "Available", 93, 2018, "Indiranagar Bengaluru store manager."),
                Person(405, "Divya Ramesh", "Head of Digital", "Head Office", "Digital Team", "+91 95444 55667", "divya.r@arvindretail.com", "Available", 95, 2019, "E-commerce & festive promotion campaigns director."),
                Person(406, "Karthik Rao", "Accounts Lead", "Head Office", "Accounts Team", "+91 95555 66778", "karthik.r@arvindretail.com", "Available", 92, 2020, "Store invoices & GST compliance lead.")
            )
        )

        // 5. CareFirst Hospitals
        val cfGroups = listOf(
            CommunityGroup(id = 501, name = "All Hospitals", type = GroupType.ALL, membersCount = 3180, messages = listOf(textMsg(null, "", "The NABH audit visit is on 12 November.", "08:50", 3180, 2610, true, 1618)), unreadCount = 2),
            CommunityGroup(id = 502, name = "Mumbai Branch", type = GroupType.BRANCH, membersCount = 1020, messages = listOf(textMsg("Nikhil Joshi", "Hospital Admin", "ICU bed count update due.", "10:10", 1020, 950))),
            CommunityGroup(id = 503, name = "Pune Branch", type = GroupType.BRANCH, membersCount = 860, messages = listOf(textMsg("Rahul Deshpande", "Admin Head", "New visitor pass system starts Monday.", "11:00", 860, 800))),
            CommunityGroup(id = 504, name = "Nagpur Branch", type = GroupType.BRANCH, membersCount = 540, messages = listOf(imgMsg("Dr. Anita Menon", "Medical Director", "Hand hygiene posters Ward 3", "12:05", 170f, 540, 480))),
            CommunityGroup(id = 505, name = "Nashik Branch", type = GroupType.BRANCH, membersCount = 410, messages = listOf(textMsg("Rekha Nair", "Branch Admin", "Festive roster ready.", "14:05", 410, 390))),
            CommunityGroup(id = 506, name = "Thane Branch", type = GroupType.BRANCH, membersCount = 380, messages = listOf(textMsg("Farhan Ali", "Branch Admin", "Weekly review Monday 10 AM.", "13:20", 380, 360))),
            CommunityGroup(id = 507, name = "Nursing Team", type = GroupType.TEAM, membersCount = 640, messages = listOf(textMsg("Grace Thomas", "Superintendent", "Night shift roster published.", "09:15", 640, 610))),
            CommunityGroup(id = 508, name = "Pharmacy Team", type = GroupType.TEAM, membersCount = 72, messages = listOf(textMsg("Vikram Shah", "Pharmacy Lead", "Cold chain log must be signed.", "07:20", 72, 68, true, 55))),
            CommunityGroup(id = 509, name = "Doctors", type = GroupType.TEAM, membersCount = 120, messages = listOf(textMsg("Dr. Anita Menon", "Medical Director", "Task updates due.", "07:50", 120, 110))),
            CommunityGroup(id = 510, name = "Radiology Team", type = GroupType.TEAM, membersCount = 45, messages = listOf(textMsg("Shreya Rao", "Lead", "Equipment calibration scheduled.", "13:20", 45, 42)))
        )

        val cfOrg = Organization(
            id = 5, name = "CareFirst Hospitals", shortName = "CF", sector = "Healthcare · 6 hospitals", accentHex = "#0B8A7C", accentColor = Color(0xFF0B8A7C), totalPeople = 3180,
            groups = cfGroups,
            description = "NABH-accredited multi-specialty hospital chain with 6 state-of-the-art medical centers.",
            headquarters = "Head Office, Mumbai & Chennai",
            foundedYear = 2014,
            founderName = "Thatchina Moorthi",
            founderEmail = "thatchinamoorthiopto@gmail.com",
            statuses = listOf(
                StatusUpdate(501, "Grace Thomas", "Superintendent", "40m", true, listOf(StatusSlide("Night shift handover checklist done.", 170f))),
                StatusUpdate(502, "Vikram Shah", "Pharmacy Lead", "1h", false, listOf(StatusSlide("Cold chain audit passed.", 160f))),
                StatusUpdate(503, "Dr. Anita Menon", "Medical Director", "2h", false, listOf(StatusSlide("NABH audit readiness 95%.", 180f))),
                StatusUpdate(504, "Rahul Deshpande", "Admin Head", "3h", false, listOf(StatusSlide("Kiosks installed at main gate.", 190f))),
                StatusUpdate(505, "Nikhil Joshi", "Hospital Admin", "4h", false, listOf(StatusSlide("ICU bed availability updated.", 150f))),
                StatusUpdate(506, "Shreya Rao", "Radiology Lead", "5h", false, listOf(StatusSlide("X-Ray calibration completed.", 200f))),
                StatusUpdate(507, "Rekha Nair", "Branch Admin", "6h", false, listOf(StatusSlide("Nashik staff roster verified.", 170f))),
                StatusUpdate(508, "Farhan Ali", "Branch Admin", "7h", false, listOf(StatusSlide("Thane branch review ready.", 210f))),
                StatusUpdate(509, "Mohan Das", "Facility Manager", "8h", false, listOf(StatusSlide("Safety walk-through completed.", 220f))),
                StatusUpdate(510, "Vikas Patil", "Housekeeping Lead", "9h", false, listOf(StatusSlide("Sanitizing drive complete.", 140f)))
            ),
            myStatus = StatusUpdate(0, "Thatchina Moorthi", "Founder & Owner", "just now", true, emptyList()),
            meetings = listOf(Meeting(501, "NABH audit preparation", "Today", "3:30 PM", "60 min", "Thatchina Moorthi", "All Department Heads", listOf(AgendaItem("3:30", "Checklist status")), 41, 52, true)),
            directory = listOf(
                Person(501, "Thatchina Moorthi", "Founder & Owner", "Head Office", "Leadership", "+91 98765 43210", "thatchinamoorthiopto@gmail.com", "Available", 97, 2014, "Founder & Owner of CareFirst Hospitals. Leading multi-specialty healthcare excellence."),
                Person(502, "Dr. Anita Menon", "Medical Director", "Nagpur Branch", "Doctors", "+91 94111 22334", "anita.m@carefirst.in", "Available", 98, 2015, "Medical Director & NABH accreditation officer."),
                Person(503, "Grace Thomas", "Superintendent", "Head Office", "Nursing Team", "+91 94222 33445", "grace.t@carefirst.in", "Available", 96, 2016, "Chief Nursing Superintendent across all 6 hospitals."),
                Person(504, "Vikram Shah", "Pharmacy Lead", "Mumbai Branch", "Pharmacy Team", "+91 94333 44556", "vikram.s@carefirst.in", "Available", 94, 2017, "Hospital pharmacy & cold-chain medicine manager."),
                Person(505, "Nikhil Joshi", "Hospital Admin", "Mumbai Branch", "Branch Operations", "+91 94444 55667", "nikhil.j@carefirst.in", "Available", 93, 2018, "Mumbai hospital general administrator."),
                Person(506, "Rahul Deshpande", "Admin Head", "Pune Branch", "Branch Operations", "+91 94555 66778", "rahul.d@carefirst.in", "Available", 92, 2019, "Pune hospital facility & visitor operations head.")
            )
        )

        // 6. Kaveri Logistics
        val klGroups = listOf(
            CommunityGroup(id = 601, name = "All Hubs", type = GroupType.ALL, membersCount = 2210, messages = listOf(textMsg(null, "", "Festive volume is up 38%. Hubs run 24-hour sorting from Monday.", "07:30", 2210, 1730, true, 1200)), unreadCount = 2),
            CommunityGroup(id = 602, name = "Chennai Hub", type = GroupType.BRANCH, membersCount = 420, messages = listOf(textMsg("Zainab Sheikh", "Hub Manager", "Dock 4 closed for repair.", "09:10", 420, 390))),
            CommunityGroup(id = 603, name = "Fleet Team", type = GroupType.TEAM, membersCount = 310, messages = listOf(textMsg("Gopal Singh", "Fleet Lead", "Twelve trucks due for service.", "08:00", 310, 290)))
        )

        val klOrg = Organization(
            id = 6, name = "Kaveri Logistics", shortName = "KL", sector = "Logistics · 14 hubs", accentHex = "#1F6FD0", accentColor = Color(0xFF1F6FD0), totalPeople = 2210,
            groups = klGroups,
            description = "Express freight and supply chain network operating 14 automated distribution hubs.",
            headquarters = "Chennai Sorting Hub",
            foundedYear = 2016,
            founderName = "Thatchina Moorthi",
            founderEmail = "thatchinamoorthiopto@gmail.com",
            statuses = listOf(
                StatusUpdate(601, "Zainab Sheikh", "Hub Manager", "30m", false, listOf(StatusSlide("Zero delays on dispatch.", 210f))),
                StatusUpdate(602, "Gopal Singh", "Fleet Lead", "1h", false, listOf(StatusSlide("Twelve trucks serviced.", 220f))),
                StatusUpdate(603, "Prakash Natarajan", "Operations Head", "2h", false, listOf(StatusSlide("Hub volume capacity expanded.", 200f))),
                StatusUpdate(604, "Arun Selvam", "Supervisor", "3h", false, listOf(StatusSlide("Safety briefing done.", 190f))),
                StatusUpdate(605, "Sunil Verma", "Hub Manager", "4h", false, listOf(StatusSlide("Conveyor B online.", 180f))),
                StatusUpdate(606, "Neha Kulkarni", "Finance Lead", "5h", false, listOf(StatusSlide("Fuel cards reconciled.", 230f))),
                StatusUpdate(607, "Suresh Kumar", "Hub Lead", "6h", false, listOf(StatusSlide("Night dispatch active.", 210f))),
                StatusUpdate(608, "Rajesh P", "Loader Supervisor", "7h", false, listOf(StatusSlide("Shift B sorting done.", 240f))),
                StatusUpdate(609, "Karan Singh", "Driver Lead", "8h", false, listOf(StatusSlide("All routes delivered on time.", 225f))),
                StatusUpdate(610, "Meghna Roy", "Customer Care", "9h", false, listOf(StatusSlide("Customer satisfaction score 99%.", 250f)))
            ),
            myStatus = StatusUpdate(0, "Thatchina Moorthi", "Founder & Owner", "just now", true, emptyList()),
            meetings = listOf(Meeting(601, "Festive peak planning", "Today", "5:30 PM", "45 min", "Thatchina Moorthi", "All Hub Managers", listOf(AgendaItem("5:30", "Volume forecast")), 12, 14, true)),
            directory = listOf(
                Person(601, "Thatchina Moorthi", "Founder & Owner", "Chennai Hub", "Leadership", "+91 98765 43210", "thatchinamoorthiopto@gmail.com", "Available", 94, 2016, "Founder & Owner of Kaveri Logistics. Managing supply chain & express freight network."),
                Person(602, "Zainab Sheikh", "Hub Manager", "Chennai Hub", "Branch Operations", "+91 93111 22334", "zainab.s@kaverilogistics.in", "Available", 95, 2017, "Chennai sorting hub & dock operations manager."),
                Person(603, "Gopal Singh", "Fleet Lead", "Fleet Team", "Operations", "+91 93222 33445", "gopal.s@kaverilogistics.in", "Available", 93, 2018, "Heavy vehicle fleet maintenance & driver safety lead."),
                Person(604, "Prakash Natarajan", "Operations Head", "Chennai Hub", "Operations", "+91 93333 44556", "prakash.n@kaverilogistics.in", "Available", 96, 2016, "Head of express freight operations across 14 hubs.")
            )
        )

        // 7. Novatech Solutions
        val nsGroups = listOf(
            CommunityGroup(id = 701, name = "All Hands", type = GroupType.ALL, membersCount = 860, messages = listOf(textMsg(null, "", "Quarterly town hall is on Friday at 5 PM.", "09:00", 860, 690, true, 510)), unreadCount = 2),
            CommunityGroup(id = 702, name = "Engineering", type = GroupType.TEAM, membersCount = 390, messages = listOf(textMsg("Ishaan Kapoor", "VP Engineering", "Release freeze begins Thursday 6 PM.", "09:45", 390, 360, true, 280)))
        )

        val nsOrg = Organization(
            id = 7, name = "Novatech Solutions", shortName = "NS", sector = "IT services · 6 offices", accentHex = "#C2285F", accentColor = Color(0xFFC2285F), totalPeople = 860,
            groups = nsGroups,
            description = "Enterprise IT consulting and cloud software transformation company with 6 development centers.",
            headquarters = "Bengaluru Office",
            foundedYear = 2017,
            founderName = "Thatchina Moorthi",
            founderEmail = "thatchinamoorthiopto@gmail.com",
            statuses = listOf(
                StatusUpdate(701, "Ishaan Kapoor", "VP Engineering", "20m", false, listOf(StatusSlide("Build time down 35%.", 330f))),
                StatusUpdate(702, "Pooja Nair", "HR Head", "1h", false, listOf(StatusSlide("Welcome 14 new joiners!", 340f))),
                StatusUpdate(703, "Rohit Sinha", "Sales Director", "2h", false, listOf(StatusSlide("3 new client wins!", 320f))),
                StatusUpdate(704, "Tanvi Desai", "Office Lead", "3h", false, listOf(StatusSlide("Level 2 parking reopened.", 310f))),
                StatusUpdate(705, "Varun Pillai", "Office Lead", "4h", false, listOf(StatusSlide("New cafeteria menu live.", 350f))),
                StatusUpdate(706, "Siddharth V", "Tech Lead", "5h", false, listOf(StatusSlide("Sprint goals completed 100%.", 300f))),
                StatusUpdate(707, "Ananya R", "UI Designer", "6h", false, listOf(StatusSlide("Design review completed.", 290f))),
                StatusUpdate(708, "Deepak M", "DevOps Engineer", "7h", false, listOf(StatusSlide("Production deployment smooth.", 280f))),
                StatusUpdate(709, "Swati K", "QA Manager", "8h", false, listOf(StatusSlide("Zero bug release achieved.", 325f))),
                StatusUpdate(710, "Vikram B", "Product Manager", "9h", false, listOf(StatusSlide("Client demo success!", 315f)))
            ),
            myStatus = StatusUpdate(0, "Thatchina Moorthi", "Founder & Owner", "just now", true, emptyList()),
            meetings = listOf(Meeting(701, "Quarterly town hall", "Today", "5:00 PM", "60 min", "Thatchina Moorthi", "All Hands", listOf(AgendaItem("5:00", "Town hall")), 512, 860, true)),
            directory = listOf(
                Person(701, "Thatchina Moorthi", "Founder & Owner", "Bengaluru Office", "Leadership", "+91 98765 43210", "thatchinamoorthiopto@gmail.com", "Available", 98, 2017, "Founder & Owner of Novatech Solutions. Leading IT transformation and enterprise consulting."),
                Person(702, "Ishaan Kapoor", "VP Engineering", "Bengaluru Office", "Engineering", "+91 92111 22334", "ishaan.k@novatech.com", "Available", 96, 2018, "VP Engineering managing cloud and enterprise software delivery."),
                Person(703, "Pooja Nair", "HR Head", "Bengaluru Office", "All Hands", "+91 92222 33445", "pooja.n@novatech.com", "Available", 94, 2019, "Chief People Officer & talent acquisition head."),
                Person(704, "Siddharth V", "Tech Lead", "Bengaluru Office", "Engineering", "+91 92333 44556", "siddharth.v@novatech.com", "Available", 93, 2020, "Enterprise microservices & cloud architect.")
            )
        )

        return listOf(maxinnovOrg, midwayOrg, ebctOrg, arvindOrg, cfOrg, klOrg, nsOrg)
    }
}
