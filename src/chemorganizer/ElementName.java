package chemorganizer;

import java.util.ArrayList;

public class ElementName {
    String input, hydrogenLetter;
    Element[] baseChain;
    ArrayList<Element> elements = new ArrayList();
    int chainLen;
    String bondType;
    
    public ElementName(String n, String hl) {
        this.input = n.toLowerCase();
        this.hydrogenLetter = hl;
    }
    
    public void generateMap(){
        //Here we set up initial variables for calculations
        if (this.input.equals("dextrose")){
            createDextrose();
            return;
        }
        String[] splitName = input.split(" ");
        int numBranches = splitName.length;        
        String last = splitName[numBranches-1];
        String base, branches;
        String[] splitBranches;
        String[] baseBonds;
        boolean addAcid = false;
        //Set up bonds by default to be single

        
        //Some elements end with "-oic acid", so splitting on " " will miss the base chain
        if (last.equals("acid")){
            base = splitName[numBranches-2];
            addAcid = true;
        } else {
            base = splitName[numBranches-1];
        }
        
        branches = splitName[0];
        if (branches.equals(base)){
            branches = "";
        }
        
        //Here we take its name and determine how many carbons are in the main chain
        if (base.contains("met"))
            chainLen = 1;
        else if (base.contains("eth"))
            chainLen = 2;
        else if (base.contains("pro"))
            chainLen = 3;
        else if (base.contains("but"))
            chainLen = 4;
        else if (base.contains("pen"))
            chainLen = 5;
        else if (base.contains("hex"))
            chainLen = 6;
        else if (base.contains("hep"))
            chainLen = 7;
        else if (base.contains("oct"))
            chainLen = 8;
        else if (base.contains("non"))
            chainLen = 9;
        else if (base.contains("dec"))
            chainLen = 10;
        
        baseChain = new Element[chainLen];
        baseBonds = new String[chainLen];
        for (int i=0; i<chainLen; i++){
            baseBonds[i] = "single";
        }
        //The base chain is an Element[] for easy access, the carbons are added to it
        int x = 325 - 20*chainLen;
        for (int i=0; i<chainLen; i++){
            baseChain[i] = new Element(x, 350, "Carbon");
            x += 40;
        }
        
        
        //Bonds in the base chain are not necessarily all single bonds
        String[] name = base.split("-");
        if (base.contains("ene")){
            for(int i=0; i<name.length; i++){
                if (name[i].contains("ene")){
                    int[] num;
                    try {
                        num = parseNumbers(name[i-1]);
                    } catch (Exception e){
                        num = new int[] {0};
                    }
                    for (int j=0; j<num.length; j++){
                        baseBonds[num[j]] = "double";
                    }
                }
            }
        }
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
                        baseBonds[num[j]] = "triple";
                    }
                }
            }
        }

        //Now add all the bonds in the base chain
        for (int i=0; i<chainLen-1; i++){
            try{
                baseChain[i].bondTo(baseChain[i+1], baseBonds[i], 3);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        //Add primary branch to the chain
        if (addAcid){
            Element Oxygen = new Element(0, 0, "Oxygen");
            Element Hydroxyl = new Element(0, 0, "Hydroxyl-R");
            baseChain[0].bondTo(Oxygen, "double", 0);
            baseChain[0].bondTo(Hydroxyl, "single", 2);
            elements.add(Oxygen);
            elements.add(Hydroxyl);
        }
        for (int i=0; i<name.length; i++){
            String numbers;
            try {
                numbers = name[i-1];
            } catch (Exception e){
                numbers = "1";
            }
            //if the previous information was not a specific number
            if (numbers.matches("^[^0-9]+$")){
                numbers = "1";
            }
            if (name[i].contains("ol")){
                String chain = numbers + "-hydroxyl-";
                branches = chain + branches;
            }
            if (name[i].contains("al")){
                String chain = "carbonyl-";
                branches = chain + branches;
            }
            if (name[i].contains("amine")){
                String chain = numbers + "-amino-";
                branches = chain + branches;
            }
            if (name[i].contains("one")){
                String chain = numbers + "-carbonyl-";
                branches = chain + branches;
            }
        }
        
        splitBranches = branches.split("-");
        
        //Adding the branches to the carbons
        for (int i=0; i<splitBranches.length; i++){
            //if the string matches regex for no digits in [0, 9], then it is a branch name
            if (splitBranches[i].matches("^[^0-9]+$")){
                int[] n;
                try{
                    n = parseNumbers(splitBranches[i-1]); //Number precedes branch name 
                } catch(Exception e){
                    n = new int[] {0}; //Assume carbon #1 by default
                }
                String nextName = parseName(splitBranches[i]);
                for (int j=0; j<n.length; j++){
                    //Add a new element off screen with no bond position
                    Element next = new Element(-10, -10, nextName);
                    int q = baseChain[n[j]].nextFreeTop();
                    if (q == -1){
                        System.out.println("This cannot be made");
                    } else {
                        if (next.name.equals("Hydroxyl") && q == 2){
                            next.letter = "HO";
                        } else if (next.name.equals("Amino") && q == 2){
                            next.letter = "H\u2082N";
                        }
                        baseChain[n[j]].bondTo(next, bondType, q);
                        elements.add(next);
                    }
                }
            }
        }

        //Add Hydrogens to bonded sites
        for (int i=0; i<chainLen; i++){
            int n = 4-baseChain[i].numBonds();
            for (int j=0; j<n; j++){
                Element H = new Element(-10, -10, "Hydrogen");
                H.letter = hydrogenLetter;
                baseChain[i].bondTo(H, "single", baseChain[i].nextFree());
                elements.add(H);
            }
        }
        
        for (int i=0; i<chainLen; i++){
            elements.add(baseChain[i]);
        }
    }
    
    
    public void createDextrose(){
        baseChain = new Element[6];
        chainLen = 6;
        int x = 325 - 20*chainLen;
        for (int i=0; i<chainLen; i++){
            baseChain[i] = new Element(x, 350, "Carbon");
            x += 40;
        }

        for (int i=0; i<chainLen-1; i++){
            try{
                baseChain[i].bondTo(baseChain[i+1], "single", 3);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        Element oxygen = new Element(-10, -10, "Oxygen");
        baseChain[0].bondTo(oxygen, "double", 2);
        elements.add(oxygen);

        //Adding the branches to the carbons
        int[] hydroxyOrder = {0, 1, 0, 0, 0};
        for (int i=1; i<6; i++){
            Element hydroxy = new Element(-10, -10, "Hydroxyl");
            baseChain[i].bondTo(hydroxy, "single", hydroxyOrder[i-1]);
            elements.add(hydroxy);
        }

        //Add Hydrogens to bonded sites
        for (int i=0; i<chainLen; i++){
            int n = 4-baseChain[i].numBonds();
            for (int j=0; j<n; j++){
                Element H = new Element(-10, -10, "Hydrogen");
                H.letter = hydrogenLetter;
                baseChain[i].bondTo(H, "single", baseChain[i].nextFree());
                elements.add(H);
            }
        }        
        
        for (int i=0; i<chainLen; i++){
            elements.add(baseChain[i]);
        }
    }
    
    public int[] parseNumbers(String a){
        String[] a0 = a.split(",");
        int[] n = new int [a0.length];
        for (int i=0; i<a0.length; i++){
            n[i] = Integer.parseInt(a0[i]) - 1;
        }
        return n;
    }
    
    public String parseName(String a){
        String name = " ";
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
            
        } else if (a.contains("propyl")){
            
        } else if (a.contains("isopropyl")){
            
        } else if (a.contains("butyl")){
            
        } else if (a.contains("secbutyl")){
            
        } else if (a.contains("isobutyl")){
            
        }
        return name;
    }    
    
}
