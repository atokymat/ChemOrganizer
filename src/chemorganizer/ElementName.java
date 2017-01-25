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
    
    public void getMap(){
        String[] branches = input.split(" ");
        int l = branches.length;        
        String last = branches[l-1];
        String base;
        if (last.equals("acid")){
            base = branches[l-2];
        } else {
            base = branches[l-1];
        }
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
        int x = 325 - 40*chainLen/2;
        for (int i=0; i<chainLen; i++){
            baseChain[i] = new Element(x, 350, "Carbon");
            x += 40;
        }
    }
    
    public void getMainChain(){
        
    }
    
    public void addToCarbon(int i){
        
    }
    
}
