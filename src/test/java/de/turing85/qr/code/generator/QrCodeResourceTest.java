package de.turing85.qr.code.generator;

import io.quarkiverse.cucumber.CucumberOptions;
import io.quarkiverse.cucumber.CucumberQuarkusTest;

@CucumberOptions(features = {"src/test/resources/de/turing85/qr/code/generator/qrCode.feature"},
    glue = "de.turing85.qr.code.generator.steps")
public class QrCodeResourceTest extends CucumberQuarkusTest {
  public static void main(String... args) {
    runMain(QrCodeResourceTest.class, args);
  }
}
