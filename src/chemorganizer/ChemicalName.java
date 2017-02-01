package chemorganizer;

import java.util.ArrayList;

public class ChemicalName {
    //Has an input and the letter to display hydrogen
    String input, hydrogenLetter, output;
    //Stores a bond type (single bond, double bond, triple bond)
    String bondType = "";
    //The base carbon chain, and its length
    Element[] baseChain;
    int chainLen;
    //Elements in the chemical
    ArrayList<Element> elements = new ArrayList();
    
    
    public ChemicalName(String n, String hl) {
        this.input = n.toLowerCase();
        this.hydrogenLetter = hl;
        this.output = "<html>Building molecule...<br>";
        this.generateMap();
    }
    
    public void generateMap(){
        //Creates the map of elements in the chemical into the elements array
        
        
        if (this.input.equals("dextrose")){
            //Dextrose does not follow default bond locations, it is a special exception
            createDextrose();
            return;
        }
        //Set up initial variables for calculations
        String[] splitName = input.split(" ");
        int numBranches = splitName.length;        
        String last = splitName[numBranches-1];
        String base;
        String branches = "";
        String[] splitBranches;
        String[] baseBonds;
        boolean addAcid = false;

        
        //Some names end with "-oic acid", note the space
        if (last.contains("acid")){
            //Gets the base chain
            try {
                base = splitName[numBranches-2];
            } catch (Exception e) {
                base = splitName[numBranches-1];
            }
            addAcid = true;
        } else {
            base = splitName[numBranches-1];
        }
        
        //Converts space characters to dashes for parsing
        for (int i=0; i<numBranches; i++){
            if (splitName[i].equals(base)){
                break;
            }
            branches = branches + "-" + splitName[i];
        }

        
        //Take the name and determine how many carbons are in the main chain
        if (base.contains("meth"))
            chainLen = 1;
        else if (base.contains("eth"))
            chainLen = 2;
        else if (base.contains("prop"))
            chainLen = 3;
        else if (base.contains("but"))
            chainLen = 4;
        else if (base.contains("pent"))
            chainLen = 5;
        else if (base.contains("hex"))
            chainLen = 6;
        else if (base.contains("hept"))
            chainLen = 7;
        else if (base.contains("oct"))
            chainLen = 8;
        else if (base.contains("non"))
            chainLen = 9;
        else if (base.contains("dec"))
            chainLen = 10;
        else{
            //Stop the program from running bad input
            output = output + String.format("\"%s\" is not a real base<br>", base);
            output = output + "BUILD FAILED<br></html>";
            return;
        }
        
        //Set up arrays with chain length
        baseChain = new Element[chainLen];
        baseBonds = new String[chainLen];
        //Bonds not specified in the IUPAC name are single bonds by default
        for (int i=0; i<chainLen; i++){
            baseBonds[i] = "single";
        }
        //Place down the row which is the base chain
        int x = 325 - 25*chainLen;
        for (int i=0; i<chainLen; i++){
            //Only the position of the first carbon matters, all others will be
            //modified when it is bonded to in Element.bondTo from the Element class
            baseChain[i] = new Element(x, 350, "Carbon");
        }
        
        
        //Bonds in the base chain are not necessarily all single bonds
        String[] name = base.split("-");
        
        //Finds double bonds in the main chain
        if (base.contains("ene")){
            for(int i=0; i<name.length; i++){
                if (name[i].contains("ene")){
                    //num will store the location of the double bonds
                    int[] num;
                    try {
                        num = parseNumbers(name[i-1]);
                    } catch (Exception e){
                        num = new int[] {0};
                    }
                    for (int j=0; j<num.length; j++){
                        //Some names cause too many bonds on a carbon, which is illegal
                        //The input was invalid, and the program assumes a single bond instead
                        if (num[j] == chainLen-1){
                            output = output + String.format("Cannot place a bond following carbon #%d<br>", chainLen);
                        } else if (num[j] != -1){
                            baseBonds[num[j]] = "double";
                        }
                    }
                }
            }
        }
        //Uses the same process to find triple bonds in the main chain
        if (base.contains("yne")){
            for(int i=0; i<name.length; i++){
                if (name[i].contains("yne")){
                    int[] num;
                    try {
                        num = parseNumbers(name[i-1]);
                    } catch (Exception e){
                        num = new int[] {0};
                    }
                    for (int j=0; j<num.length; j++){
                        //Some names cause too many bonds on a carbon, which is illegal
                        //The input was invalid, and the program assumes a single bond instead
                        if (num[j] == chainLen-1){
                            output = output + String.format("Cannot place a bond following carbon #%d<br>", chainLen);
                        } else if (num[j] != -1){
                            baseBonds[num[j]] = "triple";
                        }
                    }
                }
            }
        }

        //Adds all the bonds in the base chain
        for (int i=0; i<chainLen-1; i++){
            if (baseChain[i].canBondTo(baseChain[i+1], baseBonds[i])) {
                baseChain[i].bondTo(baseChain[i+1], baseBonds[i], 3);
            } else {
                //Error message here should not need to run, just a safety measure
                output = output + String.format("Cannot add a %s bond following carbon %d<br>",
                        baseBonds[i], i+1);
                baseChain[i].bondTo(baseChain[i+1], "single", 3);
            }
        }
        
        //Add primary branch information to the branches information
        if (addAcid){
            //Adds the carboxylic acid group to the first carbon, which is always
            //the location of a carboxylic acid group
            Element Oxygen = new Element("Oxygen");
            Element Hydroxyl = new Element("Hydroxyl-R");
            baseChain[0].bondTo(Oxygen, "double", 0);
            baseChain[0].bondTo(Hydroxyl, "single", 2);
            elements.add(Oxygen);
            elements.add(Hydroxyl);
        }
        //Takes the name of the base chain and gives it to the branch name
        //so that every branch can be processed in the same loop
        for (int i=0; i<name.length; i++){
            String numbers;
            try {
                //Some numbers will be specified in the name
                numbers = name[i-1];
            } catch (Exception e){
                //Others assume carbon #1, or can only be carbon #1
                numbers = "1";
            }
            //if the previous information was not a specific number, the number
            //should be assumed as carbon #1
            if (numbers.matches("^[^0-9]+$")){
                numbers = "1";
            }
            if (name[i].contains("ol")){        //alcohol was the primary branch
                String chain = numbers + "-hydroxyl-";
                branches = chain + branches;
            }
            if (name[i].contains("al")){        //aldehyde was the primary branch
                String chain = "carbonyl-";     //aldehydes have no number
                branches = chain + branches;
            }
            if (name[i].contains("amine")){     //amine was the primary branch
                String chain = numbers + "-amino-";
                branches = chain + branches;
            }
            if (name[i].contains("one")){       //ketone was the primary branch
                String chain = numbers + "-carbonyl-";
                branches = chain + branches;
            }
        }
        
        //split the branches into an array to be looped through
        splitBranches = branches.split("-");
        
        //Adding the branches to the carbons:
        for (int i=0; i<splitBranches.length; i++){
            //if the string matches regular expression for no digits in [0, 9]
            //then it must be a branch name, otherwise it carries no valuable information
            if (splitBranches[i].matches("^[^0-9]+$")){
                int[] n;
                try{
                    n = parseNumbers(splitBranches[i-1]); //Numbers precede branch name 
                } catch(Exception e){
                    n = new int[] {0}; //Assume carbon #1 by default if there were no numbers
                }
                
                //Uses the branch name and the numbers to determine which element should be added
                String nextName = parseName(splitBranches[i], n);
                
                //Alkyl branches are handled separately, and nextName is empty
                if (!nextName.isEmpty()){
                    for (int j=0; j<n.length; j++){
                        if (n[j] == -1){
                            //Handles cases where the name specified too many bonds to a carbon
                            continue;
                        }
                        Element next = new Element(nextName);
                        int q = baseChain[n[j]].nextFreeTop();
                        if (q == -1){
                            output = output + String.format("There aren't enough free bonds to place a(n) %s to carbon #%d<br>",
                                    nextName.toLowerCase(), n[j]+1);
                        } else {
                            //Reverse the lettering of a backwards group with more than one element letter
                            if (next.name.equals("Hydroxyl") && q == 2){
                                next.letter = "HO";
                            } else if (next.name.equals("Amino") && q == 2){
                                next.letter = "H\u2082N";
                            }
                            
                            //Add the bond and add the bonded element to the elements arraylist
                            baseChain[n[j]].bondTo(next, bondType, q);
                            elements.add(next);
                        }
                    }
                }
            }
        }
        
        //Add all the base chain to the arraylist
        for (int i=0; i<chainLen; i++){
            elements.add(baseChain[i]);
        }

        //Adds Hydrogens to unbonded sites
        Element[] e = getElementArray();
        for (int i=0; i<e.length; i++){
            if (e[i].name.equals("carbon")){
                //Determines the number of hydrogens to bond to the carbon
                int n = 4-e[i].numBonds();
                for (int j=0; j<n; j++){
                    Element H = new Element("Hydrogen");
                    H.letter = hydrogenLetter;
                    e[i].bondTo(H, "single", e[i].nextFree());
                    elements.add(H);
                }
            }
        }
        
        output = output + "BUILD SUCCESSFUL<br></html>";
    }
    
