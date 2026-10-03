package com.example.data.model

object SampleGameData {

    val initialHabits = listOf(
        HabitItem(
            title = "Brush Teeth (Morning & Night)",
            icon = "🪥",
            rewardCoins = 1,
            category = "Health",
            frequency = "DAILY",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            note = "Take care of your bright smile!"
        ),
        HabitItem(
            title = "Go to School / Finish Classes",
            icon = "🎒",
            rewardCoins = 5,
            category = "School",
            frequency = "WEEKDAYS",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            note = "Focus and learn exciting new things!"
        ),
        HabitItem(
            title = "Make the Bed & Tidy Room",
            icon = "🛏️",
            rewardCoins = 2,
            category = "Morning",
            frequency = "DAILY",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            note = "Start the day with an organized bedroom."
        ),
        HabitItem(
            title = "Put Away Toys & Blocks",
            icon = "🧸",
            rewardCoins = 2,
            category = "Evening",
            frequency = "DAILY",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            note = "Keep the playroom safe and clean."
        ),
        HabitItem(
            title = "Read a Storybook (15 mins)",
            icon = "📚",
            rewardCoins = 3,
            category = "Responsibility",
            frequency = "DAILY",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            note = "Expand your imagination and vocabulary!"
        ),
        HabitItem(
            title = "Play Outside & Exercise",
            icon = "🌳",
            rewardCoins = 2,
            category = "Health",
            frequency = "DAILY",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            note = "Get fresh air and run around outdoors."
        ),
        HabitItem(
            title = "Help Clear Family Table",
            icon = "🍽️",
            rewardCoins = 0,
            category = "Family",
            frequency = "DAILY",
            confirmationMethod = "INSTANT",
            isFamilyTeamwork = true,
            note = "Family Teamwork! We take care of our home together."
        )
    )

    val recoveryQuests = listOf(
        RecoveryQuest(
            id = "quest_tidy_bookshelf",
            title = "Organize Bookshelf & Papers",
            icon = "📚",
            rewardCoins = 2,
            description = "Sort books by size and stack neatly on the shelf.",
            category = "Home Responsibility"
        ),
        RecoveryQuest(
            id = "quest_pet_care",
            title = "Pet Brush & Fresh Water Bowl",
            icon = "🐶",
            rewardCoins = 2,
            description = "Gently brush family pet fur and refill water.",
            category = "Caring"
        ),
        RecoveryQuest(
            id = "quest_shoe_rack",
            title = "Neat Entryway Shoe Organizer",
            icon = "👟",
            rewardCoins = 2,
            description = "Line up shoes neatly by the front door.",
            category = "Teamwork"
        ),
        RecoveryQuest(
            id = "quest_plant_helper",
            title = "Garden Leaf Helper",
            icon = "🪴",
            rewardCoins = 3,
            description = "Help pick dry leaves and water the potted plants.",
            category = "Nature"
        )
    )

    val gardenSeedCatalog = listOf(
        GardenTreeOption(
            name = "Safe Oak Tree",
            icon = "🌳",
            cost = 10,
            treeType = "Safe Oak (+15% Steady)"
        ),
        GardenTreeOption(
            name = "Sweet Apple Orchard",
            icon = "🍎",
            cost = 20,
            treeType = "Apple Orchard (+25% Growth)"
        ),
        GardenTreeOption(
            name = "Golden Sparkle Tree",
            icon = "🌟",
            cost = 30,
            treeType = "Golden Compounding (+40% Adventure)"
        )
    )

    val shopProducts = listOf(
        ShopProduct(
            id = "shop_apple",
            title = "Crisp Red Apple",
            icon = "🍎",
            price = 2,
            category = "Snacks",
            description = "A crunchy, juicy fruit treat for quick energy."
        ),
        ShopProduct(
            id = "shop_stickers",
            title = "Sparkle Stickers Pack",
            icon = "⭐",
            price = 4,
            category = "Crafts",
            description = "Shiny gold stars to decorate your adventure book."
        ),
        ShopProduct(
            id = "shop_cuddly_bear",
            title = "Deluxe Glow Bear",
            icon = "🧸",
            price = 16,
            category = "Toys",
            description = "A big glowing plush teddy bear with a bowtie.",
            smartAlternativeTitle = "Classic Cuddly Bear",
            smartAlternativePrice = 8,
            smartXpReward = 10
        ),
        ShopProduct(
            id = "shop_racing_car",
            title = "Turbo Remote Race Car",
            icon = "🏎️",
            price = 24,
            category = "Toys",
            description = "High speed racer with remote antenna.",
            smartAlternativeTitle = "Swift Die-Cast Racer",
            smartAlternativePrice = 12,
            smartXpReward = 15
        ),
        ShopProduct(
            id = "shop_scooter",
            title = "Neon Glow Spark Scooter",
            icon = "🛴",
            price = 50,
            category = "Big Dreams",
            description = "Deluxe sparkling 3-wheel scooter with neon lights.",
            isLongTermWish = true,
            smartAlternativeTitle = "Classic Speedster Scooter",
            smartAlternativePrice = 28,
            smartXpReward = 25
        ),
        ShopProduct(
            id = "shop_big_castle",
            title = "Royal Knight Mega Castle",
            icon = "🏰",
            price = 60,
            category = "Big Dreams",
            description = "A massive towering castle with 10 knights & towers!",
            isLongTermWish = true,
            smartAlternativeTitle = "Kingdom Fortress Playset",
            smartAlternativePrice = 32,
            smartXpReward = 30
        )
    )

    val chores = listOf(
        ChoreItem(
            id = "chore_feed_pet",
            title = "Feed the Village Pet",
            icon = "🐶",
            rewardCoins = 2,
            isFamilyResponsibility = false,
            note = "Pour fresh food and water into pet bowls.",
            category = "Pets"
        ),
        ChoreItem(
            id = "chore_tidy_toys",
            title = "Put Toys Away in Box",
            icon = "🧸",
            rewardCoins = 3,
            isFamilyResponsibility = false,
            note = "Organize blocks and stuffed toys cleanly.",
            category = "Bedroom"
        ),
        ChoreItem(
            id = "chore_water_plants",
            title = "Help Water the Plants",
            icon = "🪴",
            rewardCoins = 2,
            isFamilyResponsibility = false,
            note = "Give thirsty houseplants a gentle drink of water.",
            category = "Yard"
        ),
        ChoreItem(
            id = "chore_family_table",
            title = "Clear Your Own Plate",
            icon = "🍽️",
            rewardCoins = 0,
            isFamilyResponsibility = true,
            note = "Part of our family teamwork! We take care of our home together without expecting pay.",
            category = "Family Teamwork"
        )
    )

    val needsVsWantsCards = listOf(
        NeedsVsWantsCard(
            id = "nvw_apple",
            title = "Healthy Food (Apple)",
            icon = "🍎",
            isNeed = true,
            explanation = "We need nutritious food every day so our bodies stay strong, healthy, and energized.",
            category = "Food"
        ),
        NeedsVsWantsCard(
            id = "nvw_videogame",
            title = "Video Game",
            icon = "🎮",
            isNeed = false,
            explanation = "Video games are super fun, but we can live happily and safely without them. That makes it a Want!",
            category = "Entertainment"
        ),
        NeedsVsWantsCard(
            id = "nvw_house",
            title = "Warm Cozy Home",
            icon = "🏠",
            isNeed = true,
            explanation = "A house protects us from rain, cold winter winds, and keeps our family safe.",
            category = "Shelter"
        ),
        NeedsVsWantsCard(
            id = "nvw_candy",
            title = "Lollipop & Candy",
            icon = "🍭",
            isNeed = false,
            explanation = "Sweets are yummy treats, but our body needs real healthy meals first. So candy is a Want!",
            category = "Snacks"
        ),
        NeedsVsWantsCard(
            id = "nvw_shoes",
            title = "School Shoes",
            icon = "👟",
            isNeed = true,
            explanation = "Shoes protect our feet from sharp rocks, heat, and cold ground.",
            category = "Clothing"
        ),
        NeedsVsWantsCard(
            id = "nvw_water",
            title = "Fresh Drinking Water",
            icon = "🚰",
            isNeed = true,
            explanation = "Water is essential for life! Every plant, animal, and human needs fresh water every single day.",
            category = "Health"
        )
    )

