package ru.nsu.ksadov.find;

import java.util.List;

/**
 * Конфиг.
 */
public class PizzeriaConfig {
    public int storageCapacity;
    public List<BakerConfig> bakers;
    public List<CourierConfig> couriers;
    public String saveFilePath;

    /**
     * Конструктор конфига пекарей.
     */
    public static class BakerConfig {
        public int id;
        public int cookingSpeed;
    }

    /**
     * Конструктор конфига курьеров.
     */
    public static class CourierConfig {
        public int id;
        public int trunkCapacity;
        public int deliverySpeed;
    }
}