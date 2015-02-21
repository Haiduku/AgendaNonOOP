import java.io.*;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SerializationUtils;


/**
 * Created by condor on 10/02/15.
 * FastTrackIT, 2015
 *
 * DEMO ONLY PURPOSES, IT MIGHT CONTAINS INTENTIONALLY ERRORS OR ESPECIALLY BAD PRACTICES
 */
public class AgendaAsPrimitiveArray {

    private final static int MAX_AGENDA_ITEMS=10;
    private Item[] agenda = new Item[MAX_AGENDA_ITEMS];
    private int currentAgendaIndex;

    public static void main(String[] args) {
        System.out.println("AgendaTa versiunea 1.0");
        AgendaAsPrimitiveArray m = new AgendaAsPrimitiveArray();
        m.readFromFile();

        do {
            m.printMenu();
            int option = m.readMenuOption();
            switch (option) {
                case 1:
                    m.listAgenda();
                    break;
                case 2:
                    m.searchAgendaAndDisplay();
                    break;
                case 3:
                    m.createItem();
                    break;
                case 4:
                    m.updateItem();
                    break;
                case 5:
                    m.deleteItem();
                    break;
                case 6:
                    m.readFromFile();
                    break;
                case 7:
                    m.writeToFile();
                    break;
                case 8:
                    m.listSortedAgenda();
                    break;
                case 9:
                    m.exitOption();
                    break;
                case 10:
                    m.findLongestName();
                    break;
                case 11:
                    m.searchAgendaForPartialMatchAndDisplay();
                    break;
                case 12:
                    m.statistics();
                    break;
                case 13:
                    m.deleteAll();
                    break;
                default:
                    m.defaultOption();
                    break;
            }
        } while (true);

    }

    private void createItem() {
        boolean wasInserted = false;
        HandleKeyboard handleKeyboard = new HandleKeyboard().invokeItem();
        String name = handleKeyboard.getName();
        String phone = handleKeyboard.getPhone();
        String firstName = handleKeyboard.getFirstName();

        Item item = new Item();
        item.setName(name);
        item.setFirstName(firstName);
        item.setPhoneNumber(phone);

        //Dupa ce citesc din fisier, vreau ca orice adaugare sa vina peste citirea din fisier
        for(int i= 0; i < MAX_AGENDA_ITEMS; i++ ){
            if(agenda[i] == null){
                currentAgendaIndex = i;
                break;
            }
        }


        if(currentAgendaIndex<MAX_AGENDA_ITEMS) {
            agenda[currentAgendaIndex] = item;
            currentAgendaIndex++;
             wasInserted = true;
        }
        else {
            //try to find null slots and add th item in the first null slot
            System.out.println("debug: try to find slots");
            for (int i = 0; i < agenda.length; i++) {
                if (agenda[i] == null) { // found one
                    agenda[i]=item;
                    wasInserted=true;
                    System.out.println("debug: slot found, inserted ok");
                    break;
                }
            }
        }
        if(wasInserted)
            System.out.println("Item was added");
        else
            System.out.println("Memory full! The item cannot be added");

    writeToFile();
    }



    private void updateItem() {
        //search and if found do an update
        int indexItem = searchAgenda();
        if (indexItem != -1) { //found
            HandleKeyboard handleKeyboard = new HandleKeyboard().invokeItem();
            String name = handleKeyboard.getName(); // so we can change the name as well
            String firstName = handleKeyboard.getFirstName();
            String phone = handleKeyboard.getPhone();


            Item i = new Item();
            i.setName(name);
            i.setFirstName(firstName);
            i.setPhoneNumber(phone);
            agenda[indexItem] = i;
            System.out.println("Item was updated!");
        } else {
            System.out.println("You cannot update an item that does not exists in agenda!");
        }

        writeToFile();
    }


    private void deleteItem() {
        //search and if found delete it and null the position
        int indexItem = searchAgenda();
        if (indexItem != -1) { //found
            agenda[indexItem] = null;
            System.out.println("Item was deleted!");
        } else {
            System.out.println("Item not found, so you cannot delete it!");
        }

        writeToFile();
    }


    /* returns the index where the name was found or -1 if the name is not in the agenda*/
    private int searchAgenda() {
        HandleKeyboard handleKeyboard = new HandleKeyboard().invokeItemName();
        String name = handleKeyboard.getName();
        int indexWhereItWasFound = -1;

        // for (Item anAgenda : agenda) might not work here , we need the index so I keep the original form of for
        for (int i = 0; i < agenda.length; i++) {
            if (agenda[i] != null) {
                Item item = agenda[i];
                String nameInAgenda = item.getName();
                if (name.equals(nameInAgenda)) {
                    indexWhereItWasFound = i;
                    break;
                }
            }
        }
        return indexWhereItWasFound;
    }