    val lifeEvents = listOf(
        LifeEventCard(
            id = "event_toy_break",
            title = "Broken Toy Wheel 🔧",
            icon = "🚗",
            description = "Uh oh! The little toy car lost a wheel. We need 2 coins from the Safety Jar for a replacement part.",
            coinImpact = -2,
            isEmergency = true,
            lesson = "This is why we have a Safety Jar! Unexpected surprises don't ruin our day because we prepared!"
        ),
        LifeEventCard(
            id = "event_grandma_visit",
            title = "Grandma's Surprise Visit 👵",
            icon = "🎁",
            description = "Grandma came over and loved seeing how tidy your room is! She gifted you +4 shiny coins!",
            coinImpact = 4,
            isEmergency = false,
            lesson = "Extra gifts are wonderful surprises! You get to decide if you want to save, spend, or share them!"
        ),
        LifeEventCard(
            id = "event_lost_pencil",
            title = "Lost School Drawing Pencil ✏️",
            icon = "✏️",
            description = "Your art pencil slipped into the grass. Use 1 coin from your Safety Jar to get a new one.",
            coinImpact = -1,
            isEmergency = true,
            lesson = "Safety Jar saves the day again! Having an emergency buffer makes us resilient and worry-free."
        ),
        LifeEventCard(
            id = "event_lemonade_bonus",
            title = "Sunny Lemonade Helper 🍋",
            icon = "🍋",
            description = "You helped make fresh lemonade for thirsty neighbors! They tipped +3 coins for your kindness!",
            coinImpact = 3,
            isEmergency = false,
            lesson = "Helping others naturally brings joy and rewards to our village!"
        )
    )

    val skillItems = listOf(
        SkillBusinessItem(
            id = "skill_card",
            title = "Handmade Greeting Card",
            icon = "💌",
            skillName = "Art & Drawing",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 8,
            category = "Art & Crafts",
            description = "Fold colorful paper, draw flowers, and write warm messages."
        ),
        SkillBusinessItem(
            id = "skill_cookie",
            title = "Fresh Oatmeal Cookie",
            icon = "🍪",
            skillName = "Baking",
            craftCost = 2,
            sellPrice = 5,
            minAge = 6,
            maxAge = 12,
            category = "Food & Beverage",
            description = "Mix oats, cinnamon and honey into tasty baked cookies."
        ),
        SkillBusinessItem(
            id = "skill_origami",
            title = "Origami Paper Crane",
            icon = "🕊️",
            skillName = "Origami Folding",
            craftCost = 1,
            sellPrice = 4,
            minAge = 5,
            maxAge = 9,
            category = "Art & Crafts",
            description = "Carefully fold intricate wings that flutter with precision."
        ),
        SkillBusinessItem(
            id = "skill_smoothie",
            title = "Berry Village Smoothie",
            icon = "🥤",
            skillName = "Chef & Blending",
            craftCost = 3,
            sellPrice = 7,
            minAge = 8,
            maxAge = 14,
            category = "Food & Beverage",
            description = "Blend fresh strawberries, blueberries and chilled yogurt."
        )
    )

