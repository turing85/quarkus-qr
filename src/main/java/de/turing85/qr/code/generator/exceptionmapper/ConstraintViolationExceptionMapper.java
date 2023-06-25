package de.turing85.qr.code.generator.exceptionmapper;

import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@SuppressWarnings("unused")
public class ConstraintViolationExceptionMapper
    implements ExceptionMapper<ConstraintViolationException> {

  public static final String BODY_FORMAT = "Parameter \"%s\": %s";
  public static final String UNNAMED_PROPERTY = "(unnamed)";

  @Override
  public Response toResponse(ConstraintViolationException exception) {
    String message = exception.getConstraintViolations().stream()
        .map(ConstraintViolationExceptionMapper::constructViolationDescription)
        .collect(Collectors.joining("\n"));
    return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
        .entity(new ErrorResponse(message)).build();
  }

  private static String constructViolationDescription(ConstraintViolation<?> violation) {
    return String.format(BODY_FORMAT, getPropertyNameFromPath(violation).orElse(UNNAMED_PROPERTY),
        violation.getMessage());
  }

  private static Optional<String> getPropertyNameFromPath(ConstraintViolation<?> violation) {
    String propertyName = null;
    for (Path.Node node : violation.getPropertyPath()) {
      propertyName = node.getName();
    }
    return Optional.ofNullable(propertyName);
  }
}
