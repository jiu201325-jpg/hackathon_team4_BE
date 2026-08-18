package com.example.likelionhackathon.config;

import com.example.likelionhackathon.entity.USMedication;
import com.example.likelionhackathon.repository.KoreanMedicationRepository;
import com.example.likelionhackathon.repository.USMedicationRepository;
import com.example.likelionhackathon.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private final KoreanMedicationRepository koreanMedicationRepository;
    private final USMedicationRepository usMedicationRepository;
    private final KoreanMedicationSyncService koreanMedicationSyncService;
    private final USMedicationSyncService usMedicationSyncService;
    private final SymptomCategoryTaggingService symptomCategoryTaggingService;
    private final IngredientEnrichmentService ingredientEnrichmentService;

    public DataSeeder(KoreanMedicationRepository koreanMedicationRepository,
                      USMedicationRepository usMedicationRepository,
                      KoreanMedicationSyncService koreanMedicationSyncService,
                      USMedicationSyncService usMedicationSyncService,
                      SymptomCategoryTaggingService symptomCategoryTaggingService,
                      IngredientEnrichmentService ingredientEnrichmentService) {
        this.koreanMedicationRepository = koreanMedicationRepository;
        this.usMedicationRepository = usMedicationRepository;
        this.koreanMedicationSyncService = koreanMedicationSyncService;
        this.usMedicationSyncService = usMedicationSyncService;
        this.symptomCategoryTaggingService = symptomCategoryTaggingService;
        this.ingredientEnrichmentService = ingredientEnrichmentService;
    }

    @Override
    public void run(String... args) {
        if (koreanMedicationRepository.count() > 0) {
            System.out.println("=== 데이터 이미 존재, 시딩 스킵");
            return;
        }

        System.out.println("=== 서버 시작 - 데이터 시딩 시작");

        String[] koreanMedications = {
                "타이레놀", "이지엔6", "낙센",
                "지르텍", "클라리틴", "알레그라",
                "베아제", "개비스콘", "스멕타"
        };
        for (String medication : koreanMedications) {
            koreanMedicationSyncService.syncOne(medication);
        }

        String[] usMedications = {
                "Tylenol", "Advil", "Aleve", "Zyrtec", "Claritin", "Benadryl",
                "Pepto-Bismol", "Tums", "Imodium", "Sudafed", "Mucinex",
                "Motrin", "Excedrin", "Allegra", "Flonase", "Gas-X",
                "Pepcid", "Dulcolax", "DayQuil", "Robitussin Cough", "Theraflu"
        };
        for (String medication : usMedications) {
            usMedicationSyncService.syncOne(medication);
        }

        symptomCategoryTaggingService.initCategoriesAndTag();
        ingredientEnrichmentService.enrichAll();
        fillImagerls();

        System.out.println("=== 데이터 시딩 완료");
    }
    private void fillImagerls() {
        Map<String, String> imageUrls = new HashMap<>();
        imageUrls.put("TYLENOL Extra Strength", "https://upload.wikimedia.org/wikipedia/commons/2/24/Tylenol_bottle_closeup_(loose_crop).jpg");
        imageUrls.put("Advil", "https://m.media-amazon.com/images/I/810tcgsqlPL._AC_UY327_FMwebp_QL65_.jpg");
        imageUrls.put("Aleve", "https://m.media-amazon.com/images/I/51ziyavtvlL._AC_UY327_FMwebp_QL65_.jpg");
        imageUrls.put("Childrens Zyrtec", "https://images.ctfassets.net/th7up0brotb1/5G6nck1XMcQB908jBshxv6/58a3b07c30b3eea7f580c3895ee2509a/ZYR_NA_US_300450205049_302050302_355804_CH_ALRGY_DF_SGRFR_SYRUP_4OZ_00000_TIF.WEBP");
        imageUrls.put("Claritin", "https://m.media-amazon.com/images/I/81Gk9Yc27ML._AC_UY327_FMwebp_QL65_.jpg");
        imageUrls.put("Benadryl", "https://m.media-amazon.com/images/I/812zSsQnGDL._AC_UY327_FMwebp_QL65_.jpg");
        imageUrls.put("Pepto-Bismol", "https://m.media-amazon.com/images/I/71JYztLltWL._AC_UY327_FMwebp_QL65_.jpg");
        imageUrls.put("TUMS", "https://m.media-amazon.com/images/I/71WP4TeCd9L._AC_UY327_FMwebp_QL65_.jpg");
        imageUrls.put("Imodium A-D", "https://m.media-amazon.com/images/I/71yCqRbW4aL._AC_UY327_FMwebp_QL65_.jpg");
        imageUrls.put("Sudafed Sinus Congestion 12 Hour", "https://m.media-amazon.com/images/I/81Qjog5flaL._AC_UL480_FMwebp_QL65_.jpg");
        imageUrls.put("Mucinex", "https://m.media-amazon.com/images/I/81fT7oxdnWL._AC_UL480_FMwebp_QL65_.jpg");
        imageUrls.put("Childrens Motrin", "https://m.media-amazon.com/images/I/81ytnZDGHgL._AC_UY327_FMwebp_QL65_.jpg");
        imageUrls.put("Excedrin Rapid Relief", "https://m.media-amazon.com/images/I/713fG60h3rL._AC_SY300_SX300_QL70_FMwebp_.jpg");
        imageUrls.put("Childrens Allegra Allergy", "https://m.media-amazon.com/images/I/61tX4hlYj-L._AC_UY327_FMwebp_QL65_.jpg");
        imageUrls.put("FLONASE SENSIMIST ALLERGY RELIEF", "https://m.media-amazon.com/images/I/71qRWx6SI0L._AC_SY300_SX300_QL70_FMwebp_.jpg");
        imageUrls.put("Gas-X Ultimate Strength Softgels", "https://i-cf65.ch-static.com/content/dam/cf-consumer-healthcare/bp-gasx/en_US/products/update-25/ultimate-strength-front_reworked.png");
        imageUrls.put("Pepcid", "https://m.media-amazon.com/images/I/71TLuur-LQL._AC_SY300_SX300_QL70_FMwebp_.jpg");
        imageUrls.put("Dulcolax", "https://m.media-amazon.com/images/I/81kirwxvfQL._AC_UL480_FMwebp_QL65_.jpg");
        imageUrls.put("Vicks DayQuil FOR PEOPLE WITH DIABETES COLD and FLU", "https://m.media-amazon.com/images/I/81IHq8DE3JL._AC_SY300_SX300_QL70_FMwebp_.jpg");
        imageUrls.put("Childrens Robitussin Cough Long-Acting", "https://pics.walgreens.com/prodimg/585377/450.jpg");
        imageUrls.put("Theraflu Flu Relief Max Strength Daytime", "https://m.media-amazon.com/images/I/71P77Xq-kwL._AC_UL480_FMwebp_QL65_.jpg");

        List<USMedication> allMeds = usMedicationRepository.findAll();
        for (USMedication med : allMeds) {
            String url = imageUrls.get(med.getName());
            if (url != null) {
                med.setImageUrl(url);
                usMedicationRepository.save(med);
            } else {
                System.out.println("=== 이미지 URL 매칭 안 됨: " + med.getName());
            }
        }
        System.out.println("=== 이미지 URL 채우기 완료");
    }
}
