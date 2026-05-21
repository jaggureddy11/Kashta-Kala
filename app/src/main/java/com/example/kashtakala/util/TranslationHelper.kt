package com.example.kashtakala.util

enum class Language(val code: String, val displayName: String) {
    ENGLISH("en", "English"),
    KANNADA("kn", "ಕನ್ನಡ"),
    TELUGU("te", "తెలుగు"),
    HINDI("hi", "हिंदी")
}

object TranslationHelper {
    private val translations = mapOf(
        Language.ENGLISH to mapOf(
            // General / Nav
            "nav_catalog" to "Catalog",
            "nav_estimator" to "Estimator",
            "nav_quotes" to "Quotes",
            "nav_portfolio" to "Portfolio",
            "nav_ai" to "AI",
            
            // Splash
            "splash_title" to "Kashta-Kala",
            "splash_subtitle" to "Premium Carpentry Companion",
            
            // Catalog Screen
            "catalog_title" to "🪑 Kashta-Kala",
            "catalog_subtitle" to "Premium Furniture Catalog",
            "catalog_search" to "Search designs...",
            "catalog_filter_all" to "All",
            "catalog_filter_bed" to "Bed",
            "catalog_filter_sofa" to "Sofa",
            "catalog_filter_dining" to "Dining",
            "catalog_filter_wardrobe" to "Wardrobe",
            "catalog_filter_door" to "Door",
            "catalog_details_title" to "Design Details",
            "catalog_details_wood" to "Suggested Wood",
            "catalog_details_size" to "Ideal Room Size",
            "catalog_details_hardware" to "Hardware Needed",
            "catalog_details_description" to "Description",
            "catalog_details_cost" to "Estimated Cost",
            "catalog_btn_estimate" to "Use in Estimator",
            "catalog_btn_quote" to "Use in Quote",
            "catalog_fav_add" to "Add to Favourites",
            "catalog_fav_remove" to "Remove from Favourites",
            "catalog_custom_title" to "Design Customizations & Upgrades",
            "catalog_custom_note" to "Need custom configurations? You can ask our AI assistant on the AI tab to customize this design with specific sizes, styles, or material options.",
            
            // Estimator Screen
            "estimator_title" to "🪚 Wood Cost Estimator",
            "estimator_subtitle" to "Calculate exact wood material and labour cost",
            "estimator_label_wood_type" to "Wood Type",
            "estimator_label_length" to "Length (feet)",
            "estimator_label_width" to "Width (feet)",
            "estimator_label_thickness" to "Thickness (inches)",
            "estimator_label_qty" to "Quantity",
            "estimator_label_labour" to "Labour Charge (Rs)",
            "estimator_btn_calculate" to "Calculate Cost",
            "estimator_summary" to "Estimation Summary",
            "estimator_summary_vol" to "Volume",
            "estimator_summary_wood_cost" to "Wood Cost",
            "estimator_summary_labour_cost" to "Labour Cost",
            "estimator_summary_total" to "Total Cost",
            "estimator_cft" to "CFT",
            
            // Quote Screen
            "quote_title" to "📋 Technical Quote Generator",
            "quote_subtitle" to "Prepare detailed professional quotations for clients",
            "quote_item_name" to "Item Name (e.g. Wardrobe, Bed Frame)",
            "quote_material_desc" to "Material Description (e.g. Commercial Plywood)",
            "quote_material_cost" to "Material Cost (Rs)",
            "quote_labour_cost" to "Labour Cost (Rs)",
            "quote_profit_margin" to "Profit Margin (%)",
            "quote_btn_add" to "Add Item to Quote",
            "quote_client_name" to "Client Name",
            "quote_client_contact" to "Client Contact Number",
            "quote_btn_generate" to "Generate Quotation",
            "quote_invoice_title" to "Quotation Invoice",
            "quote_invoice_item" to "Item / Description",
            "quote_invoice_mat" to "Material",
            "quote_invoice_labour" to "Labour",
            "quote_invoice_total" to "Total",
            "quote_invoice_margin" to "Profit Margin",
            "quote_invoice_grand" to "Total Client Quote",
            
            // Portfolio Screen
            "portfolio_title" to "🎨 My Workshop Portfolio",
            "portfolio_subtitle" to "Showcase your completed carpentry works to clients",
            "portfolio_btn_upload" to "Upload New Work",
            "portfolio_field_title" to "Work Title (e.g., Modular Kitchen)",
            "portfolio_field_wood" to "Suggested/Used Wood",
            "portfolio_field_budget" to "Budget / Price (Rs)",
            "portfolio_btn_save" to "Save to Portfolio",
            "portfolio_btn_cancel" to "Cancel",
            "portfolio_empty" to "No items in portfolio yet. Upload your first masterpiece!",
            "portfolio_details_title" to "Project Details",
            "portfolio_details_budget" to "Project Budget",
            
            // AI Tools Screen
            "ai_title" to "✨ Gemini AI Workshop",
            "ai_subtitle" to "Build guidance, material advice, pricing and quote writing",
            "ai_choose_task" to "Choose AI task",
            "ai_task_design" to "Design Guidance",
            "ai_task_material" to "Material Recommendation",
            "ai_task_negotiation" to "Price Negotiation",
            "ai_task_quote" to "Quote Description",
            "ai_task_chat" to "Chat with AI",
            "ai_recommendation_title" to "💡 AI Recommendation",
            "ai_btn_copy" to "📋 Copy",
            "ai_btn_share" to "🔗 Share",
            "ai_chat_placeholder" to "Type carpentry question...",
            "ai_chat_empty" to "Ask me anything about carpentry works...",
            "ai_btn_send" to "Send",
            "ai_btn_action_design" to "Create Design Plan",
            "ai_btn_action_material" to "Recommend Wood",
            "ai_btn_action_negotiation" to "Create Strategy",
            "ai_btn_action_quote" to "Write Description",
            "ai_btn_action_chat" to "Send Message",
            "ai_input_room" to "Room dimensions",
            "ai_input_style" to "Style preference",
            "ai_input_budget" to "Budget (Rs)",
            "ai_input_durability" to "Durability need",
            "ai_input_region" to "Region",
            "ai_input_customer" to "Customer profile",
            "ai_input_quote" to "Technical quote details",
            
            // Categories and Wood Names
            "wood_teak" to "Teak Wood",
            "wood_sheesham" to "Sheesham Wood",
            "wood_plywood" to "Commercial Plywood",
            "wood_mdf" to "MDF Board",
            "wood_rosewood" to "Rosewood",
            "wood_mango" to "Mango Wood"
        ),
        Language.KANNADA to mapOf(
            "nav_catalog" to "ಕ್ಯಾಟಲಾಗ್",
            "nav_estimator" to "ಅಂದಾಜುಗಾರ",
            "nav_quotes" to "ಕೋಟ್‌ಗಳು",
            "nav_portfolio" to "ಪೋರ್ಟ್‌ಫೋಲಿಯೋ",
            "nav_ai" to "AI ನೆರವು",
            
            "splash_title" to "ಕಷ್ಟ-ಕಲ",
            "splash_subtitle" to "ಪ್ರೀಮಿಯಂ ಕಾರ್ಪೆಂಟ್ರಿ ಸಂಗಾತಿ",
            
            "catalog_title" to "🪑 ಕಷ್ಟ-ಕಲ",
            "catalog_subtitle" to "ಪ್ರೀಮಿಯಂ ಪೀಠೋಪಕರಣ ಕ್ಯಾಟಲಾಗ್",
            "catalog_search" to "ವಿನ್ಯಾಸಗಳನ್ನು ಹುಡುಕಿ...",
            "catalog_filter_all" to "ಎಲ್ಲಾ",
            "catalog_filter_bed" to "ಹಾಸಿಗೆ",
            "catalog_filter_sofa" to "ಸೋಫಾ",
            "catalog_filter_dining" to "ಡೈನಿಂಗ್",
            "catalog_filter_wardrobe" to "ಕಪಾಟು",
            "catalog_filter_door" to "ಬಾಗಿಲು",
            "catalog_details_title" to "ವಿನ್ಯಾಸದ ವಿವರಗಳು",
            "catalog_details_wood" to "ಶಿಫಾರಸು ಮಾಡಿದ ಮರ",
            "catalog_details_size" to "ಕೊಠಡಿಯ ಸೂಕ್ತ ಗಾತ್ರ",
            "catalog_details_hardware" to "ಅಗತ್ಯವಿರುವ ಹಾರ್ಡ್‌ವೇರ್",
            "catalog_details_description" to "ವಿವರಣೆ",
            "catalog_details_cost" to "ಅಂದಾಜು ವೆಚ್ಚ",
            "catalog_btn_estimate" to "ಅಂದಾಜುಗಾರನಲ್ಲಿ ಬಳಸಿ",
            "catalog_btn_quote" to "ಕೋಟ್‌ನಲ್ಲಿ ಬಳಸಿ",
            "catalog_fav_add" to "ಮೆಚ್ಚಿನವುಗಳಿಗೆ ಸೇರಿಸಿ",
            "catalog_fav_remove" to "ಮೆಚ್ಚಿನವುಗಳಿಂದ ತೆಗೆದುಹಾಕಿ",
            "catalog_custom_title" to "ವಿನ್ಯಾಸ ಗ್ರಾಹಕೀಕರಣ ಮತ್ತು ನವೀಕರಣಗಳು",
            "catalog_custom_note" to "ನಿಮಗೆ ಕಸ್ಟಮ್ ಗಾತ್ರ ಅಥವಾ ವಿನ್ಯಾಸ ಬೇಕೇ? ನಿರ್ದಿಷ್ಟ ಗಾತ್ರ, ಶೈಲಿ ಅಥವಾ ವಸ್ತುಗಳೊಂದಿಗೆ ಈ ವಿನ್ಯಾಸವನ್ನು ಬದಲಾಯಿಸಲು ನಮ್ಮ AI ಸಹಾಯಕರನ್ನು ಕೇಳಿ.",
            
            "estimator_title" to "🪚 ಮರದ ವೆಚ್ಚ ಅಂದಾಜುಗಾರ",
            "estimator_subtitle" to "ಮರದ ವಸ್ತು ಮತ್ತು ಕಾರ್ಮಿಕ ವೆಚ್ಚವನ್ನು ನಿಖರವಾಗಿ ಲೆಕ್ಕಾಚಾರ ಮಾಡಿ",
            "estimator_label_wood_type" to "ಮರದ ವಿಧ",
            "estimator_label_length" to "ಉದ್ದ (ಅಡಿ)",
            "estimator_label_width" to "ಅಗಲ (ಅಡಿ)",
            "estimator_label_thickness" to "ದಪ್ಪ (ಇಂಚು)",
            "estimator_label_qty" to "ಪ್ರಮಾಣ",
            "estimator_label_labour" to "ಕೂಲಿ ವೆಚ್ಚ (ರೂ)",
            "estimator_btn_calculate" to "ವೆಚ್ಚವನ್ನು ಲೆಕ್ಕ ಹಾಕಿ",
            "estimator_summary" to "ಅಂದಾಜು ಸಾರಾಂಶ",
            "estimator_summary_vol" to "ಗಾತ್ರ",
            "estimator_summary_wood_cost" to "ಮರದ ವೆಚ್ಚ",
            "estimator_summary_labour_cost" to "ಕೂಲಿ ವೆಚ್ಚ",
            "estimator_summary_total" to "ಒಟ್ಟು ವೆಚ್ಚ",
            "estimator_cft" to "ಸಿ.ಎಫ್.ಟಿ",
            
            "quote_title" to "📋 ತಾಂತ್ರಿಕ ಕೋಟ್ ಜನರೇಟರ್",
            "quote_subtitle" to "ಗ್ರಾಹಕರಿಗೆ ವಿವರವಾದ ವೃತ್ತಿಪರ ಕೊಟೇಶನ್ ತಯಾರಿಸಿ",
            "quote_item_name" to "ವಸ್ತುವಿನ ಹೆಸರು (ಉದಾ. ಕಪಾಟು, ಹಾಸಿಗೆ)",
            "quote_material_desc" to "ವಸ್ತುಗಳ ವಿವರಣೆ (ಉದಾ. ಕಮರ್ಷಿಯಲ್ ಪ್ಲೈವುಡ್)",
            "quote_material_cost" to "ವಸ್ತುಗಳ ವೆಚ್ಚ (ರೂ)",
            "quote_labour_cost" to "ಕೂಲಿ ವೆಚ್ಚ (ರೂ)",
            "quote_profit_margin" to "ಲಾಭದ ಪ್ರಮಾಣ (%)",
            "quote_btn_add" to "ಕೋಟ್‌ಗೆ ಸೇರಿಸಿ",
            "quote_client_name" to "ಗ್ರಾಹಕರ ಹೆಸರು",
            "quote_client_contact" to "ಸಂಪರ್ಕ ಸಂಖ್ಯೆ",
            "quote_btn_generate" to "ಕೊಟೇಶನ್ ತಯಾರಿಸಿ",
            "quote_invoice_title" to "ಕೊಟೇಶನ್ ಬಿಲ್",
            "quote_invoice_item" to "ವಸ್ತು / ವಿವರಣೆ",
            "quote_invoice_mat" to "ವಸ್ತು ವೆಚ್ಚ",
            "quote_invoice_labour" to "ಕೂಲಿ",
            "quote_invoice_total" to "ಒಟ್ಟು",
            "quote_invoice_margin" to "ಲಾಭದ ಮಾರ್ಜಿನ್",
            "quote_invoice_grand" to "ಗ್ರಾಹಕರ ಒಟ್ಟು ವೆಚ್ಚ",
            
            "portfolio_title" to "🎨 ನನ್ನ ಕೆಲಸಗಳ ಪೋರ್ಟ್‌ಫೋಲಿಯೋ",
            "portfolio_subtitle" to "ನಿಮ್ಮ ಪೂರ್ಣಗೊಂಡ ಕಾರ್ಪೆಂಟ್ರಿ ಕೆಲಸಗಳನ್ನು ಗ್ರಾಹಕರಿಗೆ ತೋರಿಸಿ",
            "portfolio_btn_upload" to "ಹೊಸ ಚಿತ್ರ ಅಪ್‌ಲೋಡ್ ಮಾಡಿ",
            "portfolio_field_title" to "ಕೆಲಸದ ಹೆಸರು (ಉದಾ. ಮಾಡ್ಯುಲರ್ ಕಿಚನ್)",
            "portfolio_field_wood" to "ಬಳಸಿದ ಮರ",
            "portfolio_field_budget" to "ಬಜೆಟ್ / ಬೆಲೆ (ರೂ)",
            "portfolio_btn_save" to "ಉಳಿಸಿ",
            "portfolio_btn_cancel" to "ರದ್ದುಮಾಡಿ",
            "portfolio_empty" to "ಪೋರ್ಟ್‌ಫೋಲಿಯೋನಲ್ಲಿ ಇನ್ನೂ ಯಾವುದೇ ವಸ್ತುಗಳಿಲ್ಲ. ನಿಮ್ಮ ಮೊದಲ ಕೆಲಸವನ್ನು ಅಪ್‌ಲೋಡ್ ಮಾಡಿ!",
            "portfolio_details_title" to "ಯೋಜನೆಯ ವಿವರಗಳು",
            "portfolio_details_budget" to "ಯೋಜನೆಯ ಬಜೆಟ್",
            
            "ai_title" to "✨ ಜೆಮಿನಿ AI ಕಾರ್ಯಾಗಾರ",
            "ai_subtitle" to "ವಿನ್ಯಾಸ ಮಾರ್ಗದರ್ಶನ, ಮರದ ಶಿಫಾರಸು, ಬೆಲೆ ಮಾತುಕತೆ ಮತ್ತು ಕೋಟ್ ವಿವರಣೆ",
            "ai_choose_task" to "AI ಕಾರ್ಯವನ್ನು ಆರಿಸಿ",
            "ai_task_design" to "ವಿನ್ಯಾಸ ಮಾರ್ಗದರ್ಶನ",
            "ai_task_material" to "ಮರದ ಶಿಫಾರಸು",
            "ai_task_negotiation" to "ಬೆಲೆ ಮಾತುಕತೆ ತಂತ್ರ",
            "ai_task_quote" to "ಕೋಟ್ ವಿವರಣೆ",
            "ai_task_chat" to "AI ಜೊತೆ ಚಾಟ್ ಮಾಡಿ",
            "ai_recommendation_title" to "💡 AI ಶಿಫಾರಸು",
            "ai_btn_copy" to "📋 ನಕಲಿಸಿ",
            "ai_btn_share" to "🔗 ಹಂಚಿಕೊಳ್ಳಿ",
            "ai_chat_placeholder" to "ಕಾರ್ಪೆಂಟ್ರಿ ಪ್ರಶ್ನೆಯನ್ನು ಟೈಪ್ ಮಾಡಿ...",
            "ai_chat_empty" to "ಕಾರ್ಪೆಂಟ್ರಿ ಕೆಲಸದ ಬಗ್ಗೆ ಯಾವುದನ್ನಾದರೂ ಕೇಳಿ...",
            "ai_btn_send" to "ಕಳುಹಿಸಿ",
            "ai_btn_action_design" to "ವಿನ್ಯಾಸ ಯೋಜನೆಯನ್ನು ತಯಾರಿಸಿ",
            "ai_btn_action_material" to "ಮರವನ್ನು ಶಿಫಾರಸು ಮಾಡಿ",
            "ai_btn_action_negotiation" to "ತಂತ್ರವನ್ನು ರಚಿಸಿ",
            "ai_btn_action_quote" to "ವಿವರಣೆಯನ್ನು ಬರೆಯಿರಿ",
            "ai_btn_action_chat" to "ಸಂದೇಶ ಕಳುಹಿಸಿ",
            "ai_input_room" to "ಕೊಠಡಿಯ ಅಳತೆಗಳು",
            "ai_input_style" to "ಶೈಲಿಯ ಆದ್ಯತೆ",
            "ai_input_budget" to "ಬಜೆಟ್ (ರೂ)",
            "ai_input_durability" to "ಬಾಳಿಕೆ ಅಗತ್ಯತೆ",
            "ai_input_region" to "ಪ್ರದೇಶ/ಊರು",
            "ai_input_customer" to "ಗ್ರಾಹಕರ ವಿವರ",
            "ai_input_quote" to "ತಾಂತ್ರಿಕ ಕೋಟ್ ವಿವರಗಳು",
            
            "wood_teak" to "ತೇಗದ ಮರ",
            "wood_sheesham" to "ಶೀಶಮ್ ಮರ",
            "wood_plywood" to "ಕಮರ್ಷಿಯಲ್ ಪ್ಲೈವುಡ್",
            "wood_mdf" to "MDF ಬೋರ್ಡ್",
            "wood_rosewood" to "ಬೀಟೆ ಮರ",
            "wood_mango" to "ಮಾವಿನ ಮರ"
        ),
        Language.TELUGU to mapOf(
            "nav_catalog" to "క్యాటలాగ్",
            "nav_estimator" to "అంచనాదారు",
            "nav_quotes" to "కోట్‌లు",
            "nav_portfolio" to "పోర్ట్‌ఫోలియో",
            "nav_ai" to "AI సహాయం",
            
            "splash_title" to "కాష్ట-కల",
            "splash_subtitle" to "ప్రీమియం కార్పెంట్రీ సహచరుడు",
            
            "catalog_title" to "🪑 కాష్ట-కల",
            "catalog_subtitle" to "ప్రీమియం ఫర్నిచర్ క్యాటలాగ్",
            "catalog_search" to "డిజైన్ల కోసం వెతకండి...",
            "catalog_filter_all" to "అన్నీ",
            "catalog_filter_bed" to "మంచం",
            "catalog_filter_sofa" to "సోఫా",
            "catalog_filter_dining" to "డైనింగ్",
            "catalog_filter_wardrobe" to "అల్మారా",
            "catalog_filter_door" to "తలుపు",
            "catalog_details_title" to "డిజైన్ వివరాలు",
            "catalog_details_wood" to "సిఫార్సు చేసిన కలప",
            "catalog_details_size" to "గది ఆదర్శ పరిమాణం",
            "catalog_details_hardware" to "అవసరమైన హార్డ్‌వేర్",
            "catalog_details_description" to "వివరణ",
            "catalog_details_cost" to "అంచనా వ్యయం",
            "catalog_btn_estimate" to "అంచనాదారులో ఉపయోగించండి",
            "catalog_btn_quote" to "కోట్‌లో ఉపయోగించండి",
            "catalog_fav_add" to "ఇష్టమైన వాటిలో చేర్చండి",
            "catalog_fav_remove" to "ఇష్టమైన వాటి నుండి తీసివేయండి",
            "catalog_custom_title" to "డిజైన్ అనుకూలీకరణలు & అప్‌గ్రేడ్‌లు",
            "catalog_custom_note" to "మీకు కస్టమ్ సైజు లేదా డిజైన్ కావాలా? నిర్దిష్ట పరిమాణం, శైలి లేదా మెటీరియల్ ఆప్షన్‌లతో ఈ డిజైన్‌ను మార్చడానికి మా AI సహాయకుడిని అడగండి.",
            
            "estimator_title" to "🪚 కలప ధర అంచనాదారు",
            "estimator_subtitle" to "కలప మెటీరియల్ మరియు శ్రమ ఖర్చును ఖచ్చితంగా లెక్కించండి",
            "estimator_label_wood_type" to "కలప రకం",
            "estimator_label_length" to "పొడవు (అడుగులు)",
            "estimator_label_width" to "వెడల్పు (అడుగులు)",
            "estimator_label_thickness" to "మందం (అంగుళాలు)",
            "estimator_label_qty" to "పరిమాణము",
            "estimator_label_labour" to "కూలి ఖర్చు (రూ)",
            "estimator_btn_calculate" to "ధరను లెక్కించు",
            "estimator_summary" to "అంచనా సారాంశం",
            "estimator_summary_vol" to "పరిమాణము",
            "estimator_summary_wood_cost" to "కలప ఖర్చు",
            "estimator_summary_labour_cost" to "కూలి ఖర్చు",
            "estimator_summary_total" to "మొత్తం ఖర్చు",
            "estimator_cft" to "CFT",
            
            "quote_title" to "📋 సాంకేతిక కోట్ జెనరేటర్",
            "quote_subtitle" to "కస్టమర్ల కోసం వివరణాత్మక వృత్తిపరమైన కొటేషన్ సిద్ధం చేయండి",
            "quote_item_name" to "వస్తువు పేరు (ఉదా. అల్మారా, మంచం)",
            "quote_material_desc" to "మెటీరియల్ వివరాలు (ఉదా. కమర్షియల్ ప్లైవుడ్)",
            "quote_material_cost" to "మెటీరియల్ ధర (రూ)",
            "quote_labour_cost" to "కూలి ఖర్చు (రూ)",
            "quote_profit_margin" to "లాభ శాతం (%)",
            "quote_btn_add" to "కోట్‌కు చేర్చండి",
            "quote_client_name" to "కస్టమర్ పేరు",
            "quote_client_contact" to "ఫోన్ నంబర్",
            "quote_btn_generate" to "కొటేషన్ సృష్టించండి",
            "quote_invoice_title" to "కొటేషన్ బిల్లు",
            "quote_invoice_item" to "వస్తువు / వివరణ",
            "quote_invoice_mat" to "మెటీరియల్",
            "quote_invoice_labour" to "కూలి",
            "quote_invoice_total" to "మొత్తం",
            "quote_invoice_margin" to "లాభ మార్జిన్",
            "quote_invoice_grand" to "కస్టమర్ మొత్తం కొటేషన్",
            
            "portfolio_title" to "🎨 నా పనుల పోర్ట్‌ఫోలియో",
            "portfolio_subtitle" to "మీరు పూర్తి చేసిన కార్పెంట్రీ పనులను కస్టమర్లకు చూపించండి",
            "portfolio_btn_upload" to "కొత్త చిత్రం అప్‌లోడ్ చేయండి",
            "portfolio_field_title" to "పని పేరు (ఉదా. మోడ్యులర్ కిచెన్)",
            "portfolio_field_wood" to "ఉపయోగించిన కలప",
            "portfolio_field_budget" to "బడ్జెట్ / ధర (రూ)",
            "portfolio_btn_save" to "భద్రపరచు",
            "portfolio_btn_cancel" to "రద్దు చేయి",
            "portfolio_empty" to "పోర్ట్‌ఫోలియోలో ఇంకా ఏ వస్తువులూ లేవు. మీ మొదటి పనిని అప్‌లోడ్ చేయండి!",
            "portfolio_details_title" to "ప్రాజెక్ట్ వివరాలు",
            "portfolio_details_budget" to "ప్రాజెక్ట్ బడ్జెట్",
            
            "ai_title" to "✨ జెమిని AI వర్క్‌షాప్",
            "ai_subtitle" to "డిజైన్ మార్గదర్శకత్వం, మెటీరియల్ సిఫార్సు, ధరల చర్చలు మరియు కోట్ వివరణ",
            "ai_choose_task" to "AI పనిని ఎంచుకోండి",
            "ai_task_design" to "డిజైన్ మార్గదర్శకత్వం",
            "ai_task_material" to "మెటీరియల్ సిఫార్సు",
            "ai_task_negotiation" to "ధరల చర్చల వ్యూహం",
            "ai_task_quote" to "కోట్ వివరణ",
            "ai_task_chat" to "AI తో చాట్ చేయండి",
            "ai_recommendation_title" to "💡 AI సిఫార్సు",
            "ai_btn_copy" to "📋 కాపీ చేయి",
            "ai_btn_share" to "🔗 షేர் చేయి",
            "ai_chat_placeholder" to "కార్పెంట్రీ ప్రశ్నను టైప్ చేయండి...",
            "ai_chat_empty" to "కార్పెంట్రీ పనుల గురించి ఏదైనా అడగండి...",
            "ai_btn_send" to "పంపించు",
            "ai_btn_action_design" to "డిజైన్ ప్లాన్ సృష్టించు",
            "ai_btn_action_material" to "కలపను సిఫార్సు చేయి",
            "ai_btn_action_negotiation" to "వ్యూహాన్ని సృష్టించు",
            "ai_btn_action_quote" to "వివరణ రాయండి",
            "ai_btn_action_chat" to "సందేశం పంపు",
            "ai_input_room" to "గది కొలతలు",
            "ai_input_style" to "శైలి ప్రాధాన్యత",
            "ai_input_budget" to "బడ్జెట్ (రూ)",
            "ai_input_durability" to "మన్నిక అవసరం",
            "ai_input_region" to "ప్రాంతం / నగరం",
            "ai_input_customer" to "కస్టమర్ ప్రొఫైల్",
            "ai_input_quote" to "సాంకేతిక కోట్ వివరాలు",
            
            "wood_teak" to "టేకు కలప",
            "wood_sheesham" to "శీశం కలప",
            "wood_plywood" to "కమర్షియల్ ప్లైవుడ్",
            "wood_mdf" to "MDF బోర్డు",
            "wood_rosewood" to "రోజ్‌వుడ్ కలప",
            "wood_mango" to "మామిడి కలప"
        ),
        Language.HINDI to mapOf(
            "nav_catalog" to "कैटलॉग",
            "nav_estimator" to "अनुमानक",
            "nav_quotes" to "कोटेशन",
            "nav_portfolio" to "पोर्टफोलियो",
            "nav_ai" to "AI सहायता",
            
            "splash_title" to "काष्ठ-कला",
            "splash_subtitle" to "प्रीमियम बढ़ईगीरी साथी",
            
            "catalog_title" to "🪑 काष्ठ-कला",
            "catalog_subtitle" to "प्रीमियम फर्नीचर कैटलॉग",
            "catalog_search" to "डिजाइन खोजें...",
            "catalog_filter_all" to "सभी",
            "catalog_filter_bed" to "बेड",
            "catalog_filter_sofa" to "सोफा",
            "catalog_filter_dining" to "डायनिंग",
            "catalog_filter_wardrobe" to "अलमारी",
            "catalog_filter_door" to "दरवाजा",
            "catalog_details_title" to "डिजाइन का विवरण",
            "catalog_details_wood" to "सुझाई गई लकड़ी",
            "catalog_details_size" to "कमरे का आदर्श आकार",
            "catalog_details_hardware" to "आवश्यक हार्डवेयर",
            "catalog_details_description" to "विवरण",
            "catalog_details_cost" to "अनुमानित लागत",
            "catalog_btn_estimate" to "अनुमानक में उपयोग करें",
            "catalog_btn_quote" to "कोटेशन में उपयोग करें",
            "catalog_fav_add" to "पसंदीदा में जोड़ें",
            "catalog_fav_remove" to "पसंदीदा से हटाएं",
            "catalog_custom_title" to "डिजाइन अनुकूलन और अपग्रेड",
            "catalog_custom_note" to "कस्टम साइज या डिजाइन चाहिए? विशिष्ट आकार, शैली या सामग्री विकल्पों के साथ इस डिजाइन को अनुकूलित करने के लिए हमारे AI सहायक से पूछें।",
            
            "estimator_title" to "🪚 लकड़ी लागत अनुमानक",
            "estimator_subtitle" to "लकड़ी की सामग्री और श्रम लागत की सटीक गणना करें",
            "estimator_label_wood_type" to "लकड़ी का प्रकार",
            "estimator_label_length" to "लंबाई (फिट)",
            "estimator_label_width" to "चौड़ाई (फिट)",
            "estimator_label_thickness" to "मोटाई (इंच)",
            "estimator_label_qty" to "मात्रा",
            "estimator_label_labour" to "मजदूरी शुल्क (रु)",
            "estimator_btn_calculate" to "लागत की गणना करें",
            "estimator_summary" to "लागत अनुमान का सारांश",
            "estimator_summary_vol" to "आयतन",
            "estimator_summary_wood_cost" to "लकड़ी की लागत",
            "estimator_summary_labour_cost" to "श्रम लागत",
            "estimator_summary_total" to "कुल लागत",
            "estimator_cft" to "घन फीट (CFT)",
            
            "quote_title" to "📋 तकनीकी कोटेशन जनरेटर",
            "quote_subtitle" to "ग्राहकों के लिए विस्तृत पेशेवर कोटेशन तैयार करें",
            "quote_item_name" to "आइटम का नाम (जैसे अलमारी, बेड)",
            "quote_material_desc" to "सामग्री का विवरण (जैसे कमर्शियल प्लाईवुड)",
            "quote_material_cost" to "सामग्री की लागत (रु)",
            "quote_labour_cost" to "श्रम लागत (रु)",
            "quote_profit_margin" to "लाभ प्रतिशत (%)",
            "quote_btn_add" to "कोटेशन में जोड़ें",
            "quote_client_name" to "ग्राहक का नाम",
            "quote_client_contact" to "संपर्क नंबर",
            "quote_btn_generate" to "कोटेशन जनरेट करें",
            "quote_invoice_title" to "कोटेशन इनवॉइस",
            "quote_invoice_item" to "आइटम / विवरण",
            "quote_invoice_mat" to "सामग्री लागत",
            "quote_invoice_labour" to "मजदूरी",
            "quote_invoice_total" to "कुल",
            "quote_invoice_margin" to "लाभ मार्जिन",
            "quote_invoice_grand" to "ग्राहक के लिए कुल कोटेशन",
            
            "portfolio_title" to "🎨 मेरे काम का पोर्टफोलियो",
            "portfolio_subtitle" to "ग्राहकों को अपने पूरे किए गए बढ़ईगीरी काम दिखाएं",
            "portfolio_btn_upload" to "नई छवि अपलोड करें",
            "portfolio_field_title" to "काम का शीर्षक (जैसे मॉड्युलर किचन)",
            "portfolio_field_wood" to "इस्तेमाल की गई लकड़ी",
            "portfolio_field_budget" to "बजट / मूल्य (रु)",
            "portfolio_btn_save" to "सुरक्षित करें",
            "portfolio_btn_cancel" to "रद्द करें",
            "portfolio_empty" to "पोर्टफोलियो में अभी कोई आइटम नहीं है। अपना पहला काम अपलोड करें!",
            "portfolio_details_title" to "परियोजना का विवरण",
            "portfolio_details_budget" to "परियोजना का बजट",
            
            "ai_title" to "✨ जेमिनी AI कार्यशाला",
            "ai_subtitle" to "डिजाइन मार्गदर्शन, सामग्री सिफारिश, मूल्य बातचीत और कोटेशन विवरण",
            "ai_choose_task" to "AI कार्य चुनें",
            "ai_task_design" to "डिजाइन मार्गदर्शन",
            "ai_task_material" to "सामग्री सिफारिश",
            "ai_task_negotiation" to "मूल्य बातचीत रणनीति",
            "ai_task_quote" to "कोटेशन विवरण",
            "ai_task_chat" to "AI के साथ चैट करें",
            "ai_recommendation_title" to "💡 AI सिफारिश",
            "ai_btn_copy" to "📋 कॉपी करें",
            "ai_btn_share" to "🔗 साझा करें",
            "ai_chat_placeholder" to "बढ़ईगीरी से जुड़ा सवाल टाइप करें...",
            "ai_chat_empty" to "बढ़ईगीरी के काम के बारे में कुछ भी पूछें...",
            "ai_btn_send" to "भेजें",
            "ai_btn_action_design" to "डिजाइन योजना बनाएं",
            "ai_btn_action_material" to "लकड़ी की सिफारिश करें",
            "ai_btn_action_negotiation" to "रणनीति बनाएं",
            "ai_btn_action_quote" to "विवरण लिखें",
            "ai_btn_action_chat" to "संदेश भेजें",
            "ai_input_room" to "कमरे की माप",
            "ai_input_style" to "डिजाइन शैली प्राथमिकता",
            "ai_input_budget" to "बजट (रु)",
            "ai_input_durability" to "मजबूती की आवश्यकता",
            "ai_input_region" to "क्षेत्र / शहर",
            "ai_input_customer" to "ग्राहक की प्रोफाइल",
            "ai_input_quote" to "तकनीकी कोटेशन विवरण",
            
            "wood_teak" to "सागौन की लकड़ी",
            "wood_sheesham" to "शीशम की लकड़ी",
            "wood_plywood" to "कमर्शियल प्लाईवुड",
            "wood_mdf" to "MDF बोर्ड",
            "wood_rosewood" to "शीशम/रोज़वुड",
            "wood_mango" to "आम की लकड़ी"
        )
    )

