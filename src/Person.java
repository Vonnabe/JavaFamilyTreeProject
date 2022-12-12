import java.util.HashSet;

public class Person {

private String name;
private String fyllo;
private Person sizigos;
HashSet<Person> goneis;
HashSet<Person>paidia;


    public Person(String name, String fyllo) {
        this.name = name;
        this.fyllo = fyllo;
        this.sizigos=null;
        this.goneis=new HashSet<Person>();
        this.paidia=new HashSet<Person>();
    }


    public Person() {
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFyllo() {
        return this.fyllo;
    }

    public void setFyllo(String fyllo) {
        this.fyllo = fyllo;
    }


    @Override
    public String toString() {
        return "{" +
            " name='" + getName() + "'" +
            ", fyllo='" + getFyllo() + "'" +
            "}";
    }


    public static void add(Person p) {
    }



}
