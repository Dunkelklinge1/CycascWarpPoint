package net.cycasc.mc.cycascwarppoint;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class CycascWarpPoint extends JavaPlugin implements Listener {
    private String path = "plugins/" + this.getDescription().getName();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Plugin enabled");
        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Version " + this.getDescription().getVersion());
        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Plugin by " + this.getDescription().getAuthors().get(0));
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Plugin disabled");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sethome")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    setHome(p, "");
                }
                else if (args.length == 1) {
                    setHome(p, args[0]);
                }
                else {
                    return false;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("home")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    tpHome(p, "");
                }
                else if (args.length == 1) {
                    tpHome(p, args[0]);
                }
                else {
                    return false;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("delhome")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    delHome(p, "");
                }
                else if (args.length == 1) {
                    delHome(p, args[0]);
                }
                else {
                    return false;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("listhome")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    listHome(p, "");
                }
                else if (args.length == 1) {
                    listHome(p, args[0]);
                }
                else {
                    return false;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("setwarp")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    setWarp(p, args[0]);
                }
                else {
                    return false;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("warp")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    tpWarp(p, args[0]);
                }
                else {
                    return false;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("delwarp")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    delWarp(p, args[0]);
                }
                else {
                    return false;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("listwarp")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 0) {
                    listWarp(p, "");
                }
                else if (args.length == 1) {
                    listWarp(p, args[0]);
                }
                else {
                    return false;
                }
            }
        }
        else if (cmd.getName().equalsIgnoreCase("listwarpcreator")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length == 1) {
                    listWarpCreator(p, args[0]);
                }
                else {
                    return false;
                }
            }
        }

        return true;
    }

    private void setHome(Player p, String name) {
        if (getHome(p, name) == null) {
            World w = p.getLocation().getWorld();
            double x = p.getLocation().getX();
            double y = p.getLocation().getY();
            double z = p.getLocation().getZ();
            float yaw = p.getLocation().getYaw();
            float pitch = p.getLocation().getPitch();

            try {
                File dir = new File(path);
                if (dir.exists() == false) {
                    dir.mkdir();
                }

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "/homes.csv", true), StandardCharsets.UTF_8));

                // Beispiel 1: 43ae54a5-6243-49b3-a22e-62abd4720ca0;;World UID;96;65;102
                // Beispiel 2: 43ae54a5-6243-49b3-a22e-62abd4720ca0;World UID;NoobIsland;326;55;128
                bw.write(p.getUniqueId().toString() + ";" + name + ";" + w.getUID().toString() + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch);
                bw.newLine();
                bw.flush();
                bw.close();

                if (name.isEmpty()) {
                    p.sendMessage(ChatColor.DARK_GREEN + "Your home was saved successfully.");
                }
                else {
                    p.sendMessage(ChatColor.DARK_GREEN + "Your home \"" + name + "\" was saved successfully.");
                }
            }
            catch (Exception ex) {
                sendErrorMsg(ex, p);
            }
        }
        else {
            if (name.isEmpty()) {
                p.sendMessage(ChatColor.DARK_RED + "You already have a home.");
                p.sendMessage(ChatColor.GRAY + "Use the command \"/" + this.getCommand("delhome").getName() + "\" to delete it. After that you can save it again.");
            }
            else {
                p.sendMessage(ChatColor.DARK_RED + "You already have a home with the name \"" + name + "\".");
                p.sendMessage(ChatColor.GRAY + "Use the command \"/" + this.getCommand("delhome").getName() + " " + name + "\" to delete it. After that you can save it again.");
            }
        }
    }

    private Location getHome(Player p, String name) {
        Location loc = null;

        try {
            File f = new File(path + "/homes.csv");
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/homes.csv"), StandardCharsets.UTF_8));

                while (br.ready()) {
                    String currentLine = br.readLine();
                    String[] splittedLine = currentLine.split(";");
                    if ((splittedLine.length == 6) || (splittedLine.length == 8)) {

                        // 0 = Player UUID
                        // 1 = Name des Warps
                        // 2 = World UID
                        // 3 = x
                        // 4 = y
                        // 5 = z
                        // 6 = yaw (optional)
                        // 7 = pitch (optional)

                        if (UUID.fromString(splittedLine[0]).equals(p.getUniqueId()) && splittedLine[1].equalsIgnoreCase(name)) {
                            if (splittedLine.length == 6) {
                                loc = new Location(Bukkit.getWorld(UUID.fromString(splittedLine[2])), Double.parseDouble(splittedLine[3]), Double.parseDouble(splittedLine[4]), Double.parseDouble(splittedLine[5]));
                            }
                            else if (splittedLine.length == 8) {
                                loc = new Location(Bukkit.getWorld(UUID.fromString(splittedLine[2])), Double.parseDouble(splittedLine[3]), Double.parseDouble(splittedLine[4]), Double.parseDouble(splittedLine[5]), Float.parseFloat(splittedLine[6]), Float.parseFloat(splittedLine[7]));
                            }
                            break;
                        }
                    }
                    else {
                        br.close();
                        throw new Exception("Wrong number of delemiters in line \"" + currentLine + "\".");
                    }
                }

                br.close();
            }
        }
        catch (Exception ex) {
            sendErrorMsg(ex, p);
        }

        return loc;
    }

    private void tpHome(Player p, String name) {
        Location loc = getHome(p, name);

        if (loc != null) {
            try {
                p.teleport(loc);
                if (name.isEmpty()) {
                    p.sendMessage(ChatColor.DARK_GREEN + "You have been teleported home.");
                }
                else {
                    p.sendMessage(ChatColor.DARK_GREEN + "You have been teleported home (\"" + name + "\").");
                }
            }
            catch (Exception ex) {
                sendErrorMsg(ex, p);
            }
        }
        else {
            if (name.isEmpty()) {
                p.sendMessage(ChatColor.DARK_RED + "You dont have any home yet.");
                p.sendMessage(ChatColor.GRAY + "Use the command \"/" + this.getCommand("sethome").getName() + "\" to save your current location as home.");
            }
            else {
                p.sendMessage(ChatColor.DARK_RED + "You dont have any home named \"" + name + "\".");
                p.sendMessage(ChatColor.GRAY + "Use the command \"/" + this.getCommand("sethome").getName() + " " + name + "\" to save your current location as home \"" + name + "\".");
            }
        }
    }

    private void delHome(Player p, String name) {
        if (getHome(p, name) != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/homes.csv"), StandardCharsets.UTF_8));

                List<String> lines = new ArrayList<String>();

                while (br.ready()) {
                    String currentLine = br.readLine();
                    String[] splittedLine = currentLine.split(";");

                    // 0 = Player UUID
                    // 1 = Name des Warps
                    // 2 = World UID
                    // 3 = x
                    // 4 = y
                    // 5 = z

                    if ((UUID.fromString(splittedLine[0]).equals(p.getUniqueId()) == false) || (splittedLine[1].equalsIgnoreCase(name) == false)) {
                        lines.add(currentLine);
                    }
                }

                br.close();

                //Datei ��berschreiben...
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "/homes.csv", false), StandardCharsets.UTF_8));
                for (String s : lines) {
                    bw.write(s);
                    bw.newLine();
                }
                bw.flush();
                bw.close();

                if (name.isEmpty()) {
                    p.sendMessage(ChatColor.DARK_GREEN + "Your home was deleted.");
                }
                else {
                    p.sendMessage(ChatColor.DARK_GREEN + "Your home \"" + name + "\" was deleted.");
                }
            }
            catch (Exception ex) {
                sendErrorMsg(ex, p);
            }
        }
        else {
            if (name.isEmpty()) {
                p.sendMessage(ChatColor.DARK_RED + "You dont have any home to delete.");
            }
            else {
                p.sendMessage(ChatColor.DARK_RED + "You dont have any home named \"" + name + "\".");
            }
        }
    }

    private void listHome(Player p, String pattern) {
        try {
            List<String[]> homesToList = new ArrayList<String[]>();

            int allCount = 0;
            int patternCount = 0;
            File f = new File(path + "/homes.csv");
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/homes.csv"), StandardCharsets.UTF_8));

                while (br.ready()) {
                    String[] splittedLine = br.readLine().split(";");

                    // 0 = Player UUID
                    // 1 = Name des Warps
                    // 2 = World UID
                    // 3 = x
                    // 4 = y
                    // 5 = z

                    // In homesToList aufnehmen, wenn home dem player gehört...
                    if (UUID.fromString(splittedLine[0]).equals(p.getUniqueId())) {
                        allCount++;
                        // ...und pattern leer ist oder Name des Warps (splittedLine[1]) pattern enthält
                        if (pattern.isEmpty() || splittedLine[1].toLowerCase().contains(pattern.toLowerCase())) {
                            patternCount++;
                            homesToList.add(new String[]{splittedLine[1],splittedLine[2],splittedLine[3],splittedLine[4],splittedLine[5]});
                        }
                    }
                }

                br.close();
            }

            if (homesToList.size() > 0) {
                if (pattern.isEmpty()) {
                    p.sendMessage(ChatColor.DARK_GREEN + "List of your homes (count: " + allCount + ")...");
                }
                else {
                    p.sendMessage(ChatColor.DARK_GREEN + "List of your homes that match the pattern \"" + pattern + "\" (count: " + patternCount + ")...");
                }

                for (String[] s : homesToList) {
                    // 0 = Player UUID
                    // 1 = Name des Warps
                    // 2 = World UID
                    // 3 = x
                    // 4 = y
                    // 5 = z

                    String worldName = "UNKNOWN [" + s[1] + "]";
                    String biomeName = "UNKNOWN";
                    World w = Bukkit.getServer().getWorld(UUID.fromString(s[2]));
                    if (w != null) {
                        worldName = w.getName();
                        biomeName = getHome(p, s[1]).getBlock().getBiome().name();
                    }

                    p.sendMessage(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + s[1] + ChatColor.GRAY + " auf " + ChatColor.GOLD + worldName + ChatColor.GRAY + " (Biome: " + ChatColor.ITALIC + biomeName + ChatColor.RESET + ChatColor.GRAY + ")");
                }
            }
            else {
                if (pattern.isEmpty()) {
                    p.sendMessage(ChatColor.DARK_RED + "You have not created any homes.");
                }
                else {
                    p.sendMessage(ChatColor.DARK_RED + "You have no homes that match the pattern \"" + pattern + "\".");
                    p.sendMessage(ChatColor.GRAY + "Use the command \"/" + this.getCommand("listwarp").getName() + "\" (without any parameters) to list all your homes.");
                }
            }
        }
        catch (Exception ex) {
            sendErrorMsg(ex, p);
        }
    }

    private void setWarp(Player p, String name) {
        if (getWarp(p, name) == null) {
            World w = p.getLocation().getWorld();
            double x = p.getLocation().getX();
            double y = p.getLocation().getY();
            double z = p.getLocation().getZ();
            float yaw = p.getLocation().getYaw();
            float pitch = p.getLocation().getPitch();

            try {
                File dir = new File(path);
                if (dir.exists() == false) {
                    dir.mkdir();
                }

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "/warps.csv", true), StandardCharsets.UTF_8));

                // Beispiel: 43ae54a5-6243-49b3-a22e-62abd4720ca0;Park;World UID;326;55;128
                bw.write(p.getUniqueId().toString() + ";" + name + ";" + w.getUID().toString() + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch);
                bw.write(System.lineSeparator());
                bw.flush();
                bw.close();

                p.sendMessage(ChatColor.DARK_GREEN + "Warppoint \"" + name + "\" saved successfully.");
            }
            catch (Exception ex) {
                sendErrorMsg(ex, p);
            }
        }
        else {
            p.sendMessage(ChatColor.DARK_RED + "There is already a warppoint named \"" + name + "\".");
            p.sendMessage(ChatColor.GRAY + "Use the command \"/" + this.getCommand("delwarp").getName() + " " + name + "\" to delete this warppoint.");
        }
    }

    private Location getWarp(Player p, String name) {
        Location loc = null;

        try {
            File f = new File(path + "/warps.csv");
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/warps.csv"), StandardCharsets.UTF_8));

                while (br.ready()) {
                    String currentLine = br.readLine();
                    String[] splittedLine = currentLine.split(";");
                    if ((splittedLine.length == 6) || (splittedLine.length == 8)) {

                        // 0 = Player UUID
                        // 1 = Name des Warps
                        // 2 = World UID
                        // 3 = x
                        // 4 = y
                        // 5 = z
                        // 6 = yaw (optional)
                        // 7 = pitch (optional)

                        if (splittedLine[1].equalsIgnoreCase(name)) {
                            if (splittedLine.length == 6) {
                                loc = new Location(Bukkit.getWorld(UUID.fromString(splittedLine[2])), Double.parseDouble(splittedLine[3]), Double.parseDouble(splittedLine[4]), Double.parseDouble(splittedLine[5]));
                            }
                            else if (splittedLine.length == 8) {
                                loc = new Location(Bukkit.getWorld(UUID.fromString(splittedLine[2])), Double.parseDouble(splittedLine[3]), Double.parseDouble(splittedLine[4]), Double.parseDouble(splittedLine[5]), Float.parseFloat(splittedLine[6]), Float.parseFloat(splittedLine[7]));
                            }
                            break;
                        }
                    }
                    else {
                        br.close();
                        throw new Exception("Wrong number of delemiters in line \"" + currentLine + "\".");
                    }
                }

                br.close();
            }
        }
        catch (Exception ex) {
            sendErrorMsg(ex, p);
        }

        return loc;
    }

    private void tpWarp(Player p, String name) {
        Location loc = getWarp(p, name);

        if (loc != null) {
            try {
                p.teleport(loc);
                p.sendMessage(ChatColor.DARK_GREEN + "You have been teleported to \"" + name + "\".");
            }
            catch (Exception ex) {
                sendErrorMsg(ex, p);
            }
        }
        else {
            p.sendMessage(ChatColor.DARK_RED + "There is no warppoint named \"" + name + "\".");
            p.sendMessage(ChatColor.GRAY + "Use the command \"/" + this.getCommand("setwarp").getName() + " " + name + "\" to save your current location as warppoint \"" + name + "\".");
        }
    }

    private void delWarp(Player p, String name) {
        if (getWarp(p, name) != null) {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/warps.csv"), StandardCharsets.UTF_8));

                List<String> lines = new ArrayList<String>();
                boolean delete = true;

                while (br.ready()) {
                    String currentLine = br.readLine();
                    String[] splittedLine = currentLine.split(";");

                    // 0 = Player UUID
                    // 1 = Name des Warps
                    // 2 = World UID
                    // 3 = x
                    // 4 = y
                    // 5 = z

                    if (splittedLine[1].equalsIgnoreCase(name)) {
                        if ((UUID.fromString(splittedLine[0]).equals(p.getUniqueId()) == false) && (p.hasPermission(this.getDescription().getName() + ".delwarp") == false)) {
                            lines.add(currentLine);
                            delete = false;

                            p.sendMessage(ChatColor.DARK_RED + "You do not have the permissions to delete warppoint created by other players.");
                            if (Bukkit.getPlayer(UUID.fromString(splittedLine[0])) != null) {
                                p.sendMessage(ChatColor.GRAY + "This warppoint was created by " + Bukkit.getPlayer(UUID.fromString(splittedLine[0])).getName() + " (online).");
                            }
                            else {
                                p.sendMessage(ChatColor.GRAY + "This warppoint was created by " + Bukkit.getOfflinePlayer(UUID.fromString(splittedLine[0])).getName() + " (offline).");
                            }
                        }
                    }
                    else {
                        lines.add(currentLine);
                    }
                }

                br.close();

                if (delete) {
                    //Datei überschreiben...
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "/warps.csv", false), StandardCharsets.UTF_8));
                    for (String s : lines) {
                        bw.write(s);
                        bw.write(System.lineSeparator());
                    }
                    bw.flush();
                    bw.close();

                    p.sendMessage(ChatColor.DARK_GREEN + "Warppoint \"" + name + "\" deleted.");
                }
            }
            catch (Exception ex) {
                sendErrorMsg(ex, p);
            }
        }
        else {
            p.sendMessage(ChatColor.DARK_RED + "There is no warppoint named \"" + name + "\".");
        }
    }

    private void listWarp(Player p, String pattern) {
        try {
            List<String[]> warpsToList = new ArrayList<String[]>();

            int allCount = 0;
            int patternCount = 0;
            File f = new File(path + "/warps.csv");
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/warps.csv"), StandardCharsets.UTF_8));

                while (br.ready()) {
                    allCount++;
                    String[] splittedLine = br.readLine().split(";");

                    // 0 = Player UUID
                    // 1 = Name des Warps
                    // 2 = World UID
                    // 3 = x
                    // 4 = y
                    // 5 = z

                    //In warpsToList aufnehmen wenn pattern leer oder Name des Warps (splittetLine[1]) enthält pattern
                    if (pattern.isEmpty() || splittedLine[1].toLowerCase().contains(pattern.toLowerCase())) {
                        patternCount++;
                        warpsToList.add(new String[]{splittedLine[0],splittedLine[1],splittedLine[2],splittedLine[3],splittedLine[4],splittedLine[5]});
                    }
                }

                br.close();
            }

            if (warpsToList.size() > 0) {
                if (pattern.isEmpty()) {
                    p.sendMessage(ChatColor.DARK_GREEN + "List of all warppoints (count: " + allCount + ")...");
                }
                else {
                    p.sendMessage(ChatColor.DARK_GREEN + "List of warppoints that match the pattern \"" + pattern + "\" (count: " + patternCount + " of " + allCount + ")...");
                }

                for (String[] s : warpsToList) {
                    // 0 = Player UUID
                    // 1 = Name des Warps
                    // 2 = World UID
                    // 3 = x
                    // 4 = y
                    // 5 = z

                    String worldName = "UNKNOWN [" + s[2] + "]";
                    String biomeName = "UNKNOWN";
                    World w = Bukkit.getServer().getWorld(UUID.fromString(s[2]));
                    if (w != null) {
                        worldName = w.getName();
                        biomeName = getWarp(p, s[1]).getBlock().getBiome().name();
                    }
                    String pTargetName = "UNKNOWN [" + s[0] + "]";
                    OfflinePlayer pTarget = Bukkit.getOfflinePlayer(UUID.fromString(s[0]));
                    if (pTarget.hasPlayedBefore()) {
                        pTargetName = pTarget.getName();
                    }

                    p.sendMessage(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + s[1] + ChatColor.GRAY + " auf " + worldName + ChatColor.GRAY + " von " + ChatColor.GOLD + pTargetName + ChatColor.GRAY + " (Biome: " + ChatColor.ITALIC + biomeName + ChatColor.RESET + ChatColor.GRAY + ")");
                }
            }
            else {
                if (pattern.isEmpty()) {
                    p.sendMessage(ChatColor.DARK_RED + "No warppoint were created yet.");
                }
                else {
                    p.sendMessage(ChatColor.DARK_RED + "There are no warppoints that match the pattern \"" + pattern + "\".");
                    p.sendMessage(ChatColor.GRAY + "Use the command \"/" + this.getCommand("listwarp").getName() + "\" (without any parameters) to list all warppoints.");
                }
            }
        }
        catch (Exception ex) {
            sendErrorMsg(ex, p);
        }
    }

    private void listWarpCreator(Player p, String creatorName) {
        try {
            List<String[]> warpsToList = new ArrayList<String[]>();
            OfflinePlayer pTarget = Bukkit.getPlayer(creatorName);
            if (pTarget == null) {
                pTarget = Bukkit.getOfflinePlayer(creatorName);
            }

            if (pTarget.hasPlayedBefore()) {
                int allCount = 0;
                int patternCount = 0;
                File f = new File(path + "/warps.csv");
                if (f.exists()) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path + "/warps.csv"), StandardCharsets.UTF_8));

                    while (br.ready()) {
                        allCount++;
                        String[] splittedLine = br.readLine().split(";");

                        // 0 = Player UUID
                        // 1 = Name des Warps
                        // 2 = World UID
                        // 3 = x
                        // 4 = y
                        // 5 = z

                        OfflinePlayer pCurrent = Bukkit.getOfflinePlayer(UUID.fromString(splittedLine[0]));
                        if (pCurrent.hasPlayedBefore() && pCurrent.getName().equalsIgnoreCase(creatorName)) {
                            // In warpsToList aufnehmen wenn Spieler aus UUID in CSV gefunden (online ODER offline) und Name des Spielers playerPattern entspricht
                            patternCount++;
                            warpsToList.add(new String[]{splittedLine[1],splittedLine[2],splittedLine[3],splittedLine[4],splittedLine[5]});
                        }
                    }

                    br.close();
                }

                if (warpsToList.size() > 0) {
                    p.sendMessage(ChatColor.DARK_GREEN + "List of warppoints of \"" + pTarget.getName() + "\" (count: " + patternCount + " of " + allCount + ")...");

                    for (String[] s : warpsToList) {
                        // 0 = Name des Warps
                        // 1 = World UID
                        // 2 = x
                        // 3 = y
                        // 4 = z

                        String worldName = "UNKNOWN [" + s[1] + "]";
                        String biomeName = "UNKNOWN";
                        World w = Bukkit.getServer().getWorld(UUID.fromString(s[1]));
                        if (w != null) {
                            worldName = w.getName();
                            biomeName = getWarp(p, s[0]).getBlock().getBiome().name();
                        }

                        p.sendMessage(ChatColor.GRAY + "- " + ChatColor.DARK_GREEN + s[0] + ChatColor.GRAY + " auf " + ChatColor.GOLD + worldName + ChatColor.GRAY + " von " + ChatColor.GOLD + pTarget.getName() + ChatColor.GRAY + " (Biome: " + ChatColor.ITALIC + biomeName + ChatColor.RESET + ChatColor.GRAY + ")");
                    }
                }
                else {
                    p.sendMessage(ChatColor.DARK_RED + "There are no warppoints created by \"" + pTarget.getName() + "\" (but player was seen before on this server).");
                }
            }
            else {
                p.sendMessage(ChatColor.DARK_RED + "There are no warppoints created by \"" + creatorName + "\" (this player was never seen before on this server).");
            }
        }
        catch (Exception ex) {
            sendErrorMsg(ex, p);
        }
    }

    private void sendErrorMsg(Exception ex, Player p) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + ex.toString());
        p.sendMessage(ChatColor.DARK_RED + "An error has occurred. The warp or home file may be corrupt!");
        p.sendMessage(ChatColor.GRAY + "Details: " + ex.getMessage());
    }

    @EventHandler (priority = EventPriority.LOWEST)
    private void onPlayerBedEnter(PlayerBedEnterEvent e) {
        if (getHome(e.getPlayer(), "") == null) {
            setHome(e.getPlayer(), "");
        }
    }
}
