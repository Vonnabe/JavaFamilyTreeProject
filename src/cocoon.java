import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class cocoon {

    public static ArrayList<Person> person = new ArrayList<Person>();
    public static ArrayList<Role> role = new ArrayList<Role>();
    public static HashMap<String, ArrayList<String>> parent = new HashMap<String, ArrayList<String>>();
    public static HashMap<String, String> couples = new HashMap<String, String>();
    public static int extras = 0;

    public static void main(String[] args) {
        Role prpar = new Role();
        parent = prpar.getParent(role);
        Role coupler = new Role();
        couples = coupler.getCouple(role);
        printArraylist();
        runMenu();

    }


    public static void runMenu() {
        int choice;
        Scanner readerIn = new Scanner(System.in);

        do {
            String menu = "please selext a function from the menu below \n " +
                    "1. load family tree from csv file \n" +
                    "2. sort the names and write to file \n" +
                    "3. find relationship between family members\n" +
                    "4. exit\n" +
                    "\nenter your choice\n";
            System.out.println(menu);
            choice = readerIn.nextInt();
            switch (choice) {
                case 1:
                    loadCSVTree();
                    break;
                case 2:
                    sortNameAndWriteToFile();
                    break;
                case 3:
                    findRelationship(parent, couples);
                    break;
            }
        } while (choice != 4);
    }

    public static void loadCSVTree() {
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("tree.csv"));
            try {
                while ((line = br.readLine()) != null) {
                    processInputLine(line);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void sortNameAndWriteToFile() {
        ArrayList<String> alphabet = new ArrayList<String>();
        ArrayList<String> gender = new ArrayList<String>();

        for (Person p : person) {
            alphabet.add(p.getName());

            for (Person r : person) {
                gender.add(r.getFyllo());
            }
        }
        Collections.sort(alphabet);

        for (String a : alphabet) {
            for (Person r1 : person) {
                if (r1.getName().equals(a)) {
                    System.out.println(a + " - " + r1.getFyllo());
                    PrintWriter writer;
                    try {
                        writer = new PrintWriter("family.txt");

                        for (Person p : person)
                            writer.println("Name: " + p.getName() + " Gender: " + p.getFyllo());
                        writer.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }

    public static void processInputLine(String line) {
        String[] data = line.split(",");
        String name1, name2, fyllo, roles;
        Person p = new Person();
        Role r = new Role();
        switch (data.length) {
            case 2:
                name1 = data[0].trim();
                fyllo = data[1].trim();
                p.setName(name1);
                p.setFyllo(fyllo);
                person.add(p);
                break;
            case 3:
                name1 = data[0].trim();
                roles = data[1].trim();
                name2 = data[2].trim();
                r.setName1(name1);
                r.setRole(roles);
                r.setName2(name2);
                role.add(r);
                break;
            default:
                System.out.println("not a valid choice\n");

        }

        Role prpar = new Role();
        parent = prpar.getParent(role);
        Role coupler = new Role();
        couples = coupler.getCouple(role);

    }

    public static void printArraylist() {
        for (Person p : person) {
            System.out.println(p);
        }

    }

    public static void findRelationship(HashMap<String, ArrayList<String>> parent, HashMap<String, String> couples) {

        Scanner input = new Scanner(System.in);
        String name1;
        String name2;
        String genderF = "";
        boolean nameFound = false;
        int i = 0;
        extras = 0;

        do {
            System.out.println("Enter first name: ");

            name1 = input.nextLine();
            for (Person p : person) {
                if (p.getName().equals(name1)) {
                    genderF = p.getFyllo();
                    i++;
                    nameFound = true;
                }
            }
            if (nameFound == false) {
                while (nameFound == false) {
                    System.out.println("Name doesn't exist, try again.\n");
                    System.out.println("Enter first name: ");
                    name1 = input.nextLine();
                    for (Person p : person) {
                        if (p.getName().equals(name1)) {
                            genderF = p.getFyllo();
                            nameFound = true;
                        }
                    }
                }
            }
            nameFound = false;

            System.out.println("Enter second name: ");

            name2 = input.nextLine();
            if (name2.equals(name1)) {
                do {
                    System.out.println("Please search for a different person.\n");
                    System.out.println("Enter second name: ");
                    name2 = input.nextLine();
                } while (name2.equals(name1));
            }
            for (Person p : person) {
                if (p.getName().equals(name2)) {

                    nameFound = true;
                }
            }
            if (nameFound == false) {
                System.out.println("Name doesn't exist, try again.\n");
                System.out.println("Enter second name: ");
                name2 = input.nextLine();
                for (Person p : person) {
                    if (p.getName().equals(name2)) {
                        if (name2.equals(name1)) {
                            do {
                                System.out.println("Please search for a different person.\n");
                                System.out.println("Enter second name: ");
                                name2 = input.nextLine();
                            } while (name2.equals(name1));
                        }
                        nameFound = true;
                    }
                }
            }

            i++;
        } while (i == 0);

        if (fatherOrMother(name1, name2, parent, couples)) {
            if (genderF.equals("man")) {
                System.out.println("\n" + name1 + " is " + name2 + "'s Father" + "\n");
            } else {
                System.out.println("\n" + name1 + " is " + name2 + "'s Mother" + "\n");
            }
        } else if (sonOrDaughter(name1, name2, parent, couples)) {
            if (genderF.equals("man")) {
                System.out.println("\n" + name1 + " is " + name2 + "'s Son" + "\n");
            } else {
                System.out.println("\n" + name1 + " is " + name2 + "'s Daughter" + "\n");
            }
        }

        else if (husbandOrWife(name1, name2, couples)) {
            if (genderF.equals("man")) {
                System.out.println("\n" + name1 + " is " + name2 + "'s Husband" + "\n");
            } else {
                System.out.println("\n" + name1 + " is " + name2 + "'s Wife" + "\n");
            }
        } else if (brotherOrSister(name1, name2, parent, couples)) {
            if (genderF.equals("man")) {
                System.out.println("\n" + name1 + " is " + name2 + "'s Brother" + "\n");
            } else {
                System.out.println("\n" + name1 + " is " + name2 + "'s Sister" + "\n");
            }
        } else if (grandfatherOrGrandmother(name1, name2, parent)) {
            if (genderF.equals("man")) {
                System.out.println(name1 + " is " + name2 + "'s Grandfather" + "\n");
            } else {
                System.out.println(name1 + " is " + name2 + "'s Grandmother" + "\n");
            }
        } else if (cousin(name1, name2, parent)) {
            System.out.println(name1 + " is " + name2 + "'s cousin" + "\n");
        } else if (uncleOrAunt(name1, name2, parent, couples)) {
            if (genderF.equals("man")) {
                System.out.println(name1 + " is " + name2 + "'s Uncle" + "\n");
            } else {
                System.out.println(name1 + " is " + name2 + "'s Aunt" + "\n");
            }
        } else if (nephew(name1, name2, parent, couples)) {
            if (genderF.equals("man")) {
                System.out.println(name1 + " is " + name2 + "'s Nephew" + "\n");
            } else {
                System.out.println(name1 + " is " + name2 + "'s Niece" + "\n");
            }
        } else if (grandsonOrGrandaughter(name1, name2, parent)) {
            if (genderF.equals("man")) {
                System.out.println(name1 + " is " + name2 + "'s Grandson" + "\n");
            } else {
                System.out.println(name1 + " is " + name2 + "'s Grandaughter" + "\n");
            }
        } else {
            if (extras == 0) {
                System.out.println("They aren't Related." + "\n");
            }
        }
    }

    public static boolean fatherOrMother(String s, String t, HashMap<String, ArrayList<String>> parent,
            HashMap<String, String> couples) {
        String step;
        Set<String> setOfKeySet = parent.keySet();

        for (String key : setOfKeySet) {
            if (key.equals(s)) {
                for (String kid : parent.get(key)) {
                    if (kid.equals(t)) {
                        return true;
                    }
                }
            }

        }

        // ---------test---------------
        for (String i : couples.keySet()) {
            if (i.equals(s)) {
                step = couples.get(s);
                for (String newKey : setOfKeySet) {
                    if (newKey.equals(step)) {
                        for (String newKid : parent.get(newKey)) {
                            if (newKid.equals(t)) {
                                for (Person p : person) {

                                    if (p.getName().equals(s)) {
                                        if (p.getFyllo().equals("man")) {
                                            System.out.println(s + " is " + t + "'s Stepfather" + "\n");
                                        } else {
                                            System.out.println(s + " is " + t + "'s Stepmother" + "\n");
                                        }
                                        extras++;
                                        return false;
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        // ---------test--------------
        return false;
    }

    public static boolean sonOrDaughter(String s, String t, HashMap<String, ArrayList<String>> parent,
            HashMap<String, String> couples) {
        String step;
        Set<String> setOfKeySet = parent.keySet();

        for (String key : setOfKeySet) {
            for (String kid : parent.get(key)) {
                if (kid.equals(s)) {
                    if (key.equals(t)) {
                        return true;
                    }
                }
            }
        }

        for (String i : couples.keySet()) {
            if (i.equals(t)) {
                step = couples.get(t);
                for (String newKey : setOfKeySet) {
                    if (newKey.equals(step)) {
                        for (String newKid : parent.get(newKey)) {
                            if (newKid.equals(s)) {
                                for (Person p : person) {

                                    if (p.getName().equals(s)) {
                                        if (p.getFyllo().equals("man")) {
                                            System.out.println(s + " is " + t + "'s Stepson" + "\n");
                                        } else {
                                            System.out.println(s + " is " + t + "'s Stepdaughter" + "\n");
                                        }
                                        extras++;
                                        return false;
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean husbandOrWife(String s, String t, HashMap<String, String> couples) {
        for (String i : couples.keySet()) {

            if (i.equals(s)) {
                if (couples.get(s).equals(t)) {
                    return true;
                }
            }

        }
        return false;
    }

    public static boolean brotherOrSister(String s, String t, HashMap<String, ArrayList<String>> parent,
            HashMap<String, String> couples) {
        String step;
        Set<String> setOfKeySet = parent.keySet();

        for (String key : setOfKeySet) {
            for (String kid : parent.get(key)) {
                if (kid.equals(s)) {
                    for (String otherKid : parent.get(key)) {
                        if (otherKid.equals(t)) {
                            return true;
                        }
                    }
                    // ---------------test--------------------
                    for (String i : couples.keySet()) {
                        if (i.equals(key)) {
                            step = couples.get(key);
                            for (String newKey : setOfKeySet) {
                                if (newKey.equals(step)) {
                                    for (String newKid : parent.get(newKey)) {
                                        if (newKid.equals(t)) {
                                            for (Person p : person) {

                                                if (p.getName().equals(s)) {
                                                    if (p.getFyllo().equals("man")) {
                                                        System.out.println(s + " is " + t + "'s Step-Brother" + "\n");
                                                    } else {
                                                        System.out.println(s + " is " + t + "'s Step-Sister" + "\n");
                                                    }
                                                    extras++;
                                                    return false;
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // -------------test-----------------

                }
            }
        }
        return false;
    }

    public static boolean grandsonOrGrandaughter(String s, String t,
            HashMap<String, ArrayList<String>> parent) {
        String father;
        Set<String> setOfKeySet = parent.keySet();

        for (String key : setOfKeySet) {
            for (String kid : parent.get(key)) {
                if (kid.equals(s)) {
                    father = key;
                    for (String newKey : setOfKeySet) {
                        for (String newKid : parent.get(newKey)) {
                            if (newKid.equals(father)) {
                                if (newKey.equals(t)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean grandfatherOrGrandmother(String s, String t,
            HashMap<String, ArrayList<String>> parent) {
        String father;
        Set<String> setOfKeySet = parent.keySet();

        for (String key : setOfKeySet) {

            if (key.equals(s)) {
                for (String kid : parent.get(key)) {
                    father = kid;
                    for (String newKey : setOfKeySet) {
                        if (newKey.equals(father)) {
                            for (String newKid : parent.get(newKey)) {
                                if (newKid.equals(t)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean cousin(String s, String t, HashMap<String, ArrayList<String>> parent) {
        String uncle, anotherUncle, grandpa, theAunt, theUncle, theGrandpa, theFather, newUncle;
        Set<String> setOfKeySet = parent.keySet();

        for (String key : setOfKeySet) {
            for (String kid : parent.get(key)) {
                if (kid.equals(s)) {
                    uncle = key;
                    for (String newKey : setOfKeySet) {
                        for (String newKid : parent.get(newKey)) {
                            if (newKid.equals(t)) {
                                anotherUncle = newKey;
                                for (String lastKey : setOfKeySet) {
                                    for (String lastKid : parent.get(lastKey)) {
                                        if (lastKid.equals(uncle)) {
                                            for (String otherKid : parent.get(lastKey)) {
                                                if (otherKid.equals(anotherUncle)) {
                                                    return true;
                                                }
                                                // -----------------------test-------------------
                                                else {
                                                    for (String k : couples.keySet()) {
                                                        if (k.equals(anotherUncle)) {
                                                            newUncle = couples.get(anotherUncle);
                                                            for (String lastKid2 : parent.get(lastKey)) {
                                                                if (lastKid2.equals(newUncle)
                                                                        && lastKid2.equals(uncle) == false) {
                                                                    return true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                // ---------------------test-------------------
                                            }

                                        }
                                    }
                                }

                            }
                        }
                    }

                }
            }
        }
        // ---------------------------------test----------------------------
        for (String key1 : setOfKeySet) {
            for (String kid1 : parent.get(key1)) {
                if (kid1.equals(s)) {

                    theAunt = key1;

                    for (String i : couples.keySet()) {
                        if (i.equals(theAunt)) {
                            theUncle = couples.get(theAunt);
                            for (String newKey1 : setOfKeySet) {
                                for (String newKid1 : parent.get(newKey1)) {
                                    if (newKid1.equals(theUncle)) {
                                        theGrandpa = newKey1;
                                        for (String nextKey1 : setOfKeySet) {
                                            if (nextKey1.equals(theGrandpa)) {
                                                for (String nextKid1 : parent.get(nextKey1)) {
                                                    if (nextKid1.equals(theUncle) == false) {
                                                        theFather = nextKid1;
                                                        for (String finalKey1 : setOfKeySet) {
                                                            if (finalKey1.equals(theFather)) {
                                                                for (String finalKid1 : parent.get(finalKey1)) {
                                                                    if (finalKid1.equals(t)) {
                                                                        return true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                }
            }
        }
        // ----------------------------------------------------------test---------------------------------
        return false;
    }

    public static boolean uncleOrAunt(String s, String t, HashMap<String, ArrayList<String>> parent,
            HashMap<String, String> couples) {
        String grandpa, father, possibleUncle, possibleContigentUncle, possibleContigentGranpa;
        String possibleStep, possibleMom, possibleContigentUncle1, possibleContigentGranpa1, possibleParent,
                possibleMom1;
        Set<String> setOfKeySet = parent.keySet();

        for (String key : setOfKeySet) {
            for (String kid : parent.get(key)) {
                if (kid.equals(s)) {
                    grandpa = key;
                    for (String newKey : setOfKeySet) {
                        if (newKey.equals(grandpa)) {
                            for (String newKid : parent.get(newKey)) {
                                if (newKid.equals(s) == false) {
                                    father = newKid;
                                    for (String finalKey : setOfKeySet) {
                                        if (finalKey.equals(father)) {
                                            for (String finalKid : parent.get(finalKey)) {
                                                if (finalKid.equals(t)) {
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (String i : couples.keySet()) {

                        if (i.equals(s)) {
                            possibleUncle = couples.get(s);
                            for (String key2 : setOfKeySet) {
                                for (String kid2 : parent.get(key2)) {
                                    if (kid2.equals(possibleUncle)) {
                                        grandpa = key2;
                                        for (String newKey : setOfKeySet) {
                                            if (newKey.equals(grandpa)) {
                                                for (String newKid : parent.get(newKey)) {
                                                    if (newKid.equals(possibleUncle) == false) {
                                                        father = newKid;
                                                        for (String finalKey : setOfKeySet) {
                                                            if (finalKey.equals(father)) {
                                                                for (String finalKid : parent.get(finalKey)) {
                                                                    if (finalKid.equals(t)) {
                                                                        return true;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // --------apo edo kai katw contigent----------------
        for (String key1 : setOfKeySet) {

            for (String newKid1 : parent.get(key1)) {
                if (newKid1.equals(s)) {
                    possibleContigentUncle = newKid1;
                    possibleContigentGranpa = key1;
                    for (String newKey1 : setOfKeySet) {
                        for (String newKid2 : parent.get(possibleContigentGranpa)) {
                            if (newKid2.equals(possibleContigentUncle) == false) {
                                possibleStep = newKid2;
                                for (String i : couples.keySet()) {
                                    if (i.equals(possibleStep)) {
                                        possibleMom = couples.get(possibleStep);
                                        for (String finalKey1 : setOfKeySet) {
                                            if (finalKey1.equals(possibleMom))
                                                for (String finalKid1 : parent.get(finalKey1)) {
                                                    if (finalKid1.equals(t)) {
                                                        for (Person p : person) {

                                                            if (p.getName().equals(s)) {
                                                                if (p.getFyllo().equals("man")) {
                                                                    System.out.println(
                                                                            s + " is " + t + "'s Contigent Uncle" + "\n");
                                                                } else {
                                                                    System.out.println(
                                                                            s + " is " + t + "'s Contigent Aunt" + "\n");
                                                                }
                                                                extras++;
                                                                return false;
                                                            }

                                                        }

                                                    }
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

            }
        }

        // ------------------test---------------
        for (String j : couples.keySet()) {
            if (j.equals(s)) {
                possibleContigentUncle1 = couples.get(s);
                for (String key3 : setOfKeySet) {
                    for (String kid3 : parent.get(key3)) {
                        if (kid3.equals(possibleContigentUncle1)) {
                            possibleContigentGranpa1 = key3;
                            for (String key4 : parent.get(possibleContigentGranpa1)) {
                                if (key4.equals(possibleContigentUncle1) == false) {
                                    possibleParent = key4;
                                    for (String l : couples.keySet()) {
                                        if (l.equals(possibleParent)) {
                                            possibleMom1 = couples.get(possibleParent);
                                            {
                                                for (String lastKey4 : setOfKeySet) {
                                                    if (lastKey4.equals(possibleMom1)) {
                                                        for (String lastKid4 : parent.get(lastKey4)) {
                                                            if (lastKid4.equals(t)) {
                                                                for (String lastKid5 : parent.get(possibleParent)) {
                                                                    if (lastKid5.equals(t) == false) {
                                                                        for (Person p : person) {

                                                                            if (p.getName().equals(s)) {
                                                                                if (p.getFyllo().equals("man")) {
                                                                                    System.out.println(s + " is " + t
                                                                                            + "'s Contigent Uncle" + "\n");
                                                                                } else {
                                                                                    System.out.println(s + " is " + t
                                                                                            + "'s Contigent Aunt" + "\n");
                                                                                }
                                                                                extras++;
                                                                                return false;
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // ---------------end of test----------------------------

        return false;
    }

    public static boolean nephew(String s, String t, HashMap<String, ArrayList<String>> parent,
            HashMap<String, String> couples) {
        String father, grandpa, possibleUncle, theParent, theGrandparent, possibleStep, theUncle;
        Set<String> setOfKeySet = parent.keySet();

        for (String key : setOfKeySet) {
            for (String kid : parent.get(key)) {
                if (kid.equals(s)) {
                    father = key;

                    for (String newKey : setOfKeySet) {
                        for (String newKid : parent.get(newKey)) {
                            if (newKid.equals(father)) {
                                grandpa = newKey;

                                {
                                    for (String finalKey : setOfKeySet) {
                                        if (finalKey.equals(grandpa)) {
                                            for (String finalKid : parent.get(finalKey)) {
                                                if (finalKid.equals(t) && finalKey.equals(father) == false) {
                                                    return true;
                                                } else {
                                                    possibleUncle = finalKid;

                                                    for (String i : couples.keySet()) {

                                                        if (i.equals(possibleUncle)
                                                                && possibleUncle.equals(father) == false) {
                                                            if (couples.get(possibleUncle).equals(t)) {
                                                                return true;
                                                            }
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (String key1 : setOfKeySet) {
            for (String kid1 : parent.get(key1)) {
                if (kid1.equals(s)) {
                    theParent = key1;
                    for (String j : couples.keySet()) {
                        if (j.equals(theParent)) {
                            possibleStep = couples.get(theParent);
                            for (String newKey1 : setOfKeySet) {
                                for (String newKid1 : parent.get(newKey1)) {
                                    if (newKid1.equals(possibleStep)) {
                                        theGrandparent = newKey1;
                                        for (String finalKey1 : setOfKeySet) {
                                            if (finalKey1.equals(theGrandparent)) {
                                                for (String finalKid1 : parent.get(finalKey1)) {
                                                    if (finalKid1.equals(possibleStep) == false) {
                                                        if (finalKid1.equals(t)) {
                                                            for (Person p : person) {

                                                                if (p.getName().equals(s)) {
                                                                    if (p.getFyllo().equals("man")) {
                                                                        System.out.println(
                                                                                s + " is " + t + "'s Contigent Nephew" + "\n");
                                                                    } else {
                                                                        System.out.println(
                                                                                s + " is " + t + "'s Contigent Niece" + "\n");
                                                                    }
                                                                    extras++;
                                                                    return false;
                                                                }

                                                            }
                                                        } else {
                                                            theUncle = finalKid1;
                                                            for (String f : couples.keySet()) {
                                                                if (f.equals(theUncle)) {
                                                                    if (couples.get(theUncle).equals(t)) {
                                                                        for (Person p : person) {

                                                                            if (p.getName().equals(s)) {
                                                                                if (p.getFyllo().equals("man")) {
                                                                                    System.out.println(s + " is " + t
                                                                                            + "'s Contigent Nephew" + "\n");
                                                                                } else {
                                                                                    System.out.println(s + " is " + t
                                                                                            + "'s Contigent Niece" + "\n");
                                                                                }
                                                                                extras++;
                                                                                return false;
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }
}
