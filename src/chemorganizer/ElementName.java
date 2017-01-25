package chemorganizer;

import java.util.ArrayList;

public class ElementName {
    String input;
    Element[] baseChain;
    ArrayList<Element> elements = new ArrayList();
    int chainLen;
    
    public ElementName(String n) {
        this.input = n;
    }
    
    public void generateMap(){
        //Here we set up initial variables for calculations
        String[] branches = input.split(" ");
        int numBranches = branches.length;        
        String last = branches[numBranches-1];
        String base;
        String[] baseBonds;
        //Set up bonds by default to be single

        
        //Some elements end with "-oic acid", so splitting on " " will miss the base chain
        if (last.equals("acid")){
            base = branches[numBranches-2];
        } else {
            base = branches[numBranches-1];
        }
                
        //Here we take its name and determine how many carbons are in the main chain
        switch (base.substring(0, 3)){
            case "met":
                chainLen = 1;
                break;
            case "eth":
                chainLen = 2;
                break;
            case "pro":
                chainLen = 3;
                break;
            case "but":
                chainLen = 4;
                break;
            case "pen":
                chainLen = 5;
                break;
            case "hex":
                chainLen = 6;
                break;
            case "hep":
                chainLen = 7;
                break;
            case "oct":
                chainLen = 8;
                break;
            case "non":
                chainLen = 9;
                break;
            case "dec":
                chainLen = 10;
                break;     
        }
        
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
        if (base.contains("ene")){
            String[] name = base.split("-");
            for(int i=0; i<name.length; i++){
                if (name[i].equals("ene")){
                    int[] num = parseNumbers(name[i-1]);
                    for (int j=0; j<num.length; j++){
                        baseBonds[num[j]] = "double";
                    }
                }
            }
        }
        if (base.contains("yne")){
            String[] name = base.split("-");
            for(int i=0; i<name.length; i++){
                if (name[i].equals("yne")){
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
        
        //Adding the branches to the carbons
        for (int i=0; i<numBranches; i++){
            if (branches[i].equals(base)){
                break;
            }
            String[] branchInfo = branches[i].split("-");
            int[] n = parseNumbers(branchInfo[0]);
            String name = parseName(branchInfo[1]);         
            for (int j=0; j<n.length; j++){
                Element next = new Element(10, 10, name);
                int q=-1;
                for (int k=3; k>=0; k--){
                    if (baseChain[n[j]].usedSites[k] == false){
                        q = k;
                    }
                }
                baseChain[n[j]].bondTo(next, "single", q);
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
        } else if (a.contains("chloro")){
            name = "Chlorine";
        } else if (a.contains("bromo")){
            name = "Bromine";
        } else if (a.contains("iodo")){
            name = "Iodine";
        } else if (a.contains("amino")){
            name = "Nitrogen";
        } else if (a.contains("hydroxyl")){
            name = "Oxygen";
        } else if (a.contains("carbonyl")){
            
        } else if (a.contains("methyl")){
            name = "Carbon";
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
