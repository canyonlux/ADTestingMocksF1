package org.api.springf1;

import org.api.springf1.dto.DriverDTO;
import org.api.springf1.exception.DriverNotFoundException;
import org.api.springf1.model.Driver;
import org.api.springf1.repository.DriverRepository;
import org.api.springf1.service.DriverServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {

    @Mock
    DriverRepository driverRepository;

    @InjectMocks
    DriverServiceImpl driverService;

    Driver driver;
    DriverDTO driverDTO;

    @BeforeEach
    public void setup() {
        driver = Driver.builder().id(1L).code("AAA").forename("Ayrton").surname("Senna").build();
        driverDTO = DriverDTO.builder().id(1L).code("AAA").forename("Ayrton").surname("Senna").build();
    }

    @Test
    void shouldReturnDriverDTOWhenCreateDriver() {
        //Given
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        //When
        DriverDTO savedDriver = driverService.saveDriver(driver);

        //Then
        assertNotNull(savedDriver);
        assertEquals("AAA", savedDriver.code());


        verify(driverRepository, times(1)).save(driver);
    }
    @Test
    public void shouldReturnDriverDTOWhenFindDriverByCode() {
        // Given
        String code = "AAA";
        Driver driver = new Driver();
        driver.setCode(code);
        driver.setForename("Ayrton");
        driver.setSurname("Senna");


        when(driverRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.of(driver));

        // When
        DriverDTO result = driverService.getDriverByCode(code);

        // Then
        assertNotNull(result);
        assertEquals(driver.getCode(), result.code());
        assertEquals(driver.getForename(), result.forename());
        assertEquals(driver.getSurname(), result.surname());



        verify(driverRepository, times(1)).findByCodeIgnoreCase(code);
    }

    @Test
    public void shouldThrowExceptionWhenDriverNotFoundByCode() {
        // Given
        String code = "ZZZ";
        when(driverRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(DriverNotFoundException.class, () -> {
            driverService.getDriverByCode(code);
        });

        // Then
        assertNotNull(exception);
        assertEquals("Driver not found", exception.getMessage());

        // Verificar interacciones
        verify(driverRepository, times(1)).findByCodeIgnoreCase(code);
    }



}
