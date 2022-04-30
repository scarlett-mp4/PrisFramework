package net.prismc.prishub.menus.profilemenu.pagefriends;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import net.prismc.priscore.api.PrisCoreApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.prisplayer.PrisFriend;
import net.prismc.priscore.utils.PatternCollection;
import net.prismc.prishub.menus.profilemenu.pagefriends.sub.Add;
import net.prismc.prishub.menus.profilemenu.shared.ProfileMenuUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Friends {

    public void open(PrisBukkitPlayer p) {
        ChestGui gui = new ChestGui(6, "");
        ArrayList<PrisFriend> friends = PrisCoreApi.getFriends(p.getTempFriendObject());

        if (friends.size() <= 0) {
            StaticPane noFriends = new StaticPane(0, 0, 9, 5, Pane.Priority.HIGH);
            gui.setTitle(PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.NoFriendsTitle")).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.ChestTitle")));
            noFriends.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.AddPage")), event -> {
                p.getPlayer().closeInventory();
                new Add().open(p);
            }), 6, 2);
            gui.addPane(noFriends);
        } else {
            String guiTitle = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Title")).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Page.1"));
            guiTitle = PatternCollection.TITLE_PATTERN.matcher(guiTitle).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.ChestTitle"));
            PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 5);
            int possiblePages = (int) Math.ceil((float) friends.size() / 36.00f);

            for (int i = 1; i <= possiblePages; i++) {
                int slotX = 0;
                int slotY = 1;
                StaticPane pane = new StaticPane(0, 0, 9, 5, Pane.Priority.HIGH);
                List<PrisFriend> sFriends;
                int fromIndex = i * 36;
                int toIndex = fromIndex - 36;

                try {
                    sFriends = friends.subList(toIndex, fromIndex);
                } catch (IndexOutOfBoundsException e) {
                    sFriends = friends.subList(toIndex, friends.size());
                }

                pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.AddPage")), event -> {
                    p.getPlayer().closeInventory();
                    new Add().open(p);
                }), 8, 0);
                if (i > 1) {
                    String title = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.PreviousPage")).replaceAll(String.valueOf(i - 1));
                    int finalI = i;
                    pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, title), event -> {
                        String newTitle = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Title")).
                                replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Page." + (finalI - 1)));
                        gui.setTitle(format(possiblePages, (finalI - 1), newTitle, p));
                        paginatedPane.setPage(finalI - 1);
                        gui.update();
                    }), 2, 0);
                }
                if (i != possiblePages) {
                    String title = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.NextPage")).replaceAll(String.valueOf(i + 1));
                    int finalI = i;
                    pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, title), event -> {
                        String newTitle = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Title")).
                                replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Page." + (finalI + 1)));
                        gui.setTitle(format(possiblePages, (finalI + 1), newTitle, p));
                        paginatedPane.setPage(finalI + 1);
                        gui.update();
                    }), 6, 0);
                }

                for (PrisFriend prisFriend : sFriends) {
                    if (slotX > 8) {
                        slotX = 0;
                        slotY++;
                    }

                    if (prisFriend.isOnline()) {
                        String server;
                        if (prisFriend.getServer().contains("lobby")) {
                            server = "Lobby";
                        } else {
                            server = "Prison";
                        }
                        List<String> lore = new ArrayList<>();
                        for (String ss : UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.FriendsPage.OnlineFriends.Lore")) {
                            String m = PatternCollection.FRIEND_SERVER.matcher(ss).replaceAll(server);
                            m = PatternCollection.FIRSTJOIN_PATTERN.matcher(m).replaceAll(prisFriend.getFirstJoined());
                            lore.add(m);
                        }

                        pane.addItem(new GuiItem(UtilApi.createPlayerHead(prisFriend.getOfflinePlayer(), ChatColor.GREEN + prisFriend.getName(), lore), event -> {


                            // Add More Options


                        }), slotX, slotY);

                    } else {
                        List<String> lore = new ArrayList<>();
                        for (String ss : UtilApi.getStringList(p.getLanguageFile(), "ProfileMenu.FriendsPage.OfflineFriends.Lore")) {
                            String m = PatternCollection.FRIEND_LAST_ONLINE.matcher(ss).replaceAll(prisFriend.getLastSeen());
                            m = PatternCollection.FIRSTJOIN_PATTERN.matcher(m).replaceAll(prisFriend.getFirstJoined());
                            lore.add(m);
                        }
                        pane.addItem(new GuiItem(UtilApi.createItem(Material.SKELETON_SKULL, ChatColor.RED + prisFriend.getName(), lore), event -> {


                            // Add More Options


                        }), slotX, slotY);
                    }
                    slotX++;
                }

                gui.setTitle(format(possiblePages, 1, guiTitle, p));
                paginatedPane.addPane(i, pane);
            }

            gui.addPane(paginatedPane);
            paginatedPane.setPage(1);
        }
        gui.addPane(ProfileMenuUtils.createMenuBar(p));
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.show(p.getPlayer());
    }

    private String format(int possiblePages, int i, String title, PrisBukkitPlayer p) {
        if (i == 1 && i == possiblePages) {
            title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Format.NoArrows"));
        }
        if (i == 1 && i != possiblePages) {
            title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Format.RightArrow"));
        }
        if (i != 1 && i == possiblePages) {
            title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Format.LeftArrow"));
        }
        if (i != 1 && i != possiblePages) {
            title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.Format.BothArrows"));
        }

        title = PatternCollection.TITLE_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.FriendsPage.ChestTitle"));
        return title;
    }
}