package de.turing85.qr.code.generator.actor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.imageio.ImageIO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import com.google.common.truth.Truth;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

@ApplicationScoped
public class QrCodeActor {
  private static final MultiFormatReader MULTI_FORMAT_READER = new MultiFormatReader();

  private ValidatableResponse lastResponse;

  public void getQrCodeFor(String data) {
    // @formatter:off
    lastResponse = given().queryParam("data", data)
        .when().get("qr-code")
        .then();
    // @formatter:on
  }

  public void qrCodeDecodesTo(String expected) throws IOException, NotFoundException {
    try {
      // @formatter:off
      byte[] actual = Optional.ofNullable(lastResponse)
          .orElseThrow(() ->
              new IllegalStateException("please call \"getQrCodeFor(String)\" first"))
          .statusCode(Response.Status.OK.getStatusCode())
          .contentType("image/png")
          .extract().body().asByteArray();
      // @formatter:on
      Truth.assertThat(actual).isNotNull();
      Truth.assertThat(actual).isNotEmpty();
      Truth.assertThat(extractTextFromQrImage(actual)).isEqualTo(expected);
    } finally {
      clearState();
    }
  }

  private static String extractTextFromQrImage(byte[] actual)
      throws IOException, NotFoundException {
    // @formatter:off
    BinaryBitmap binaryBitmap = new BinaryBitmap(
        new HybridBinarizer(
            new BufferedImageLuminanceSource(
                ImageIO.read(new ByteArrayInputStream(actual)))));
    // @formatter:on
    return MULTI_FORMAT_READER.decode(binaryBitmap, Map.of()).getText();
  }

  private void clearState() {
    lastResponse = null;
  }
}
