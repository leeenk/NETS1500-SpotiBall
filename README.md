# NETS1500-SpotiBall

This program finds the degree of connection between two artists on Spotify, where a connection is measured as a song feature. 

The user inputs two artists that they want to find the "shortest path" between. The first artist is used to build a graph, where artists are nodes and song features are directed edges, using Spotify's API. BFS is used to find the shortest path between the two artists in the populated graph.
