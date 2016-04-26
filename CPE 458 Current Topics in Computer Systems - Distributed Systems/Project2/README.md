# Build a distributed key-value store out of an existing, single-node key-value store using the Google LevelDB library.


### Distributed transactions and distributed consensus


### Assume that you have statically partitioned your data into key ranges mapped to each node (e.g., keys 1-100 on node1, keys 101-200 on node 2 and so on).
#### Assume only one writer can update a key’s value but multiple readers can read a key.
### Details:
### Implement a server (process) on each node that presents a REST API that implements put(key,value), and value = get(key). To do this, you will use LevelDB.

* Golang
* https://github.com/syndtr/goleveldb
* Check tutorials

### Implement a gateway process that can be connected to from any client. This gateway process implements a REST API that implements put(key,value) and value = get(key). When the gateway process receives a request, it routes the request to the appropriate node that maintains the key specified. To create a REST API, we suggest python which has several libraries to assist in this.

* 

### Your project is to explore how transactions can be replicated in an atomic manner so that after a failure of any single node, you can recover a key range from another replica. You’ll do this in two ways:
* implement every PUT using a two-phase commit protocol. Assume that there are 3-replicas for every key range.
* write every update to a distributed, replicated log. You can do this using a consensus protocol such as Paxos or Raft (we suggest Raft since it’s simpler to understand than Paxos). To do this, use any open source Raft protocol implementation.
* Test that your program continues to update keys in the face of failures.
* Compare your 2-phase commit and Raft implementations