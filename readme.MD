# Java-ChatterBox

Welcome to the Java ChatterBox Project; this is a discord bot aimed to bring back the feature that made Music-Bots so powerful.

Playing REDACTED Videos either by URL or search query.

## How Do I Use The Bot?

Well first make sure you have Java Runtime Environment 1.8 installed onto your computer.

Go to [releases](https://github.com/Identityofsine/Java-ChatterBox/releases/tag/v2)

Move it to a folder, `ChatterBox`

In this folder, create an .env file with:
```
API_KEY = ENTER_YOUR_DISCORD_BOT_API_KEY
YT_API_KEY = ENTER_YOUR_YOUTUBE_API_KEY_HERE
```

API_KEY is for the Discord bot.
YT_API_KEY is for the YouTube API.

Run the bot in a terminal:
- cd into the `ChatterBox` folder
- run java -cp .\java-chatterbox.jar App.App
- Enjoy!

### Some Documentation

Command Logic are stored in `Commands.Runnable` and they extend the `CommandBehavior` class, for instance the Play command is stored in Command.Runnable.Play

Each Command is added at initalization in Commands.CommandInit
```java
    public static void addChatCommands(){
    	//All The Commands The Bot Uses
        CommandHandler.addCommands(new Command("java", new Java()));
        CommandHandler.addCommands(new Command("echo", new Echo()));
        CommandHandler.addCommands(new Command("join", new Join()));
        CommandHandler.addCommands(new Command("stop", new Stop()));
        CommandHandler.addCommands(new Command("skip", new Skip()));
        CommandHandler.addCommands(new Command("play", new Play()));
        CommandHandler.addCommands(new Command("queue", new Queue()));
    }
```

To Add A Command(That you have added in Commands.Runnable), you would add :
```java
CommandHandler.addCommands(new Command("commandName", new CommandName()));
```

### Requester Class

The Requester Class (located in API.Requester) is a class that only does GetRequests as of now. It's intended purpose is to be a subsititue for all Requests that the Bot may want to make (as of now it only does a GET request to YouTube); but is easily expandable.

To do a GET Request:
```java

void dummy(){
	Requester requester = new Requester(API_KEY, URL);
	//this assumes that your queries are not the first one inline i.e example.com/?alreadyhere=something&query1=value&query2=value
	JsonNode response = requester.GetRequest(new Parameter("query1","value"), new Parameter("query2","value")); 
}
```

### Goals

- Get SoundCloud To Work
- Not Get Banned


