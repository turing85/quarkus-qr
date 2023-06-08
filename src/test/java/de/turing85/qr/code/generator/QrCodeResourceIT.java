package de.turing85.qr.code.generator;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@QuarkusIntegrationTest
@TestHTTPEndpoint(QrCodeResource.class)
class QrCodeResourceIT extends QrCodeResourceTest {
}
