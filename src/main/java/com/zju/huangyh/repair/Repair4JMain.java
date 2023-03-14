package com.zju.huangyh.repair;

import com.zju.huangyh.repair.context.Context;
import com.zju.huangyh.repair.context.ContextFactory;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * Repair4J start
 * args: the path to the configuration file that is used to configure Repair4J.
 *
 * @ClassName Repair4JMain
 * @Author huangyaohua
 */
public class Repair4JMain {

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            Repair4J repair = readConfigFile(new File(args[0]));
            repair.buildASTs();
            repair.repair();
        } else {
            System.out.println("Use: java SampleUse [/path/to/repair4J.properties]");
        }
    }

    /**
     * read configuration file, creat context (environment) and creat Repair4J
     *
     * @param config The .properties file.
     * @return An instance of Repair4J configures using the settings in the .properties file.
     * @throws Exception Throws an exception if a parameter is missing or incorrect.
     */
    public static Repair4J readConfigFile(File config) throws Exception {

        Properties properties = new Properties();
        properties.load(new FileReader(config));

        Context context = ContextFactory.buildContext(properties);

        Repair4J repair = new Repair4J(context);

        return repair;
    }
}
