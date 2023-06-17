package de.turing85.qr.code.generator.steps;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.google.zxing.NotFoundException;
import de.turing85.qr.code.generator.actor.QrCodeActor;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Http {
  private final QrCodeActor actor;

  @When("I get {string} as QR-code")
  public void whenIAccessEndpoint(String text) {
    actor.getQrCodeFor(text);
  }

  @Then("I expect to get the correct QR code")
  public void thenIExpectToGetTheCorrectQrCode() throws NotFoundException, IOException {
    actor.qrCodeIsAsExpected();
  }
}
