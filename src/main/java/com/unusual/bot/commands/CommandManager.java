package com.unusual.bot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommandManager extends ListenerAdapter {
    final static File win = new File("FILE:\\PATH");
    final static File lose = new File("FILE:\\PATH");
    //Sets file paths to a local destination
    HashMap<Long,Long> COOLDOWN_TIMER = new HashMap<Long, Long>();
    //Hashmap assigns when command was last ran
    HashMap<Long,Integer> NUM_LOSS = new HashMap<Long,Integer>();
    HashMap<Long,Integer> NUM_WINS = new HashMap<Long,Integer>();
    //These two HasMaps allow a counter for wins and losses
    int wins, losses;
    //Initializes wins and losses



    //This method allows for command to be run from application
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String luck = event.getName();
        long user = event.getUser().getIdLong();
        //Sets delay to 10 seconds
        long delayTimer = 5000000000L;
                //5seconds= 5000000000L
                //10seconds= 10000000000L
                //30seconds = 30000000000L

        if (luck.equals("luck")) {
            //if statement that checks if hashmaps do not contain an id value
            if (!COOLDOWN_TIMER.containsKey(user) && !NUM_WINS.containsKey(user) && !NUM_LOSS.containsKey(user)) {
                NUM_WINS.put(user, losses);
                NUM_LOSS.put(user, wins);
                onChanceReturn(event, user);
               COOLDOWN_TIMER.put(user, System.nanoTime());
            } else {
                //Sets cooldown timer
                Long userTimeStamp = COOLDOWN_TIMER.get(user);
                if (System.nanoTime() > userTimeStamp + delayTimer) {
                    onChanceReturn(event, user);
                    COOLDOWN_TIMER.replace(user, System.nanoTime());
                } else {
                    //Output message showing user how much time is left before they can run a command again
                    long timeLeft = TimeUnit.SECONDS.convert(userTimeStamp + delayTimer - System.nanoTime(), TimeUnit.NANOSECONDS);
                    event.reply(timeLeft + " seconds left").setEphemeral(true).queue();
                }
            }

        }
    }

    //Method which returns the 50/50 chance
    public void onChanceReturn (@NotNull SlashCommandInteractionEvent event, long id) {
        Random rand = new Random();
        User user = event.getUser();
        double chance = Math.random();
        if (chance >= 0.50) {
            //Creates file array of the winning path
            File[] fileswin = win.listFiles();
            //Retrieves file from indexed array using rand and setting the max value of rand to num of files in a folder
            File filewin = fileswin[rand.nextInt(fileswin.length)];
            NUM_WINS.replace(id, wins+=1);
            //Output reply which updates user wins
            event.reply(user.getAsMention() + " got lucky!" + "\nWins: " + NUM_WINS.get(id) + "\nLosses: " + NUM_LOSS.get(id)).addFiles(FileUpload.fromData(filewin)).queue();
        }
        else {
            File[] fileslose = lose.listFiles();
            File file = fileslose[rand.nextInt(fileslose.length)];
            NUM_LOSS.replace(id, losses+=1);
            //Output reply which updates user losses
            event.reply(user.getAsMention() + " was unlucky!" + "\nWins: " + NUM_WINS.get(id) + "\nLosses: " + NUM_LOSS.get(id)).addFiles(FileUpload.fromData(file)).queue();
        }
    }


    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        //Creates a list of commands
        List<CommandData> commandData = new ArrayList<>();
        commandData.add(Commands.slash("luck", "Test your luck!"));
        //Initializes command to server
        event.getGuild().updateCommands().addCommands(commandData).queue();
    }
}
