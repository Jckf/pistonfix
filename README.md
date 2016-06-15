# PistonFix

Behavior of the BlockPistonRetractEvent was changed when Spigot updated
CraftBukkit to Minecraft 1.8. This new behavior can be very confusing, and
outright breaks existing plugins.

The problem is an additional trigger of BlockPistonRetractEvent for non-sticky
pistons, causing plugins to see the same piston retract twice in a row. This
plugin works around that by cancelling the second event in a LOWEST priority
handler, and then uncancelling it again in a MONITOR priority event handler.
This means plugins running on the same server as PistonFix will still see two
BlockPistonRetractEvents, but the second one will be cancelled for all handlers
that are not at LOWEST priority. Behavior for such handlers is undefined.

In summary, this means you can ignore the extraneous event by setting
`ignoreCancelled=true` on your BlockPistonRetractEvent handler like so:

```java
@EventHandler(ignoreCancelled=true)
public void onBlockPistonRetract(BlockPistonRetractEvent) {
    // I will only trigger once per actual retract :)
}
```
