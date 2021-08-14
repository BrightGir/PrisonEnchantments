package me.bright.enums;

import me.bright.util.M;
import me.bright.util.PEManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public enum PEnchantment {

    //ARMOR
    ANGELIC("&eАнгел", Type.ARMOR,1,2,M.asList(
            "&2Данное зачарование добавит",
            "&2вам эффект регенерации"
    ),35, PotionEffectType.REGENERATION),
    DEVIL("&eДьявол",Type.ARMOR,1,1,M.asList(
            "&2С данным зачарованием",
            "&2вы больше не будете получать",
            "&2урон от огня"),
           20, PotionEffectType.FIRE_RESISTANCE),
    ANIMAL("&eРаненный зверь",Type.ARMOR,1,2,M.asList(
            "&2Данное зачарования добавит",
            "&2вам эффект силы и регенерации",
            "&2когда у вас будет мало здоровья"
    ),40),
    SATIETY("&eСытость",Type.ARMOR,1,1,M.asList(
            "&2С данным зачарованием",
            "&2вы будете всегда сытые"
    ),30),
    JUMPER("&eПопрыгун",Type.ARMOR,1,2,M.asList(
            "&2С данным зачарованием",
            "&2вы сможете прыгать выше",
            "&2чем обычно"
    ),60,PotionEffectType.JUMP),
    REFLECTION("&eОтражение",Type.ARMOR,1,3,M.asList(
            "&2Данное зачарование позволит",
            "&2вам с некоторым шансом отразить",
            "&2урон во врага"
    ),50),
    DOMINATE("&eДоминирование",Type.ARMOR,1,3,M.asList(
            "&2Данное зачарование позволит",
            "&2вам с некоторым шансом",
            "&2наложить медлительность и",
            "&2слабость на врага"
    ),60),
    TANK("&eТанк",Type.ARMOR,1,5,M.asList(
            "&2С данным зачарованием вы",
            "&2будете получать меньше урона"
    ),30),
    GOD("&eБог",Type.ARMOR,1,1,M.asList(
            "&2С данным зачарованием",
            "&2вы не будете получать",
            "&2урон от падения"
    ),30),
    SURVIVAL("&eВторая жизнь",Type.ARMOR,1,3,M.asList(
            "&2Данное зачарование позволит",
            "&2с некоторым шансом восстановить",
            "&2здоровье во время боя"
    ),30),
    SPEED("&eСкорость",Type.ARMOR,1,2,M.asList("" +
                    "&2С данным зачарованием ваша",
            "&2скорость увеличиться"
    ),40,PotionEffectType.SPEED),
    GLOW("&eСвет",Type.ARMOR,1,1,M.asList(
            "&2Данное зачарование добавит",
            "&2вам эффект ночного зрения"
    ),80, PotionEffectType.NIGHT_VISION),
    LAVA("&eЛава",Type.ARMOR,1,3,M.asList(
            "&2Данное зачарование позволит",
            "&2вам с некоторым шансом поджечь",
            "&2врага во время боя"
    ),100),

    //SWORD
    RAZOR("&cТаран",Type.SWORD,1,3,M.asList(
            "&2Данное зачарование позволит",
            "&2вам нанести критический",
            "&2удар с некоторым шансом"),30),
    THROW("&cБросок",Type.SWORD,1,2,M.asList(
            "&2Данное зачарование позволит",
            "&2вам подбросить врага",
            "&2в воздух во время боя"
    ),50),
    FOCUS("&cФокусник",Type.SWORD,3,3,M.asList(
            "&2Данное зачарование позволит",
            "&2вам неожиданно телепортироваться",
            "&2за спину врага во время боя"
    ),70),
    COVERAGE("&cОхват", Type.SWORD,1,3,M.asList(
            "&2Данное зачарование позволит",
            "&2вам ударить всех врагов",
            "&2находящихся в определенном радиусе"
    ),50),
    ILLUSION("&cИллюзия",Type.SWORD,1,2,M.asList(
            "&2Данное зачарование позволит",
            "&2вам с некоторым шансом ослепить",
            "&2и вызвать тошноту у противника"
    ),100),
    TOR("&cТор",Type.SWORD,3,3,M.asList(
            "&2Данное зачарование позволит",
            "&2поразить врага молненией",
            "&2во время боя"
    ),100),
    TOXIC("&cЯд",Type.SWORD,1,3,M.asList(
            "&2Данное зачарование позволит",
            "&2вам с некоторым шансом",
            "&2отравить врага"
            ),100),
    LIFESTEAL("&cКража жизни",Type.SWORD,3,3,M.asList(
            "&2Данное зачарование позволит",
            "&2вам возвращать пол сердца",
            "&2во время боя"
    ),10),

    //TOOLS
    MINER("&9Безумный шахтер",Type.TOOLS,1,2,M.asList(
            "&2Данное зачарование позволит",
            "&2вам с некоторым шансом",
            "&2получить эффект спешки"
    ),20),
    TRENCH("&9Бур",Type.TOOLS,3,3,M.asList(
            "&2Данное зачарования позволит",
            "&2вам с некоторым шансом выкопать",
            "&2территорию 3x3x1"
    ),20),
    MONEY_BAG("&9Денежный мешок", Type.TOOLS,1,1,M.asList(
            "&2Данное зачарование позволит",
            "&2вам получать дополнительные",
            "&2монеты с некоторым шансом",
            "&2во время копания"
    ),50),

    //BOW
    ARSON("&7Поджог",Type.BOW,1,1,M.asList(
            "&2Данное зачарование будет",
            "&2поджигать вылетающую стрелу"
    ),100),
    FIREBALL("&7Пекло",Type.BOW,1,1,M.asList(
            "&2С данным зачарованием",
            "&2вы будете стрелять огненными",
            "&2шарами вместо стрел"
    ),60),
    FROZEN("&7Замораживание",Type.BOW,1,3,M.asList(
            "&2Данное зачарование позволит",
            "&2вам с некоторым шансом",
            "&2заморозить врага"
    ),100);




    private String displayName;
    private PEnchantment.Type type;
    private int minLevel;
    private int maxLevel;
    private List<PotionEffectType> pottionTypes;
    private List<String> lore;
    private int chance;

    PEnchantment(String displayName, PEnchantment.Type type, int minLevel, int maxLevel, List<String> lore, int chance, PotionEffectType... pottionTypes) {
        this.displayName = ChatColor.translateAlternateColorCodes('&',displayName);;
        this.type = type;
        this.chance = chance;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.pottionTypes = new ArrayList<>();
        if(pottionTypes != null) {
            this.pottionTypes.addAll(Arrays.asList(pottionTypes));
        }
        this.lore = lore;
    }


    public String getDisplayName() {
        return M.colored(this.displayName);
    }

    public int getLevelOfBook(ItemStack item) {
        try {
            String name = item.getItemMeta().getDisplayName();
            String display = this.displayName;
            name = name.replace(display,"");
            name = name.trim();
            return (int) PEManager.getIntByRome(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getLevel(ItemStack item) {
    // проверяем если на предмете 2 зачарования то возвращаем максимальный уровень из них
        try {
            int counter = 0;
            String maxLevel = "";
            int lvl1 = 0;
            int lvl2 = 0;
            for(String string : item.getItemMeta().getLore()) {
                if(string.contains(this.displayName)) {
                    string = string.replace(this.displayName, "");
                    string = string.trim();
                    if(counter == 0) {
                        lvl1 = (int) PEManager.getIntByRome(string);
                    } else {
                        lvl2 = (int) PEManager.getIntByRome(string);
                    }
                    counter++;
                }
            }
            return (Math.max(lvl1, lvl2));
        } catch(Exception e) {

        }
        return 0;
    }

    public int getChance() {
        return chance;
    }
    public List<PotionEffectType> getPottionTypes() {
        return pottionTypes;
    }

    public Type getType() {
        return type;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public static PEnchantment getByName(String name) {
        for(PEnchantment ench : PEnchantment.values()) {
            if(name.contains(ench.getDisplayName())) {
                return ench;
            }
        }
        return null;
    }


    public enum Type {
        ARMOR("&e","Для брони",
                Material.LEATHER_HELMET,Material.CHAINMAIL_HELMET,Material.GOLD_HELMET,Material.DIAMOND_HELMET,Material.IRON_HELMET,
                Material.LEATHER_CHESTPLATE,Material.CHAINMAIL_HELMET,Material.GOLD_CHESTPLATE,Material.DIAMOND_CHESTPLATE,Material.IRON_CHESTPLATE,
                Material.LEATHER_LEGGINGS,Material.CHAINMAIL_LEGGINGS,Material.GOLD_LEGGINGS,Material.DIAMOND_LEGGINGS,Material.IRON_LEGGINGS,
                Material.LEATHER_BOOTS,Material.CHAINMAIL_BOOTS,Material.GOLD_BOOTS,Material.DIAMOND_BOOTS,Material.IRON_BOOTS),

        SWORD("&c","Для меча",
                Material.WOOD_SWORD,Material.STONE_SWORD,Material.IRON_SWORD,Material.GOLD_SWORD,Material.DIAMOND_SWORD),

        TOOLS("&9","Для инструментов",
                Material.WOOD_PICKAXE,Material.STONE_PICKAXE,Material.IRON_PICKAXE,Material.GOLD_PICKAXE,Material.DIAMOND_PICKAXE,
                Material.WOOD_AXE,Material.STONE_AXE,Material.IRON_AXE,Material.GOLD_AXE,Material.DIAMOND_AXE,
                Material.WOOD_SPADE,Material.STONE_SPADE,Material.IRON_SPADE,Material.GOLD_SPADE,Material.DIAMOND_SPADE),
        BOW("&7","Для лука",Material.BOW);


        private String color;
        private String displayName;
        private HashSet<Material> materials;
        Type(String color, String displayName, Material... materials) {
            this.color = ChatColor.translateAlternateColorCodes('&',color);
            this.displayName = displayName;
            this.materials = new HashSet<>();
            this.materials.addAll(Arrays.asList(materials));
        }

        public String getColor() {
            return color;
        }

        public HashSet<Material> getMaterials() {
            return materials;
        }

        public String getDisplayName() {
            return this.color + this.displayName;
        }
    }
}
