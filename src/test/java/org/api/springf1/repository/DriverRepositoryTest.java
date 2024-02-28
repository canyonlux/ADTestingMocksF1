package org.api.springf1.repository;

import org.api.springf1.model.Driver;
import org.api.springf1.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // Usar una base de datos H2 en memoria
public class DriverRepositoryTest {

    @Autowired
    private DriverRepository driverRepository;

    private Driver crearPilotoDePrueba(String codigo, String nombre, String apellido) { // Crear un piloto de prueba
        return Driver.builder()
                .code(codigo)
                .forename(nombre)
                .surname(apellido)
                .dob(LocalDate.of(1984, 4, 26))
                .nationality("España")
                .url("http://papafrita.com ")
                .build();
    }


    @Test
    void shouldReturnMoreThanOneDriverWhenSaveTwoDrivers() { // Debería devolver más de un piloto cuando guardamos dos pilotos
        Driver piloto1 = crearPilotoDePrueba("001", "Ruben", "Garcia");
        Driver piloto2 = crearPilotoDePrueba("002", "Adrian", "Garcia");
        driverRepository.save(piloto1);
        driverRepository.save(piloto2);

        List<Driver> pilotos = driverRepository.findAll();


        assertThat(pilotos.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldReturnDriverNotNullWhenFindByCode() { // Debería devolver un piloto no nulo cuando se busca por código

        Driver piloto = crearPilotoDePrueba("003", "Mikka", "Hakkinen");
        driverRepository.save(piloto);

        Optional<Driver> pilotoEncontrado = driverRepository.findByCodeIgnoreCase("003");

        // Entonces el piloto encontrado no debe ser null
        assertThat(pilotoEncontrado).isPresent();
    }

    @Test
    void shouldReturnDriverNotNullWhenUpdateDriver() { // Debería devolver un piloto no nulo cuando se actualiza un piloto

        Driver piloto = crearPilotoDePrueba("004", "Felipe", "Massa");
        driverRepository.save(piloto);

        // Cuando actualizamos algunos detalles de este piloto
        piloto.setForename("Felipe Actualizado");
        driverRepository.save(piloto);

        Optional<Driver> pilotoActualizado = driverRepository.findById(piloto.getId());


        assertThat(pilotoActualizado).isPresent();
        assertThat(pilotoActualizado.get().getForename()).isEqualTo("Felipe Actualizado");
    }

    @Test
    void shouldReturnNullDriverWhenDelete() { // Debería devolver un piloto nulo cuando se elimina

        Driver piloto = crearPilotoDePrueba("005", "Roberto", "Baggio");
        driverRepository.save(piloto);
        driverRepository.deleteByCodeIgnoreCase(piloto.getCode());


        Optional<Driver> pilotoEliminado = driverRepository.findByCodeIgnoreCase(piloto.getCode());


        assertThat(pilotoEliminado).isNotPresent();
    }

}
