package chemorganizer;

public class Element {
    
    //Location on screen
    int x, y;
    //Maximum number of bonds it can make
    int maxBonds;
    //The element name and abbreviation
    String name, letter;
    //All arrays store in the order: top, bottom, left, right:
    Element[] bonds = new Element[4];           //stores bonded elements
    String[] bondTypes = new String[4];         //stores bond types
    boolean[] usedSites = new boolean[4];       //stores bond location
    
    public Element(String name){
        this(0, 0, name);
    }
    
    public Element(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name.toLowerCase();
        this.addBondNumber();
        this.addAbbreviation();
    }
    
    public void addBondNumber(){
        //Finds the maximum number of bonds the element can make
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
        //Gives the element an abbreviation
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
        //Returns true if there is enough space to bond, false otherwise
        int n;
        if (type.equals("single")){
            n = 1;
        } else if (type.equals("double")){
            n = 2;
        } else {
            n = 3;
        }
        return (this.numBonds()+n <= this.maxBonds) && (e.numBonds()+n <= e.maxBonds);
    }
    
    public void bondTo(Element e, String type, int location){   
        //Sets up a bond between the element and another element
        assert this.canBondTo(e, type);
        //Sets the bond element and type array
        this.bonds[location] = e;
        this.bondTypes[location] = type;
        
        int x, y, sendTo;
        //Uses the location to determine the position of the other element
        switch (location) {
            case 0:
                //Elements will be bonded above
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
                //Element will be bonded below
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
                //Element will be bonded to the left
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
                //Element will be bonded to the right
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
        //Updates the bond information of the second element
        e.bondTo(this, type, x, y, sendTo);       
    }
    
    private void bondTo(Element e, String type, int x, int y, int l){
        //Updates information of a secondary element
        this.bonds[l] = e;
        this.bondTypes[l] = type;
        this.x = x;
        this.y = y;
    }
    
    public int numBonds(){
        //Determines the number of bonds that have been made to this element
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
        //Returns the first free positon in the order right, left, top, bottom
        //Or -1 if there is no free position
        if (this.numBonds() == this.maxBonds)
            return -1;
        for (int i: new int[] {3, 2, 0, 1}){
            if (!this.usedSites[i]){
                return i;
            }
        }
        return -1;
    }
    
    public int nextFreeTop(){
        //Returns the first free positon in the order top, left, right, bottom
        //Or -1 if there is no free position
        if (this.numBonds() == this.maxBonds)
            return -1;
        for (int i: new int[] {0, 2, 3, 1}){
            if (!this.usedSites[i]){
                return i;
            }
        }
        return -1;
    }
    
    public int nextFreeTopB(){
        //Returns the first free position in the order top, bottom, left, right
        //Or -1 if there is no free position
        if (this.numBonds() == this.maxBonds)
            return -1;
        for (int i=0; i<4; i++){
            if (!this.usedSites[i]){
                return i;
            }
        }
        return -1;
    }
}
