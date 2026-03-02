package ru.nsu.ksadov.find;

import java.util.List;

public class PizzaConfig {
    public int storageCapacity;
    public List<BakerConfig> bakers;
    public List<CourierConfig> couriers;

    public static class BakerConfig {
        public int id;
        public int cookingSpeed;
    }

    public static class CourierConfig {
        public int id;
        public int trunkCapacity;
        public int deliverySpeed;
    }
}