    public void createDextrose(){
        //Creates the special case of drawing dextrose
        
        //Add a hexane chain
        baseChain = new Element[6];
        chainLen = 6;
        int x = 325 - 25*chainLen;
        for (int i=0; i<chainLen; i++){
            baseChain[i] = new Element(x, 350, "Carbon");
        }

        for (int i=0; i<chainLen-1; i++){
            baseChain[i].bondTo(baseChain[i+1], "single", 3);
        }
        
        
        //Dextrose is an aldehyde, add the oxygen
        Element oxygen = new Element("Oxygen");
        baseChain[0].bondTo(oxygen, "double", 0);
        elements.add(oxygen);

        //Adding the hydroxyl branches to the carbons
        //They are in strict locations, in the array below
        int[] hydroxyOrder = {0, 1, 0, 0, 0};
        //Thankfully, I can hardcode the information here and bond it
        for (int i=1; i<6; i++){
            Element hydroxy = new Element("Hydroxyl");
            baseChain[i].bondTo(hydroxy, "single", hydroxyOrder[i-1]);
            elements.add(hydroxy);
        }

        //Adds the carbons to the elements array
        for (int i=0; i<chainLen; i++){
            elements.add(baseChain[i]);
        }

        //Add Hydrogens to unbonded sites
        for (int i=0; i<chainLen; i++){
            int n = 4-baseChain[i].numBonds();
            for (int j=0; j<n; j++){
                Element H = new Element(-10, -10, "Hydrogen");
                H.letter = hydrogenLetter;
                baseChain[i].bondTo(H, "single", baseChain[i].nextFree());
                elements.add(H);
            }
        }
        
        output = output + "BUILD SUCCESSFUL<br></html>";
    }
    
