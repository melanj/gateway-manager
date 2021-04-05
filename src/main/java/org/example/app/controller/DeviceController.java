package org.example.app.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.example.app.dto.DeviceDTO;
import org.example.app.service.DeviceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/devices", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(final DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        return ResponseEntity.ok(deviceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable final Long id) {
        return ResponseEntity.ok(deviceService.get(id));
    }

    @PostMapping
    public ResponseEntity<Void> createDevice(@RequestBody @Valid final DeviceDTO deviceDTO) {
      MultiValueMap<String, String> headers = new HttpHeaders();
      URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(deviceService.create(deviceDTO)).toUri();
      headers.add(HttpHeaders.LOCATION, location.toString());
      return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDevice(@PathVariable final Long id,
            @RequestBody @Valid final DeviceDTO deviceDTO) {
        deviceService.update(id, deviceDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable final Long id) {
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
