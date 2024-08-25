import Classes.Finds.Find;
import Classes.Museum;
import Classes.Person;
import Menu.Menu;

public class Main {
    public static void main(String[] args) {
        Person.readPersonFile();
        Museum.readMuseumFile();
        Find.readFindFile();
        Menu.showMenu();
    }
}