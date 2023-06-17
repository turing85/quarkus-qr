package de.turing85.qr.code.generator;

import jakarta.enterprise.inject.spi.CDI;

import de.turing85.qr.code.generator.steps.Http;
import io.quarkiverse.cucumber.CucumberQuarkusTest;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public abstract class CucumberQuarkusIT extends CucumberQuarkusTest {
  static {
    // @formatter:off
    Weld weld = new Weld()
        .addBeanClass(Http.class);
    // @formatter:on
    WeldContainer container = weld.initialize();
    CDI.setCDIProvider(() -> container);
  }
}
