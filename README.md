## nets-150-final-project 

We hope to do an Empirical Analysis in order to answer the following question: do U.S. senators from the 117th Congress have a greater likelihood of sharing a generation if they are members of the same sub-committee? We plan to do web-scraping on the [Senate Committee Assignments](https://www.senate.gov/general/committee_assignments/assignments.htm) page, [this Wikipedia page about senators](https://en.wikipedia.org/wiki/List_of_United_States_senators_in_the_117th_Congress), and each senator's respective page in order to gather information about which senators belong to which sub-committees and what their respective seniority is. 

With this information, we'll create a graph where the nodes are the senators and the edges are constructed based on shared sub-committees. Then, we want to compute the average clustering coefficients for each node of 
1. the complete graph
2. an isolated graph where all the nodes share a common age
3. an isolated graph where all the nodes have a distinct age 

