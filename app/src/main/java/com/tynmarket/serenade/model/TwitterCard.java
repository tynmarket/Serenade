package com.tynmarket.serenade.model;

/**
 * Created by tynmarket on 2018/02/10.
 */

public class TwitterCard {
    public static final String CARD_SUMMARY = "summary";
    public static final String CARD_SUMMARY_LARGE_Image = "summary_large_image";

    public String card;
    public String image;
    public String title;

    public TwitterCard(String card, String image, String title) {
        this.card = card;
        this.image = image;
        this.title = title;
    }

    public boolean isSummary() {
        return card.equals(CARD_SUMMARY);
    }

    public boolean isSummaryLargeImage() {
        return card.equals(CARD_SUMMARY_LARGE_Image);
    }
}
