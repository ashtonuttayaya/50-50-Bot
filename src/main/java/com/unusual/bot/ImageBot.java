package com.unusual.bot;

import com.unusual.bot.commands.CommandManager;
import com.unusual.bot.listeners.EventListener;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManager;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;

public class ImageBot {

    private final Dotenv config;
    //This allows us to use the .env file to hide private tokens
    private final ShardManager shardManager;

    public ImageBot() throws LoginException {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        //Initializes Shard Manager
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        shardManager = builder.build();
        //Allows access to CommandManager
        shardManager.addEventListener(new CommandManager());
    }

//    public Dotenv getConfig() {
//        return config;
//    }
//
//    public ShardManager getShardManager() {
//        return shardManager;
//    }

    public static void main (String[] args) {
        String token = "MTAzMzI2OTUzNTE5NDc1OTI3OA.GqCs9J.sRy5wFsW2M18NYmpjYQmdXDRbBXUr7j61URhTU";
        //Allows local usage
        JDABuilder.createLight(token, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new CommandManager())
                .build();
    }

    private Dotenv getConfig() {
        return config;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }
}
