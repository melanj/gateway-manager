package org.example.app.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GatewayDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String serial;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String ipv4Address;

}
