package net.prismc.prishub.menus.profilemenu.pageparty;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.google.gson.JsonObject;
import net.prismc.priscore.api.PartyParseApi;
import net.prismc.priscore.api.UtilApi;
import net.prismc.priscore.prisplayer.PrisBukkitPlayer;
import net.prismc.priscore.prisplayer.PrisPartyMember;
import net.prismc.priscore.utils.PatternCollection;
import net.prismc.prishub.PrisHub;
import net.prismc.prishub.menus.profilemenu.pageparty.sub.Invite;
import net.prismc.prishub.menus.profilemenu.pageparty.sub.Join;
import net.prismc.prishub.menus.profilemenu.shared.ProfileMenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Party {

    public void open(PrisBukkitPlayer p) {
        ChestGui gui = new ChestGui(6, "");
        JsonObject rawParty = p.getParty();
        PartyParseApi party = new PartyParseApi(rawParty);

        if (!rawParty.has("members")) {
            StaticPane pane = new StaticPane(0, 0, 9, 5);
            String s = PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Title")).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.ChestTitle"));
            s = PatternCollection.FORMAT_PATTERN.matcher(s).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.NoParty.Format"));
            s = PatternCollection.BUTTONS_PATTERN.matcher(s).replaceAll("");
            s = PatternCollection.STATUS_PATTERN.matcher(s).replaceAll("");
            gui.setTitle(s);
            GuiItem open = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.NoParty.Open")), event -> {
                p.createParty(true);
                Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> open(p), 2L);
            });
            GuiItem closed = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.NoParty.Closed")), event -> {
                p.createParty(false);
                Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> open(p), 2L);
            });
            GuiItem join = new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.NoParty.Join")), event -> {
                p.getPlayer().closeInventory();
                new Join().open(p);
            });
            pane.addItem(open, 3, 2);
            pane.addItem(open, 4, 2);
            pane.addItem(open, 5, 2);
            pane.addItem(closed, 3, 3);
            pane.addItem(closed, 4, 3);
            pane.addItem(closed, 5, 3);
            pane.addItem(join, 3, 4);
            pane.addItem(join, 4, 4);
            pane.addItem(join, 5, 4);
            gui.addPane(pane);
        } else {
            String guiTitle = PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Title")).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.ChestTitle"));
            PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, 5);
            ArrayList<PrisPartyMember> members = party.getPeasants();
            int possiblePages = (int) Math.ceil((float) members.size() / 32.00f);

            if (possiblePages != 0) {
                for (int i = 1; i <= possiblePages; i++) {
                    int slotX = 1;
                    int slotY = 1;
                    StaticPane pane = new StaticPane(0, 0, 9, 5, Pane.Priority.HIGH);
                    List<PrisPartyMember> sMembers;
                    int fromIndex = i * 32;
                    int toIndex = fromIndex - 32;

                    try {
                        sMembers = members.subList(toIndex, fromIndex);
                    } catch (IndexOutOfBoundsException e) {
                        sMembers = members.subList(toIndex, members.size());
                    }

                    if (i > 1) {
                        String title = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.Previous")).replaceAll(String.valueOf(i - 1));
                        int finalI = i;
                        pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, title), event -> {
                            paginatedPane.setPage(finalI - 1);
                            String newTitle = PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Title")).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.ChestTitle"));
                            gui.setTitle(format(possiblePages, (finalI - 1), newTitle, p, party));
                            gui.update();
                        }), 7, 0);
                    }
                    if (i != possiblePages) {
                        String title = PatternCollection.PAGE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.Next")).replaceAll(String.valueOf(i + 1));
                        int finalI = i;
                        pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, title), event -> {
                            paginatedPane.setPage(finalI + 1);
                            String newTitle = PatternCollection.TITLE_PATTERN.matcher(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Title")).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.ChestTitle"));
                            gui.setTitle(format(possiblePages, (finalI + 1), newTitle, p, party));
                            gui.update();
                        }), 8, 0);
                    }

                    for (PrisPartyMember prisPartyMember : sMembers) {
                        if (slotX > 8) {
                            slotX = 1;
                            slotY++;
                        }
                        if (prisPartyMember.isOnline()) {
                            pane.addItem(new GuiItem(UtilApi.createPlayerHead(prisPartyMember.getOfflinePlayer(), ChatColor.GREEN + prisPartyMember.getName())), slotX, slotY);
                        } else {
                            pane.addItem(new GuiItem(UtilApi.createItem(Material.SKELETON_SKULL, ChatColor.RED + prisPartyMember.getName())), slotX, slotY);
                        }
                        slotX++;
                    }

                    gui.setTitle(format(possiblePages, i, guiTitle, p, party));
                    paginatedPane.addPane(i, pane);
                }


                gui.addPane(paginatedPane);
                paginatedPane.setPage(1);
            } else {
                gui.setTitle(format(possiblePages, 1, guiTitle, p, party));
            }
            gui.addPane(buttons(p));
        }
        gui.addPane(ProfileMenuUtils.createMenuBar(p));
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.show(p.getPlayer());
    }


    private String format(int possiblePages, int i, String title, PrisBukkitPlayer p, PartyParseApi party) {
        if (possiblePages == 0) {
            title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.NoArrows"));
        }
        if (i == 1 && i == possiblePages) {
            title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.NoArrows"));
        }
        if (i == 1 && i != possiblePages) {
            title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.RightArrow"));
        }
        if (i != 1 && i == possiblePages) {
            title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.LeftArrow"));
        }
        if (i != 1 && i != possiblePages) {
            title = PatternCollection.FORMAT_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.BothArrows"));
        }

        if (party.getLeader().getId() == p.getID()) {
            title = PatternCollection.BUTTONS_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.LeaderButtons"));
        } else {
            title = PatternCollection.BUTTONS_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.MemberButtons"));
        }

        if (party.isOpen()) {
            title = PatternCollection.STATUS_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.OpenIcon") + "\uF801");
        } else {
            title = PatternCollection.STATUS_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.ClosedIcon"));
        }

        title = PatternCollection.TITLE_PATTERN.matcher(title).replaceAll(UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Title"));
        return title;
    }

    private StaticPane buttons(PrisBukkitPlayer p) {
        StaticPane pane = new StaticPane(0, 0, 9, 6);
        PartyParseApi party = new PartyParseApi(p.getParty());
        pane.addItem(new GuiItem(UtilApi.createPlayerHead(party.getLeader().getOfflinePlayer(), PatternCollection.PLAYER_PATTERN.matcher(
                UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.LeaderPrefix")).replaceAll(party.getLeader().getName()))), 0, 0);

        if (party.getLeader().getId() == p.getID()) {
            pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.StatusChangeButton")), inventoryClickEvent -> {
                p.setPartyStatus();
                Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> open(p), 2L);
            }), 0, 2);
            pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.InviteButton")), inventoryClickEvent -> {
                p.getPlayer().closeInventory();
                new Invite().open(p);
            }), 0, 3);
            pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.DisbandButton")), inventoryClickEvent -> {
                p.leaveParty();
                Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> open(p), 2L);
            }), 0, 4);
        } else {
            pane.addItem(new GuiItem(UtilApi.createItem(Material.NETHER_SPROUTS, UtilApi.getString(p.getLanguageFile(), "ProfileMenu.PartyPage.Party.LeaveButton")), inventoryClickEvent -> {
                p.leaveParty();
                Bukkit.getScheduler().runTaskLater(PrisHub.getInstance(), () -> open(p), 2L);
            }), 0, 4);
        }

        return pane;
    }
}