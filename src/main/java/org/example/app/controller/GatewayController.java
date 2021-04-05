package org.example.app.controller;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.example.app.dto.GatewayDTO;
import org.example.app.service.GatewayService;
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
@RequestMapping(value = "/api/gateways", produces = MediaType.APPLICATION_JSON_VALUE)
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(final GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping
    public ResponseEntity<List<GatewayDTO>> getAllGateways() {
        return ResponseEntity.ok(gatewayService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GatewayDTO> getGateway(@PathVariable final Long id) {
        return ResponseEntity.ok(gatewayService.get(id));
    }

    @PostMapping
    public ResponseEntity<Void> createGateway(@RequestBody @Valid final GatewayDTO gatewayDTO) {
      MultiValueMap<String, String> headers = new HttpHeaders();
      URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(gatewayService.create(gatewayDTO)).toUri();
      headers.add(HttpHeaders.LOCATION, location.toString());
      return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGateway(@PathVariable final Long id,
            @RequestBody @Valid final GatewayDTO gatewayDTO) {
        gatewayService.update(id, gatewayDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGateway(@PathVariable final Long id) {
        gatewayService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