    val ageWiseSkillsCatalog = listOf(
        // ==========================================
        // AGES 4 - 6: Early Makers & Little Helpers
        // ==========================================
        SkillBusinessItem(
            id = "skill_greeting_card",
            title = "Handmade Greeting Cards",
            icon = "💌",
            skillName = "Art & Folding",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 6,
            category = "Art & Crafts",
            description = "Draw cute pictures and cheerful birthday messages for family and friends!"
        ),
        SkillBusinessItem(
            id = "skill_origami_birds",
            title = "Origami Zoo Animals",
            icon = "🕊️",
            skillName = "Paper Folding",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 6,
            category = "Art & Crafts",
            description = "Fold paper puppies, frogs, and swans to decorate family desks!"
        ),
        SkillBusinessItem(
            id = "skill_bookmark_art",
            title = "Monster & Rainbow Bookmarks",
            icon = "🔖",
            skillName = "Crafts & Design",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 6,
            category = "Art & Crafts",
            description = "Create colorful cardstock corner bookmarks with yarn tassels."
        ),
        SkillBusinessItem(
            id = "skill_herb_pots",
            title = "Painted Herb Mini-Pots",
            icon = "🪴",
            skillName = "Gardening & Painting",
            craftCost = 2,
            sellPrice = 5,
            minAge = 4,
            maxAge = 6,
            category = "Nature & Green",
            description = "Paint clay pots and plant basil or mint seeds for the kitchen windowsill."
        ),
        SkillBusinessItem(
            id = "skill_painted_rocks",
            title = "Kindness Story Rocks",
            icon = "🪨",
            skillName = "Creative Painting",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 6,
            category = "Art & Crafts",
            description = "Paint smooth garden stones with inspirational words and bright ladybugs."
        ),
        SkillBusinessItem(
            id = "skill_pasta_necklaces",
            title = "Dyed Pasta Bead Necklaces",
            icon = "📿",
            skillName = "Bead Craft",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 6,
            category = "Fashion & Jewelry",
            description = "Thread rainbow food-dyed macaroni onto yarn for playful wearable jewelry."
        ),
        SkillBusinessItem(
            id = "skill_leaf_prints",
            title = "Autumn Leaf Stamp Art",
            icon = "🍃",
            skillName = "Nature Art",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 6,
            category = "Art & Crafts",
            description = "Collect fallen leaves, press washable watercolor paint, and stamp onto cards."
        ),
        SkillBusinessItem(
            id = "skill_pinecone_feeders",
            title = "Pinecone Bird Feeders",
            icon = "🥜",
            skillName = "Wildlife Care",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 6,
            category = "Nature & Green",
            description = "Roll pinecones in sun-butter and birdseed to hang in neighborhood branches."
        ),
        SkillBusinessItem(
            id = "skill_sticker_packs",
            title = "Handmade Sticker Mini-Sets",
            icon = "⭐",
            skillName = "Sticker Art",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 6,
            category = "Art & Crafts",
            description = "Draw cute doodle faces on sticker paper and cut out fun collector sheets."
        ),
        SkillBusinessItem(
            id = "skill_gift_wrapping",
            title = "Hand-Stamped Gift Paper",
            icon = "🎁",
            skillName = "Printmaking",
            craftCost = 1,
            sellPrice = 4,
            minAge = 4,
            maxAge = 6,
            category = "Art & Crafts",
            description = "Stamp butcher paper with cookie cutter shapes to create unique wrapping paper."
        ),
        SkillBusinessItem(
            id = "skill_sprout_starter",
            title = "Sunflower Sprout Starter Cups",
            icon = "🌻",
            skillName = "Botanical Care",
            craftCost = 1,
            sellPrice = 4,
            minAge = 4,
            maxAge = 6,
            category = "Nature & Green",
            description = "Plant giant striped sunflower seeds in compostable paper cups."
        ),
        SkillBusinessItem(
            id = "skill_story_puppets",
            title = "Wooden Spoon Story Puppets",
            icon = "🎭",
            skillName = "Storytelling & Craft",
            craftCost = 1,
            sellPrice = 3,
            minAge = 4,
            maxAge = 6,
            category = "Art & Crafts",
            description = "Glue yarn hair and felt clothes onto wooden craft spoons for bedtime stories."
        ),

        // ==========================================
        // AGES 7 - 9: Elementary Crafters & Sales
        // ==========================================
        SkillBusinessItem(
            id = "skill_lemonade_stand",
            title = "Sunny Citrus Lemonade Stand",
            icon = "🍋",
            skillName = "Culinary & Sales",
            craftCost = 2,
            sellPrice = 6,
            minAge = 7,
            maxAge = 9,
            category = "Food & Beverage",
            description = "Squeeze fresh lemons, mix cane sugar, ice, and serve smiling neighbors!"
        ),
        SkillBusinessItem(
            id = "skill_friendship_bracelets",
            title = "Woven Friendship Bracelets",
            icon = "🧵",
            skillName = "Fiber Crafts",
            craftCost = 2,
            sellPrice = 5,
            minAge = 7,
            maxAge = 9,
            category = "Fashion & Jewelry",
            description = "Weave embroidery thread into Chevron and spiral patterned wristbands."
        ),
        SkillBusinessItem(
            id = "skill_cookie_bakery",
            title = "Choc-Chip & Honey Oat Cookies",
            icon = "🍪",
            skillName = "Baking",
            craftCost = 3,
            sellPrice = 8,
            minAge = 7,
            maxAge = 9,
            category = "Food & Beverage",
            description = "Bake golden crispy cookies and package them in decorated treat bags."
        ),
        SkillBusinessItem(
            id = "skill_diy_slime",
            title = "Sparkle Galaxy Fluffy Slime",
            icon = "🧪",
            skillName = "Chemistry Craft",
            craftCost = 2,
            sellPrice = 6,
            minAge = 7,
            maxAge = 9,
            category = "Sensory Toys",
            description = "Combine safe non-toxic ingredients, food color, and biodegradable glitter!"
        ),
        SkillBusinessItem(
            id = "skill_comic_strips",
            title = "Superhero Mini Comic Books",
            icon = "🦸",
            skillName = "Storytelling & Illustration",
            craftCost = 1,
            sellPrice = 4,
            minAge = 7,
            maxAge = 9,
            category = "Publishing & Media",
            description = "Write fun 4-page adventure comics starring village animal heroes!"
        ),
        SkillBusinessItem(
            id = "skill_seed_bombs",
            title = "Wildflower Seed Bomb Balls",
            icon = "🌸",
            skillName = "Eco Gardening",
            craftCost = 2,
            sellPrice = 5,
            minAge = 7,
            maxAge = 9,
            category = "Nature & Green",
            description = "Mix clay, compost, and bee-friendly wildflower seeds into planting balls."
        ),
        SkillBusinessItem(
            id = "skill_painted_totes",
            title = "Custom Painted Canvas Bags",
            icon = "🎨",
            skillName = "Textile Art",
            craftCost = 3,
            sellPrice = 8,
            minAge = 7,
            maxAge = 9,
            category = "Fashion & Jewelry",
            description = "Use fabric markers and stencils to design reusable grocery tote bags."
        ),
        SkillBusinessItem(
            id = "skill_bead_keychains",
            title = "Pixel Bead Animal Keychains",
            icon = "🔑",
            skillName = "Perler Bead Art",
            craftCost = 1,
            sellPrice = 4,
            minAge = 7,
            maxAge = 9,
            category = "Art & Crafts",
            description = "Arrange fuse beads into pixel hearts, foxes, and stars with split rings."
        ),
        SkillBusinessItem(
            id = "skill_fruit_popsicles",
            title = "Organic Fresh Berry Ice Pops",
            icon = "🍧",
            skillName = "Culinary Treats",
            craftCost = 2,
            sellPrice = 5,
            minAge = 7,
            maxAge = 9,
            category = "Food & Beverage",
            description = "Puree fresh watermelon, strawberries, and lemonade into frozen ice molds."
        ),
        SkillBusinessItem(
            id = "skill_clay_pots",
            title = "Air-Dry Clay Pinch Dish",
            icon = "🏺",
            skillName = "Sculpture & Pottery",
            craftCost = 2,
            sellPrice = 6,
            minAge = 7,
            maxAge = 9,
            category = "Art & Crafts",
            description = "Sculpt miniature ring dishes with scalloped edges and metallic acrylic glaze."
        ),
        SkillBusinessItem(
            id = "skill_magic_show",
            title = "Neighborhood Magic Trick Show",
            icon = "🪄",
            skillName = "Performance & Sleight of Hand",
            craftCost = 1,
            sellPrice = 6,
            minAge = 7,
            maxAge = 9,
            category = "Entertainment",
            description = "Learn coin vanish and card prediction tricks to perform for backyard guests!"
        ),
        SkillBusinessItem(
            id = "skill_popcorn_bags",
            title = "Gourmet Cinnamon & Cheese Popcorn",
            icon = "🍿",
            skillName = "Snack Making",
            craftCost = 2,
            sellPrice = 5,
            minAge = 7,
            maxAge = 9,
            category = "Food & Beverage",
            description = "Air-pop corn kernels, season with savory nutritional yeast or cinnamon sugar."
        ),
        SkillBusinessItem(
            id = "skill_dog_biscuits",
            title = "Peanut Butter Pet Biscuits",
            icon = "🐾",
            skillName = "Pet Baking",
            craftCost = 2,
            sellPrice = 6,
            minAge = 7,
            maxAge = 9,
            category = "Pet & Animal Care",
            description = "Bake healthy bone-shaped oat, pumpkin, and peanut butter treats for village dogs."
        ),
        SkillBusinessItem(
            id = "skill_pressed_coasters",
            title = "Pressed Flower Coaster Sets",
            icon = "🌼",
            skillName = "Botanical Design",
            craftCost = 2,
            sellPrice = 6,
            minAge = 7,
            maxAge = 9,
            category = "Home Decor",
            description = "Press blooming clover and marigolds between glass or cork coaster tiles."
        ),

        // ==========================================
        // AGES 10 - 12: Middle School Makers & Services
        // ==========================================
        SkillBusinessItem(
            id = "skill_pet_walking",
            title = "Neighborhood Pet Care & Walking",
            icon = "🐕",
            skillName = "Animal Care",
            craftCost = 1,
            sellPrice = 8,
            minAge = 10,
            maxAge = 12,
            category = "Pet & Animal Care",
            description = "Exercise friendly neighborhood dogs and replenish fresh water bowls."
        ),
        SkillBusinessItem(
            id = "skill_car_wash",
            title = "Eco Bubble Car Wash Squad",
            icon = "🚗",
            skillName = "Auto Care & Teamwork",
            craftCost = 3,
            sellPrice = 12,
            minAge = 10,
            maxAge = 12,
            category = "Services & Tasks",
            description = "Soap sponge wash, rinse, and dry family cars to a sparkling mirror shine!"
        ),
        SkillBusinessItem(
            id = "skill_digital_stickers",
            title = "Digital Planner & Emoji Stickers",
            icon = "✨",
            skillName = "Digital Design",
            craftCost = 2,
            sellPrice = 7,
            minAge = 10,
            maxAge = 12,
            category = "Digital & Tech",
            description = "Design cute transparent PNG stickers for tablets and digital journals."
        ),
        SkillBusinessItem(
            id = "skill_diy_candles",
            title = "Scented Soy Wax Mason Candles",
            icon = "🕯️",
            skillName = "Artisan Craft",
            craftCost = 4,
            sellPrice = 11,
            minAge = 10,
            maxAge = 12,
            category = "Home Decor",
            description = "Melt natural soy wax with lavender essential oils into reusable glass jars."
        ),
        SkillBusinessItem(
            id = "skill_lawn_mow",
            title = "Yard Raking & Flowerbed Care",
            icon = "🌱",
            skillName = "Landscaping",
            craftCost = 2,
            sellPrice = 10,
            minAge = 10,
            maxAge = 12,
            category = "Services & Tasks",
            description = "Rake autumn leaves, pull garden weeds, and sweep clean the walkways."
        ),
        SkillBusinessItem(
            id = "skill_cupcake_box",
            title = "Artisan Buttercream Cupcake Box",
            icon = "🧁",
            skillName = "Pastry & Piping",
            craftCost = 4,
            sellPrice = 12,
            minAge = 10,
            maxAge = 12,
            category = "Food & Beverage",
            description = "Bake vanilla cupcakes and pipe two-tone swirls with rainbow sprinkles."
        ),
        SkillBusinessItem(
            id = "skill_bottle_decals",
            title = "Water Bottle Vinyl Name Decals",
            icon = "🏷️",
            skillName = "Vinyl Lettering",
            craftCost = 2,
            sellPrice = 7,
            minAge = 10,
            maxAge = 12,
            category = "Art & Crafts",
            description = "Cut waterproof vinyl name tags for school sports bottles and lunchboxes."
        ),
        SkillBusinessItem(
            id = "skill_handcrafted_soaps",
            title = "Honey Oatmeal Melt-and-Pour Soap",
            icon = "🧼",
            skillName = "Cosmetic Craft",
            craftCost = 3,
            sellPrice = 9,
            minAge = 10,
            maxAge = 12,
            category = "Home Decor",
            description = "Melt glycerin soap base with ground oats, pure honey, and sweet almond oil."
        ),
        SkillBusinessItem(
            id = "skill_tiedye_shirts",
            title = "Spiral Pastel Tie-Dye T-Shirts",
            icon = "👕",
            skillName = "Textile Dyeing",
            craftCost = 4,
            sellPrice = 12,
            minAge = 10,
            maxAge = 12,
            category = "Fashion & Jewelry",
            description = "Rubber-band cotton tees in spiral shapes and apply vibrant colorfast dyes."
        ),
        SkillBusinessItem(
            id = "skill_bike_tuneup",
            title = "Bicycle Clean & Chain Lube Service",
            icon = "🚲",
            skillName = "Bicycle Mechanics",
            craftCost = 2,
            sellPrice = 9,
            minAge = 10,
            maxAge = 12,
            category = "Services & Tasks",
            description = "Degrease bicycle chains, pump tires to optimal PSI, and polish frames."
        ),
        SkillBusinessItem(
            id = "skill_grandparent_tech",
            title = "Elder Tech Helper (Phone & Tablet)",
            icon = "📱",
            skillName = "Technology Support",
            craftCost = 1,
            sellPrice = 10,
            minAge = 10,
            maxAge = 12,
            category = "Digital & Tech",
            description = "Help grandparents organize photo albums, zoom family calls, and clear storage."
        ),
        SkillBusinessItem(
            id = "skill_resin_bookmarks",
            title = "Gold Foil & Botanical Resin Bookmarks",
            icon = "✨",
            skillName = "Resin Art",
            craftCost = 3,
            sellPrice = 8,
            minAge = 10,
            maxAge = 12,
            category = "Art & Crafts",
            description = "Cast clear resin with embedded dried flowers and shimmering gold leaf."
        ),
        SkillBusinessItem(
            id = "skill_herb_bundles",
            title = "Fresh Rosemary & Herb Cooking Bundles",
            icon = "🌿",
            skillName = "Organic Gardening",
            craftCost = 1,
            sellPrice = 6,
            minAge = 10,
            maxAge = 12,
            category = "Nature & Green",
            description = "Harvest garden rosemary, thyme, and sage tied with rustic jute twine."
        ),
        SkillBusinessItem(
            id = "skill_balloon_animals",
            title = "Balloon Animal Party Sculptor",
            icon = "🎈",
            skillName = "Balloon Art",
            craftCost = 2,
            sellPrice = 10,
            minAge = 10,
            maxAge = 12,
            category = "Entertainment",
            description = "Twist colorful 260 balloons into dachshunds, swords, flowers, and crowns!"
        ),

        // ==========================================
        // AGES 13 - 15: Junior Entrepreneurs & STEM
        // ==========================================
        SkillBusinessItem(
            id = "skill_coding_web",
            title = "Custom Mini Website Portfolio",
            icon = "💻",
            skillName = "Web Development",
            craftCost = 2,
            sellPrice = 16,
            minAge = 13,
            maxAge = 15,
            category = "Digital & Tech",
            description = "Code HTML & CSS responsive landing pages for local clubs and artists."
        ),
        SkillBusinessItem(
            id = "skill_tutor_math",
            title = "Junior Math & Logic Puzzle Tutor",
            icon = "📐",
            skillName = "Teaching & Academics",
            craftCost = 1,
            sellPrice = 14,
            minAge = 13,
            maxAge = 15,
            category = "Tutoring & STEM",
            description = "Help younger students understand fractions, algebra, and logic puzzles."
        ),
        SkillBusinessItem(
            id = "skill_photo_portraits",
            title = "Community Portrait Photography",
            icon = "📸",
            skillName = "Photography & Lighting",
            craftCost = 3,
            sellPrice = 18,
            minAge = 13,
            maxAge = 15,
            category = "Media & Creative",
            description = "Capture golden-hour family and pet portraits with high-contrast framing."
        ),
        SkillBusinessItem(
            id = "skill_upcycled_clothes",
            title = "Upcycled Denim & Tote Bags",
            icon = "👖",
            skillName = "Sustainable Fashion",
            craftCost = 4,
            sellPrice = 15,
            minAge = 13,
            maxAge = 15,
            category = "Fashion & Jewelry",
            description = "Sew stylish custom tote bags and patch pockets from recycled denim jeans."
        ),
        SkillBusinessItem(
            id = "skill_podcast_editing",
            title = "Podcast Audio Cleanup & Trimming",
            icon = "🎙️",
            skillName = "Audio Engineering",
            craftCost = 2,
            sellPrice = 16,
            minAge = 13,
            maxAge = 15,
            category = "Digital & Tech",
            description = "Remove background hums, cut pauses, and balance voice volume for podcasts."
        ),
        SkillBusinessItem(
            id = "skill_logo_design",
            title = "Vector Logo & Brand Icon Design",
            icon = "🖌️",
            skillName = "Graphic Design",
            craftCost = 2,
            sellPrice = 17,
            minAge = 13,
            maxAge = 15,
            category = "Media & Creative",
            description = "Design modern minimalist logos for school clubs, creators, and micro-brands."
        ),
        SkillBusinessItem(
            id = "skill_lawn_mowing_pro",
            title = "Neighborhood Lawn Mowing Service",
            icon = "🏡",
            skillName = "Property Maintenance",
            craftCost = 3,
            sellPrice = 18,
            minAge = 13,
            maxAge = 15,
            category = "Services & Tasks",
            description = "Mow front and back lawns with clean edge trimming and blow away clippings."
        ),
        SkillBusinessItem(
            id = "skill_3d_keychains",
            title = "3D-Printed Custom Name Keychains",
            icon = "🖨️",
            skillName = "3D CAD & Printing",
            craftCost = 3,
            sellPrice = 14,
            minAge = 13,
            maxAge = 15,
            category = "Digital & Tech",
            description = "Model 3D text in Tinkercad and slice with PLA filament on a 3D printer."
        ),
        SkillBusinessItem(
            id = "skill_guitar_lessons",
            title = "Beginner Guitar & Ukulele Chords",
            icon = "🎸",
            skillName = "Music Instruction",
            craftCost = 1,
            sellPrice = 15,
            minAge = 13,
            maxAge = 15,
            category = "Tutoring & STEM",
            description = "Teach basic strumming patterns, tuning, and C-G-Am-F chords for beginners."
        ),
        SkillBusinessItem(
            id = "skill_sourdough_bakery",
            title = "Artisan Sourdough Boule & Focaccia",
            icon = "🥖",
            skillName = "Artisan Baking",
            craftCost = 4,
            sellPrice = 16,
            minAge = 13,
            maxAge = 15,
            category = "Food & Beverage",
            description = "Feed wild yeast starter, fold dough, score leaf patterns, and bake in Dutch oven."
        ),
        SkillBusinessItem(
            id = "skill_garage_organizer",
            title = "Garage & Storage Shelving Squad",
            icon = "📦",
            skillName = "Space Organization",
            craftCost = 2,
            sellPrice = 16,
            minAge = 13,
            maxAge = 15,
            category = "Services & Tasks",
            description = "Categorize sports gear, label clear storage bins, and sweep garage floors."
        ),
        SkillBusinessItem(
            id = "skill_drone_photography",
            title = "Aerial Roof & Garden Drone Photos",
            icon = "🚁",
            skillName = "Drone Piloting",
            craftCost = 3,
            sellPrice = 19,
            minAge = 13,
            maxAge = 15,
            category = "Media & Creative",
            description = "Fly certified quadcopters to snap high-res 4K bird's-eye views of properties."
        ),
        SkillBusinessItem(
            id = "skill_coding_coach",
            title = "Junior Scratch & Python Game Coach",
            icon = "👾",
            skillName = "Software Mentoring",
            craftCost = 1,
            sellPrice = 16,
            minAge = 13,
            maxAge = 15,
            category = "Tutoring & STEM",
            description = "Guide young kids in building maze games and interactive animations."
        ),
        SkillBusinessItem(
            id = "skill_cold_brew",
            title = "Craft Bottled Vanilla Cold Brew",
            icon = "🧋",
            skillName = "Beverage Brewing",
            craftCost = 3,
            sellPrice = 12,
            minAge = 13,
            maxAge = 15,
            category = "Food & Beverage",
            description = "Steep coarse roasted beans 18 hours in cold water with vanilla bean infusion."
        ),

        // ==========================================
        // AGES 16 - 18+: Advanced Micro-Enterprises
        // ==========================================
        SkillBusinessItem(
            id = "skill_fullstack_app",
            title = "Mobile App UI Prototype & Code",
            icon = "🚀",
            skillName = "Software Engineering",
            craftCost = 3,
            sellPrice = 25,
            minAge = 16,
            maxAge = 18,
            category = "Digital & Tech",
            description = "Build full interactive Kotlin Jetpack Compose or React Native app mockups."
        ),
        SkillBusinessItem(
            id = "skill_sat_tutor",
            title = "SAT / ACT & High School Math Prep",
            icon = "📚",
            skillName = "Advanced Tutoring",
            craftCost = 1,
            sellPrice = 24,
            minAge = 16,
            maxAge = 18,
            category = "Tutoring & STEM",
            description = "Coach test-taking strategies, algebra 2, calculus, and practice exam drills."
        ),
        SkillBusinessItem(
            id = "skill_video_editing_reels",
            title = "Short-Form Video & Reel Production",
            icon = "🎬",
            skillName = "Video Post-Production",
            craftCost = 3,
            sellPrice = 22,
            minAge = 16,
            maxAge = 18,
            category = "Media & Creative",
            description = "Edit dynamic captions, smooth b-roll, and sound effects for creators."
        ),
        SkillBusinessItem(
            id = "skill_event_dj",
            title = "Party DJ & Sound System Setup",
            icon = "🎧",
            skillName = "Music & Live Audio",
            craftCost = 4,
            sellPrice = 25,
            minAge = 16,
            maxAge = 18,
            category = "Entertainment",
            description = "Set up PA speakers, curate beat-matched playlists, and MC festive events."
        ),
        SkillBusinessItem(
            id = "skill_pc_hardware_repair",
            title = "PC Dusting, Thermal Paste & SSD Upgrade",
            icon = "🖥️",
            skillName = "Computer Hardware",
            craftCost = 4,
            sellPrice = 22,
            minAge = 16,
            maxAge = 18,
            category = "Digital & Tech",
            description = "Disassemble desktop towers, clean fan bearings, and clone OS onto fast NVMe SSDs."
        ),
        SkillBusinessItem(
            id = "skill_fitness_coach",
            title = "Youth Sports Agility & Conditioning",
            icon = "⚽",
            skillName = "Athletic Coaching",
            craftCost = 2,
            sellPrice = 20,
            minAge = 16,
            maxAge = 18,
            category = "Services & Tasks",
            description = "Run ladder drills, speed cones, and stretching routines for junior soccer players."
        ),
        SkillBusinessItem(
            id = "skill_spreadsheet_dashboard",
            title = "Automated Budget & Excel Dashboards",
            icon = "📊",
            skillName = "Financial Modeling",
            craftCost = 2,
            sellPrice = 21,
            minAge = 16,
            maxAge = 18,
            category = "Digital & Tech",
            description = "Create automated Google Sheets with pivot tables and monthly expense charts."
        ),
        SkillBusinessItem(
            id = "skill_watercolor_portraits",
            title = "Custom Watercolor Pet Commission",
            icon = "🎨",
            skillName = "Fine Art & Painting",
            craftCost = 4,
            sellPrice = 23,
            minAge = 16,
            maxAge = 18,
            category = "Media & Creative",
            description = "Paint detailed cold-press 300gsm watercolor pet portraits framed in wood."
        ),
        SkillBusinessItem(
            id = "skill_hardwood_boards",
            title = "End-Grain Walnut & Maple Cutting Boards",
            icon = "🪵",
            skillName = "Fine Woodworking",
            craftCost = 6,
            sellPrice = 26,
            minAge = 16,
            maxAge = 18,
            category = "Home Decor",
            description = "Plane, glue, sand, and season hardwood boards with food-safe mineral oil."
        ),
        SkillBusinessItem(
            id = "skill_social_media_mgmt",
            title = "Local Business Social Content Creator",
            icon = "📱",
            skillName = "Digital Marketing",
            craftCost = 2,
            sellPrice = 22,
            minAge = 16,
            maxAge = 18,
            category = "Media & Creative",
            description = "Photograph weekly specials, write engaging captions, and schedule posts."
        ),
        SkillBusinessItem(
            id = "skill_car_detailing_pro",
            title = "Interior Deep Clean & Leather Conditioning",
            icon = "✨",
            skillName = "Auto Detailing",
            craftCost = 5,
            sellPrice = 25,
            minAge = 16,
            maxAge = 18,
            category = "Services & Tasks",
            description = "Steam clean upholstery, vacuum tight crevices, and polish dashboard trim."
        ),
        SkillBusinessItem(
            id = "skill_window_cleaning",
            title = "Streak-Free Residential Window Washing",
            icon = "🪟",
            skillName = "Exterior Cleaning",
            craftCost = 3,
            sellPrice = 20,
            minAge = 16,
            maxAge = 18,
            category = "Services & Tasks",
            description = "Squeegee exterior ground windows and wash fly screens with microfiber cloth."
        ),
        SkillBusinessItem(
            id = "skill_leather_craft",
            title = "Hand-Stitched Leather Card Wallets",
            icon = "👛",
            skillName = "Leatherworking",
            craftCost = 5,
            sellPrice = 24,
            minAge = 16,
            maxAge = 18,
            category = "Fashion & Jewelry",
            description = "Saddle-stitch veg-tan leather with waxed thread and burnish edges smooth."
        ),
        SkillBusinessItem(
            id = "skill_cad_prototyping",
            title = "Custom 3D CAD Replacement Parts",
            icon = "⚙️",
            skillName = "Parametric CAD",
            craftCost = 4,
            sellPrice = 24,
            minAge = 16,
            maxAge = 18,
            category = "Digital & Tech",
            description = "Measure broken plastic parts with calipers, model in Fusion 360, and 3D print."
        )
    )

