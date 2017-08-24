# RoomMateAccounting
Needed to split spendings among us, and limit the number of transactions

So if the debt network looks like:

A --25-->B
B --50-->C
A --25-->C

The algorithm should output:

A--50-->C
B--25-->C

The input is a list of 3-tuples (String, Double, Option[List[String]])
where:
-first String is the person who initially paid
-Double is amount paid
-Optional list may contain list of people who must participate on the spending (if None, assumed to be everyone)


A weighted directed graph G is built, with weight function w, where w(u,v) is amount v owes to u, s.t w(u,v)=-w(v,u) for all u,v in E.

The algorithm generates a best-effort G', where for each u in V, sum(w'(u,v))=sum(w(u,v)), with sums ranging over all other nodes, and |E'|<|E|.

In the future, the algorithm will be updated to find global minimum of |E'| under given constraints.

