package de.turing85.qr.code.generator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import jakarta.ws.rs.core.Response;

import com.google.common.truth.Truth;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(QrCodeResource.class)
class QrCodeResourceTest {
  public static final MultiFormatReader MULTI_FORMAT_READER = new MultiFormatReader();

  @Test
  void testGetQrCode() throws IOException, NotFoundException {
    // GIVEN
    String expectedText = "lorem ipsum dolor";

    // WHEN
    // @formatter:off
    byte[] actual = RestAssured
        .when().get(expectedText)

    // THEN
        .then()
          .statusCode(Response.Status.OK.getStatusCode())
          .contentType("image/png")
          .extract().body().asByteArray();
    // @formatter:on
    Truth.assertThat(actual).isNotNull();
    Truth.assertThat(actual).isNotEmpty();
    Truth.assertThat(extractTextFromQrImage(actual)).isEqualTo(expectedText);
  }

  private static String extractTextFromQrImage(byte[] actual)
      throws IOException, NotFoundException {
    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
        new BufferedImageLuminanceSource(ImageIO.read(new ByteArrayInputStream(actual)))));
    return MULTI_FORMAT_READER.decode(binaryBitmap, Map.of()).getText();
  }
}
