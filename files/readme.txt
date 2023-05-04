README SPOTIBALL:

User Manual:
Welcome to Spotiball! Inspired by Pitbull number, we decided to parse Spotify's API to be able to get the
degree of separation or two artists (and the artists on this path relating them). Artists are related through
features on tracks.
Since parsing the Spotify API and building a graph from it can take an extremely long amount of time with
large data sets, we decided that we would build a graph fo size 1500 and write this graph to a file, which we could
store and then read from to run BFS on it. This is so that when the user is running the program, the program
won't take an hour to run as the graph is already built in the presaved file. Therefore, if the two artists
that you want to find the degree of separation of are in this 1500 size graph, then you will be in luck
and the program will run quickly as we will read from data.txt and run BFS and then output the path. Otherwise,
if your artists are not in the 1500 size graph, we will have an option to run the entire program and build the graph,
but be warned, this may take an hour to run.
To run this application, run the Run file, and then input the two artists that you want to find the degree
of separation of. If they are both in the graph then great we will output the path of artists are you are done!
Otherwise, if they are not, we will have an option to abort the program or run it(and we will parse the API and build
the graph from scratch), and this will take longer. However, in the end, the path will be outputted.

Summary of this Project:
We will give descriptions of what each file does:
DataCollection: This file parses Spotify's API...

Graph: This file represents an undirected graph and has methods such as addArtist, containsArtist,
hasEdge, and addEdge. Therefore, we use these methods to build a graph in DataCollection an BFS (when
we are rebuilding the graph from data.txt).

BFS: This file reads in data.txt and populates a graph from the txt file, then runs BFS on this graph
which finds the shortest path from the first artist to the second artist.

Run: This file contains the main method which gives a pop up window for the user to run the application.