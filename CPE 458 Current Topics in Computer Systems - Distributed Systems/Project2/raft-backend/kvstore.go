package main

import (
    "bytes"
    "encoding/gob"
    "log"
    "sync"
)

// a key-value store backed by raft
type kvstore struct {
    proposeC chan<- string // channel for proposing updates
    mu       sync.RWMutex
    db       *LevelDB
}

type kv struct {
    Key string
    Val string
}

func newKVStore(proposeC chan<- string, commitC <-chan *string, errorC <-chan error) *kvstore {
    db, err := OpenLevelDB("stocks")

    if err != nil {
        return &kvstore{proposeC: nil, db: nil}
    }

    s := &kvstore{
        proposeC: proposeC,
        db: db,
    }

    // replay log into key-value map
    s.readCommits(commitC, errorC)
    // read commits from raft into kvStore map until error
    go s.readCommits(commitC, errorC)

    return s
}

func (s *kvstore) Lookup(key string) (string, bool) {
    s.mu.RLock()
    v, e := s.db.Get(key)
    log.Printf("val of v: %s", v)
    s.mu.RUnlock()
    if e == nil {
    	return v, true
    } else {
    	return v, false
    }
}

func (s *kvstore) Propose(k string, v string) {
    var buf bytes.Buffer
    if err := gob.NewEncoder(&buf).Encode(kv{k, v}); err != nil {
        log.Fatal(err)
    }
    s.proposeC <- string(buf.Bytes())
}

func (s *kvstore) readCommits(commitC <-chan *string, errorC <-chan error) {
    for data := range commitC {
        if data == nil {
            // done replaying log; new data incoming
            return
        }

        var data_kv kv
        dec := gob.NewDecoder(bytes.NewBufferString(*data))
        if err := dec.Decode(&data_kv); err != nil {
            log.Fatalf("raftexample: could not decode message (%v)", err)
        }
        s.mu.Lock()

        s.db.Put(data_kv.Key, data_kv.Val)

        s.mu.Unlock()
    }
    if err, ok := <-errorC; ok {
        log.Fatal(err)
    }
}
