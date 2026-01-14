package com.tierranativa.aplicacion.tierra.nativa.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class IconMappingService {
    private static final Map<String, String> KEYWORD_TO_ICON = new HashMap<>();

    static {
        KEYWORD_TO_ICON.put("glaciar", "IceCream");
        KEYWORD_TO_ICON.put("hielo", "Snowflake");
        KEYWORD_TO_ICON.put("trekking", "Footprints");
        KEYWORD_TO_ICON.put("montaña", "Mountain");
        KEYWORD_TO_ICON.put("cataratas", "Waves");
        KEYWORD_TO_ICON.put("agua", "Droplets");
        KEYWORD_TO_ICON.put("selva", "TreePalm");
        KEYWORD_TO_ICON.put("vuelo", "Plane");
        KEYWORD_TO_ICON.put("hotel", "Hotel");
    }

    public String getIconForText(String text) {
        if (text == null) return "MapPin";

        String lowerText = text.toLowerCase();
        return KEYWORD_TO_ICON.entrySet().stream()
                .filter(entry -> lowerText.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("MapPin");
    }
}

