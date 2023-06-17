package de.turing85.qr.code.generator;

import io.quarkiverse.cucumber.CucumberOptions;
import io.quarkiverse.cucumber.CucumberQuarkusTest;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;

@CucumberOptions(features = {"src/test/resources/de/turing85/qr/code/generator/qrCode.feature"},
    glue = "de.turing85.qr.code.generator.steps")
@QuarkusIntegrationTest
@EnableAutoWeld
@AddPackages(value = {QrCodeResourceIT.class})
public class QrCodeResourceIT extends CucumberQuarkusTest {
}
