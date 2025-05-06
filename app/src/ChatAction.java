public class ChatAction implements ActionStartegy {
    private static final Logger logger = LoggerFactory.getLogger(ChatAction.class); 
    private String npcName;

    public ChatAction(String npcName) {
        this.npcName = npcName;
    }
    
    @Override
    public void execute(Player player) throws GameException {
        NPC npc = player.getWorldMap().getNPC(npcName);
        if (npc == null) throw new GameException("NPC tidak ditemukan: " + npcName);
        String location = player.getLocation();
        if (!location.equals(npc.getHouse()) && !location.equals("Store")) {
            throw new GameException("Harus berada di rumah NPC atau Store untuk mengobrol.");
        }
        if (player.getEnergy() < 10) throw new GameException("Energi tidak cukup untuk mengobrol.");
        npc.chat();
        player.setEnergy(player.getEnergy() - 10);
        player.getTimeManager().advanceTime(10);
        player.getStats().incrementChatCount(npcName);
        logger.info("Pemain mengobrol dengan {}.", npcName);
    }
}
