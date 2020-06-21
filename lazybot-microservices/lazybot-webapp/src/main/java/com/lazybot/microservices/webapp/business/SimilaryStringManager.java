package com.lazybot.microservices.webapp.business;

import info.debatty.java.stringsimilarity.Levenshtein;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class SimilaryStringManager {
    Levenshtein l = new Levenshtein();

    /**
     * Get the closest name of file
     * @param file name of the file to get
     * @param files directory where the files are
     * @return return the name of the closest file
     */
    public String testLevenshtein (String file, File[] files) {
        double smallestDistance = Double.MAX_VALUE;
        String closestFile = "";

        for (File value : files) {
            double distance = l.distance(file, value.getName());
            if (distance < smallestDistance) {
                closestFile = value.getName();
                smallestDistance = distance;
            }
        }

        return closestFile;
    }
}
