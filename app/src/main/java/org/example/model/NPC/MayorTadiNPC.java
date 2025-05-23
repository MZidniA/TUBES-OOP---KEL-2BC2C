package org.example.model.NPC;
import java.util.List;
import java.util.Collections;
import org.example.model.Items.Items;
import org.example.model.enums.LocationType;
import org.example.model.Items.ItemDatabase;
import org.example.model.enums.RelationshipStats;

public class MayorTadiNPC extends NPC{    
    public MayorTadiNPC() {
        super("Mayor Tadi", LocationType.RUMAH_MAYOR_TADI, 
              Collections.emptyList(),Collections.emptyList(),Collections.emptyList(), 
              0, RelationshipStats.SINGLE);
            
        List<Items> loved = ItemDatabase.itemList(new String[] {"Legend"});
        List<Items> liked = ItemDatabase.itemList(new String[] {"Angler", "Crimsonfish", "Glacierfish"});
        List<Items> hated = HatedItemsExceptLovednLiked(loved, liked);

        setLovedItems(loved);
        setLikedItems(liked);
        setHatedItems(hated);
    }
}
    