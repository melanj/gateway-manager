package org.example.app;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.stream.IntStream;

import static org.testng.Assert.*;
import static org.testng.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AppApplication.class)
@ActiveProfiles("test")
public class AppApplicationTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int randomServerPort;
    private String createdGatewayPath;
    private String createdDevicePath;
    private RestTemplate restTemplate;
    private final HttpHeaders headers = new HttpHeaders();

    @BeforeMethod
    public void setUp() {
        restTemplate = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void testBootstrap() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/actuator");
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        assertEquals(result.getStatusCodeValue(), 200);
    }

    @Test
    void testRequestForwarding() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/add-gateway");
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        assertEquals(result.getStatusCodeValue(), 200);
    }

    @Test(priority = 1)
    void testCreateGatewayWithValidPayload() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/gateways");
        String request = "{\n" +
                "  \"serial\": \"10001230\",\n" +
                "  \"name\": \"my-gateway-1\",\n" +
                "  \"ipv4Address\": \"192.168.1.1\"\n" +
                "}";
        ResponseEntity<String> result = restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
        URI location = result.getHeaders().getLocation();
        assertNotNull(location);
        createdGatewayPath = location.getPath();
        assertEquals(result.getStatusCodeValue(), 201);
    }

    @Test(priority = 1, expectedExceptions = HttpClientErrorException.class)
    void testCreateGatewayWithBadIP() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/gateways");
        String request = "{\n" +
                "  \"serial\": \"10001230\",\n" +
                "  \"name\": \"my-gateway-1\",\n" +
                "  \"ipv4Address\": \"192.168.1.400\"\n" +
                "}";
        restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
    }

    @Test(priority = 1, expectedExceptions = HttpClientErrorException.class)
    void testCreateGatewayWithNullIP() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/gateways");
        String request = "{\n" +
                "  \"serial\": \"10001230\",\n" +
                "  \"name\": \"my-gateway-1\",\n" +
                "  \"ipv4Address\": null" +
                "}";
        restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
    }

    @Test(priority = 1, expectedExceptions = HttpClientErrorException.class)
    void testCreateGatewayWithBadName() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/gateways");
        String request = "{\n" +
                "  \"serial\": \"10001230\",\n" +
                "  \"name\": null,\n" +
                "  \"ipv4Address\": \"192.168.1.1\"\n" +
                "}";
        restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
    }

    @Test(priority = 1, expectedExceptions = HttpClientErrorException.class)
    void testCreateGatewayWithBadSerial() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/gateways");
        String request = "{\n" +
                "  \"serial\": null,\n" +
                "  \"name\": \"my-gateway-1\",\n" +
                "  \"ipv4Address\": \"192.168.1.1\"\n" +
                "}";
        restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
    }

    @Test(priority = 2)
    void testGetGateway() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + createdGatewayPath);
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        assertEquals(result.getStatusCodeValue(), 200);
    }

    @Test(priority = 2, dependsOnMethods = "testCreateGatewayWithValidPayload")
    void testGetAllGateways() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/gateways");
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        assertEquals(result.getStatusCodeValue(), 200);
    }

    @Test(priority = 2, dependsOnMethods = "testCreateGatewayWithValidPayload")
    void testUpdateGateway() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + createdGatewayPath);
        String request = "{\n" +
                "  \"id\": \"" + createdGatewayPath.split("/")[3] +  "\",\n" +
                "  \"serial\": \"10001230\",\n" +
                "  \"name\": \"my-gateway-1\",\n" +
                "  \"ipv4Address\": \"192.168.1.100\"\n" +
                "}";
        restTemplate.put(uri,new HttpEntity<>(request, headers));
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        assertEquals(result.getStatusCodeValue(), 200);
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("192.168.1.100"));
    }

    @Test(priority = 2, dependsOnMethods = "testCreateGatewayWithValidPayload",
            expectedExceptionsMessageRegExp = "404 : .*", expectedExceptions = HttpClientErrorException.class)
    void testUpdateGatewayNonExists() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/gateways/2001000");
        String request = "{\n" +
                "  \"id\": 2001000,\n" +
                "  \"serial\": \"10001230\",\n" +
                "  \"name\": \"my-gateway-1\",\n" +
                "  \"ipv4Address\": \"192.168.1.100\"\n" +
                "}";
        restTemplate.put(uri, new HttpEntity<>(request, headers));
    }

    @Test(priority = 3, dependsOnMethods = "testCreateGatewayWithValidPayload")
    void testCreateDeviceValidPayload() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/devices");
        String request = "{" +
                "\"uid\":2110," +
                "\"vendor\":\"Dell\"," +
                "\"dateCreated\":\"2021-04-08\"," +
                "\"status\":\"ONLINE\"," +
                "\"gateway\":" + createdGatewayPath.split("/")[3] +
                "}";
        ResponseEntity<String> result = restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
        URI location = result.getHeaders().getLocation();
        assertNotNull(location);
        createdDevicePath = location.getPath();
        assertEquals(result.getStatusCodeValue(), 201);
    }

    @Test(priority = 3, dependsOnMethods = "testCreateDeviceValidPayload",
            expectedExceptionsMessageRegExp = ".*You have exceeded the maximum number of devices allowed for this gateway.*",
            expectedExceptions = HttpClientErrorException.class)
    void testCreateDevicesMoreThan10PerGateway() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/devices");
        IntStream.range(3110, 3121).forEach(i -> {
            String request = "{" +
                    "\"uid\":" + i + "," +
                    "\"vendor\":\"Dell\"," +
                    "\"dateCreated\":\"2021-04-08\"," +
                    "\"status\":\"ONLINE\"," +
                    "\"gateway\":" + createdGatewayPath.split("/")[3] +
                    "}";
            restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
        });

    }

    @Test(priority = 3, dependsOnMethods = "testCreateDeviceValidPayload", expectedExceptions = HttpClientErrorException.class)
    void testCreateDeviceValidPayloadExistsSameUid() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/devices");
        String request = "{" +
                "\"uid\":2110," +
                "\"vendor\":\"Dell\"," +
                "\"dateCreated\":\"2021-04-08\"," +
                "\"status\":\"ONLINE\"," +
                "\"gateway\":" + createdGatewayPath.split("/")[3] +
                "}";
        restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
    }

    @Test(priority = 3, expectedExceptions = HttpClientErrorException.class)
    void testCreateDeviceNullGateway() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/devices");
        String request = "{" +
                "\"uid\":2113," +
                "\"vendor\":\"Dell\"," +
                "\"dateCreated\": \"2021-04-08\"," +
                "\"status\":\"ONLINE\"," +
                "\"gateway\": null" +
                "}";
        restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
    }

    @Test(priority = 3, expectedExceptions = HttpClientErrorException.class)
    void testCreateDeviceNonExistsGateway() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/devices");
        String request = "{" +
                "\"uid\":2113," +
                "\"vendor\":\"Dell\"," +
                "\"dateCreated\": \"2021-04-08\"," +
                "\"status\":\"ONLINE\"," +
                "\"gateway\": 1000200" +
                "}";
        restTemplate.postForEntity(uri, new HttpEntity<>(request, headers), String.class);
    }

    @Test(priority = 4, dependsOnMethods = "testCreateDeviceValidPayload")
    void testGetDevice() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + createdDevicePath);
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        assertEquals(result.getStatusCodeValue(), 200);
    }

    @Test(priority = 4, dependsOnMethods = "testCreateDeviceValidPayload")
    void testGetAllDevices() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/devices");
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        assertEquals(result.getStatusCodeValue(), 200);
    }

    @Test(priority = 4, dependsOnMethods = "testCreateDeviceValidPayload")
    void testUpdateDevice() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + createdDevicePath);
        String request = "{\n" +
                "\"id\":" + createdDevicePath.split("/")[3] +  ",\n" +
                "\"uid\":2110,\n" +
                "\"vendor\":\"Dell\",\n" +
                "\"dateCreated\":\"2021-04-08\",\n" +
                "\"status\":\"OFFLINE\",\n" +
                "\"gateway\":" + createdGatewayPath.split("/")[3] +  "\n" +
                "}";
        restTemplate.put(uri,new HttpEntity<>(request, headers));
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
        assertEquals(result.getStatusCodeValue(), 200);
        assertNotNull(result.getBody());
        assertEquals(result.getBody(),request.replaceAll("\n", ""));
    }

    @Test(priority = 4, dependsOnMethods = "testCreateDeviceValidPayload",
            expectedExceptionsMessageRegExp = "404 : .*", expectedExceptions = HttpClientErrorException.class)
    void testUpdateDeviceNonExist() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + "/api/devices/2001000");
        String request = "{\n" +
                "\"id\":2001000," +
                "\"uid\":2110,\n" +
                "\"vendor\":\"Dell\",\n" +
                "\"dateCreated\":\"2021-04-08\",\n" +
                "\"status\":\"OFFLINE\",\n" +
                "\"gateway\":" + createdGatewayPath.split("/")[3] +  "\n" +
                "}";
        restTemplate.put(uri,new HttpEntity<>(request, headers));
    }

    @Test(priority = 4, dependsOnMethods = "testCreateDeviceValidPayload",
            expectedExceptionsMessageRegExp = ".*gateway not found.*", expectedExceptions = HttpClientErrorException.class)
    void testUpdateDeviceWithNonExistGateway() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + createdDevicePath);
        String request = "{\n" +
                "\"id\":" + createdDevicePath.split("/")[3] + ",\n" +
                "\"uid\":2110,\n" +
                "\"vendor\":\"Dell\",\n" +
                "\"dateCreated\":\"2021-04-08\",\n" +
                "\"status\":\"OFFLINE\",\n" +
                "\"gateway\": 200500" +
                "}";
        restTemplate.put(uri, new HttpEntity<>(request, headers));;
    }

    @Test(priority = 4, dependsOnMethods = "testCreateDeviceValidPayload",
            expectedExceptionsMessageRegExp = ".*\"field\":\"gateway\",\"errorCode\":\"NotNull\".*", expectedExceptions = HttpClientErrorException.class)
    void testUpdateDeviceWithNullGateway() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + createdDevicePath);
        String request = "{\n" +
                "\"id\":" + createdDevicePath.split("/")[3] + ",\n" +
                "\"uid\":2110,\n" +
                "\"vendor\":\"Dell\",\n" +
                "\"dateCreated\":\"2021-04-08\",\n" +
                "\"status\":\"OFFLINE\",\n" +
                "\"gateway\": null" +
                "}";
        restTemplate.put(uri, new HttpEntity<>(request, headers));
    }

    @Test(priority = 4, dependsOnMethods = "testCreateDeviceValidPayload",
            expectedExceptions = HttpServerErrorException.class)
    void testUpdateDeviceWithInvalidDateCreated() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + createdDevicePath);
        String request = "{\n" +
                "\"id\":" + createdDevicePath.split("/")[3] + ",\n" +
                "\"uid\":2110,\n" +
                "\"vendor\":\"Dell\",\n" +
                "\"dateCreated\":\"zzzzzzzzzzz\",\n" +
                "\"status\":\"OFFLINE\",\n" +
                "\"gateway\": null" +
                "}";
        restTemplate.put(uri, new HttpEntity<>(request, headers));
    }

    @Test(priority = 5, dependsOnMethods = "testCreateGatewayWithValidPayload",
            expectedExceptionsMessageRegExp = "404 : .*", expectedExceptions = HttpClientErrorException.class)
    void testDeleteDevice() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + createdDevicePath);
        restTemplate.delete(uri.toString(), headers);
        restTemplate.getForEntity(uri, String.class);
    }


    @Test(priority = 6, dependsOnMethods = "testCreateGatewayWithValidPayload",
            expectedExceptionsMessageRegExp = "404 : .*", expectedExceptions = HttpClientErrorException.class)
    void testDeleteGateway() throws Exception {
        URI uri = new URI("http://localhost:" + randomServerPort + createdGatewayPath);
        restTemplate.delete(uri.toString(), headers);
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    }

}