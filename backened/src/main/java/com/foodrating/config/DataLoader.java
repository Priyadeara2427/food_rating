package com.foodrating.config;

import com.foodrating.entity.Canteen;
import com.foodrating.entity.Dish;
import com.foodrating.repository.CanteenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CanteenRepository canteenRepository;

    @Override
    public void run(String... args) throws Exception {
        // Load sample data only if database is empty
        if (canteenRepository.count() == 0) {
            // Create canteens
            Canteen mainCanteen = new Canteen();
            mainCanteen.setName("Main Campus Canteen");
            mainCanteen.setLocation("Building near Ladies Hostel");
            mainCanteen.setDescription("The main canteen serving variety of dishes");
            canteenRepository.save(mainCanteen);

            Canteen rishabsCanteen = new Canteen();
            rishabsCanteen.setName("Rishabs Canteen");
            rishabsCanteen.setLocation("Near SSN Admin Block");
            rishabsCanteen.setDescription("Healthy and organic food options along with all juices");
            canteenRepository.save(rishabsCanteen);

            Canteen snowCubeCanteen = new Canteen();
            snowCubeCanteen.setName("Snow Cubes Canteen");
            snowCubeCanteen.setLocation("Near SSN Admin Block");
            snowCubeCanteen.setDescription("All fancy snacks with cakes and burgers.");
            canteenRepository.save(snowCubeCanteen);
            
            Canteen ashwinsCanteen = new Canteen();
            ashwinsCanteen.setName("Ashwins Canteen");
            ashwinsCanteen.setLocation("Near Clock Tower and beside Ladies hostel");
            ashwinsCanteen.setDescription("Stationery, groceries, foods and snacks available");
            canteenRepository.save(ashwinsCanteen);

            Canteen metroCafeCanteen = new Canteen();
            metroCafeCanteen.setName("Metro Cafe Canteen");
            metroCafeCanteen.setLocation("Near SSN ECE Block");
            metroCafeCanteen.setDescription("Newly canteen with all variety dishes");
            canteenRepository.save(metroCafeCanteen);



            // Create dishes for main canteen using helper method
            Dish noodles_main = new Dish("Veg Noodles", "Classic veg noodles with tomato sauce and sweet.", "Chinese", 90.00, 4);
            noodles_main.setAvailableDate(LocalDate.now());
            mainCanteen.addDish(noodles_main);
            
            Dish chapathi_main = new Dish("Chapathi with Kurma", "2 Chapathis with available vegetables and sweets.", "Indian", 35.00, 2);
            chapathi_main.setAvailableDate(LocalDate.now());
            mainCanteen.addDish(chapathi_main);


            // Create dishes for rishabs canteen using helper method
            Dish noodles_rishab = new Dish("Veg Noodles", "Classic veg noodles with tomato sauce and onion raita.", "Chinese", 80.00, 4);
            noodles_rishab.setAvailableDate(LocalDate.now());
            rishabsCanteen.addDish(noodles_rishab);
            
            Dish chapathi_rishab = new Dish("Chapathi with Kurma", "3 Chapathis with available kurma and onion raita.", "Indian", 40.00, 2);
            chapathi_rishab.setAvailableDate(LocalDate.now());
            rishabsCanteen.addDish(chapathi_rishab);

            Dish orangeJuice_rishab = new Dish("Orange Juice", "Sweet and tasty Orange", "Indian", 50.00, 4);
            orangeJuice_rishab.setAvailableDate(LocalDate.now());
            rishabsCanteen.addDish(orangeJuice_rishab);


            // Create dishes for snow cube canteen using helper method
            Dish pizza = new Dish("Margherita Pizza", "Classic pizza with tomato sauce and mozzarella cheese", "Italian", 140.00, 4);
            pizza.setAvailableDate(LocalDate.now());
            snowCubeCanteen.addDish(pizza);
            
            Dish burger = new Dish("Cheese Burger", "Beef patty with cheese, lettuce and special sauce", "American", 50.00, 4);
            burger.setAvailableDate(LocalDate.now());
            snowCubeCanteen.addDish(burger);

            // Create dishes for ashwins canteen using helper method
            Dish samosa = new Dish("Samosa with sauce", "Fresh potato samosa", "Healthy", 15.00, 4);
            samosa.setAvailableDate(LocalDate.now());
            ashwinsCanteen.addDish(samosa);

            Dish chips = new Dish("Potato Chips", "All variety of chips", "Healthy", 20.00, 4);
            chips.setAvailableDate(LocalDate.now());
            ashwinsCanteen.addDish(chips);


            // Create dishes for metro cafe canteen using helper method
            Dish panipuri = new Dish("Pani Puri", "Pani Puri with best filling", "Indian", 40.00, 5);
            panipuri.setAvailableDate(LocalDate.now());
            metroCafeCanteen.addDish(panipuri);
            

            // Save the canteens with their dishes (cascade will save dishes automatically)
            canteenRepository.save(mainCanteen);
            canteenRepository.save(rishabsCanteen);
            canteenRepository.save(metroCafeCanteen);
            canteenRepository.save(ashwinsCanteen);
            canteenRepository.save(snowCubeCanteen);

            System.out.println("Sample data loaded successfully!");
        }
    }
}