    fun getString(key: String, lang: Language): String {
        return translations[lang]?.get(key) ?: translations[Language.ENGLISH]?.get(key) ?: key
    }

    fun getDesignName(designId: Int, lang: Language): String {
        return when(lang) {
            Language.KANNADA -> when(designId) {
                1 -> "ಆಧುನಿಕ ಎಲ್-ಸೋಫಾ"
                2 -> "ಕ್ಲಾಸಿಕ್ 3-ಸೀಟರ್"
                3 -> "ಕಿಂಗ್ ಪ್ಲಾಟ್‌ಫಾರ್ಮ್ ಬೆಡ್"
                4 -> "ಕ್ವೀನ್ ಪೋಸ್ಟರ್ ಬೆಡ್"
                5 -> "ಟಿವಿ ಕ್ಯಾಬಿನೆಟ್"
                6 -> "ಕಿಚನ್ ಕ್ಯಾಬಿನೆಟ್"
                7 -> "3-ಬಾಗಿಲಿನ ಕಪಾಟು"
                8 -> "ಸ್ಲೈಡಿಂಗ್ ಕಪಾಟು"
                9 -> "ಡೈನಿಂಗ್ ಟೇಬಲ್ 6-ಸೀಟರ್"
                10 -> "ಸ್ಟಡಿ ಡೆಸ್ಕ್"
                else -> ""
            }
            Language.TELUGU -> when(designId) {
                1 -> "మోడ్రన్ ఎల్-సోఫా"
                2 -> "క్లాసిక్ 3-సీటర్"
                3 -> "కింగ్ ప్లాట్‌ఫార్మ్ బెడ్"
                4 -> "క్వీన్ పోస్టర్ బెడ్"
                5 -> "టీవీ క్యాబినెట్"
                6 -> "కిచెన్ క్యాబినెట్"
                7 -> "3-డోర్ల అల్మారా"
                8 -> "స్లైడింగ్ అల్మారా"
                9 -> "డైనింగ్ టేబుల్ 6-సీటర్"
                10 -> "స్టడీ డెస్క్"
                else -> ""
            }
            Language.HINDI -> when(designId) {
                1 -> "मॉडर्न एल-सोफा"
                2 -> "क्लासिक 3-सीटर"
                3 -> "किंग प्लेटफॉर्म बेड"
                4 -> "क्वीन पोस्टर बेड"
                5 -> "टीवी कैबिनेट"
                6 -> "किचन कैबिनेट"
                7 -> "3-दरवाजों वाली अलमारी"
                8 -> "स्लाइडिंग अलमारी"
                9 -> "डाइनिंग टेबल 6-सीटर"
                10 -> "स्टडी डेस्क"
                else -> ""
            }
            else -> ""
        }.ifEmpty { null } ?: when(designId) {
            1 -> "Modern L-Sofa"
            2 -> "Classic 3-Seater"
            3 -> "King Platform Bed"
            4 -> "Queen Poster Bed"
            5 -> "TV Cabinet"
            6 -> "Kitchen Cabinet"
            7 -> "3-Door Wardrobe"
            8 -> "Sliding Wardrobe"
            9 -> "Dining Table 6-Seater"
            10 -> "Study Desk"
            else -> ""
        }
    }

