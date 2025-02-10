package com.gamescollection.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(packages = "com.gamescollection")
public class GamesCollectionArchitectureTest {

    public static final String BASE_PACKAGE = "com.gamescollection";
    public static final String CORE_PACKAGE = BASE_PACKAGE + ".core";
    public static final String ADAPTER_PACKAGE = BASE_PACKAGE + ".adapter";
    public static final String CONFIG_PACKAGE = BASE_PACKAGE + ".config";

    @ArchTest
    public static final ArchRule HEXAGONAL_ARCHITECTURE = Architectures
            .onionArchitecture()
                .domainModels(CORE_PACKAGE + ".model..")
                .domainServices(CORE_PACKAGE + ".service", CORE_PACKAGE + ".port..")
                .applicationServices(CONFIG_PACKAGE)
                    .adapter("Persistence", ADAPTER_PACKAGE + ".persistence..")
                    .adapter("Controller", ADAPTER_PACKAGE + ".controller..");


}
