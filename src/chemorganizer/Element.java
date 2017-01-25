package chemorganizer;

public class Element {
    
    int x, y;
    int numBonds, currBond;
    String name, letter;
    Element[] bonds = new Element[4];
    String[] bondTypes = new String[4];    

    public Element(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.currBond = 0;
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
    
    public void bondTo(Element e, String type, String location){
        int n;
        if (type.equals("single")){
            n = 1;
        } else if (type.equals("double")){
            n = 2;
        } else {
            n = 3;
        }        
        if (this.currBond+n < this.numBonds && e.currBond+n < e.numBonds){
            this.bonds[currBond] = e;
            this.bondTypes[currBond] = type;            
            this.currBond++;
            int x, y;
            if (location.equals("U")){
                x = this.x;
                y = this.y-40;
            } else if (location.equals("D")){
                x = this.x;
                y = this.y+40;
            } else if (location.equals("L")){
                x = this.x-40;
                y = this.y;
            } else {
                x = this.x+40;
                y = this.y;
            }
            e.bondTo(this, type, n, x, y);
        } else {
            System.out.println("There weren't enough available bond sites");
        }        
    }
    
    private void bondTo(Element e, String type, int n, int x, int y){
        assert this.currBond+n < this.numBonds && e.currBond+n < e.numBonds;
        this.bonds[currBond] = e;
        this.bondTypes[currBond] = type;            
        this.currBond++;
        this.x = x;
        this.y = y;
    }
}