    public int[] parseNumbers(String a){
        //Takes a string like "1" or "2,2,3" and returns the int[] array
        String[] a0 = a.split(",");
        int[] n = new int [a0.length];
        for (int i=0; i<a0.length; i++){
            n[i] = Integer.parseInt(a0[i]) - 1;
            if (n[i] >= chainLen || n[i] < 0){
                //gives an error number of -1 if the number is invalid
                output = output + String.format("\"%d\" is not a valid carbon number<br>", n[i]+1);
                n[i] = -1;
            }
        }
        return n;
    }
    
    public String parseName(String a, int[] n){
        //Takes the branch name and converts it into the element name or
        //keeps it as the branch name for branches with multiple elements
        String name = "";
        if (a.contains("fluoro")){
            name = "Fluorine";
            bondType = "single";
        } else if (a.contains("chloro")){
            name = "Chlorine";
            bondType = "single";
        } else if (a.contains("bromo")){
            name = "Bromine";
            bondType = "single";
        } else if (a.contains("iodo")){
            name = "Iodine";
            bondType = "single";
        } else if (a.contains("amino")){
            name = "Amino";
            bondType = "single";
        } else if (a.contains("hydroxy")){
            name = "Hydroxyl";
            bondType = "single";
        } else if (a.contains("carbonyl") || a.contains("oxo")){
            name = "Oxygen";
            bondType = "double";
        } else if (a.contains("methyl")){
            name = "Carbon";
            bondType = "single";
        } else if (a.contains("ethyl")){
            addAlkylBranch(2, n, "ethyl");
            name = "";
        } else if (a.contains("propyl")){
            addAlkylBranch(3, n, "propyl");
        } else if (a.contains("butyl")){
            addAlkylBranch(4, n, "butyl");
        } else {
            output = output + String.format("\"%s\" could not be added as a branch<br>", a);
        }
        return name;
    }

    public void addAlkylBranch(int l, int[] pos, String name){
        //Adds an alkyl branch (which has length > 1) to the base chain
        
        //Array which stores the last carbons, bonds it to the base chain
        Element[] nextCarbon = new Element[pos.length];
        for (int i=0; i<pos.length; i++){
            if (pos[i] != -1){
                int p = baseChain[pos[i]].nextFreeTopB();
                if (p != -1){
                    nextCarbon[i] = new Element("Carbon");
                    baseChain[pos[i]].bondTo(nextCarbon[i], "single", p);
                    elements.add(nextCarbon[i]);
                } else {
                    output = output + String.format("Could not add a %s branch to carbon #%d",
                            name, pos[i]);
                }
            }
        }
        
        //Adds more carbons to the alkyl branch until it reaches the branch length
        for (int i=0; i<pos.length; i++){
            for (int j=1; j<l; j++){
                //Makes a new carbon, and bonds it to the last added carbon
                //then resets the last added carbon to prepare for the next carbon
                if (nextCarbon[i] != null){
                    Element nextC = new Element("Carbon");
                    nextCarbon[i].bondTo(nextC, "single", nextCarbon[i].nextFreeTopB());
                    nextCarbon[i] = nextC;
                    elements.add(nextC);
                }
            }
        }
    } 
    
    public Element[] getElementArray(){
        //Converts each element in the arraylist to an element in a new array
        int l = elements.size();
        Object[] objects = elements.toArray();
        Element[] e0 = new Element[l];
        
        for (int i=0; i<l; i++){
            e0[i] = (Element) objects[i];
        }
        
        return e0;
    }
}
