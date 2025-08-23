package com.example.inventory.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class CsvUniqIdReader {

    private static final String H_UNIQ_ID = "uniq_id";

    private static final CSVFormat FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setIgnoreSurroundingSpaces(true)
            .build();

    public static List<String> loadFromClasspath(final String classpathResource, final int maxRows) {
        try (InputStream in = CsvUniqIdReader.class.getResourceAsStream(classpathResource)) {
            if (in == null) {
                throw new IllegalArgumentException("Classpath resource not found: " + classpathResource);
            }
            return parse(in, maxRows);
        } catch (IOException e) {
            throw new RuntimeException("Failed reading CSV from classpath: " + classpathResource, e);
        }
    }

    public static List<String> loadFromFile(Path path, int maxRows) {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return parse(reader, maxRows);
        } catch (IOException e) {
            throw new RuntimeException("Failed reading CSV file: " + path, e);
        }
    }

    private static List<String> parse(InputStream in, int maxRows) throws IOException {
        return parse(new java.io.InputStreamReader(in, StandardCharsets.UTF_8), maxRows);
    }

    private static List<String> parse(Reader reader, int maxRows) throws IOException {
        List<String> out = new ArrayList<>(Math.max(16, Math.min(maxRows, 10_000)));
        try (CSVParser parser = new CSVParser(reader, FORMAT)) {
            for (CSVRecord r : parser) {
                String uniqId = val(r, H_UNIQ_ID);
                if (uniqId == null) continue;

                out.add(uniqId);
                if (out.size() >= maxRows) break;
            }
        }
        return out;
    }

    private static String val(CSVRecord r, String header) {
        try {
            String v = r.get(header);
            if (v == null) return null;
            v = v.trim();
            if (v.isEmpty() || "null".equalsIgnoreCase(v) || "NULL".equals(v)) return null;
            return v;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
