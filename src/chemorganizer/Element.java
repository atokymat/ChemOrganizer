package chemorganizer;

public class Element {
    
    int x, y;
    int maxBonds, currBond;
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
            this.maxBonds = 4;            
        }
        else if (this.name.equals("Nitrogen")){
            this.maxBonds = 3;
        }
        else if (this.name.equals("Oxygen")){
            this.maxBonds = 2;            
        }
        else {
            this.maxBonds = 1;
        }
    }
    
    public void addAbbreviation(){
        if (this.name.equals("Chlorine")){
            this.letter = "Cl";      
        }
        else if (this.name.equals("Bromine")){
            this.letter = "Br";
        }
        else if (this.name.equals("Amino")){
            this.letter = "NH\u2082";
        }
        else if (this.name.equals("Amino-R")){
            this.letter = "H\u2082N";
        }
        else if (this.name.equals("Hydroxyl")){
            this.letter = "OH";
        }
        else if (this.name.equals("Hydroxyl-R")){
            this.letter = "HO";
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
        if (this.numBonds()+n <= this.maxBonds && e.numBonds()+n <= e.maxBonds){
            this.bonds[location] = e;
            this.bondTypes[location] = type;
            this.currBond += n;
            int x, y, sendTo;
            switch (location) {
                case 0:
                    x = this.x;
                    y = this.y-40;
                    this.usedSites[0] = true;
                    e.usedSites[1] = true;
                    sendTo = 1;
                    break;
                case 1:
                    x = this.x;
                    y = this.y+40;
                    this.usedSites[1] = true;
                    e.usedSites[0] = true;
                    sendTo = 0;
                    break;
                case 2:
                    x = this.x-40;
                    y = this.y;
                    this.usedSites[2] = true;
                    e.usedSites[3] = true;
                    sendTo = 3;
                    break;
                default:
                    x = this.x+40;
                    y = this.y;
                    this.usedSites[3] = true;
                    e.usedSites[2] = true;
                    sendTo = 2;
                    break;
            }
            e.bondTo(this, type, n, x, y, sendTo);
        } else {
            System.out.println("There weren't enough available bond sites");
//            System.out.println(this.currBond + " " + n + " " + this.numBonds);
//            System.out.println(e.currBond + " " + n + " " + e.numBonds);
//            System.out.println(this.name + " " + e.name);
        }        
    }
    
    private void bondTo(Element e, String type, int n, int x, int y, int l){
        assert this.numBonds()+n <= this.maxBonds && e.numBonds()+n <= e.maxBonds;
        this.bonds[l] = e;
        this.bondTypes[l] = type;
        this.currBond+=n;
        this.x = x;
        this.y = y;
    }
    
    private int numBonds(){
        int n = 0;
        for (int i=0; i<4; i++){
            if (bondTypes[i] != null){
                switch (bondTypes[i]){
                    case "single":
                        n++;
                        break;
                    case "double":
                        n+=2;
                        break;
                    default:
                        n+=3;
                        break;
                }
            }
        }
        return n;
    }
}
