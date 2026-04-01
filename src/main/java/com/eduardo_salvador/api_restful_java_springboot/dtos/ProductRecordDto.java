package com.eduardo_salvador.api_restful_java_springboot.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductRecordDto(@NotBlank String name, @NotNull BigDecimal price) { }