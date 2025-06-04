package com.entornos.project.Demo.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "estados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estado implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "estado")
    private String nombre;
}
