package chemorganizer;

public class Element {
    
    int x, y;
    int maxBonds, currBond;
    String name, letter;
    //All arrays store in the order: top, bottom, left, right
    Element[] bonds = new Element[4];
    String[] bondTypes = new String[4];
    boolean[] usedSites = new boolean[4];
    
    public Element(String name){
        this(0, 0, name);
    }
    
    public Element(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name.toLowerCase();
        this.currBond = 0;
        this.addBondNumber();
        this.addAbbreviation();
    }
    
    public void addBondNumber(){
        if (this.name.equals("carbon")){
            this.maxBonds = 4;            
        }
        else if (this.name.equals("nitrogen")){
            this.maxBonds = 3;
        }
        else if (this.name.equals("oxygen")){
            this.maxBonds = 2;            
        }
        else {
            this.maxBonds = 1;
        }
    }
    
    public void addAbbreviation(){
        if (this.name.equals("chlorine")){
            this.letter = "Cl";      
        }
        else if (this.name.equals("bromine")){
            this.letter = "Br";
        }
        else if (this.name.equals("amino")){
            this.letter = "NH\u2082";
        }
        else if (this.name.equals("amino-r")){
            this.letter = "H\u2082N";
        }
        else if (this.name.equals("hydroxyl")){
            this.letter = "OH";
        }
        else if (this.name.equals("hydroxyl-r")){
            this.letter = "HO";
        }
        else {
            this.letter = this.name.substring(0, 1).toUpperCase();
        }
    }
    
    public boolean canBondTo(Element e, String type){
        int n;
        if (type.equals("single")){
            n = 1;
        } else if (type.equals("double")){
            n = 2;
        } else {
            n = 3;
        }
        return this.numBonds()+n <= this.maxBonds && e.numBonds()+n <= e.maxBonds;
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
                    if (e.name.equals("hydrogen")){
                        y = this.y-30;
                    } else {
                        y = this.y-50;
                    }
                    this.usedSites[0] = true;
                    e.usedSites[1] = true;
                    sendTo = 1;
                    break;
                case 1:
                    x = this.x;
                    if (e.name.equals("hydrogen")){
                        y = this.y+30;
                    } else {
                        y = this.y+50;
                    }
                    this.usedSites[1] = true;
                    e.usedSites[0] = true;
                    sendTo = 0;
                    break;
                case 2:
                    if (e.name.equals("hydrogen")){
                        x = this.x-30;
                    } else {
                        x = this.x-50;
                    }
                    y = this.y;
                    this.usedSites[2] = true;
                    e.usedSites[3] = true;
                    sendTo = 3;
                    break;
                default:
                    if (e.name.equals("hydrogen")){
                        x = this.x+30;
                    } else {
                        x = this.x+50;
                    }
                    y = this.y;
                    this.usedSites[3] = true;
                    e.usedSites[2] = true;
                    sendTo = 2;
                    break;
            }
            e.bondTo(this, type, n, x, y, sendTo);
        } else {
            System.out.println("There weren't enough available bond sites");
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
    
    public int numBonds(){
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
    
    public int nextFree(){
        for (int i: new int[] {3, 2, 0, 1}){
            if (!this.usedSites[i]){
                return i;
            }
        }
        return -1;
    }
    
    public int nextFreeTop(){
        for (int i: new int[] {0, 2, 3, 1}){
            if (!this.usedSites[i]){
                return i;
            }
        }
        return -1;
    }
    
    public int nextFreeTopB(){
        for (int i: new int[] {0, 1, 2, 3}){
            if (!this.usedSites[i]){
                return i;
            }
        }
        return -1;
    }
}