    private Item[] searchAgendaForPartialMatch() {
        HandleKeyboard handleKeyboard = new HandleKeyboard().invokeItemName();
        String name = handleKeyboard.getName();
            Item[] partialMatches = new Item[MAX_AGENDA_ITEMS];
        int j =0;
        // for (Item anAgenda : agenda) might not work here , we need the index so I keep the original form of for
        for (int i = 0; i < agenda.length; i++) {
            if (agenda[i] != null) {
                Item item = agenda[i];
                String nameInAgenda = item.getName();
                if (nameInAgenda.toLowerCase().contains(name)) {
                    partialMatches[j] = item;
                    j++;
                }
            }
        }
        return partialMatches;
    }





    /* returns the index where the name was found or -1 if the name is not in the agenda */
    private void searchAgendaAndDisplay() {
        int index = searchAgenda();
        if (index != -1) { //found
            Item item = agenda[index];
            String name = item.getName();
            String firstName = item.getFirstName();
            String phoneNumber = item.getPhoneNumber();
            System.out.println("Name:" + name);
            System.out.println("First Name" + firstName);
            System.out.println("Phone Number:" + phoneNumber);
        } else {
            System.out.println("This name does not exists in agenda!");
        }
    }



    private void searchAgendaForPartialMatchAndDisplay() {
        Item[] partialMatches = searchAgendaForPartialMatch();
        if (partialMatches != null) { //found
            for( int i=0; i<partialMatches.length; i++) {
                Item item = partialMatches[i];
                if(item != null) {
                    String name = item.getName();
                    String firstName = item.getFirstName();
                    String phoneNumber = item.getPhoneNumber();
                    System.out.println("Name:" + name);
                    System.out.println("First Name:" + firstName);
                    System.out.println("Phone Number:" + phoneNumber);
                }
            }
        } else {
            System.out.println("You are stupid!");
        }
    }





    private void listAgenda() {

        int emptySpaces = 0;
        //System.out.println("agenda.length = " + agenda.length); //sout tab, or soutv tab, or soutm tab
        System.out.println("Your Agenda:");
        for (Item anAgenda : agenda) {
            if (anAgenda != null) {
                String name = anAgenda.getName();
                String firstName = anAgenda.getFirstName();
                String telephone = anAgenda.getPhoneNumber();
                System.out.println("Name: "+name + "; First Name: " +firstName+ "; Phone: " + telephone);
            } else {
                emptySpaces++;
            }
        }
       // System.out.println("empty spaces:" + emptySpaces);
        System.out.println("---------------");
    }

    private void findLongestName() {
        System.out.println("The longest name is: ");
        Item longest = new Item();
        longest = agenda[0];
        for ( int i = 1; i < agenda.length; i++) {
            if (agenda[i] != null) {
                if (longest.getName().length() < agenda[i].getName().length()) {
                    longest = agenda[i];
                }
            }
        }
        System.out.println(longest.getName());
    }

    private void listSortedAgenda() {
        for (int i = (agenda.length - 1); i >= 0; i--) {
            for (int j = 1; j <= i; j++) {
               if(agenda[j-1] != null && agenda[j] != null) {
                   if (agenda[j - 1].getName().compareTo(agenda[j].getName()) > 0) {
                       Item temp = agenda[j - 1];
                       agenda[j - 1] = agenda[j];
                       agenda[j] = temp;
                   }
               }
            }
        }

        listAgenda();

    }
    private void printMenu() {
        System.out.println("1. List");
        System.out.println("2. Search");
        System.out.println("3. Create");
        System.out.println("4. Update");
        System.out.println("5. Delete");
        System.out.println("6. Read From File");
        System.out.println("7. Write to File");
        System.out.println("8. Sorted List");
        System.out.println("9. Exit");
        System.out.println("10. Longest Name");
        System.out.println("11. Search for partial match");
        System.out.println("12. Statistics");
        System.out.println("13. Delete all");
    }

    private void exitOption() {
        System.out.println("Bye, bye...the content not saved will now be erased");
        System.exit(0);
    }

    private void defaultOption() {
        System.out.println("This option does not exist. Pls take another option");
    }

