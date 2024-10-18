package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Schema(name = "User id", example = "12345", required = true, type = "number")
    private long id;

    @Schema(name = "User name", example = "John", required = true, type = "string")
    private String name;

    @Schema(name = "Created at", example = "15/10/2204", required = true, type = "string")
    private Date createdAt;
}
