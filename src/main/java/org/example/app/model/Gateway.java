package org.example.app.model;

import java.util.Set;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Gateway {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serial;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private Integer ipv4Address;

    @OneToMany(mappedBy = "gateway", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Device> gatewayDevices;

}
