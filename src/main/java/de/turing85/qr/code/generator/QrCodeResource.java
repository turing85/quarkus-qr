package de.turing85.qr.code.generator;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import io.quarkiverse.barcode.zxing.ZebraCrossing;
import io.smallrye.mutiny.Uni;
import lombok.extern.log4j.Log4j2;

@Path(QrCodeResource.PATH)
@Log4j2
public class QrCodeResource {
  public static final String PATH = "qr-code";

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
    return ZebraCrossing.barcodetoPng(ZebraCrossing.qrCode(text, 200, 200));
  }
}
