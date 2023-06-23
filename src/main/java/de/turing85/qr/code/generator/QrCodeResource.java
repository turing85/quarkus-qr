package de.turing85.qr.code.generator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

@Path("qr-code")
@Log4j2
public class QrCodeResource {
  private static final QRCodeWriter barcodeWriter = new QRCodeWriter();

  @GET
  @Produces("image/png")
  public Uni<byte[]> getQrCode(@QueryParam("text") @Valid @NotNull @NotBlank String text) {
    log.info("Generating qr-code for text: \"{}\"", text);
    //@formatter:off
    return Uni.createFrom().item(text)
        .map(encodedData -> URLDecoder.decode(encodedData, StandardCharsets.UTF_8))
        .onItem().transform(QrCodeResource::textToQrCode);
    //@formatter:on
  }

  private static byte[] textToQrCode(String text) {
    try {
      BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
      return outputStream.toByteArray();
    } catch (WriterException | IOException e) {
      throw new ProcessingException(e);
    }
  }
}
