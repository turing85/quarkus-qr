package de.turing85.qr.code.generator;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

import io.quarkiverse.barcode.zxing.ZebraCrossing;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

@Path(QrCodeResource.PATH)
public class QrCodeResource {
  public static final String PATH = "qr-code";

  @GET
  @Produces("image/png")
  public Uni<byte[]> getQrCode(@QueryParam("text") @Valid @NotNull @NotBlank String text) {
    Log.infof("Generating qr-code for text: \"%s\"", text);
    return Uni.createFrom().item(text).map(QrCodeResource::textToQrCode);
  }

  private static byte[] textToQrCode(String text) {
    return ZebraCrossing.barcodetoPng(ZebraCrossing.qrCode(text, 200, 200));
  }
}
