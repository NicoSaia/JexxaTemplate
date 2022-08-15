package io.jexxa.jexxatemplate.architecture;

import io.jexxa.jexxatemplate.JexxaTemplate;
import org.junit.jupiter.api.Test;

import static io.jexxa.jexxatest.architecture.ArchitectureRules.aggregateRules;
import static io.jexxa.jexxatest.architecture.ArchitectureRules.patternLanguage;
import static io.jexxa.jexxatest.architecture.ArchitectureRules.portsAndAdapters;

/**
 * This test can be used in all your applications.
 * <p>
 * You have only to adjust test validatePortsAndAdapters. Here you have to add all your
 * packages containing the driven and driving adapters. This information is required to ensure
 * that there are no dependencies between these packages.
 */
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
        patternLanguage(JexxaTemplate.class).validate();
    }

    @Test
    void validateAggregateRules()
    {
        aggregateRules(JexxaTemplate.class).validate();
    }
}