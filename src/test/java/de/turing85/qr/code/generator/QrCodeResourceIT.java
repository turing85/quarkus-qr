package de.turing85.qr.code.generator;

import io.quarkiverse.cucumber.CucumberOptions;
import io.quarkiverse.cucumber.CucumberQuarkusTest;

@CucumberOptions(features = {"src/test/resources/de/turing85/qr/code/generator/qrCode.feature"},
    glue = {"de.turing85.qr.code.generator.step"})
public class QrCodeResourceIT extends CucumberQuarkusTest {
}