    val skillBusinesses = ageWiseSkillsCatalog

    val givingCauses = listOf(
        GivingCause(
            name = "Animal Shelter Pets",
            icon = "🐶",
            description = "Buy soft blankets and yummy food bowls for rescue puppies and kittens."
        ),
        GivingCause(
            name = "Village Tree Planting",
            icon = "🌳",
            description = "Plant new shaded fruit trees in the neighborhood community park."
        ),
        GivingCause(
            name = "School Library Books",
            icon = "📚",
            description = "Donate colorful picture books so all children can discover stories."
        ),
        GivingCause(
            name = "Elderly Neighbors Care",
            icon = "👵",
            description = "Deliver fresh fruits and handwritten cheerful cards."
        )
    )

    val initialParentToys = listOf(
        CustomToyItem(
            id = 1,
            title = "Galactic Lego Spaceship",
            icon = "🚀",
            price = 28,
            category = "Building & Toys",
            description = "Build a fast cosmic cruiser with glow-in-the-dark engines!",
            smartAlternativeTitle = "DIY Cardboard Rocket Kit",
            smartAlternativePrice = 8,
            smartXpReward = 20
        ),
        CustomToyItem(
            id = 2,
            title = "Turbo Remote Control Car",
            icon = "🏎️",
            price = 24,
            category = "Electronics & Fun",
            description = "High-speed racer that drifts and spins with flashing headlights!",
            smartAlternativeTitle = "Hot Wheels Matchbox 3-Pack",
            smartAlternativePrice = 6,
            smartXpReward = 15
        ),
        CustomToyItem(
            id = 3,
            title = "Pro Art Easel & Canvas Set",
            icon = "🎨",
            price = 20,
            category = "Creative Art",
            description = "Wood easel with acrylic paint tubes and artist brushes.",
            smartAlternativeTitle = "Sketchpad & 24 Colored Pencils",
            smartAlternativePrice = 5,
            smartXpReward = 15
        ),
        CustomToyItem(
            id = 4,
            title = "Family Adventure Board Game",
            icon = "🎲",
            price = 18,
            category = "Family Fun",
            description = "Fun strategy game for game night with parents and siblings!",
            smartAlternativeTitle = "Deck of Mystery Playing Cards",
            smartAlternativePrice = 4,
            smartXpReward = 10
        ),
        CustomToyItem(
            id = 5,
            title = "Speed Roller Skates",
            icon = "🛼",
            price = 35,
            category = "Sports & Active",
            description = "Outdoor quad skates with light-up polyurethane wheels!",
            smartAlternativeTitle = "Jump Rope & Agility Cone Set",
            smartAlternativePrice = 7,
            smartXpReward = 25
        )
    )

