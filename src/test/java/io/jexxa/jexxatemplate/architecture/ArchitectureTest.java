package io.jexxa.jexxatemplate.architecture;

import io.jexxa.jexxatemplate.JexxaTemplate;
import org.junit.jupiter.api.Test;

import static io.jexxa.jexxatest.architecture.ArchitectureRules.aggregateRules;
import static io.jexxa.jexxatest.architecture.ArchitectureRules.patternLanguage;
import static io.jexxa.jexxatest.architecture.ArchitectureRules.portsAndAdapters;

class ArchitectureTest {

    @Test
    void validatePortsAndAdapters()
    {
        portsAndAdapters(JexxaTemplate.class)
                .addDrivenAdapterPackage("persistence")
                .addDrivenAdapterPackage("messaging")
                .validate();
    }

    @Test
    void validatePatternLanguage()
    {
        patternLanguage(JexxaTemplate.class)
                .validate();
    }

    @Test
    void validateAggregateRules()
    {
        aggregateRules(JexxaTemplate.class)
                .validate();
    }
}