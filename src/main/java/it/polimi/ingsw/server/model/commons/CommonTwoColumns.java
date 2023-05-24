package it.polimi.ingsw.server.model.commons;

import it.polimi.ingsw.server.model.Shelf;
import it.polimi.ingsw.server.model.commons.CommonTargetCard;
import it.polimi.ingsw.server.model.items.ItemColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonTwoColumns extends CommonTargetCard {
    public CommonTwoColumns(int maxPlayerNumber) {
        super(maxPlayerNumber);
        this.name="CommonTwoColumns";
    }
    @Override
    public boolean check(Shelf shelf) {
        int columnFound=0;
        for(int i=0;i<COLUMNS;i++){
            List<ItemColor> differentItemsInColumn=new ArrayList<>();
            for(int k=0;k<ROWS;k++){
                if(shelf.getItemByCoordinates(k,i)!= null && !differentItemsInColumn.contains(shelf.getItemByCoordinates(k,i).getColor())){
                    differentItemsInColumn.add(shelf.getItemByCoordinates(k,i).getColor());
                }
            }
            if(differentItemsInColumn.size()==6) {
                columnFound++;
                if(columnFound==2)
                    return true;
            }
        }
        return false;
    }
}
