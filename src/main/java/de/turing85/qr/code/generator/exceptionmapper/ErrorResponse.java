package de.turing85.qr.code.generator.exceptionmapper;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection public record ErrorResponse(String message){}
