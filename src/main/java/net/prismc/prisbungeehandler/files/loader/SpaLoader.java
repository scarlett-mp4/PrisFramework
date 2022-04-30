package net.prismc.prisbungeehandler.files.loader;

import net.prismc.prisbungeehandler.files.configs.Config;

public class SpaLoader {
    private final Config config;

    public SpaLoader(Config config) {
        this.config = config;
    }

    /**
     * Loads the values into the specified config file
     */
    public void loadMessages() {

        // Friends
        config.getConfig().set("Friends.General.UnknownCommand", "");
        config.getConfig().set("Friends.General.MissingPerms", "");
        config.getConfig().set("Friends.General.NotOnline", "");
        config.getConfig().set("Friends.General.MissingArgs", "");
        config.getConfig().set("Friends.General.Spacer", "");

        config.getConfig().set("Friends.Help.Accept", "");
        config.getConfig().set("Friends.Help.Add", "");
        config.getConfig().set("Friends.Help.Deny", "");
        config.getConfig().set("Friends.Help.List", "");
        config.getConfig().set("Friends.Help.Notifications", "");
        config.getConfig().set("Friends.Help.Remove", "");
        config.getConfig().set("Friends.Help.Toggle", "");
        config.getConfig().set("Friends.Help.Header", "");

        config.getConfig().set("Friends.Add.Toggled", "");
        config.getConfig().set("Friends.Add.CantFriendYourself", "");
        config.getConfig().set("Friends.Add.TimedOutFrom", "");
        config.getConfig().set("Friends.Add.TimedOutTo", "");
        config.getConfig().set("Friends.Add.Sent", "");
        config.getConfig().set("Friends.Add.AlreadySent", "");
        config.getConfig().set("Friends.Add.AlreadyReceived", "");
        config.getConfig().set("Friends.Add.AlreadyFriends", "");
        config.getConfig().set("Friends.Add.Request", "");
        config.getConfig().set("Friends.Add.ADD", "");
        config.getConfig().set("Friends.Add.OR", "");
        config.getConfig().set("Friends.Add.DENY", "");
        config.getConfig().set("Friends.Add.ADDHover", "");
        config.getConfig().set("Friends.Add.DENYHover", "");

        config.getConfig().set("Friends.Accept.NoRequest", "");
        config.getConfig().set("Friends.Accept.NowFriends", "");

        config.getConfig().set("Friends.List.Header", "");
        config.getConfig().set("Friends.List.RightArrow", "");
        config.getConfig().set("Friends.List.LeftArrow", "");
        config.getConfig().set("Friends.List.NoRight", "");
        config.getConfig().set("Friends.List.NoLeft", "");
        config.getConfig().set("Friends.List.HoverPage", "");
        config.getConfig().set("Friends.List.OnlineMessage", "");
        config.getConfig().set("Friends.List.OfflineMessage", "");
        config.getConfig().set("Friends.List.Status.RPG", "");
        config.getConfig().set("Friends.List.Status.Lobby", "");
        config.getConfig().set("Friends.List.NoFriends", "");
        config.getConfig().set("Friends.List.UwU", "");
        config.getConfig().set("Friends.List.NotAPage", "");
        config.getConfig().set("Friends.List.CurrentPage", "");

        config.getConfig().set("Friends.Remove.CantRemoveYourself", "");
        config.getConfig().set("Friends.Remove.NotFriends", "");
        config.getConfig().set("Friends.Remove.BFFRemoved", "");
        config.getConfig().set("Friends.Remove.FrenemyRemoved", "");

        config.getConfig().set("Friends.Deny.NoRequest", "");
        config.getConfig().set("Friends.Deny.YouDeclined", "");
        config.getConfig().set("Friends.Deny.GotDeclined", "");

        config.getConfig().set("Friends.Notifications.Online", "");
        config.getConfig().set("Friends.Notifications.Offline", "");
        config.getConfig().set("Friends.Notifications.ToggledOn", "");
        config.getConfig().set("Friends.Notifications.ToggledOff", "");

        config.getConfig().set("Friends.Requests.ToggledOn", "");
        config.getConfig().set("Friends.Requests.ToggledOff", "");

        // Language
        config.getConfig().set("Language.General.ImproperUsage", "");
        config.getConfig().set("Language.General.Update", "");
        config.getConfig().set("Language.General.Unknown", "");

        // Message
        config.getConfig().set("Message.NotOnline", "");
        config.getConfig().set("Message.ImproperUsage", "");
        config.getConfig().set("Message.From", "");
        config.getConfig().set("Message.To", "");
        config.getConfig().set("Message.CantMessagePlayer", "");
        config.getConfig().set("Message.ToggledOff", "");
        config.getConfig().set("Message.ToggledFriends", "");
        config.getConfig().set("Message.ToggledOn", "");
        config.getConfig().set("Message.CantMessageYourself", "");

        // Reply
        config.getConfig().set("Reply.ImproperUsage", "");
        config.getConfig().set("Reply.PlayerIsOffline", "");
        config.getConfig().set("Reply.NoOneToReplyTo", "");

        // Party
        config.getConfig().set("Party.General.UnknownCommand", "");
        config.getConfig().set("Party.General.MissingPerms", "");
        config.getConfig().set("Party.General.NotOnline", "");
        config.getConfig().set("Party.General.MissingArgs", "");
        config.getConfig().set("Party.General.Spacer", "");

        config.getConfig().set("Party.Events.CantSwitchServers", "");
        config.getConfig().set("Party.Events.LeaderDisconnected", "");
        config.getConfig().set("Party.Events.TimeToReconnect", "");
        config.getConfig().set("Party.Events.LeaderReconnected", "");
        config.getConfig().set("Party.Events.GetBackInThere", "");
        config.getConfig().set("Party.Events.OfflinePlayers", "");

        config.getConfig().set("Party.Help.Join", "");
        config.getConfig().set("Party.Help.Create", "");
        config.getConfig().set("Party.Help.Invite", "");
        config.getConfig().set("Party.Help.Kick", "");
        config.getConfig().set("Party.Help.Leave", "");
        config.getConfig().set("Party.Help.Disband", "");
        config.getConfig().set("Party.Help.Promote", "");
        config.getConfig().set("Party.Help.Chat", "");
        config.getConfig().set("Party.Help.Mute", "");
        config.getConfig().set("Party.Help.Status", "");
        config.getConfig().set("Party.Help.Warp", "");
        config.getConfig().set("Party.Help.List", "");
        config.getConfig().set("Party.Help.Header", "");

        config.getConfig().set("Party.Invite.CantPartyPlayer", "");
        config.getConfig().set("Party.Invite.SentAnInvite", "");
        config.getConfig().set("Party.Invite.AlreadySent", "");
        config.getConfig().set("Party.Invite.ReceivedInvite", "");
        config.getConfig().set("Party.Invite.JOIN", "");
        config.getConfig().set("Party.Invite.JOINHover", "");
        config.getConfig().set("Party.Invite.TimedOutFrom", "");
        config.getConfig().set("Party.Invite.TimedOutTo", "");
        config.getConfig().set("Party.Invite.CantInviteSelf", "");
        config.getConfig().set("Party.Invite.AlreadyInParty", "");
        config.getConfig().set("Party.Invite.NotMoreOrLeader", "");
        config.getConfig().set("Party.Invite.Full", "");

        config.getConfig().set("Party.Join.AlreadyInAParty", "");
        config.getConfig().set("Party.Join.NoRequest", "");
        config.getConfig().set("Party.Join.HasJoined", "");
        config.getConfig().set("Party.Join.LeaderMustBeOnline", "");
        config.getConfig().set("Party.Join.Full", "");

        config.getConfig().set("Party.List.NotInParty", "");
        config.getConfig().set("Party.List.PartyHeader", "");
        config.getConfig().set("Party.List.Status.STATUS", "");
        config.getConfig().set("Party.List.Status.OPEN", "");
        config.getConfig().set("Party.List.Status.CLOSED", "");
        config.getConfig().set("Party.List.Leader", "]");
        config.getConfig().set("Party.List.Mods", "");
        config.getConfig().set("Party.List.Members", "");

        config.getConfig().set("Party.Leave.NotInParty", "");
        config.getConfig().set("Party.Leave.LeftTheParty", "");
        config.getConfig().set("Party.Leave.YouLeft", "");
        config.getConfig().set("Party.Leave.NotEnoughPlayers", "");

        config.getConfig().set("Party.Disband.NotInParty", "");
        config.getConfig().set("Party.Disband.MustBeLeader", "");
        config.getConfig().set("Party.Disband.HasBeenDisbanded", "");

        config.getConfig().set("Party.Kick.NoPermission", "");
        config.getConfig().set("Party.Kick.CantKickPlayer", "");
        config.getConfig().set("Party.Kick.NotInParty", "");
        config.getConfig().set("Party.Kick.PlayerNotInParty", "");
        config.getConfig().set("Party.Kick.KickedPlayer", "");
        config.getConfig().set("Party.Kick.NotEnoughPlayers", "");
        config.getConfig().set("Party.Kick.YouWereKicked", "");
        config.getConfig().set("Party.Kick.CantKickSelf", "");

        config.getConfig().set("Party.Promote.NotInParty", "");
        config.getConfig().set("Party.Promote.PlayerNotInParty", "");
        config.getConfig().set("Party.Promote.NoPermission", "");
        config.getConfig().set("Party.Promote.AlreadyMod", "");
        config.getConfig().set("Party.Promote.AlreadyLeader", "");
        config.getConfig().set("Party.Promote.ModPromoted", "");
        config.getConfig().set("Party.Promote.LeaderPromoted", "");

        config.getConfig().set("Party.Create.AlreadyInParty", "");
        config.getConfig().set("Party.Create.Created", "");
        config.getConfig().set("Party.Create.CreatedOpen", "");
        config.getConfig().set("Party.Create.InvalidType", "");

        config.getConfig().set("Party.Status.NotInParty", "");
        config.getConfig().set("Party.Status.NoPermission", "");
        config.getConfig().set("Party.Status.Open", "");
        config.getConfig().set("Party.Status.Closed", "");

        config.getConfig().set("Party.Warp.NotInParty", "");
        config.getConfig().set("Party.Warp.NoPermission", "");
        config.getConfig().set("Party.Warp.YouWarped", "");
        config.getConfig().set("Party.Warp.PlayersWarped", "");

        config.saveConfig();
        config.reloadConfig();
    }
}
