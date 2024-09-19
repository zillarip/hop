# Hop
Hop is a smart, efficient, and fast queue plugin for Spigot servers.

## Features
- Simple to use
- Completely configurable messages
- Whitelisted, full, and offline detection: no spamming trying to get players in
- Commands to easily join and leave queues
- Comprehensive API

## Contributing
Hop is a multi-module Maven project which uses Lombok.

Contributions are very welcome, however please make sure to follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)!

If you are making a big feature change, please make sure to test before making a pull request.

## How does it work?
There are two plugins, one which you would install on a hub server, and one which you would install on your backend servers.

The "hub" plugins will send Redis messages to the "backend" plugins and if the backend plugins respond saying the server is able to accept players, it will send the player in the first position of the queue.

## How do I set it up?
**Required**
- A Redis server
- Spigot (or a Spigot-based fork) using >=Java 8
- A BungeeCord proxy connecting all the servers

Go to the Releases page ([here](https://github.com/zillarip/hop/releases)) and download the latest "Hop3.jar" and place it on a "hub" server. Start the server, shut it down, and then fill out the configuration files (settings.yml, queues.yml). Once you're done configuring, start the server back up and you are good to go.

Then, download the "HopClient3.jar" and place it on your backend server(s). Again, start the server, shut it down, and then fill out the configuration options in settings.yml.

Once this process is complete, you should have a functioning queue system. 

## Extending functionality
Hop also has an API which can tell you;
- if the player is queued
- what queue they are in
- what position of the queue they are at
- how many players are in a given queue

Additionally, it can let you add and remove someone to/from a queue. 

Add the API jar which is available in the releases, but do NOT shade it in.

You can get an instance of IQueueManager via `ManagerHandle.getImplementation()`.

```
me.hydro.queue.api.IQueueManager.getPlayer(Player player) -> PlayerData
me.hydro.queue.api.IQueueManager.getQueue(String queueName) -> Queue

me.hydro.queue.api.IQueueManager.isQueued(PlayerData player) -> boolean
me.hydro.queue.api.IQueueManager.getQueued(PlayerData player) -> String (queue id, null if not queued)
me.hydro.queue.api.IQueueManager.getPlayerPos(PlayerData player) -> int (position in queue, starts at 0, -1 if not queued)

me.hydro.queue.api.IQueueManager.addToQueue(PlayerData player, String queueName, int position) -> void (pass -1 for position to insert at the end)
me.hydro.queue.api.IQueueManager.removeFromQueue(PlayerData player) -> void
```
