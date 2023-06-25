package de.turing85.qr.code.generator.step;

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
public class QrCodeStep {
  private final QrCodeActor actor;

  @When("I get a QR Code for the text {string}")
  public void getQrCodeFor(String text) {
    actor.getQrCodeFor(text);
  }

  @Then("I expect the QR code to decode to that text")
  public void thenIExpectToGetTheCorrectQrCode() throws NotFoundException, IOException {
    actor.qrCodeDecodesToExpectedText();
  }

  @When("I pass no text to generate a QR Code")
  public void getQrCodeForNoText() {
    actor.getQrCodeForNoText();
  }

  @Then("I expect to receive a bad request response")
  public void responseIsBadRequest() {
    actor.responseIsBadRequest();
  }
}
