package main

import (
    "flag"
    "io"
    "io/ioutil"
    "log"
    "net/http"
    "encoding/json"
    "os"
    "fmt"
)

func main() {
    // parse command line arguments
    addr := os.Args[1]

    flag.Parse()

    // open database and start backend
    backend, err := NewBackend("DB@" + addr)
    if err != nil {
        log.Fatal("Cannot open the levelDB database", err)
    }

    backend.Start()
    defer backend.Shutdown()

    // listen and serve http
    http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
        switch r.Method {
        case "GET":
            HandleGet(w, r, backend)
        case "POST", "PUT":
            HandlePut(w, r, backend)
        case "DELETE":
            HandleDelete(w, r, backend)
        }
    })

    log.Printf("Server started, listening on port: %s", addr)
    http.ListenAndServe(":" + addr, nil)
}

func HandleGet(w http.ResponseWriter, r *http.Request, backend *Backend) {
    key := r.URL.Path[len("/"):]
    log.Printf("GET - key: %v", key)

    val, err := backend.Get(key)
    if err != nil {
        w.WriteHeader(http.StatusInternalServerError)
        return
    }

    if val == "" {
        w.WriteHeader(http.StatusNotFound)
        return
    }

    w.WriteHeader(http.StatusOK)
    w.Write([]byte(val))
}

func HandlePut(w http.ResponseWriter, r *http.Request, backend *Backend) {
    var stock Stock

    body, err := ioutil.ReadAll(io.LimitReader(r.Body, 1048576))

    if err != nil {
        panic(err)
    }

    if err := r.Body.Close(); err != nil {
        panic(err)
    }

    if err := json.Unmarshal(body, &stock); err != nil {
        w.Header().Set("Content-Type", "application/json; charset=UTF-8")
        w.WriteHeader(422) // unprocessable entity
        if err := json.NewEncoder(w).Encode(err); err != nil {
            panic(err)
        }
    }

    fmt.Printf("Key:%s - Val:%s", stock.Key, stock.Value)
    backend.Put(stock.Key, stock.Value)

    w.Header().Set("Content-Type", "application/json; charset=UTF-8")
    w.WriteHeader(http.StatusCreated)
}

func HandleDelete(w http.ResponseWriter, r *http.Request, backend *Backend) {
    key := r.URL.Path[len("/"):]
    log.Printf("DELETE - key: %v", key)

    backend.Delete(key)

    w.WriteHeader(http.StatusNoContent)
}
