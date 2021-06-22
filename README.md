# HydroQueue
HydroQueue is an extremely smart, efficient, and fast queue plugin that is built to work.

## Features
- Simple to use
- Completely configurable messages
- Whitelisted, full, and offline detection meaning no spamming trying to get players in
- Commands to easily join and leave queues
- Comprehensive API

## Contributing
HydroQueue is a multi-module Maven project which uses Lombok.

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

Go to the Releases page ([here](https://github.com/thehydrogen/queue/releases)) and download the latest "HydroQueue3.jar" and place it on a "hub" server. Start the server, shut it down, and then fill out the configuration files (settings.yml, queues.yml). Once you're done configuring, start the server back up and you are good to go.

Then, download the "QueueClient3.jar" and place it on your backend server(s). Again, start the server, shut it down, and then fill out the configuration options in settings.yml.

Once this process is complete, you should have a functioning queue system. 

## Extending functionality
HydroQueue also has an API which can tell you;
- if the player is queued
- what queue they are in
- what position of the queue they are at
- how many players are in a given queue

Additionally, it can let you add and remove someone to/from a queue. 

Add the API jar which is available in the releases, but do NOT shade it in. These are the methods you can use;
```
me.hydro.queue.api.QueueManager.getPlayer(Player player) -> PlayerData
me.hydro.queue.api.QueueManager.getQueue(String queueName) -> Queue

me.hydro.queue.api.QueueManager.isQueued(PlayerData player) -> boolean
me.hydro.queue.api.QueueManager.getQueued(PlayerData player) -> String (queue id, null if not queued)
me.hydro.queue.api.QueueManager.getPlayerPos(PlayerData player) -> int (position in queue, starts at 0, -1 if not queued)

me.hydro.queue.api.QueueManager.addToQueue(PlayerData player, String queueName, int position) -> void (pass -1 for position to insert at the end)
me.hydro.queue.api.QueueManager.removeFromQueue(PlayerData player) -> void
```