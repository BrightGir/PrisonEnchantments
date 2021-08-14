package me.bright.pevents;


import me.bright.enums.PEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class RemoveEnchantmentEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private Player player;
    private ItemStack item;
    private PEnchantment ench;
    private boolean isSimilarEnchantment;
    private String enchString;

    public RemoveEnchantmentEvent(Player player, ItemStack item, String enchString) {
        this.player = player;
        this.item = item;
    //    this.ench = ench;
    //    this.isSimilarEnchantment = isSimilarEnchantment;
        this.enchString = enchString;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

   // public HandlerList getHandlerList() {
   //     return handlers;
   // }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Player getPlayer() {
        return player;
    }

 //  public PEnchantment getEnchantment() {
 //      return ench;
 //  }

    public ItemStack getItem() {
        return item;
    }

    // Находятся ли на предмете 2 одинаковых зачарования
  //  public boolean isSimilarEnchantment() {
  //      return isSimilarEnchantment;
  //  }
  //
   public String getEnchString() {
       return enchString;
   }
}
