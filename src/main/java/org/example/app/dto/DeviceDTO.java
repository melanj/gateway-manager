package org.example.app.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.app.model.Status;

@Getter
@Setter
public class DeviceDTO {

    private Long id;

    @NotNull
    private Long uid;

    @NotNull
    @Size(max = 255)
    private String vendor;

    @NotNull
    private LocalDate dateCreated;

    @NotNull
    private Status status;

    @NotNull
    private Long gateway;

}
