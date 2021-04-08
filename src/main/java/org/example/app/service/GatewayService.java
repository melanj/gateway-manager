package org.example.app.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.example.app.model.Gateway;
import org.example.app.dto.GatewayDTO;
import org.example.app.repository.GatewayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.example.app.util.ConversionUtils.dtoToGateway;
import static org.example.app.util.ConversionUtils.gatewayToDTO;


@Service
public class GatewayService {

    String IPV4_PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

    private final GatewayRepository gatewayRepository;

    public GatewayService(final GatewayRepository gatewayRepository) {
        this.gatewayRepository = gatewayRepository;
    }

    public List<GatewayDTO> findAll() {
        return gatewayRepository.findAll()
                .stream()
                .map(gateway -> gatewayToDTO(gateway, new GatewayDTO()))
                .collect(Collectors.toList());
    }

    public GatewayDTO get(final Long id) {
        return gatewayRepository.findById(id)
                .map(gateway -> gatewayToDTO(gateway, new GatewayDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final GatewayDTO gatewayDTO) {
        validateGatewayIP(gatewayDTO);
        final Gateway gateway = new Gateway();
        dtoToGateway(gatewayDTO, gateway);
        return gatewayRepository.save(gateway).getId();
    }

    public void update(final Long id, final GatewayDTO gatewayDTO) {
        validateGatewayIP(gatewayDTO);
        final Gateway gateway = gatewayRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        dtoToGateway(gatewayDTO, gateway);
        gatewayRepository.save(gateway);
    }

    public void delete(final Long id) {
        gatewayRepository.deleteById(id);
    }

    private void validateGatewayIP(GatewayDTO gatewayDTO) {
        if (!(Objects.nonNull(gatewayDTO.getIpv4Address()) &&
                (gatewayDTO.getIpv4Address().matches(IPV4_PATTERN)))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Gateway IPv4 address is not valid");
        }
    }

}
