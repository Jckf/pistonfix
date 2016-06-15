package no.jckf.pistonfix;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

class PistonListener implements Listener {
    private final PistonFix plugin;

    PistonListener(PistonFix plugin) {
        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        this.plugin.loadChunk(event.getChunk());
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        this.plugin.unloadChunk(event.getChunk());
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        if (event.isSticky())
            return;

        this.plugin.setPistonState(event.getBlock(), PistonState.EXTENDED);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPistonRetractFirst(BlockPistonRetractEvent event) {
        if (event.isSticky())
            return;

        switch (this.plugin.getPistonState(event.getBlock())) {
            case NEW:
            case EXTENDED:
                this.plugin.setPistonState(event.getBlock(), PistonState.RETRACTED);
                break;

            case RETRACTED:
                this.plugin.setPistonState(event.getBlock(), PistonState.CANCELLED);
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPistonRetractLast(BlockPistonRetractEvent event) {
        if (this.plugin.getPistonState(event.getBlock()) == PistonState.CANCELLED)
            event.setCancelled(false);
    }
}
