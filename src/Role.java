import java.util.ArrayList;
import java.util.HashMap;


public class Role {


    private String name1;
    private String name2;
    private String role;
    private HashMap<String, ArrayList<String>> parent=new HashMap<String, ArrayList<String>>(); 

    public Role(String name1, String name2, String role) {
        this.name1 = name1;
        this.name2 = name2;
        this.role = role;
        this.parent=new HashMap<String, ArrayList<String>>();
    }

    public Role() {
    }

    public String getName1() {
        return this.name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return this.name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "{" +
                " name1='" + getName1() + "'" +
                ", name2='" + getName2() + "'" +
                ", role='" + getRole() + "'" +
                "}";
    }


    public HashMap<String, ArrayList<String>> getParent(ArrayList<Role> roles)
	{
	    for (Role s:roles)
	    {
	        if(s.getRole().equalsIgnoreCase("father")||s.getRole().equalsIgnoreCase("mother"))
	        {
	            
	            parent.put(s.getName1(), new ArrayList<String>());
	            
	            for(Role f:roles){
	                if(f.getName1().equalsIgnoreCase(s.getName1())&&(f.getRole().equalsIgnoreCase("father")||f.getRole().equalsIgnoreCase("mother"))){
	            parent.get(s.getName1()).add(f.getName2());

	            
	                }
	            }
	        }
	       
	        
	    }
	    
	    return parent;
	}

    public static HashMap<String, String> getCouple(ArrayList<Role> role) {
        HashMap<String, String> couples = new HashMap<String, String>();
        for (Role s : role) {
            if (s.getRole().equalsIgnoreCase("husband") || s.getRole().equalsIgnoreCase("wife")) {
                couples.put(s.getName1(), s.getName2());
            }
        }
        return couples;
    }
	
}
    


