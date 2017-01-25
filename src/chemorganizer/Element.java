package chemorganizer;

public class Element {
    
    int x, y;
    int numBonds, currBond;
    String name, letter;
    Element[] bonds = new Element[4];
    String[] bondTypes = new String[4];
    boolean[] usedSites = new boolean[4]; //U,D,L,R

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
    
    public void bondTo(Element e, String type, int location){
        int n;
        if (type.equals("single")){
            n = 1;
        } else if (type.equals("double")){
            n = 2;
        } else {
            n = 3;
        }        
        if (this.currBond+n <= this.numBonds && e.currBond+n <= e.numBonds){
            this.bonds[currBond] = e;
            this.bondTypes[currBond] = type;
            this.currBond += n;
            int x, y;
            switch (location) {
                case 0:
                    x = this.x;
                    y = this.y-40;
                    this.usedSites[0] = true;
                    e.usedSites[1] = true;
                    break;
                case 1:
                    x = this.x;
                    y = this.y+40;
                    this.usedSites[1] = true;
                    e.usedSites[0] = true;
                    break;
                case 2:
                    x = this.x-40;
                    y = this.y;
                    this.usedSites[2] = true;
                    e.usedSites[3] = true;
                    break;
                default:
                    x = this.x+40;
                    y = this.y;
                    this.usedSites[3] = true;
                    e.usedSites[2] = true;
                    break;
            }
            e.bondTo(this, type, n, x, y);
        } else {
            System.out.println("There weren't enough available bond sites");
//            System.out.println(this.currBond + " " + n + " " + this.numBonds);
//            System.out.println(e.currBond + " " + n + " " + e.numBonds);
//            System.out.println(this.name + " " + e.name);
        }        
    }
    
    private void bondTo(Element e, String type, int n, int x, int y){
        assert this.currBond+n <= this.numBonds && e.currBond+n <= e.numBonds;
        this.bonds[currBond] = e;
        this.bondTypes[currBond] = type;
        this.currBond+=n;
        this.x = x;
        this.y = y;
    }
}
