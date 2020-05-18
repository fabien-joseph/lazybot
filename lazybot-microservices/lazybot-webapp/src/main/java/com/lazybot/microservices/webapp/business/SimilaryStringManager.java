package com.lazybot.microservices.webapp.business;

import info.debatty.java.stringsimilarity.Levenshtein;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class SimilaryStringManager {
    Levenshtein l = new Levenshtein();

    public String testLevenshtein (String file, File[] files) {
        double smallestDistance = Double.MAX_VALUE;
        String closestFile = "";

        for (int i = 0; i < files.length; i++) {
            double distance = l.distance(file, files[i].getName());
            if (distance < smallestDistance) {
                closestFile = files[i].getName();
                smallestDistance = distance;
            }
        }

        return closestFile;
    }
}
