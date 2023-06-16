package de.turing85.qr.code.generator.steps;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;

import com.google.zxing.NotFoundException;
import de.turing85.qr.code.generator.actor.QrCodeActor;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@ApplicationScoped
public class Http {
  private final QrCodeActor actor = new QrCodeActor();

  @When("I get {string} as QR-code")
  public void whenIAccessEndpoint(String text) {
    actor.getQrCodeFor(text);
  }

  @Then("I expect to get the correct QR code")
  public void thenIExpectToGetTheCorrectQrCode() throws NotFoundException, IOException {
    actor.qrCodeIsAsExpected();
  }
}
