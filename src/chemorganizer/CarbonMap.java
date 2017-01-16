package chemorganizer;

public class CarbonMap {
    
    int[][] indexMap;
    int[] carbonNumbering;
    Element[][] bonds;
    String[][] bondTypes;
    int index;
    
    public CarbonMap(int numCarbons){
        this.indexMap = new int[numCarbons][4];
        this.carbonNumbering = new int[numCarbons];
        this.bonds = new Element[numCarbons][4];
        this.bondTypes = new String[numCarbons][4];
        this.index = 0;
    }
    
    public void addToMap(Element[] bonded, String[] bondType){
        assert bondType.length == 4 && bonded.length == 4;        
        bonds[index] = bonded;
        bondTypes[index] = bondType;                
        
        //Check every new bonded element
        for(int i=0; i<4; i++){
            
            //If the bond has an element attached
            if(!(bonded[i] == null)){
                //Check every mapped element
                for (int j=0; j<index; j++){
                    
                    //Check the bonds to that element
                    for (int k=0; k<4; k++){
                        
                        //If one of the mapped elements is also bonded to this
                        if (bonds[j][k].equals(bonded[i])){
                            //Map carbons together
                            if (bonded[i].letter.equals("C")){
                                indexMap[index][i] = k;
                                indexMap[j][k] = i;
                            }
//                            else{
//                                //This naming feature can exist in chemistry, but has not
//                                //been implemented, and gets very complex and is 
//                                //therefore beyond the scope of a project of this scale
//                            }
                        }
                    }                    
                }
            }
        }
        index++;
    }
    
    public void numberCarbons(){
        //The cancerous naming system, after this code, it can only get better...
    }
    
    private int trackBranch(int node, int arrived){
        return 0;
    }

    
}
