package de.turing85.qr.code.generator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.Produces;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Path("qr-code")
@Log4j2
public class QrCodeResource {
  private static final QRCodeWriter barcodeWriter = new QRCodeWriter();

  @GET
  @Path("{data}")
  @Produces("image/png")
  public Uni<byte[]> getBarcode(@PathParam("data") String data) {
    log.info("Generating qr-code for text: \"{}\"", data);
    return Uni.createFrom().item(data)
        .onItem().transform(QrCodeResource::textToQrCode);
  }

  private static byte[] textToQrCode(String data) {
    try {
      BitMatrix bitMatrix =
          barcodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
      return outputStream.toByteArray();
    } catch (WriterException | IOException e) {
      throw new ProcessingException(e);
    }
  }
}
