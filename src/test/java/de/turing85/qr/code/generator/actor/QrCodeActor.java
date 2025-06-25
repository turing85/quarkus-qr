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
import de.turing85.qr.code.generator.QrCodeResource;
import de.turing85.qr.code.generator.exceptionmapper.ErrorResponse;
import io.restassured.response.ValidatableResponse;
import org.jspecify.annotations.Nullable;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@ApplicationScoped
public class QrCodeActor {
  private static final MultiFormatReader MULTI_FORMAT_READER = new MultiFormatReader();

  @Nullable
  private String expectedText;

  @Nullable
  private ValidatableResponse response;

  public void getQrCodeFor(String text) {
    expectedText = text;
    // @formatter:off
    response = given().queryParam("text", text)
        .when().get(QrCodeResource.PATH)
        .then();
    // @formatter:on
  }

  public void qrCodeDecodesToExpectedText() throws IOException, NotFoundException {
    try {
    // @formatter:off
      byte[] actual = Optional.ofNullable(response)
          .orElseThrow(() -> new IllegalStateException("please call \"getQrCodeFor(String)\" first"))
          .statusCode(Response.Status.OK.getStatusCode())
          .contentType("image/png")
          .extract().body().asByteArray();
      // @formatter:on
      Truth.assertThat(extractTextFromQrImage(actual)).isEqualTo(expectedText);
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
    response = null;
    expectedText = null;
  }

  public void getQrCodeForNoText() {
    response = when().get(QrCodeResource.PATH).then();
  }

  public void responseIsBadRequest() {
    try {
      // @formatter:off
      String errorMessage = Optional.ofNullable(response)
          .orElseThrow(() -> new IllegalStateException("please call \"getQrCodeFor(String)\" first"))
          .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
          .extract().body().as(ErrorResponse.class)
          .message();
      // @formatter:on
      Truth.assertThat(errorMessage).isNotNull();
      Truth.assertThat(errorMessage).isNotEmpty();
    } finally {
      clearState();
    }
  }
}