    fun getDesignDesc(designId: Int, lang: Language): String {
        return when(lang) {
            Language.KANNADA -> when(designId) {
                1 -> "ಆಧುನಿಕ ಶೈಲಿಯ ಎಲ್-ಆಕಾರದ ಸೋಫಾ, ಲಿವಿಂಗ್ ರೂಮ್‌ಗಳಿಗೆ ಸೂಕ್ತವಾಗಿದೆ."
                2 -> "ಕೆತ್ತನೆಯ ಮರದ ಚೌಕಟ್ಟಿನೊಂದಿಗೆ ಸಾಂಪ್ರದಾಯಿಕ 3-ಸೀಟರ್ ಸೋಫಾ."
                3 -> "ಕೆಳಗೆ ಶೇಖರಣಾ ಡ್ರಾಯರ್‌ಗಳನ್ನು ಹೊಂದಿರುವ ಹಾಸಿಗೆ."
                4 -> "ಅಲಂಕಾರಿಕ ಹೆಡ್‌ಬೋರ್ಡ್‌ನೊಂದಿಗೆ ನಾಲ್ಕು ಕಂಬಗಳ ಹಾಸಿಗೆ."
                5 -> "ಫ್ಲೋಟಿಂಗ್ ಶೆಲ್ಫ್‌ಗಳೊಂದಿಗೆ ಗೋಡೆಗೆ ಜೋಡಿಸಲಾದ ಟಿವಿ ಯುನಿಟ್."
                6 -> "ಸಾಫ್ಟ್-ಕ್ಲೋಸ್ ಹಿಂಜ್‌ಗಳೊಂದಿಗೆ ಮಾಡ್ಯುಲರ್ ಕಿಚನ್ ಕ್ಯಾಬಿನೆಟ್."
                7 -> "ಕನ್ನಡಿ ಮತ್ತು ಆಂತರಿಕ ಶೆಲ್ಫ್‌ಗಳನ್ನು ಹೊಂದಿರುವ ವಿಶಾಲವಾದ 3-ಬಾಗಿಲಿನ ಕಪಾಟು."
                8 -> "ಗಾಜಿನ ಪ್ಯಾನಲ್‌ಗಳೊಂದಿಗೆ ಸ್ಥಳಾವಕಾಶ ಉಳಿಸುವ ಸ್ಲೈಡಿಂಗ್ ಬಾಗಿಲಿನ ಕಪಾಟು."
                9 -> "ತಿರುಗಿಸಿದ ಕಾಲುಗಳನ್ನು ಹೊಂದಿರುವ ಘನ ಮರದ ಡೈನಿಂಗ್ ಟೇಬಲ್."
                10 -> "ಮೇಲಿನ ಶೆಲ್ಫ್‌ಗಳನ್ನು ಹೊಂದಿರುವ ಎಲ್-ಆಕಾರದ ಸ್ಟಡಿ ಡೆಸ್ಕ್."
                else -> ""
            }
            Language.TELUGU -> when(designId) {
                1 -> "ఆధునిక లివింగ్ రూమ్‌లకు సరిపోయే ఎల్-షేప్ సోఫా."
                2 -> "చెక్క శిల్పాలతో కూడిన సాంప్రదాయ 3-సీటర్ సోఫా."
                3 -> "కింద స్టోరేజ్ డ్రాయర్‌లతో కూడిన మంచం."
                4 -> "అలంకరణ హెడ్‌బోర్డ్‌తో కూడిన నాలుగు స్తంభాల మంచం."
                5 -> "ఫ్లోటింగ్ షెల్ఫ్‌లతో కూడిన టీవీ క్యాబినెట్."
                6 -> "సాఫ్ట్-క్లోజ్ హింగ్‌లతో కూడిన మోడ్యులర్ కిచెన్ క్యాబినెట్."
                7 -> "అద్దం మరియు షెల్ఫ్‌లతో కూడిన విశాలమైన 3-డోర్ల అల్మారా."
                8 -> "గ్లాస్ ప్యానెల్‌లతో స్థలాన్ని ఆదా చేసే స్లైడింగ్ డోర్ అల్మారా."
                9 -> "ఘనమైన కలపతో తయారుచేసిన డైనింగ్ టేబుల్."
                10 -> "పైభాగంలో షెల్ఫ్‌లతో కూడిన ఎల్-షేప్ స్టడీ డెస్క్."
                else -> ""
            }
            Language.HINDI -> when(designId) {
                1 -> "आधुनिक लिविंग रूम के लिए उपयुक्त एल-आकार का सोफा।"
                2 -> "नक्काशीदार लकड़ी के फ्रेम वाला पारंपरिक 3-सीटर सोफा।"
                3 -> "नीचे स्टोरेज दराज वाला किंग प्लेटफॉर्म बेड।"
                4 -> "सजावटी हेडबोर्ड वाला क्वीन पोस्टर बेड।"
                5 -> "फ्लोटिंग अलमारियों के साथ दीवार पर लगा टीवी कैबिनेट।"
                6 -> "सॉफ्ट-क्लोज टिका के साथ मॉड्यूलर किचन कैबिनेट।"
                7 -> "दर्पण और आंतरिक अलमारियों के साथ विशाल 3-दरवाजे वाली अलमारी।"
                8 -> "ग्लास पैनलों के साथ जगह बचाने वाली स्लाइडिंग अलमारी।"
                9 -> "नक्काशीदार पैरों वाली ठोस लकड़ी की डाइनिंग टेबल।"
                10 -> "ओवरहेड अलमारियों के साथ एल-आकार की स्टडी डेस्क।"
                else -> ""
            }
            else -> ""
        }.ifEmpty { null } ?: when(designId) {
            1 -> "Contemporary L-shaped sofa with clean lines, perfect for modern living rooms."
            2 -> "Traditional 3-seater sofa with carved wooden frame."
            3 -> "Low-profile platform bed with storage drawers underneath."
            4 -> "Four-poster queen bed with decorative headboard."
            5 -> "Wall-mounted TV unit with floating shelves."
            6 -> "Modular kitchen cabinet with soft-close hinges."
            7 -> "Spacious 3-door wardrobe with mirror and internal shelves."
            8 -> "Space-saving sliding door wardrobe with glass panels."
            9 -> "Solid wood dining table with turned legs."
            10 -> "L-shaped study desk with overhead shelves."
            else -> ""
        }
    }
}
