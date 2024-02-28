package org.api.springf1.repository;

import org.api.springf1.model.Constructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
public class ConstructorRepositoryTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15.5")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    ConstructorRepository constructorRepository;

    private Constructor createTestConstructor(String name, String nationality, String ref) { // Crear un constructor de prueba
        Constructor constructor = new Constructor();
        constructor.setName(name);
        constructor.setNationality(nationality);
        constructor.setRef(ref);
        return constructorRepository.save(constructor);
    }

    @BeforeEach
    void setUp() {
        constructorRepository.deleteAll();
    }

    @Test
    void shouldSaveAndRetrieveConstructor() { // Debería guardar y recuperar un constructor

        String refValue = "1";
        Constructor savedConstructor = createTestConstructor("Triumph", "England", refValue);


        Optional<Constructor> foundConstructor = constructorRepository.findById(savedConstructor.getId());

        // Entonces deberíamos encontrarlo y sus datos deben coincidir
        assertTrue(foundConstructor.isPresent(), "");
        assertEquals(savedConstructor.getName(), foundConstructor.get().getName(), "");
        assertEquals(savedConstructor.getNationality(), foundConstructor.get().getNationality(), "");
        assertEquals(savedConstructor.getRef(), foundConstructor.get().getRef(), "");
    }

    @Test
    void shouldUpdateConstructorDetails() { // Debería actualizar los detalles del constructor

        String refValue = "1";
        Constructor savedConstructor = createTestConstructor("Triumph", "England", refValue);
        String updatedName = "Triumph Racing Team";


        savedConstructor.setName(updatedName);
        constructorRepository.save(savedConstructor);


        Constructor foundConstructor = constructorRepository.findById(savedConstructor.getId()).orElse(null);

        // Entonces su nombre debería haberse actualizado
        assertNotNull(foundConstructor, "");
        assertEquals(updatedName, foundConstructor.getName(), "");
    }

    @Test
    void shouldDeleteConstructor() { // Debería eliminar el constructor

        String refValue = "2";
        Constructor savedConstructor = createTestConstructor("Lamborghini", "Italy", refValue);


        constructorRepository.deleteById(savedConstructor.getId());


        Optional<Constructor> foundConstructor = constructorRepository.findById(savedConstructor.getId());

        // Entonces no deberíamos encontrar nada
        assertTrue(foundConstructor.isEmpty(), "");
    }

    @Test
    void shouldFindAllConstructors() { // Debería encontrar todos los constructores
        // Dado que tenemos varios constructores
        createTestConstructor("Triumph", "England", "ref1");
        createTestConstructor("Lamborghini", "Italy", "ref2");

        // Cuando solicitamos todos los constructores
        List<Constructor> constructors = constructorRepository.findAll();

        // Entonces deberíamos encontrar al menos los dos creados
        assertTrue(constructors.size() >= 2, "Deberíamos encontrar al menos dos constructores");
    }
}
