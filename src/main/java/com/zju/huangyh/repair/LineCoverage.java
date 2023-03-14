package com.zju.huangyh.repair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Stores line coverage information from a file (produced by JaCoCo).
 *
 * @ClassName LineCoverage
 * @Author huangyaohua
 */
public class LineCoverage {

    private HashMap<LCNode, Double> coverage;

    /**
     * Initialize the line coverage by reading the JaCoCo output.
     *
     * @param file filePath
     */
    public LineCoverage(File file) throws Exception {
        this.coverage = LineCoverage.readCoverageFile(file);
    }

    /**
     * Returns the weight of the statement, or null if the statement was not covered.
     *
     * @param statement The statement that we are checking coverage for.
     * @return The weight of the statement or null if is not covered.
     */
    public Double contains(LCNode statement) {
        return this.coverage.get(statement);
    }

    /**
     * Reads a line coverage file into a HashSet<LCNode> object.
     *
     * @param file The JaCoCo line coverage file.
     * @return The set of lines covered.
     * @throws Exception
     */
    private static HashMap<LCNode, Double> readCoverageFile(File file) throws Exception {
        HashMap<LCNode, Double> map = new HashMap<LCNode, Double>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            String[] tokens = line.split(":");
            if (tokens.length == 4) {
                LCNode node = new LCNode(tokens[0], tokens[1], Integer.valueOf(tokens[2]));
                map.put(node, Double.valueOf(tokens[3]));
            } else {
                reader.close();
                throw new Exception("LineCoverage.readCoverageFile: file not formatted properly... too many tokens.\n" + line);
            }
        }
        reader.close();
        return map;
    }
}
