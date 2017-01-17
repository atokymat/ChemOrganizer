package chemorganizer;

public class Element {
    
    int x, y;
    int numBonds;
    String name, letter;
    Element[] bonds = new Element[4];
    String[] bondTypes = new String[4];

    public Element(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.addBondNumber();
        this.addAbbreviation();
    }
    
    public void addBondNumber(){
        if (this.name.equals("Carbon")){
            this.numBonds = 4;            
        }
        else if (this.name.equals("Nitrogen")){
            this.numBonds = 3;
        }
        else if (this.name.equals("Oxygen")){
            this.numBonds = 2;            
        }
        else {
            this.numBonds = 1;
        }
    }
    
    public void addAbbreviation(){
        if (this.name.equals("Chlorine")){
            this.letter = "Cl";      
        }
        else if (this.name.equals("Bromine")){
            this.letter = "Br";
        }
        else {
            this.letter = this.name.substring(0, 1);
        }
    }
    
}
