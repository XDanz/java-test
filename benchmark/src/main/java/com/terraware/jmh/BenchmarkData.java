package com.terraware.jmh;


import java.util.List;
import java.util.Random;

public class BenchmarkData {
    /** Cache the articles generated to make sharing data easier. */
    private static List<Article> articles;

    public static List<Article> generateArticles(int size) {
        if (articles == null)
            articles = new ArticleGenerator(new Random()).generate(size);
        return articles;
    }
}