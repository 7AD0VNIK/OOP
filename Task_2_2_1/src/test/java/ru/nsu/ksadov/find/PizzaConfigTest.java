package ru.nsu.ksadov.find;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PizzaConfigTest {
    @Test
    void testJsonDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getClassLoader().getResourceAsStream("config.json");
        assertNotNull(is, "файл config.json должен существовать в src/main/resources");

        PizzaConfig config = mapper.readValue(is, PizzaConfig.class);

        assertTrue(config.storageCapacity > 0, "емкость склада должна быть больше 0");
        assertNotNull(config.bakers, "список пекарей не должен быть пустым");
        assertNotNull(config.couriers, "список курьеров не должен быть пустым");

        assertFalse(config.bakers.isEmpty());
        assertTrue(config.bakers.get(0).cookingSpeed > 0);
    }
}