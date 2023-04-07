# Java-ChatterBox

Welcome to the Java ChatterBox Project! This is a Discord bot aimed to bring back the feature that made music bots so powerful: playing REDACTED videos either by URL or search query.

## How to Use the Bot

First, make sure you have Java Runtime Environment 1.8 installed on your computer.

1. Go to [releases](https://github.com/Identityofsine/Java-ChatterBox/releases/tag/v2)
2. Download the bot and move it to a folder called `ChatterBox`
3. In the `ChatterBox` folder, create an `.env` file with the following information:
```
API_KEY = ENTER_YOUR_DISCORD_BOT_API_KEY
YT_API_KEY = ENTER_YOUR_YOUTUBE_API_KEY_HERE
```

- `API_KEY` is for the Discord bot.
- `YT_API_KEY` is for the YouTube API.

4. Run the bot in a terminal:
- `cd` into the `ChatterBox` folder
- run `java -cp .\java-chatterbox.jar App.App`
5. Enjoy!

## Documentation

Command logic is stored in `Commands.Runnable` and extends the `CommandBehavior` class. For instance, the Play command is stored in `Command.Runnable.Play`.

Each command is added during initialization in `Commands.CommandInit`:
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
To add a new command that you have added in Commands.Runnable, you would use:
```java
CommandHandler.addCommands(new Command("commandName", new CommandName()));
```
## Requester Class
The Requester class (located in API.Requester) is a class that only does GET requests as of now. Its intended purpose is to be a substitute for all requests that the bot may want to make (as of now it only does a GET request to YouTube), but it is easily expandable.

To do a GET request:
```java
void dummy(){
	Requester requester = new Requester(API_KEY, URL);
	JsonNode response = requester.GetRequest(new Parameter("query1","value"), new Parameter("query2","value")); 
}
```

## Goals
- Get SoundCloud to work
- Not get banned
