import java.util.logging.Logger;

public class BuyAction  implements ActionStartegy{
    private static final Logger logger = LoggerFactory.getLogger(BuyAction.class); 
    private String itemName; 
    private int quantity;

    public BuyAction(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }
    
    @Override
    public void execute(Player player) throws GameException {
        if (!player.getLocation().equals("Store")) {
            throw new GameException("Harus berada di Store untuk membeli item.");
        }
        if (player.getEnergy() < 5) throw new GameException("Energi tidak cukup untuk membeli item.");
        int price = getItemPrice(itemName) * quantity;
        if (player.getGold() < price) {
            throw new GameException("Gold tidak cukup untuk membeli " + quantity + " " + itemName);
        }
        Item item;
        switch (itemName) {
            case "Parsnip Seeds":
            case "Cauliflower Seeds":
            case "Potato Seeds":
            case "Wheat Seeds":
                item = ItemFactory.createSeed(itemName, quantity);
                break;
            case "Firewood":
            case "Coal":
                item = ItemFactory.createMiscItem(itemName, quantity);
                break;
            default:
                throw new GameException("Item tidak tersedia untuk dibeli: " + itemName);
        }
        player.spendGold(price);
        player.setEnergy(player.getEnergy() - 5);
        player.getTimeManager().advanceTime(10);
        player.getInventory().add(item);
        player.getStats().addSpending(price);
        logger.info("Pemain membeli {} {} seharga {} gold.", quantity, itemName, price);
    }
    
    private int getItemPrice(String itemName) {
        switch (itemName) {
            case "Parsnip Seeds":
                return 20;
            case "Cauliflower Seeds":
                return 80;
            case "Potato Seeds":
                return 50;
            case "Wheat Seeds":
                return 10;
            case "Firewood":
                return 5;
            case "Coal":
                return 15;
            default:
                return 0;
        }
    }
}
