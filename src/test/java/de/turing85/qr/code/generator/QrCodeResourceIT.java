package de.turing85.qr.code.generator;

import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
public class QrCodeResourceIT extends CucumberQuarkusIntegrationTestParent {
  public static void main(String... args) {
    runMain(QrCodeResourceIT.class, args);
  }
}
