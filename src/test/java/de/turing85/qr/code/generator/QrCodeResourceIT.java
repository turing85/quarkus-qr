package de.turing85.qr.code.generator;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.when;

@QuarkusIntegrationTest
@TestHTTPEndpoint(QrCodeResource.class)
class QrCodeResourceIT {
  public static final MultiFormatReader MULTI_FORMAT_READER = new MultiFormatReader();

  @Test
  void testGetQrCode() throws IOException, NotFoundException {
    // GIVEN
    String expectedText = "lorem ipsum dolor";

    // WHEN
    // @formatter:off
    byte[] actual = when().get(expectedText)

    // THEN
        .then()
          .statusCode(Response.Status.OK.getStatusCode())
          .contentType("image/png")
          .extract().body().asByteArray();
    // @formatter:on
    assertThat(actual).isNotNull();
    assertThat(actual).isNotEmpty();
    assertThat(extractTextFromQrImage(actual)).isEqualTo(expectedText);
  }

  private static String extractTextFromQrImage(byte[] actual)
      throws IOException, NotFoundException {
    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
        new BufferedImageLuminanceSource(ImageIO.read(new ByteArrayInputStream(actual)))));
    return MULTI_FORMAT_READER.decode(binaryBitmap, Map.of()).getText();
  }
}