    val sampleDemeritHabits = listOf(
        HabitItem(
            title = "Eating Junk Food Instead of Meals",
            icon = "🍔",
            rewardCoins = 0,
            category = "Health Demerit",
            frequency = "DAILY",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            isNegative = true,
            deductionCoins = 1,
            note = "Nutritious foods build strong bones and superhero brain power!"
        ),
        HabitItem(
            title = "Too Much Screen Time / Refusing to Stop",
            icon = "📱",
            rewardCoins = 0,
            category = "Discipline Demerit",
            frequency = "DAILY",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            isNegative = true,
            deductionCoins = 1,
            note = "Balancing screen time with real-world adventures keeps our eyes and minds sharp."
        ),
        HabitItem(
            title = "Left Room in Big Mess / Refused to Tidy",
            icon = "🧸",
            rewardCoins = 0,
            category = "Responsibility Demerit",
            frequency = "DAILY",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            isNegative = true,
            deductionCoins = 1,
            note = "Taking care of our own space creates calm and safe living."
        ),
        HabitItem(
            title = "Arguing / Fighting with Sibling",
            icon = "😤",
            rewardCoins = 0,
            category = "Kindness Demerit",
            frequency = "DAILY",
            confirmationMethod = "PARENT_CONFIRMS",
            isFamilyTeamwork = false,
            isNegative = true,
            deductionCoins = 2,
            note = "Practice calm communication and kindness with family teammates."
        )
    )