    private int readMenuOption() {
        HandleKeyboard handleKeyboard = new HandleKeyboard().invokeOption();
        return handleKeyboard.getOption();
    }


    private void readFromFile() {

        //warning, it is going to overwrite
        HandleKeyboard handleKeyboard = new HandleKeyboard().invokeYesNo();
        String yesNo = handleKeyboard.getYesNo();
        if(yesNo.equalsIgnoreCase("Y")) {
            FileInputStream fis = null;
            ByteArrayOutputStream out = null;
            try {
                File f = new File("agenda.txt");
                fis = new FileInputStream(f);
                out = new ByteArrayOutputStream();
                IOUtils.copy(fis, out);
                byte[] data = out.toByteArray();
//                agenda = SerializationUtils.deserialize(data);
                Item[] tempAgenda = SerializationUtils.deserialize(data);
                for (int i =0; i < agenda.length; i++){
                    if(tempAgenda[i] != null){
                        agenda[i] = tempAgenda[i];
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(out);
                IOUtils.closeQuietly(fis);
            }
            System.out.println("Read from file done!");
        }

    }


    private void writeToFile() {

        FileOutputStream fwr = null;
        try {
            byte[] data = SerializationUtils.serialize(agenda);
            File f = new File("agenda.txt");
            fwr = new FileOutputStream(f);
            fwr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(fwr);
        }
        System.out.println("Write to file done!");


    }





    private class HandleKeyboard {
        private String name;
        private String phone;
        private String firstName;

        private int option;

        private String yesNo;


        public String getName() {
            return name;
        }
        public String getFirstName() { return firstName; }
        public String getPhone() {
            return phone;
        }

        public int getOption() {
            return option;
        }

        public String getYesNo() {
            return yesNo;
        }

        public HandleKeyboard invokeItem() {
            Scanner s = new Scanner(System.in);
            System.out.print("Name: ");
            name = s.nextLine();
            System.out.print("First Name:");
            firstName = s.nextLine();
            System.out.print("Phone Number: ");
            phone = s.nextLine();
            return this;
        }

        public HandleKeyboard invokeItemName() {
            Scanner s = new Scanner(System.in);
            System.out.print("Name: ");
            name = s.nextLine();
            return this;
        }

        public HandleKeyboard invokeOption() {
            Scanner s = new Scanner(System.in);
            System.out.print("Option: ");
            option = s.nextInt();
            return this;
        }

        public HandleKeyboard invokeYesNo() {
            Scanner s = new Scanner(System.in);
            System.out.print("Are you sure you want to overwrite your current content in memory ? (Y,N): ");
            yesNo = s.nextLine();
            return this;
        }
    }
    private void statistics() {
        int x = 0;
        int y = 0;
        int z = 0;
        for (int i = 0; i < agenda.length; i++){
            if(agenda[i] != null){
                x++;
            }
            else
                z++;
        }
        System.out.println("Your agenda contains " +x+ " telephone numbers");
       Item[] startsWithA = new Item[MAX_AGENDA_ITEMS];
        for (int j = 0; j < agenda.length; j++){
            Item item = agenda[j];
            if(item != null) {
                String name = item.getName();
                if (name.startsWith("A")) {
                    startsWithA[y] = item;
                    y++;
                }
            }
        }

        System.out.println(y+ " names start with the letter A");
        printPartofAgenda(startsWithA);

        System.out.println("You have "+z+ " slots left" );

    }
    private void printPartofAgenda(Item[] partOfAgenda) {
        if (partOfAgenda != null) {
            for (int i = 0; i < partOfAgenda.length; i++) {
                if (partOfAgenda[i] != null) {
                    Item item = partOfAgenda[i];
                    System.out.println(item.getName());
                    System.out.println(item.getFirstName());
                    System.out.println(item.getPhoneNumber());
                }
            }
        }
    }

    private void deleteAll() {
        System.out.println("Are you sure you want to delete all?(y/n)");
        Scanner in = new Scanner(System.in);
        String yesNo = in.nextLine();
        if (yesNo.equalsIgnoreCase("y")) {


            if (agenda != null) {
                for (int i = 0; i < agenda.length; i++) {
                    if (agenda[i] != null) {
                        agenda[i] = null;
                    }
                }
            }


            try {
                boolean deleted = false;
                File f = new File("agenda.txt");
                deleted = f.delete();
                if (deleted) {
                    System.out.println("Your agenda is now empty");
                }

            } catch (Exception e) {
                System.out.println("Try again later!");
            }
        }
    }
}

