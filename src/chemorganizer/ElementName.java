package chemorganizer;

import java.util.ArrayList;

public class ElementName {
    String input;
    Element[] baseChain;
    ArrayList<Element> elements = new ArrayList();
    int chainLen;
    String bondType;
    
    public ElementName(String n) {
        this.input = n.toLowerCase();
    }
    
    public void generateMap(){
        //Here we set up initial variables for calculations
        String[] branches = input.split(" ");
        int numBranches = branches.length;        
        String last = branches[numBranches-1];
        String base;
        String[] baseBonds;
        boolean addAcid = false;
        //Set up bonds by default to be single

        
        //Some elements end with "-oic acid", so splitting on " " will miss the base chain
        if (last.equals("acid")){
            base = branches[numBranches-2];
            addAcid = true;
        } else {
            base = branches[numBranches-1];
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
        int x = 325 - 40*chainLen/2;
        for (int i=0; i<chainLen; i++){
            baseChain[i] = new Element(x, 350, "Carbon");
            x += 40;
        }
        
        
        //Bonds in the base chain are not necessarily all single bonds
        String[] name = base.split("-");
        if (base.contains("ene")){
            for(int i=0; i<name.length; i++){
                if (name[i].contains("ene")){
                    int[] num = parseNumbers(name[i-1]);
                    for (int j=0; j<num.length; j++){
                        baseBonds[num[j]] = "double";
                    }
                }
            }
        }
        if (base.contains("yne")){
            for(int i=0; i<name.length; i++){
                if (name[i].contains("yne")){
                    int[] num = parseNumbers(name[i-1]);
                    for (int j=0; j<num.length; j++){
                        baseBonds[num[j]] = "triple";
                    }
                }
            }
        }

        
        //Get stuff from the end of the base chain here too "pent-2-ol"
        
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
            if (name[i].contains("ol")){
                String chain = name[i-1] + "-hydroxyl ";
                input = chain + input;
            }
            if (name[i].contains("al")){
                String chain = "1-carbonyl ";
                input = chain + input;
            }
            if (name[i].contains("amine")){
                String chain = name[i-1] + "-amino ";
                input = chain + input;
            }
            if (name[i].contains("one")){
                String chain = name[i-1] + "-carbonyl";
                input = chain + input;
            }
        }
        branches = input.split(" ");
        
        //Adding the branches to the carbons
        for (int i=0; i<numBranches; i++){
            if (branches[i].equals(base)){
                break;
            }
            String[] branchInfo = branches[i].split("-");
            int[] n = parseNumbers(branchInfo[0]);
            String nextName = parseName(branchInfo[1]);         
            for (int j=0; j<n.length; j++){
                Element next = new Element(10, 10, nextName);
                int q=-1;
                for (int k=3; k>=0; k--){
                    if (baseChain[n[j]].usedSites[k] == false){
                        q = k;
                    }
                }
                if (next.name.equals("Hydroxyl") && q == 2){
                    next.letter = "HO";
                } else if (next.name.equals("Amino") && q == 2){
                    next.letter = "H\u2082N";
                }
                baseChain[n[j]].bondTo(next, bondType, q);
                elements.add(next);
            }
        }
        
        //Don't forget to add Hydrogens to unused places
        
        
        for (int i=0; i<chainLen; i++){
            elements.add(baseChain[i]);
        }
    }
    
    
    public void addToCarbon(String a, int i){
        
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
        } else if (a.contains("hydroxyl")){
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