    val quizQuestionBank = listOf(
        // --- Ages 4-6 (Foundation Math, Money, Choices) ---
        QuizQuestion(
            id = "q_4_6_01",
            question = "If you have 3 gold coins and you earn 2 more, how many coins do you have in total?",
            options = listOf("4 coins", "5 coins", "6 coins", "3 coins"),
            correctAnswerIndex = 1,
            category = "Math & Counting",
            minAge = 4,
            maxAge = 6,
            explanation = "3 + 2 = 5 coins! Counting up makes your treasure grow.",
            funFact = "Ancient coins were made of pure electrum, a mix of gold and silver!"
        ),
        QuizQuestion(
            id = "q_4_6_02",
            question = "Which of these is a NEED for our body to stay healthy?",
            options = listOf("Candy lollipop", "Clean fresh water", "Video game console", "Toy robot"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 4,
            maxAge = 6,
            explanation = "Clean water is a Need because our bodies need it to live and stay strong!",
            funFact = "Drinking enough water gives your brain super thinking power!"
        ),
        QuizQuestion(
            id = "q_4_6_03",
            question = "What should you do before spending your coins on a new toy?",
            options = listOf("Spend everything immediately", "Check if you have enough in your Spend Jar", "Borrow without asking", "Hide coins under the bed"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 4,
            maxAge = 6,
            explanation = "Smart savers always check their Spend Jar first!",
            funFact = "Piggy banks were originally made from pygg clay over 600 years ago."
        ),
        QuizQuestion(
            id = "q_4_6_04",
            question = "Count the shapes: 🟦 + 🟦 + 🟦 + 🟦 = ?",
            options = listOf("3 squares", "4 squares", "5 squares", "2 squares"),
            correctAnswerIndex = 1,
            category = "Math & Counting",
            minAge = 4,
            maxAge = 6,
            explanation = "There are 4 bright blue squares!",
            funFact = "A square has 4 equal sides and 4 square corners."
        ),
        QuizQuestion(
            id = "q_4_6_05",
            question = "Which animal puts acorns in a safe place for winter (like saving in a bank)?",
            options = listOf("Squirrel 🐿️", "Fish 🐟", "Butterfly 🦋", "Giraffe 🦒"),
            correctAnswerIndex = 0,
            category = "Brain Riddles",
            minAge = 4,
            maxAge = 6,
            explanation = "Squirrels store nuts in advance so they have food in winter — just like saving!",
            funFact = "A single squirrel can hide up to 10,000 nuts in a single season!"
        ),
        QuizQuestion(
            id = "q_4_6_06",
            question = "If you have 5 coins and buy an apple for 2 coins, how many coins are left?",
            options = listOf("2 coins", "3 coins", "4 coins", "1 coin"),
            correctAnswerIndex = 1,
            category = "Math & Counting",
            minAge = 4,
            maxAge = 6,
            explanation = "5 - 2 = 3 coins remaining in your pocket!",
            funFact = "Apples float in water because they are 25% air!"
        ),
        QuizQuestion(
            id = "q_4_6_07",
            question = "What is the Magic Give Jar used for?",
            options = listOf("Buying sweets for yourself", "Helping pets, friends, and village causes", "Keeping hidden forever", "Losing coins"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 4,
            maxAge = 6,
            explanation = "The Give Jar helps us share kindness with our community and animals!",
            funFact = "Sharing with others releases happy chemicals in our brains."
        ),
        QuizQuestion(
            id = "q_4_6_08",
            question = "Which is bigger: 10 coins or 4 coins?",
            options = listOf("4 coins", "10 coins", "They are equal", "0 coins"),
            correctAnswerIndex = 1,
            category = "Logic & Puzzles",
            minAge = 4,
            maxAge = 6,
            explanation = "10 is greater than 4!",
            funFact = "The number zero was invented by mathematicians in ancient India."
        ),
        QuizQuestion(
            id = "q_4_6_09",
            question = "If you put 1 coin in your piggy bank every day for 5 days, how many coins will you have?",
            options = listOf("3 coins", "5 coins", "10 coins", "1 coin"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 4,
            maxAge = 6,
            explanation = "1 + 1 + 1 + 1 + 1 = 5 coins! Daily habits build big treasures.",
            funFact = "Consistency is the number one secret of world-class achievers."
        ),
        QuizQuestion(
            id = "q_4_6_10",
            question = "What comes next in the pattern: 🔴 🔵 🔴 🔵 🔴 ?",
            options = listOf("🔴 Red", "🔵 Blue", "🟡 Yellow", "🟢 Green"),
            correctAnswerIndex = 1,
            category = "Logic & Puzzles",
            minAge = 4,
            maxAge = 6,
            explanation = "The pattern alternates Red then Blue, so Blue comes next!",
            funFact = "Patterns are how mathematicians decode the secrets of nature."
        ),

        // --- Ages 7-9 (Multiplication, Compounding, Financial Logic, Puzzles) ---
        QuizQuestion(
            id = "q_7_9_01",
            question = "What is 12% annual interest on 100 coins over one whole year?",
            options = listOf("6 coins", "12 coins", "24 coins", "10 coins"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 7,
            maxAge = 9,
            explanation = "12% of 100 = 12 coins! So 100 coins becomes 112 coins at the end of the year.",
            funFact = "Compound interest was called the 8th wonder of the world by Albert Einstein!"
        ),
        QuizQuestion(
            id = "q_7_9_02",
            question = "What does SIP stand for in smart money planning?",
            options = listOf("Super Ice Pop", "Systematic Investment Plan", "Savings In Pocket", "Speedy Internet Protocol"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 7,
            maxAge = 9,
            explanation = "SIP means Systematic Investment Plan — investing a small fixed amount regularly!",
            funFact = "SIP helps you grow wealth without stressing about market ups and downs."
        ),
        QuizQuestion(
            id = "q_7_9_03",
            question = "If you have 4 boxes with 6 shiny coins in each box, how many total coins do you have?",
            options = listOf("20 coins", "24 coins", "26 coins", "18 coins"),
            correctAnswerIndex = 1,
            category = "Math & Counting",
            minAge = 7,
            maxAge = 9,
            explanation = "4 x 6 = 24 coins in total!",
            funFact = "Multiplication is simply super-fast repeated addition."
        ),
        QuizQuestion(
            id = "q_7_9_04",
            question = "A toy costs 20 coins. If you choose the DIY Smart Alternative for 5 coins, how many coins do you SAVE?",
            options = listOf("10 coins", "15 coins", "25 coins", "5 coins"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 7,
            maxAge = 9,
            explanation = "20 - 5 = 15 coins saved for your Dream Goals!",
            funFact = "Smart spenders ask 'Do I really need the expensive brand or can I craft it?'"
        ),
        QuizQuestion(
            id = "q_7_9_05",
            question = "What happens when you water a money tree in the Magic Garden once a day with patience?",
            options = listOf("It dies instantly", "It grows stage by stage into a profitable harvest", "Nothing ever happens", "It turns into candy"),
            correctAnswerIndex = 1,
            category = "Logic & Puzzles",
            minAge = 7,
            maxAge = 9,
            explanation = "Daily patience and steady watering yield compound returns and more seeds!",
            funFact = "Real apple trees take 4 to 8 years to produce their sweetest harvest."
        ),
        QuizQuestion(
            id = "q_7_9_06",
            question = "Solve this puzzle: I am an odd number between 20 and 30. My digits add up to 9. What number am I?",
            options = listOf("25", "27", "29", "23"),
            correctAnswerIndex = 1,
            category = "Logic & Puzzles",
            minAge = 7,
            maxAge = 9,
            explanation = "2 + 7 = 9, and 27 is an odd number between 20 and 30!",
            funFact = "Any number whose digits sum to a multiple of 9 is divisible by 9!"
        ),
        QuizQuestion(
            id = "q_7_9_07",
            question = "If you earn 5 coins on Monday, 5 on Tuesday, and 5 on Wednesday, and spend 4 coins on Thursday, how much is left?",
            options = listOf("15 coins", "11 coins", "10 coins", "19 coins"),
            correctAnswerIndex = 1,
            category = "Math & Counting",
            minAge = 7,
            maxAge = 9,
            explanation = "(5 + 5 + 5) - 4 = 15 - 4 = 11 coins!",
            funFact = "Tracking your income and expenses is called keeping a ledger."
        ),
        QuizQuestion(
            id = "q_7_9_08",
            question = "Why do we keep a Safety Jar with emergency coins?",
            options = listOf("To buy video games on sale", "To be protected when unexpected surprises or accidents happen", "To hide from friends", "To spend all at once"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 7,
            maxAge = 9,
            explanation = "The Safety Jar protects us when unforeseen expenses or broken supplies occur!",
            funFact = "Financial experts recommend adults keep 3 to 6 months of emergency reserves."
        ),
        QuizQuestion(
            id = "q_7_9_09",
            question = "What is 8 x 7?",
            options = listOf("54", "56", "58", "62"),
            correctAnswerIndex = 1,
            category = "Math & Counting",
            minAge = 7,
            maxAge = 9,
            explanation = "8 x 7 = 56! Great mental arithmetic.",
            funFact = "The number 56 is the atomic number of Barium on the periodic table."
        ),
        QuizQuestion(
            id = "q_7_9_10",
            question = "A bat and ball together cost 11 coins. The bat costs 10 coins more than the ball. How much does the ball cost?",
            options = listOf("1 coin", "0.5 coins (half coin)", "2 coins", "10 coins"),
            correctAnswerIndex = 1,
            category = "Brain Riddles",
            minAge = 7,
            maxAge = 9,
            explanation = "If the ball is 0.5 coins, the bat is 10.5 coins (10 more). 10.5 + 0.5 = 11 coins!",
            funFact = "This classic cognitive riddle tricks more than 50% of university students!"
        ),

        // --- Ages 10-12 (Percentages, Business Profit, Budgeting, Logic) ---
        QuizQuestion(
            id = "q_10_12_01",
            question = "If you craft handmade bookmarks for 2 coins of paper and sell them for 6 coins, what is your net profit per bookmark?",
            options = listOf("2 coins", "4 coins", "6 coins", "8 coins"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 10,
            maxAge = 12,
            explanation = "Profit = Revenue (6) - Cost (2) = 4 coins earned!",
            funFact = "Profit is the reward an entrepreneur gets for creating value for customers."
        ),
        QuizQuestion(
            id = "q_10_12_02",
            question = "If you start a monthly SIP of 5 coins into Benny's 12% Bank for 12 months, how many coins did you invest in total (before interest)?",
            options = listOf("50 coins", "60 coins", "72 coins", "120 coins"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 10,
            maxAge = 12,
            explanation = "5 coins/month x 12 months = 60 principal coins invested!",
            funFact = "With interest added, your final balance will be even higher than 60 coins!"
        ),
        QuizQuestion(
            id = "q_10_12_03",
            question = "Solve: (15 + 25) ÷ 5 = ?",
            options = listOf("6", "8", "10", "12"),
            correctAnswerIndex = 1,
            category = "Math & Counting",
            minAge = 10,
            maxAge = 12,
            explanation = "(15 + 25) = 40. 40 ÷ 5 = 8!",
            funFact = "PEMDAS / BODMAS tells us to solve parentheses first."
        ),
        QuizQuestion(
            id = "q_10_12_04",
            question = "What is the recommended 4-Jar money allocation ratio in Coin Quest?",
            options = listOf("100% Spend", "Spend (10%), Save (50%), Give (20%), Safety (20%)", "Spend only on games", "Save 0%"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 10,
            maxAge = 12,
            explanation = "Balancing your coins across Spend, Save, Give, and Safety ensures true wealth and resilience!",
            funFact = "The 50/30/20 budget rule is used by leading financial advisors worldwide."
        ),
        QuizQuestion(
            id = "q_10_12_05",
            question = "If 3 painters can paint 3 houses in 3 days, how many days will it take 1 painter to paint 1 house?",
            options = listOf("1 day", "3 days", "9 days", "6 days"),
            correctAnswerIndex = 1,
            category = "Logic & Puzzles",
            minAge = 10,
            maxAge = 12,
            explanation = "Each painter takes 3 days to paint 1 house, so 1 painter will still take 3 days!",
            funFact = "Rate problems require calculating the work rate per individual worker."
        ),
        QuizQuestion(
            id = "q_10_12_06",
            question = "What is 25% of 80 coins?",
            options = listOf("15 coins", "20 coins", "25 coins", "30 coins"),
            correctAnswerIndex = 1,
            category = "Math & Counting",
            minAge = 10,
            maxAge = 12,
            explanation = "25% is one-quarter (1/4). 80 ÷ 4 = 20 coins!",
            funFact = "Percent means 'per one hundred' in Latin (per centum)."
        ),
        QuizQuestion(
            id = "q_10_12_07",
            question = "Why is it important to complete Recovery Quests when you miss a daily routine?",
            options = listOf("It does nothing", "It restores your confidence, builds resilience, and recovers lost coins", "It takes away points", "It ends the game"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 10,
            maxAge = 12,
            explanation = "Recovery Quests teach us that mistakes are stepping stones — bounce back with positive action!",
            funFact = "Resilience is the strongest predictor of long-term success."
        ),
        QuizQuestion(
            id = "q_10_12_08",
            question = "What is the next prime number after 13?",
            options = listOf("14", "15", "17", "19"),
            correctAnswerIndex = 2,
            category = "Math & Counting",
            minAge = 10,
            maxAge = 12,
            explanation = "17 has only two factors (1 and 17), making it the next prime number!",
            funFact = "Prime numbers form the foundation of all modern internet encryption."
        ),
        QuizQuestion(
            id = "q_10_12_09",
            question = "If inflation causes a 10-coin item to cost 11 coins next year, what is the best defense?",
            options = listOf("Keep coins under a mattress", "Invest coins in compounding assets and high-yield growth", "Spend everything in one hour", "Stop saving"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 10,
            maxAge = 12,
            explanation = "Investing and compounding at 12% beats inflation and grows purchasing power!",
            funFact = "Real assets like businesses, stocks, and productive land grow with the economy."
        ),
        QuizQuestion(
            id = "q_10_12_10",
            question = "A clock shows 3:15. What is the angle between the hour and minute hands?",
            options = listOf("0 degrees", "7.5 degrees", "15 degrees", "30 degrees"),
            correctAnswerIndex = 1,
            category = "Logic & Puzzles",
            minAge = 10,
            maxAge = 12,
            explanation = "At 3:15, the minute hand is at 90°, and the hour hand has moved 1/4 of an hour (7.5°) past 3. Difference = 7.5°!",
            funFact = "Each hour mark on a clock represents exactly 30 degrees of a full circle."
        ),

        // --- Ages 13+ (Advanced Economics, Compounding, Algebra, Logic) ---
        QuizQuestion(
            id = "q_13_plus_01",
            question = "Using the Rule of 72, approximately how many years will it take money to double at a 12% annual return?",
            options = listOf("12 years", "6 years", "8 years", "10 years"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 13,
            maxAge = 18,
            explanation = "Rule of 72: 72 ÷ 12% = approximately 6 years to double your investment!",
            funFact = "The Rule of 72 is the most famous mental math shortcut in modern finance."
        ),
        QuizQuestion(
            id = "q_13_plus_02",
            question = "What is the key advantage of Dollar-Cost Averaging via SIP?",
            options = listOf("You buy more units when prices are lower and fewer when prices are higher", "You guarantee 100% wins every day", "You never pay any taxes", "It requires predicting the exact market peak"),
            correctAnswerIndex = 0,
            category = "Financial Smarts",
            minAge = 13,
            maxAge = 18,
            explanation = "SIP removes emotion and lowers your average purchase price over market cycles!",
            funFact = "Automating investments removes emotional bias and panic selling."
        ),
        QuizQuestion(
            id = "q_13_plus_03",
            question = "Solve for x: 3x - 7 = 14",
            options = listOf("5", "7", "8", "6"),
            correctAnswerIndex = 1,
            category = "Math & Counting",
            minAge = 13,
            maxAge = 18,
            explanation = "3x = 14 + 7 = 21. x = 21 ÷ 3 = 7!",
            funFact = "The word 'Algebra' comes from the Arabic 'al-jabr' meaning restoration."
        ),
        QuizQuestion(
            id = "q_13_plus_04",
            question = "What is the difference between an Asset and a Liability according to Robert Kiyosaki?",
            options = listOf("Assets put money into your pocket; Liabilities take money out of your pocket", "Assets are always cars", "Liabilities are gold coins", "They mean the exact same thing"),
            correctAnswerIndex = 0,
            category = "Financial Smarts",
            minAge = 13,
            maxAge = 18,
            explanation = "Assets generate cash flow and capital appreciation; liabilities create ongoing expenses!",
            funFact = "Rich Dad Poor Dad is one of the top-selling personal finance books of all time."
        ),
        QuizQuestion(
            id = "q_13_plus_05",
            question = "If you roll two standard 6-sided dice, what is the most probable sum?",
            options = listOf("6", "7", "8", "12"),
            correctAnswerIndex = 1,
            category = "Logic & Puzzles",
            minAge = 13,
            maxAge = 18,
            explanation = "There are 6 distinct ways to roll a sum of 7 (1+6, 2+5, 3+4, 4+3, 5+2, 6+1), probability = 6/36 = 1/6!",
            funFact = "Casinos design games around the high probability of rolling a 7."
        ),
        QuizQuestion(
            id = "q_13_plus_06",
            question = "What is the definition of Opportunity Cost?",
            options = listOf("The price tag on a store shelf", "The value of the next best alternative given up when making a choice", "The sales tax paid to the government", "The shipping fee"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 13,
            maxAge = 18,
            explanation = "When you spend 20 coins on a fleeting toy, the opportunity cost is the 20 coins plus compound interest you could have earned!",
            funFact = "Every decision in life carries an unseen opportunity cost."
        ),
        QuizQuestion(
            id = "q_13_plus_07",
            question = "If a business has Revenue of 500 coins, Cost of Goods Sold of 200 coins, and Operating Expenses of 100 coins, what is the Net Profit Margin?",
            options = listOf("20%", "40%", "50%", "30%"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 13,
            maxAge = 18,
            explanation = "Net Profit = 500 - 200 - 100 = 200 coins. Net Margin = (200 / 500) x 100 = 40%!",
            funFact = "High margin businesses have durable competitive advantages (economic moats)."
        ),
        QuizQuestion(
            id = "q_13_plus_08",
            question = "Which of the following describes compound interest vs simple interest?",
            options = listOf("Simple interest earns interest on interest; compound does not", "Compound interest earns returns on both initial principal and accumulated interest", "Compound interest only works on weekends", "Simple interest always pays higher rates"),
            correctAnswerIndex = 1,
            category = "Financial Smarts",
            minAge = 13,
            maxAge = 18,
            explanation = "Compound interest generates exponential growth because your earnings generate their own earnings!",
            funFact = "Starting investing 10 years earlier can more than double your retirement wealth."
        ),
        QuizQuestion(
            id = "q_13_plus_09",
            question = "Solve the sequence: 2, 6, 12, 20, 30, ?",
            options = listOf("40", "42", "44", "48"),
            correctAnswerIndex = 1,
            category = "Logic & Puzzles",
            minAge = 13,
            maxAge = 18,
            explanation = "Differences are +4, +6, +8, +10, so next difference is +12. 30 + 12 = 42!",
            funFact = "These are called pronic numbers (n * (n+1))."
        ),
        QuizQuestion(
            id = "q_13_plus_10",
            question = "Why is diversification described as 'the only free lunch in finance'?",
            options = listOf("It lowers total portfolio risk without necessarily reducing expected returns", "It gives you free food at bank branches", "It guarantees you will never lose a single coin", "It eliminates all market volatility"),
            correctAnswerIndex = 0,
            category = "Financial Smarts",
            minAge = 13,
            maxAge = 18,
            explanation = "Spreading investments across different assets (like our 4 Jars & Garden Trees) reduces overall risk!",
            funFact = "Harry Markowitz won the Nobel Prize in Economics for Modern Portfolio Theory."
        )
    )
}
