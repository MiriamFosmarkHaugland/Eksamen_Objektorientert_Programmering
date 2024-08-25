package Menu;

import Classes.Finds.Coin;
import Classes.Finds.Find;
import Classes.Finds.Jewelry;
import Classes.Finds.Weapon;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
    private static final Scanner input = new Scanner(System.in);

    public static void showMenu() {
        System.out.println();
        System.out.println("a. See information about all the items found so far");
        System.out.println("b. See information about all items older than <year> (You choose a year yourself)");
        System.out.println("c. See total number of all items registered");
        System.out.println("d. Exit the program");

        System.out.print("-->");
        String userInput = input.nextLine();

        // Java switch specifically the use of default
        //https://www.w3schools.com/java/java_switch.asp
        switch (userInput.toLowerCase()) {
            case "a" :
                ArrayList<Coin> coins = Coin.getAll();
                System.out.println("Here are all the coins and their information: ");
                System.out.println("---");
                for (Coin coin : coins) {
                    System.out.println(STR."Id: \{coin.getId()}, Finding location: \{coin.getFindingLocation()}, Discoverer: \{coin.getDiscoverer().getName()}, Discovery date: \{coin.getDiscoveryDate()}, Assumed date of origin: \{coin.getAssumedDateOfOrigin()}, Museum: \{coin.getMuseum() != null ? coin.getMuseum().getName() : "Not in a museum"}, Diameter \{coin.getDiameter()}, Metal: \{coin.getMetal()}.");
                }
                ArrayList<Weapon> weapons = Weapon.getAll();
                System.out.println("---");
                System.out.println("Here are all the weapons and their information: ");
                System.out.println("---");
                for (Weapon weapon : weapons) {
                    // Ternary operator, using this to short and simply check if museum is null (the item is not in any museum), if there is no museum, we write that, otherwise show the museums name.
                    //https://www.w3schools.com/java/java_conditions_shorthand.asp
                    System.out.println(STR."Id: \{weapon.getId()}, Finding location: \{weapon.getFindingLocation()}, Discoverer: \{weapon.getDiscoverer().getName()}, Discovery date: \{weapon.getDiscoveryDate()}, Assumed date of origin: \{weapon.getAssumedDateOfOrigin()}, Museum: \{weapon.getMuseum() != null ? weapon.getMuseum().getName() : "Not in a museum"}, Diameter \{weapon.getType()}, Metal: \{weapon.getMaterial()}, Vekt: \{weapon.getWeight()}.");
                }
                ArrayList<Jewelry> jewelries = Jewelry.getAll();
                System.out.println("---");
                System.out.println("Here are all the jewelries and their information: ");
                System.out.println("---");
                for (Jewelry jewelry : jewelries) {
                    System.out.println(STR."Id: \{jewelry.getId()}, Finding location: \{jewelry.getFindingLocation()}, Discoverer: \{jewelry.getDiscoverer().getName()}, Discovery date: \{jewelry.getDiscoveryDate()}, Assumed date of origin: \{jewelry.getAssumedDateOfOrigin()}, Museum: \{jewelry.getMuseum() != null ? jewelry.getMuseum().getName() : "Not in a museum"}, Diameter \{jewelry.getType()}, Metal: \{jewelry.getExpectedValue()}, Vekt: \{jewelry.getFileName()}.");
                }
                showMenu();
                break;
            case "b" :
                System.out.println("---");
                System.out.print("Type in a year you wish to look at: ");
                userInput = input.nextLine();
                int userInputNumber = 0;
                try {
                    userInputNumber = Integer.parseInt(userInput);
                } catch (NumberFormatException e) {
                    System.out.println();
                    System.out.println("You must enter a valid number, please try again");
                    showMenu();
                }
                ArrayList<Coin> coinsFromSpecificYears = Coin.getSpecificYears(userInputNumber);
                System.out.println("---");
                System.out.println(STR."Here are all the coins from \{userInput} and later: ");
                System.out.println("---");
                for (Coin coin : coinsFromSpecificYears) {
                    System.out.println(STR."Id: \{coin.getId()}, Finding location: \{coin.getFindingLocation()}, Discoverer: \{coin.getDiscoverer().getName()}, Discovery date: \{coin.getDiscoveryDate()}, Assumed date of origin: \{coin.getAssumedDateOfOrigin()}, Museum: \{coin.getMuseum() != null ? coin.getMuseum().getName() : "Not in a museum"}, Diameter \{coin.getDiameter()}, Metal: \{coin.getMetal()}.");
                }
                ArrayList<Weapon> weaponsFromSpecificYears = Weapon.getSpecificYears(userInputNumber);
                System.out.println("---");
                System.out.println(STR."Here are all the weapons from \{userInput} and later: ");
                System.out.println("---");
                for (Weapon weapon : weaponsFromSpecificYears) {
                    System.out.println(STR."Id: \{weapon.getId()}, Finding location: \{weapon.getFindingLocation()}, Discoverer: \{weapon.getDiscoverer().getName()}, Discovery date: \{weapon.getDiscoveryDate()}, Assumed date of origin: \{weapon.getAssumedDateOfOrigin()}, Museum: \{weapon.getMuseum() != null ? weapon.getMuseum().getName() : "Not in a museum"}, Diameter \{weapon.getType()}, Metal: \{weapon.getMaterial()}, Vekt: \{weapon.getWeight()}.");
                }
                ArrayList<Jewelry> jewelriesFromSpecificYears = Jewelry.getSpecificYears(userInputNumber);
                System.out.println("---");
                System.out.println(STR."Here are all the jewelries from \{userInput} and later: ");
                System.out.println("---");
                for (Jewelry jewelry : jewelriesFromSpecificYears) {
                    System.out.println(STR."Id: \{jewelry.getId()}, Finding location: \{jewelry.getFindingLocation()}, Discoverer: \{jewelry.getDiscoverer().getName()}, Discovery date: \{jewelry.getDiscoveryDate()}, Assumed date of origin: \{jewelry.getAssumedDateOfOrigin()}, Museum: \{jewelry.getMuseum() != null ? jewelry.getMuseum().getName() : "Not in a museum"}, Diameter \{jewelry.getType()}, Metal: \{jewelry.getExpectedValue()}, Vekt: \{jewelry.getFileName()}.");
                }
                showMenu();
                break;
            case "c" :
                System.out.println("---");
                System.out.println(STR."There is a total of \{Find.getTotalNumberOfItems()} items collected so far!");
                showMenu();
                break;
            case "d" :
                System.exit(0);
            default:
                System.out.println();
                System.out.println("Use a valid menu option");
                showMenu();
                break;
        }
    }
}
