package com.gamescollection.config;

import com.gamescollection.core.port.HexagonalService;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = HexagonalService.class))
public class CoreConfiguration {

}